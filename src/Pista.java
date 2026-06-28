import interfaces.IPista;
import interfaces.IAviao;

public class Pista implements IPista {
    private final int idPista;
    private boolean ocupada;
    private IAviao aviaoAtual;
    private final String nomeArquivoStatus;

    public Pista(int idPista) {
        this.idPista = idPista;
        this.ocupada = false;
        this.aviaoAtual = null;
        this.nomeArquivoStatus = "status_pista_" + idPista + ".txt"; // [cite: 20]
    }

    @Override
    public int getIdPista() { return this.idPista; }

    @Override
    public synchronized void alocarPista(IAviao aviao) {
        // [ZONA DE ASSERÇÃO CRÍTICA] Exigência do PDF 
        // Se duas threads entrarem aqui simultaneamente devido a falhas na torre, o assert dispara.
        assert !this.ocupada : "RACE CONDITION DETECTADA: Pista " + idPista + " invadida por " + aviao.getIdVoo() + " enquanto ocupada!";

        this.ocupada = true;
        this.aviaoAtual = aviao;
    }

    @Override
    public synchronized void desocuparPista() {
        this.ocupada = false;
        this.aviaoAtual = null;
    }

    @Override
    public synchronized boolean estaOcupada() { return this.ocupada; }

    @Override
    public String getNomeArquivoStatus() { return this.nomeArquivoStatus; }
}