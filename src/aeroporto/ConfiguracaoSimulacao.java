package aeroporto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracaoSimulacao {
    private final Properties props = new Properties();

    public ConfiguracaoSimulacao(String caminhoArquivo) {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
            props.load(fis);
            System.out.println("[CONFIG] Arquivo carregado: " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("[CONFIG] Arquivo nao encontrado. Usando valores padrao.");
        }
    }

    public int getNumeroDePistas() {
        return Integer.parseInt(props.getProperty("numeroDePistas", "2"));
    }

    public int getAvioesParaPousar() {
        return Integer.parseInt(props.getProperty("avioesParaPousar", "3"));
    }

    public int getAvioesParaDecolar() {
        return Integer.parseInt(props.getProperty("avioesParaDecolar", "3"));
    }

    public int getDelaySimulacao() {
        return Integer.parseInt(props.getProperty("delaySimulacao", "2000"));
    }

    public int getIntervaloAtualizacaoPainel() {
        return Integer.parseInt(props.getProperty("intervaloAtualizacaoPainel", "1000"));
    }
}