package aeroporto.interfaces;

public interface IAviao {
    enum EstadoAviao {
        VOANDO, QUER_POUSAR, NA_PISTA, ESTACIONADO, QUER_DECOLAR
    }

    String getIdVoo();
    IAviao.EstadoAviao getEstadoAtual();
    void interrupt();
}