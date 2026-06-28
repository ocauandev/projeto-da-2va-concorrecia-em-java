import interfaces.IAviao;
import interfaces.ITorreDeControle;
import interfaces.IPista;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Aviao extends Thread implements IAviao {
    private final String idVoo;
    private final ITorreDeControle torre;
    private final int delaySimulacao; // Intervalo vindo do arquivo de configuração [cite: 35]
    private final Random random = new Random();
    private EstadoAviao estadoAtual;

    public Aviao(String companhia, int numeroVoo, ITorreDeControle torre, int delaySimulacao) {
        // Define o nome da Thread nativa com o padrão solicitado (Ex: "AD 2774")
        super(companhia + " " + numeroVoo);
        this.idVoo = companhia + " " + numeroVoo;
        this.torre = torre;
        this.delaySimulacao = delaySimulacao;
        // Dinâmica inicial aleatória conforme exemplo do PDF [cite: 34]
        this.estadoAtual = random.nextBoolean() ? EstadoAviao.VOANDO : EstadoAviao.ESTACIONADO;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) { // Ciclo infinito

                if (estadoAtual == EstadoAviao.VOANDO) {
                    simularTempo("Voando em rota...");
                    this.estadoAtual = EstadoAviao.QUER_POUSAR;

                } else if (estadoAtual == EstadoAviao.QUER_POUSAR) {
                    // Pede autorização à coordenadora central e bloqueia cooperativamente na fila [cite: 21, 23]
                    IPista pistaDesignada = torre.solicitarPouso(this);
                    this.estadoAtual = EstadoAviao.NA_PISTA;

                    pistaDesignada.alocarPista(this);
                    atualizarArquivoTexto(pistaDesignada, "OCUPADA para Pouso"); // REQUISITO: Avião atualiza o arquivo

                    simularTempo("Executando procedimento de pouso na Pista " + pistaDesignada.getIdPista() + "...");

                    this.estadoAtual = EstadoAviao.ESTACIONADO;
                    atualizarArquivoTexto(pistaDesignada, "LIVRE"); // REQUISITO: Avião limpa o arquivo
                    torre.liberarPista(pistaDesignada, this);

                } else if (estadoAtual == EstadoAviao.ESTACIONADO) {
                    simularTempo("Estacionado no pátio (Embarque/Reabastecimento)...");
                    this.estadoAtual = EstadoAviao.QUER_DECOLAR;

                } else if (estadoAtual == EstadoAviao.QUER_DECOLAR) {
                    // Pede autorização de decolagem à torre [cite: 22, 23]
                    IPista pistaDesignada = torre.solicitarDecolagem(this);
                    this.estadoAtual = EstadoAviao.NA_PISTA;

                    pistaDesignada.alocarPista(this);
                    atualizarArquivoTexto(pistaDesignada, "OCUPADA para Decolagem"); // REQUISITO: Avião atualiza o arquivo

                    simularTempo("Correndo na Pista " + pistaDesignada.getIdPista() + " e decolando...");

                    this.estadoAtual = EstadoAviao.VOANDO; // Volta a voar fechando o ciclo infinito
                    atualizarArquivoTexto(pistaDesignada, "LIVRE"); // REQUISITO: Avião limpa o arquivo
                    torre.liberarPista(pistaDesignada, this);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Atende estritamente à regra: "Aviões atualizam os arquivos de status das pistas".
     */
    private void atualizarArquivoTexto(IPista pista, String status) {
        try (FileWriter fw = new FileWriter(pista.getNomeArquivoStatus(), false);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Pista " + pista.getIdPista() + ": " + status + " por Voo " + this.idVoo); //
        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivo de texto pelo avião " + idVoo);
        }
    }

    private void simularTempo(String acao) throws InterruptedException {
        // Variação pseudo-aleatória somada ao delay base do arquivo de configuração [cite: 35]
        Thread.sleep(delaySimulacao + random.nextInt(1000));
    }

    @Override public String getIdVoo() { return this.idVoo; }
    @Override public EstadoAviao getEstadoAtual() { return this.estadoAtual; }
}