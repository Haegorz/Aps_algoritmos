package aps;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BateriaTestes {

    public enum Cenario { ALEATORIO, DECRESCENTE, CRESCENTE }

    public static int[] gerarVetorPublico(int tamanho, Cenario cenario) {
        int[] arr = new int[tamanho];
        Random rand = new Random(42);
        for (int i = 0; i < tamanho; i++) {
            arr[i] = rand.nextInt(tamanho * 10);
        }
        if (cenario == Cenario.CRESCENTE) {
            Arrays.sort(arr);
        } else if (cenario == Cenario.DECRESCENTE) {
            Arrays.sort(arr);
            for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
                int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        return arr;
    }

    public static void executarAlgoritmoPublico(String alg, int[] arr) {
        switch (alg) {
            case "QuickSort": AlgoritmosOrdenacao.quickSort(arr, 0, arr.length - 1); break;
            case "HeapSort": AlgoritmosOrdenacao.heapSort(arr); break;
            case "InsertionSort": AlgoritmosOrdenacao.insertionSort(arr); break;
            case "MergeSort": AlgoritmosOrdenacao.mergeSort(arr, 0, arr.length - 1); break;
            case "BubbleSort": AlgoritmosOrdenacao.bubbleSort(arr); break;
            case "SelectionSort": AlgoritmosOrdenacao.selectionSort(arr); break;
        }
    }

    public static void executarBateriaEExportarCSV(String caminhoArquivoCSV) {
        int[] tamanhos = {100, 1000, 5000, 10000, 50000, 100000};
        String[] algoritmos = {"InsertionSort", "QuickSort", "HeapSort", "MergeSort", "BubbleSort", "SelectionSort"};
        int repeticoes = 5;

        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivoCSV))) {
            writer.println("Cenario;Tamanho;Algoritmo;TempoMedio_ns;TempoMedio_ms");
            for (Cenario cenario : Cenario.values()) {
                for (int n : tamanhos) {
                    for (String alg : algoritmos) {
                        if ((alg.equals("BubbleSort") || alg.equals("SelectionSort") || alg.equals("InsertionSort")) && n > 50000) {
                            writer.println(cenario + ";" + n + ";" + alg + ";N/A;N/A");
                            continue;
                        }
                        long somaTempo = 0;
                        for (int r = 0; r < repeticoes; r++) {
                            int[] vetor = gerarVetorPublico(n, cenario);
                            long inicio = System.nanoTime();
                            executarAlgoritmoPublico(alg, vetor);
                            long fim = System.nanoTime();
                            somaTempo += (fim - inicio);
                        }
                        long tempoMedioNs = somaTempo / repeticoes;
                        double tempoMedioMs = tempoMedioNs / 1_000_000.0;
                        writer.printf("%s;%d;%s;%d;%.4f%n", cenario, n, alg, tempoMedioNs, tempoMedioMs);
                    }
                }
            }
        } catch (IOException ignored) {}
    }
}