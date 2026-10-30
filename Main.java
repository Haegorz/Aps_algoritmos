import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final int TAMANHO_MAXIMO = 1_000_000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<AlgoritmoOrdenacao> algoritmos = List.of(
                new InsertionSort(), new QuickSort(), new HeapSort(), new MergeSort()
        );

        System.out.println("=== Sistema de Analise de Performance de Algoritmos de Ordenacao ===");

        try {
            boolean continuar = true;
            while (continuar) {
                exibirMenu();
                int opcao = Entrada.lerInteiro(scanner, "Escolha uma opcao: ", 0, 6);

                try {
                    switch (opcao) {
                        case 1:
                            opcaoAleatorios(scanner, algoritmos);
                            break;
                        case 2:
                            opcaoCenario(scanner, algoritmos);
                            break;
                        case 3:
                            opcaoManual(scanner, algoritmos);
                            break;
                        case 4:
                            opcaoArquivo(scanner, algoritmos);
                            break;
                        case 5:
                            opcaoGerarCatalogo(scanner);
                            break;
                        case 6:
                            opcaoBateria(scanner, algoritmos);
                            break;
                        default:
                            continuar = false;
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Erro: arquivo ou pasta nao encontrado (" + e.getMessage() + ").");
                } catch (IOException e) {
                    System.out.println("Erro de leitura/gravacao de arquivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro nos dados: " + e.getMessage());
                } catch (OutOfMemoryError e) {
                    System.out.println("Erro: memoria insuficiente para esse tamanho de vetor.");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada.");
        }

        System.out.println("Encerrando o sistema.");
    }

    private static void exibirMenu() {
        System.out.println("\n------------------------- MENU -------------------------");
        System.out.println("1 - Dados internos: aleatorios");
        System.out.println("2 - Dados internos: cenario (ordenado / decrescente / semi-ordenado)");
        System.out.println("3 - Dados internos: digitados manualmente");
        System.out.println("4 - Dados externos: ler de arquivo texto");
        System.out.println("5 - Gerar arquivo de exemplo (catalogo simulado de imagens de satelite)");
        System.out.println("6 - Bateria automatica de testes (varios tamanhos e cenarios) + CSV");
        System.out.println("0 - Sair");
        System.out.println("--------------------------------------------------------");
    }

    private static void opcaoAleatorios(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos) throws IOException {
        int tamanho = Entrada.lerInteiro(scanner,
                "Quantos valores deseja gerar (1 a " + TAMANHO_MAXIMO + ")? ", 1, TAMANHO_MAXIMO);
        int[] dados = GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000);
        executarComparacao(scanner, algoritmos, dados);
    }

    private static void opcaoCenario(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos) throws IOException {
        int tamanho = Entrada.lerInteiro(scanner,
                "Quantos valores deseja gerar (1 a " + TAMANHO_MAXIMO + ")? ", 1, TAMANHO_MAXIMO);

        System.out.println("Cenarios disponiveis:");
        System.out.println("1 - Ordenado (melhor caso)");
        System.out.println("2 - Decrescente (pior caso para varios algoritmos)");
        System.out.println("3 - Semi-ordenado (10% de trocas)");
        int cenario = Entrada.lerInteiro(scanner, "Escolha o cenario: ", 1, 3);

        int[] dados;
        if (cenario == 1) {
            dados = GeradorDados.gerarDadosOrdenados(tamanho);
        } else if (cenario == 2) {
            dados = GeradorDados.gerarDadosDecrescentes(tamanho);
        } else {
            dados = GeradorDados.gerarDadosSemiOrdenados(tamanho, 0.10);
        }
        executarComparacao(scanner, algoritmos, dados);
    }

    private static void opcaoManual(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos) throws IOException {
        int tamanho = Entrada.lerInteiro(scanner, "Quantos valores voce vai digitar (1 a 1000)? ", 1, 1000);
        int[] dados = GeradorDados.lerDadosDoUsuario(scanner, tamanho);
        executarComparacao(scanner, algoritmos, dados);
    }

    private static void opcaoArquivo(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos) throws IOException {
        String caminho = Entrada.lerTexto(scanner,
                "Caminho do arquivo (ex: " + Benchmark.PASTA_SAIDA + "/catalogo_satelite.txt): ");
        int[] dados = GeradorDados.lerDadosDeArquivo(caminho);
        System.out.println(dados.length + " valores lidos do arquivo.");
        executarComparacao(scanner, algoritmos, dados);
    }

    private static void opcaoGerarCatalogo(Scanner scanner) throws IOException {
        int quantidade = Entrada.lerInteiro(scanner,
                "Quantos registros simular (1 a " + TAMANHO_MAXIMO + ")? ", 1, TAMANHO_MAXIMO);
        String nome = Entrada.lerTexto(scanner, "Nome do arquivo (ex: catalogo_satelite.txt): ");
        String caminho = Benchmark.caminhoSaida(nome);
        GeradorDados.gerarArquivoCatalogo(caminho, quantidade);
        System.out.println("Arquivo gerado em: " + caminho);
    }

    private static void opcaoBateria(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos) throws IOException {
        System.out.println("A bateria testa 4 algoritmos x 4 cenarios x 6 tamanhos (100 a 100000),");
        System.out.println("com " + Benchmark.REPETICOES + " repeticoes cada. Pode levar alguns minutos.");
        if (!Entrada.lerSimNao(scanner, "Executar agora? [s/n]: ")) {
            return;
        }
        String caminho = BateriaTestes.executar(algoritmos, "resultados.csv");
        System.out.println("\nBateria concluida. Resultados salvos em: " + caminho);
    }

    private static void executarComparacao(Scanner scanner, List<AlgoritmoOrdenacao> algoritmos, int[] dados)
            throws IOException {
        boolean mostrarPassos = Entrada.lerSimNao(scanner,
                "Mostrar passo a passo? Recomendado so com poucos elementos [s/n]: ");
        if (mostrarPassos && dados.length > 50) {
            System.out.println("Aviso: com " + dados.length + " elementos o passo a passo gera MUITA saida");
            System.out.println("e o tempo medido deixa de fazer sentido (a impressao domina o tempo).");
            mostrarPassos = Entrada.lerSimNao(scanner, "Continuar mesmo assim? [s/n]: ");
        }
        if (dados.length > 50_000) {
            System.out.println("Aviso: Insertion Sort e O(n^2) e pode demorar bastante com " + dados.length + " elementos.");
        }

        System.out.print("\nDados ANTES de ordenar: ");
        Benchmark.imprimirArray(dados, 10);
        String caminhoAntes = Benchmark.salvarArrayEmArquivo(dados, "dados_antes.txt");
        System.out.println("Listagem completa (ANTES) salva em: " + caminhoAntes);

        Benchmark benchmark = new Benchmark();
        for (AlgoritmoOrdenacao algoritmo : algoritmos) {
            benchmark.testarAlgoritmo(algoritmo, Benchmark.copiarArray(dados), mostrarPassos);
        }
    }
}
