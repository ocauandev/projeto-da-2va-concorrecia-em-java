package aeroporto;

import aeroporto.interfaces.IPista;
import aeroporto.interfaces.IAviao;

public class Pista implements IPista {
    private final int idPista;
    private boolean ocupada;
    private IAviao aviaoAtual;
    private final String nomeArquivoStatus;

    public Pista(int idPista) {
        this.idPista = idPista;
        this.ocupada = false;
        this.aviaoAtual = null;
        this.nomeArquivoStatus = "status_pista_" + idPista + ".txt";
    }

    @Override
    public int getIdPista() { return this.idPista; }

    @Override
    public synchronized void alocarPista(IAviao aviao) {
        // [ASSERÇÃO CRÍTICA]
        // Para desativar o controle de concorrência e fazer esta asserção falhar,
        // remova o 'synchronized' de solicitarPouso() e solicitarDecolagem() na TorreDeControle.
        assert !this.ocupada : "RACE CONDITION DETECTADA: Pista " + idPista
                + " invadida por " + aviao.getIdVoo() + " enquanto já estava ocupada!";

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

    @Override
    public String toString() { return "Pista-" + idPista; }
}