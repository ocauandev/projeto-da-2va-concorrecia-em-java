package aeroporto;

import aeroporto.interfaces.IAviao;
import aeroporto.interfaces.IPista;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConfiguracaoSimulacao config = new ConfiguracaoSimulacao("config.properties");

        // Cria as pistas
        List<IPista> pistas = new ArrayList<>();
        for (int i = 1; i <= config.getNumeroDePistas(); i++) {
            pistas.add(new Pista(i));
        }

        // Cria a torre de controle
        TorreDeControle torre = new TorreDeControle(pistas);

        // Voos reais de companhias brasileiras
        String[][] voosPouso = {
                {"AD", "2774"}, {"LA", "3050"}, {"G3", "1401"}, {"AA", "945"}
        };
        String[][] voosDecolagem = {
                {"AD", "1830"}, {"LA", "4012"}, {"G3", "1150"}
        };

        List<IAviao> avioes = new ArrayList<>();
        int totalPouso = Math.min(config.getAvioesParaPousar(), voosPouso.length);
        int totalDecolagem = Math.min(config.getAvioesParaDecolar(), voosDecolagem.length);

        for (int i = 0; i < totalPouso; i++) {
            avioes.add(new Aviao(voosPouso[i][0], Integer.parseInt(voosPouso[i][1]),
                    torre, config.getDelaySimulacao(), true)); // inicia VOANDO
        }
        for (int i = 0; i < totalDecolagem; i++) {
            avioes.add(new Aviao(voosDecolagem[i][0], Integer.parseInt(voosDecolagem[i][1]),
                    torre, config.getDelaySimulacao(), false)); // inicia ESTACIONADO
        }

        // Inicia o painel (thread daemon)
        new PainelDeControle(pistas, avioes, config.getIntervaloAtualizacaoPainel()).start();

        // Inicia as threads dos aviões
        System.out.println("[SIMULACAO] Iniciando com "
                + pistas.size() + " pistas e " + avioes.size() + " avioes...");
        for (IAviao aviao : avioes) {
            ((Thread) aviao).start();
        }

        // Encerra threads ao fechar (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[SIMULACAO] Encerrando...");
            for (IAviao aviao : avioes) {
                aviao.interrupt();
            }
        }));
    }
}