package aeroporto;

import aeroporto.interfaces.IAviao;
import aeroporto.interfaces.IPista;
import java.util.List;

public class PainelDeControle extends Thread {
    private final List<IPista> pistas;
    private final List<IAviao> avioes;
    private final int intervaloMs;

    public PainelDeControle(List<IPista> pistas, List<IAviao> avioes, int intervaloMs) {
        super("Painel-de-Controle");
        this.pistas = pistas;
        this.avioes = avioes;
        this.intervaloMs = intervaloMs;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(intervaloMs);
                exibirEstado();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void exibirEstado() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          SIMULADOR DE AEROPORTO - PAINEL             ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");

        System.out.println("  PISTAS:");
        for (IPista pista : pistas) {
            String status = pista.estaOcupada() ? "OCUPADA ✈" : "LIVRE    ○";
            System.out.printf("    Pista %d: %s%n", pista.getIdPista(), status);
        }

        System.out.println();
        System.out.println("  AVIOES:");
        for (IAviao aviao : avioes) {
            System.out.printf("    %-10s -> %s%n", aviao.getIdVoo(), aviao.getEstadoAtual());
        }

        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("  Atualizado em: " + java.time.LocalTime.now());
    }
}