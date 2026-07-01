package aeroporto;

import aeroporto.interfaces.ITorreDeControle;
import aeroporto.interfaces.IPista;
import aeroporto.interfaces.IAviao;
import java.util.ArrayList;
import java.util.List;

public class TorreDeControle implements ITorreDeControle {
    private final List<IPista> pistas;
    private final List<IAviao> filaPouso = new ArrayList<>();
    private final List<IAviao> filaDecolagem = new ArrayList<>();

    public TorreDeControle(List<IPista> pistas) {
        this.pistas = pistas;
    }

    // NOTA: Remova o 'synchronized' dos três métodos abaixo para desativar o controle de
    // concorrência. Sem ele, múltiplos aviões podem passar pelo while() ao mesmo tempo,
    // alocar a mesma pista e disparar a asserção em Pista.alocarPista().

    @Override
    public synchronized IPista solicitarPouso(IAviao aviao) throws InterruptedException {
        filaPouso.add(aviao);

        while (filaPouso.get(0) != aviao || encontrarPistaDisponivel() == null) {
            wait();
        }

        filaPouso.remove(aviao);
        IPista pistaEscolhida = encontrarPistaDisponivel();
        pistaEscolhida.alocarPista(aviao);
        notifyAll();
        return pistaEscolhida;
    }

    @Override
    public synchronized IPista solicitarDecolagem(IAviao aviao) throws InterruptedException {
        filaDecolagem.add(aviao);

        // Pouso tem prioridade: só decola se não houver nenhum avião aguardando pouso
        while (filaDecolagem.get(0) != aviao || encontrarPistaDisponivel() == null || !filaPouso.isEmpty()) {
            wait();
        }

        filaDecolagem.remove(aviao);
        IPista pistaEscolhida = encontrarPistaDisponivel();
        pistaEscolhida.alocarPista(aviao);
        notifyAll();
        return pistaEscolhida;
    }

    @Override
    public synchronized void liberarPista(IPista pista, IAviao aviao) {
        pista.desocuparPista();
        notifyAll();
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