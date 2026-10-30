import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeradorDados {

    private static final Random RANDOM = new Random();
    private static final int TIMESTAMP_BASE = 1_700_000_000;
    private static final int JANELA_SEGUNDOS = 60_000_000;

    public static int[] gerarDadosAleatorios(int tamanho, int min, int max) {
        int[] dados = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            dados[i] = min + RANDOM.nextInt(max - min + 1);
        }
        return dados;
    }

    public static int[] gerarDadosOrdenados(int tamanho) {
        int[] dados = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            dados[i] = i;
        }
        return dados;
    }

    public static int[] gerarDadosDecrescentes(int tamanho) {
        int[] dados = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            dados[i] = tamanho - i;
        }
        return dados;
    }

    public static int[] gerarDadosSemiOrdenados(int tamanho, double percentualDesordem) {
        int[] dados = gerarDadosOrdenados(tamanho);
        int trocas = (int) (tamanho * percentualDesordem);
        for (int i = 0; i < trocas; i++) {
            int a = RANDOM.nextInt(tamanho);
            int b = RANDOM.nextInt(tamanho);
            int temp = dados[a];
            dados[a] = dados[b];
            dados[b] = temp;
        }
        return dados;
    }

    public static int[] lerDadosDoUsuario(Scanner scanner, int tamanho) {
        int[] dados = new int[tamanho];
        System.out.println("Digite " + tamanho + " valores inteiros (um por vez):");
        for (int i = 0; i < tamanho; i++) {
            dados[i] = Entrada.lerInteiro(scanner, "Valor " + (i + 1) + ": ");
        }
        return dados;
    }

    public static int[] lerDadosDeArquivo(String caminho) throws IOException {
        List<Integer> valores = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminho))) {
            String linha;
            int numeroLinha = 0;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                for (String parte : linha.split("[,;\\s]+")) {
                    if (parte.isEmpty()) {
                        continue;
                    }
                    try {
                        valores.add(Integer.parseInt(parte));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Linha " + numeroLinha + ": \"" + parte + "\" nao e um inteiro valido.");
                    }
                }
            }
        }

        if (valores.isEmpty()) {
            throw new IllegalArgumentException("O arquivo nao contem nenhum valor.");
        }

        int[] dados = new int[valores.size()];
        for (int i = 0; i < dados.length; i++) {
            dados[i] = valores.get(i);
        }
        return dados;
    }

    public static void gerarArquivoCatalogo(String caminho, int quantidade) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(caminho))) {
            escritor.write("# Catalogo simulado de imagens de satelite (codigo = timestamp de captura)");
            escritor.newLine();
            for (int i = 0; i < quantidade; i++) {
                escritor.write(String.valueOf(TIMESTAMP_BASE + RANDOM.nextInt(JANELA_SEGUNDOS)));
                escritor.newLine();
            }
        }
    }
}
