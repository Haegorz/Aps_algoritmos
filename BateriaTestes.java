import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BateriaTestes {

    private static final int[] TAMANHOS = {100, 1000, 5000, 10000, 50000, 100000};
    private static final String[] CENARIOS = {"Aleatorio", "Ordenado", "Decrescente", "Semi-ordenado"};

    public static String executar(List<AlgoritmoOrdenacao> algoritmos, String nomeCSV) throws IOException {
        System.out.println("Aquecendo a JVM...");
        Benchmark.aquecer(algoritmos);

        List<ResultadoTeste> resultados = new ArrayList<>();
        int total = TAMANHOS.length * CENARIOS.length * algoritmos.size();
        int atual = 0;

        for (String cenario : CENARIOS) {
            for (int tamanho : TAMANHOS) {
                int[] dados = gerarPorCenario(cenario, tamanho);
                for (AlgoritmoOrdenacao algoritmo : algoritmos) {
                    atual++;
                    ResultadoTeste r = Benchmark.medirTempoMedio(algoritmo, dados, cenario);
                    resultados.add(r);
                    imprimirProgresso(atual, total, r);
                }
            }
        }

        return salvarCSV(resultados, nomeCSV);
    }

    private static int[] gerarPorCenario(String cenario, int tamanho) {
        switch (cenario) {
            case "Ordenado":
                return GeradorDados.gerarDadosOrdenados(tamanho);
            case "Decrescente":
                return GeradorDados.gerarDadosDecrescentes(tamanho);
            case "Semi-ordenado":
                return GeradorDados.gerarDadosSemiOrdenados(tamanho, 0.10);
            default:
                return GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000);
        }
    }

    private static void imprimirProgresso(int atual, int total, ResultadoTeste r) {
        String tempo = r.ok() ? String.format("%.4f ms (media)", r.getMediaMs()) : r.getStatus();
        System.out.printf("[%d/%d] %-14s | %-13s | N=%-7d | %s%n",
                atual, total, r.getAlgoritmo(), r.getCenario(), r.getTamanho(), tempo);
    }

    private static String salvarCSV(List<ResultadoTeste> resultados, String nomeCSV) throws IOException {
        String caminho = Benchmark.caminhoSaida(nomeCSV);
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminho))) {
            escritor.write("Algoritmo;Cenario;Tamanho;Media_ms;Mediana_ms;Status");
            escritor.newLine();
            for (ResultadoTeste r : resultados) {
                escritor.write(r.linhaCSV());
                escritor.newLine();
            }
        }
        return caminho;
    }
}
