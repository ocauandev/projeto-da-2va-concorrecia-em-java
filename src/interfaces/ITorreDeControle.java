package interfaces;

public interface ITorreDeControle {
    /**
     * Solicita autorização de pouso. Bloqueia o avião na fila até uma pista ser liberada[cite: 21, 23].
     * @return IPista designada pela torre[cite: 23].
     */
    IPista solicitarPouso(IAviao aviao) throws InterruptedException;

    /**
     * Solicita autorização de decolagem. Bloqueia o avião na fila respectiva[cite: 21, 23].
     * @return IPista designada pela torre[cite: 23].
     */
    IPista solicitarDecolagem(IAviao aviao) throws InterruptedException;

    /**
     * Notifica a torre que o avião desocupou a pista, permitindo que a torre acorde outras threads[cite: 23].
     */
    void liberarPista(IPista pista, IAviao aviao);
}