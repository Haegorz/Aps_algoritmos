import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Benchmark {

    public static final String PASTA_SAIDA = "saida";
    public static final int REPETICOES = 5;

    private static final long ESTOUROU_PILHA = -1;
    private static final long NAO_ORDENOU = -2;

    public static String caminhoSaida(String nomeArquivo) {
        File pasta = new File(PASTA_SAIDA);
        pasta.mkdirs();
        return new File(pasta, nomeArquivo).getPath();
    }

    public void testarAlgoritmo(AlgoritmoOrdenacao algoritmo, int[] dados, boolean mostrarPassos) throws IOException {
        String nome = algoritmo.getNome();

        System.out.println("\n=============================================");
        System.out.println("Ordenacao com " + nome + ":");
        System.out.println("=============================================");
        if (mostrarPassos) {
            System.out.println("Passos da ordenacao:");
            System.out.println("---------------------------------------------");
        }

        boolean estourouPilha = false;
        long inicio = System.nanoTime();

        try {
            algoritmo.ordenar(dados, mostrarPassos);
        } catch (StackOverflowError e) {
            estourouPilha = true;
            System.out.println("\nATENCAO: " + nome + " estourou a pilha de recursao (StackOverflowError).");
            System.out.println("Costuma acontecer com vetores grandes ja ordenados ou decrescentes.");
            System.out.println("Tente um vetor menor para esse teste especifico.");
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;

        if (estourouPilha) {
            return;
        }

        System.out.println("\n" + nome + " -> tempo de ordenacao: " + tempoMs + " ms");
        System.out.print("Depois de ordenar: ");
        imprimirArray(dados, 10);

        if (estaOrdenado(dados)) {
            System.out.println(nome + ": ordenacao CORRETA (vetor em ordem crescente).");
        } else {
            System.out.println(nome + ": *** ATENCAO *** o vetor NAO ficou ordenado corretamente!");
        }

        String nomeArquivo = "dados_depois_" + nome.toLowerCase().replace(" ", "_") + ".txt";
        String caminho = salvarArrayEmArquivo(dados, nomeArquivo);
        System.out.println("Listagem completa (DEPOIS) salva em: " + caminho);
    }

    public static void aquecer(List<AlgoritmoOrdenacao> algoritmos) {
        int[] base = GeradorDados.gerarDadosAleatorios(2000, 0, 100_000);
        for (AlgoritmoOrdenacao algoritmo : algoritmos) {
            for (int i = 0; i < 5; i++) {
                algoritmo.ordenar(copiarArray(base), false);
            }
        }
    }

    public static ResultadoTeste medirTempoMedio(AlgoritmoOrdenacao algoritmo, int[] original, String cenario) {
        long[] tempos = new long[REPETICOES];

        for (int r = 0; r < REPETICOES; r++) {
            long tempo = medirNanos(algoritmo, original);
            if (tempo == ESTOUROU_PILHA) {
                return new ResultadoTeste(algoritmo.getNome(), cenario, original.length, 0, 0,
                        ResultadoTeste.ESTOUROU_PILHA);
            }
            if (tempo == NAO_ORDENOU) {
                return new ResultadoTeste(algoritmo.getNome(), cenario, original.length, 0, 0,
                        ResultadoTeste.NAO_ORDENOU);
            }
            tempos[r] = tempo;
        }

        long soma = 0;
        for (long t : tempos) {
            soma += t;
        }
        long[] ordenados = tempos.clone();
        Arrays.sort(ordenados);

        double mediaMs = (soma / (double) REPETICOES) / 1_000_000.0;
        double medianaMs = ordenados[REPETICOES / 2] / 1_000_000.0;
        return new ResultadoTeste(algoritmo.getNome(), cenario, original.length, mediaMs, medianaMs,
                ResultadoTeste.OK);
    }

    private static long medirNanos(AlgoritmoOrdenacao algoritmo, int[] original) {
        int[] copia = copiarArray(original);

        long inicio = System.nanoTime();
        try {
            algoritmo.ordenar(copia, false);
        } catch (StackOverflowError e) {
            return ESTOUROU_PILHA;
        }
        long fim = System.nanoTime();

        if (!estaOrdenado(copia)) {
            return NAO_ORDENOU;
        }
        return fim - inicio;
    }

    public static boolean estaOrdenado(int[] dados) {
        for (int i = 0; i < dados.length - 1; i++) {
            if (dados[i] > dados[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static String salvarArrayEmArquivo(int[] dados, String nomeArquivo) throws IOException {
        String caminho = caminhoSaida(nomeArquivo);
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminho))) {
            for (int valor : dados) {
                escritor.write(String.valueOf(valor));
                escritor.newLine();
            }
        }
        return caminho;
    }

    public static void imprimirArray(int[] dados, int limite) {
        int qtd = Math.min(limite, dados.length);
        System.out.print("[");
        for (int i = 0; i < qtd; i++) {
            System.out.print(dados[i]);
            if (i < qtd - 1) {
                System.out.print(", ");
            }
        }
        if (dados.length > limite) {
            System.out.print(", ...");
        }
        System.out.println("]");
    }

    public static int[] copiarArray(int[] original) {
        int[] copia = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }
}
