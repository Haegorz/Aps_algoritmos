import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Versao BASE do sistema de comparacao de algoritmos de ordenacao.
 * Tudo em uma unica classe, de proposito, para ficar facil de entender e
 * de expandir aos poucos, depois da para deixar em outros .java para ficar mais organizado.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] dados;

        System.out.println("=== Sistema de Ordenacao - Versao Base ===");
        System.out.println("1 - Gerar dados aleatorios (dados internos)");
        System.out.println("2 - Ler dados de um arquivo texto (dados externos)");
        System.out.print("Escolha uma opcao: ");
        int opcao = scanner.nextInt();

        if (opcao == 1) {
            System.out.print("Quantos valores deseja gerar? ");
            int tamanho = scanner.nextInt();
            dados = gerarDadosAleatorios(tamanho, 0, 1000000);
        } else if (opcao == 2) {
            System.out.print("Digite o caminho do arquivo: ");
            String caminho = scanner.next();
            dados = lerDadosDeArquivo(caminho);
            System.out.println(dados.length + " valores lidos do arquivo.");
        } else {
            System.out.println("Opcao invalida.");
            return;
        }

        System.out.print("\nDados ANTES de ordenar: ");
        imprimirArray(dados, 10);

        // Testa cada algoritmo separadamente, sempre com uma COPIA dos dados
        // originais (assim todos ordenam exatamente o mesmo conjunto de dados).
        testarAlgoritmo("Insertion Sort", copiarArray(dados));
        testarAlgoritmo("Quick Sort", copiarArray(dados));
        testarAlgoritmo("Heap Sort", copiarArray(dados));
        testarAlgoritmo("Merge Sort", copiarArray(dados));
    }

    /** Executa UM algoritmo, mede o tempo gasto e mostra o resultado. */
    static void testarAlgoritmo(String nome, int[] dados) {
        long inicio = System.nanoTime();

        if (nome.equals("Insertion Sort")) {
            insertionSort(dados);
        } else if (nome.equals("Heap Sort")) {
            heapSort(dados);
        } else if (nome.equals("Merge Sort")) {
            mergeSort(dados, 0, dados.length - 1);
        } else if (nome.equals("Quick Sort")) {
            quickSort(dados, 0, dados.length - 1);
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;

        System.out.println("\n" + nome + " -> tempo de ordenacao: " + tempoMs + " ms");
        System.out.print("Depois de ordenar: ");
        imprimirArray(dados, 10);
    }

    // ALGORITMOS DE ORDENACAO

    static void insertionSort(int[] dados) {
        int n = dados.length;
        for (int i = 1; i < n; i++) {
            int atual = dados[i];
            int j = i - 1;
            // Empurra os elementos maiores que "atual" uma posicao para a direita
            while (j >= 0 && dados[j] > atual) {
                dados[j + 1] = dados[j];
                j--;
            }
            dados[j + 1] = atual;
        }
    }

    static void heapSort(int[] dados) {
        int n = dados.length;

        // Constroi o heap (max-heap): reorganiza o array a partir do meio para tras
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(dados, n, i);
        }

        // Retira o maior elemento (raiz) e o coloca no final, repetindo o processo
        for (int i = n - 1; i > 0; i--) {
            int temp = dados[0];
            dados[0] = dados[i];
            dados[i] = temp;
            heapify(dados, i, 0);
        }
    }

    // Garante que a subarvore com raiz no indice "i" respeite a propriedade de max-heap
    static void heapify(int[] dados, int tamanho, int i) {
        int maior = i;
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;

        if (esquerda < tamanho && dados[esquerda] > dados[maior]) {
            maior = esquerda;
        }
        if (direita < tamanho && dados[direita] > dados[maior]) {
            maior = direita;
        }
        if (maior != i) {
            int temp = dados[i];
            dados[i] = dados[maior];
            dados[maior] = temp;
            heapify(dados, tamanho, maior);
        }
    }

    static void mergeSort(int[] dados, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSort(dados, inicio, meio);
            mergeSort(dados, meio + 1, fim);
            intercalar(dados, inicio, meio, fim);
        }
    }

    static void intercalar(int[] dados, int inicio, int meio, int fim) {
        int[] esquerda = new int[meio - inicio + 1];
        int[] direita = new int[fim - meio];

        for (int i = 0; i < esquerda.length; i++) {
            esquerda[i] = dados[inicio + i];
        }
        for (int i = 0; i < direita.length; i++) {
            direita[i] = dados[meio + 1 + i];
        }

        int i = 0, j = 0, k = inicio;
        while (i < esquerda.length && j < direita.length) {
            if (esquerda[i] <= direita[j]) {
                dados[k] = esquerda[i];
                i++;
            } else {
                dados[k] = direita[j];
                j++;
            }
            k++;
        }
        while (i < esquerda.length) {
            dados[k] = esquerda[i];
            i++;
            k++;
        }
        while (j < direita.length) {
            dados[k] = direita[j];
            j++;
            k++;
        }
    }

    // Pivo = ultimo elemento (versao classica de livro-texto). Reparem que
    // isso degrada para O(n^2) em dados ja ordenados/decrescentes -- e um
    // otimo ponto pra investigar e melhorar depois (ex: pivo do meio).
    static void quickSort(int[] dados, int inicio, int fim) {
        if (inicio < fim) {
            int posicao = particionar(dados, inicio, fim);
            quickSort(dados, inicio, posicao - 1);
            quickSort(dados, posicao + 1, fim);
        }
    }

    static int particionar(int[] dados, int inicio, int fim) {
        int pivo = dados[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (dados[j] <= pivo) {
                i++;
                int temp = dados[i];
                dados[i] = dados[j];
                dados[j] = temp;
            }
        }
        int temp = dados[i + 1];
        dados[i + 1] = dados[fim];
        dados[fim] = temp;
        return i + 1;
    }

    // GERACAO E LEITURA DE DADOS

    static int[] gerarDadosAleatorios(int tamanho, int min, int max) {
        Random random = new Random();
        int[] dados = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            dados[i] = min + random.nextInt(max - min + 1);
        }
        return dados;
    }

    // Le um valor inteiro por linha. Linhas vazias ou comecando com "#" sao ignoradas.
    static int[] lerDadosDeArquivo(String caminho) throws IOException {
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

    // UTILITARIOS

    static int[] copiarArray(int[] original) {
        int[] copia = new int[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }

    static void imprimirArray(int[] dados, int limite) {
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
}
