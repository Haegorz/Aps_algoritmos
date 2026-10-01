import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] dados;

        System.out.println("=== Sistema de Ordenacao ===");
        System.out.println("1 - Gerar dados aleatorios (dados internos)");
        System.out.println("2 - Gerar dados internos com cenarios (ordenados, decrescente e semi ordenado)");
        System.out.println("3 - Digitar os dados manualmente (dados dado pelo usuario )");
        System.out.println("4 - Ler dados de um arquivo texto (dados externos)");
        System.out.print("Escolha uma opcao: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1: {
                System.out.print("Quantos valores deseja gerar? ");
                int tamanho = scanner.nextInt();
                dados = GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000);
                break;
            }
            case 2: {
                System.out.print("Quantos valores deseja gerar? ");
                int tamanho = scanner.nextInt();

                System.out.println("Cenarios disponiveis:");
                System.out.println("1 - Ordenado (melhor caso)");
                System.out.println("2 - Decrescente (pior caso para varios algoritmos)");
                System.out.println("3 - Semi-ordenado (10% de desordem)");
                System.out.print("Escolha o cenario: ");
                int cenario = scanner.nextInt();

                if (cenario == 1) {
                    dados = GeradorDados.gerarDadosOrdenados(tamanho);
                } else if (cenario == 2) {
                    dados = GeradorDados.gerarDadosDecrescentes(tamanho);
                } else if (cenario == 3) {
                    dados = GeradorDados.gerarDadosSemiOrdenados(tamanho, 0.10);
                } else {
                    System.out.println("Opcao invalida, usando aleatorio.");
                    dados = GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000);
                }
                break;
            }
            case 3: {
                System.out.print("Quantos valores voce vai digitar? ");
                int tamanho = scanner.nextInt();
                dados = GeradorDados.lerDadosDoUsuario(scanner, tamanho);
                break;
            }
            case 4: {
                System.out.print("Digite o caminho do arquivo: ");
                String caminho = scanner.next();
                dados = GeradorDados.lerDadosDeArquivo(caminho);
                System.out.println(dados.length + " valores lidos do arquivo.");
                break;
            }
            default:
                System.out.println("Opcao invalida.");
                return;
        }

        System.out.print("Mostrar passo a passo? Recomendado so com poucos elementos [s/n]: ");
        String resposta = scanner.next();
        boolean mostrarPassos = resposta.equalsIgnoreCase("s");
        if (mostrarPassos && dados.length > 50) {
            System.out.println("Aviso: com " + dados.length + " elementos, o passo a passo vai gerar MUITA saida");
            System.out.println("no console e vai deixar o tempo medido sem sentido (a impressao toma muito tempo).");
        }

        System.out.print("\nDados ANTES de ordenar: ");
        Benchmark.imprimirArray(dados, 10);

        Benchmark.salvarArrayEmArquivo(dados, "dados_antes.txt");
        System.out.println("Listagem completa (ANTES) salva em: dados_antes.txt");

        List<AlgoritmoOrdenacao> algoritmos = List.of(
                new InsertionSort(), new QuickSort(), new HeapSort(), new MergeSort()
        );

        Benchmark benchmark = new Benchmark();
        for (AlgoritmoOrdenacao algoritmo : algoritmos) {
            benchmark.testarAlgoritmo(algoritmo, Benchmark.copiarArray(dados), mostrarPassos);
        }
    }
}
