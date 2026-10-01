import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeradorDados {

    private static final Random RANDOM = new Random();

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
            System.out.print("Valor " + (i + 1) + ": ");
            dados[i] = scanner.nextInt();
        }
        return dados;
    }

    public static int[] lerDadosDeArquivo(String caminho) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(caminho));
        List<Integer> valores = new ArrayList<>();

        String linha;
        while ((linha = leitor.readLine()) != null) {
            linha = linha.trim();
            if (linha.isEmpty() || linha.startsWith("#")) {
                continue;
            }
            valores.add(Integer.parseInt(linha));
        }
        leitor.close();

        int[] dados = new int[valores.size()];
        for (int i = 0; i < dados.length; i++) {
            dados[i] = valores.get(i);
        }
        return dados;
    }
}
