import interfaces.ITorreDeControle;
import interfaces.IPista;
import interfaces.IAviao;
import java.util.ArrayList;
import java.util.List;

public class TorreDeControle implements ITorreDeControle {
    private final List<IPista> pistas;
    private final List<IAviao> filaPouso = new ArrayList<>(); //
    private final List<IAviao> filaDecolagem = new ArrayList<>(); //

    public TorreDeControle(List<IPista> pistas) {
        this.pistas = pistas;
    }


    // Remova a palavra reservada 'synchronized' dos três métodos abaixo para desativar a concorrência.

    @Override
    public synchronized IPista solicitarPouso(IAviao aviao) throws InterruptedException {
        filaPouso.add(aviao);

        // Aguarda enquanto não for o primeiro da fila OU não houver nenhuma pista totalmente livre [cite: 23]
        while (filaPouso.get(0) != aviao || encontrarPistaDisponivel() == null) {
            wait();
        }

        filaPouso.remove(0);
        IPista pistaEscolhida = encontrarPistaDisponivel();
        return pistaEscolhida;
    }

    @Override
    public synchronized IPista solicitarDecolagem(IAviao aviao) throws InterruptedException {
        filaDecolagem.add(aviao);

        // Bloqueia se não for o primeiro da fila, se não houver pista livre OU se houver prioridade de pouso ativa [cite: 22, 23]
        while (filaDecolagem.get(0) != aviao || encontrarPistaDisponivel() == null || !filaPouso.isEmpty()) {
            wait();
        }

        filaDecolagem.remove(0);
        IPista pistaEscolhida = encontrarPistaDisponivel();
        return pistaEscolhida;
    }

    @Override
    public synchronized void liberarPista(IPista pista, IAviao aviao) {
        pista.desocuparPista();
        notifyAll(); // Acorda cooperativamente os aviões retidos nos métodos wait() [cite: 23]
    }

    private IPista encontrarPistaDisponivel() {
        for (IPista pista : pistas) {
            if (!pista.estaOcupada()) {
                return pista;
            }
        }
        return null;
    }
}