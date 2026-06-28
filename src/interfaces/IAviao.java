package interfaces;

public interface IAviao {
    // Enum unificado para o controle de estados exigido pelo simulador
    enum EstadoAviao {
        VOANDO, QUER_POUSAR, NA_PISTA, ESTACIONADO, QUER_DECOLAR
    }

    /**
     * Retorna o identificador único do voo seguindo o padrão brasileiro (ex: "AD 2774").
     */
    String getIdVoo();

    /**
     * Retorna o estado atual da thread na simulação[cite: 3].
     */
    EstadoAviao getEstadoAtual();

    /**
     * Interrompe a execução da thread de forma segura ao encerrar o programa[cite: 23].
     */
    void interrupt();
}