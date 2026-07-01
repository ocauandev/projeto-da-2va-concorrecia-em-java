package aeroporto.interfaces;

public interface IPista {
    int getIdPista();
    void alocarPista(IAviao aviao);
    void desocuparPista();
    boolean estaOcupada();
    String getNomeArquivoStatus();
}