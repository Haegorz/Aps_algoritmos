import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {

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
        salvarArrayEmArquivo(dados, nomeArquivo);
        System.out.println("Listagem completa (DEPOIS) salva em: " + nomeArquivo);
    }

    public static boolean estaOrdenado(int[] dados) {
        for (int i = 0; i < dados.length - 1; i++) {
            if (dados[i] > dados[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public static void salvarArrayEmArquivo(int[] dados, String caminho) throws IOException {
        BufferedWriter escritor = new BufferedWriter(new FileWriter(caminho));
        for (int valor : dados) {
            escritor.write(String.valueOf(valor));
            escritor.newLine();
        }
        escritor.close();
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
