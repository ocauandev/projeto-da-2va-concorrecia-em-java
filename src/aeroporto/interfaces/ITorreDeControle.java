package aeroporto.interfaces;

public interface ITorreDeControle {
    IPista solicitarPouso(IAviao aviao) throws InterruptedException;
    IPista solicitarDecolagem(IAviao aviao) throws InterruptedException;
    void liberarPista(IPista pista, IAviao aviao);
}