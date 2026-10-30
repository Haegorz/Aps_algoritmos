import java.util.Locale;

public class ResultadoTeste {

    public static final String OK = "OK";
    public static final String ESTOUROU_PILHA = "ESTOUROU_PILHA";
    public static final String NAO_ORDENOU = "NAO_ORDENOU";

    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    private final String algoritmo;
    private final String cenario;
    private final int tamanho;
    private final double mediaMs;
    private final double medianaMs;
    private final String status;

    public ResultadoTeste(String algoritmo, String cenario, int tamanho,
                          double mediaMs, double medianaMs, String status) {
        this.algoritmo = algoritmo;
        this.cenario = cenario;
        this.tamanho = tamanho;
        this.mediaMs = mediaMs;
        this.medianaMs = medianaMs;
        this.status = status;
    }

    public boolean ok() {
        return status.equals(OK);
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public String getCenario() {
        return cenario;
    }

    public int getTamanho() {
        return tamanho;
    }

    public double getMediaMs() {
        return mediaMs;
    }

    public double getMedianaMs() {
        return medianaMs;
    }

    public String getStatus() {
        return status;
    }

    public String linhaCSV() {
        if (!ok()) {
            return algoritmo + ";" + cenario + ";" + tamanho + ";;;" + status;
        }
        return algoritmo + ";" + cenario + ";" + tamanho + ";"
                + String.format(BR, "%.4f", mediaMs) + ";"
                + String.format(BR, "%.4f", medianaMs) + ";" + status;
    }
}
