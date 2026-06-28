package interfaces;

public interface IPista {
    /**
     * Retorna o número/ID identificador da pista.
     */
    int getIdPista();

    /**
     * Altera o estado interno da pista para ocupada pelo avião informado.
     * Contém a asserção contra Race Conditions.
     */
    void alocarPista(IAviao aviao);

    /**
     * Libera o estado da pista, limpando o avião atual.
     */
    void desocuparPista();

    /**
     * Retorna se a pista está sendo utilizada por alguma thread no momento.
     */
    boolean estaOcupada();

    /**
     * Retorna o nome do arquivo texto associado ao status desta pista[cite: 20].
     */
    String getNomeArquivoStatus();
}