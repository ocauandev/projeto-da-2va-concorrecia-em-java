package aeroporto;

import aeroporto.interfaces.IAviao;
import aeroporto.interfaces.IAviao.EstadoAviao;
import aeroporto.interfaces.ITorreDeControle;
import aeroporto.interfaces.IPista;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Aviao extends Thread implements IAviao {
    private final String idVoo;
    private final ITorreDeControle torre;
    private final int delaySimulacao;
    private final Random random = new Random();
    private volatile EstadoAviao estadoAtual;

    public Aviao(String companhia, int numeroVoo, ITorreDeControle torre, int delaySimulacao, boolean iniciaVoando) {
        super(companhia + " " + numeroVoo);
        this.idVoo = companhia + " " + numeroVoo;
        this.torre = torre;
        this.delaySimulacao = delaySimulacao;
        this.estadoAtual = iniciaVoando ? EstadoAviao.VOANDO : EstadoAviao.ESTACIONADO;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                switch (estadoAtual) {

                    case VOANDO:
                        simularTempo("Voando em rota...");
                        this.estadoAtual = EstadoAviao.QUER_POUSAR;
                        break;

                    case QUER_POUSAR:
                        IPista pistaPouso = torre.solicitarPouso(this);
                        this.estadoAtual = EstadoAviao.NA_PISTA;
                        atualizarArquivoTexto(pistaPouso, "OCUPADA - Pouso de " + idVoo);
                        simularTempo("Pousando na Pista " + pistaPouso.getIdPista() + "...");
                        this.estadoAtual = EstadoAviao.ESTACIONADO;
                        atualizarArquivoTexto(pistaPouso, "LIVRE");
                        torre.liberarPista(pistaPouso, this);
                        break;

                    case ESTACIONADO:
                        simularTempo("Estacionado (Embarque / Reabastecimento)...");
                        this.estadoAtual = EstadoAviao.QUER_DECOLAR;
                        break;

                    case QUER_DECOLAR:
                        IPista pistaDecolagem = torre.solicitarDecolagem(this);
                        this.estadoAtual = EstadoAviao.NA_PISTA;
                        atualizarArquivoTexto(pistaDecolagem, "OCUPADA - Decolagem de " + idVoo);
                        simularTempo("Decolando da Pista " + pistaDecolagem.getIdPista() + "...");
                        this.estadoAtual = EstadoAviao.VOANDO;
                        atualizarArquivoTexto(pistaDecolagem, "LIVRE");
                        torre.liberarPista(pistaDecolagem, this);
                        break;

                    default:
                        break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void atualizarArquivoTexto(IPista pista, String status) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(pista.getNomeArquivoStatus(), false))) {
            pw.println("Pista " + pista.getIdPista() + ": " + status);
            pw.println("Horario: " + java.time.LocalTime.now());
        } catch (IOException e) {
            System.err.println("[ERRO] " + idVoo + " falhou ao gravar arquivo da pista " + pista.getIdPista());
        }
    }

    private void simularTempo(String acao) throws InterruptedException {
        System.out.println("[" + idVoo + "] " + acao);
        Thread.sleep(delaySimulacao + random.nextInt(1000));
    }

    @Override public String getIdVoo() { return this.idVoo; }
    @Override public EstadoAviao getEstadoAtual() { return this.estadoAtual; }
}