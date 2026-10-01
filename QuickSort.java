public class QuickSort implements AlgoritmoOrdenacao {

    @Override
    public void ordenar(int[] dados, boolean mostrarPassos) {
        quickSort(dados, 0, dados.length, mostrarPassos);
    }

    private void quickSort(int[] v, int inicio, int tam, boolean mostrarPassos) {
        if (tam <= 1) {
            return;
        }

        int pivot = v[inicio];
        int a = inicio + 1;
        int b = inicio + tam - 1;

        do {
            while ((a < inicio + tam) && (v[a] <= pivot)) {
                a++;
            }
            while (v[b] > pivot) {
                b--;
            }
            if (a < b) {
                int temp = v[a];
                v[a] = v[b];
                v[b] = temp;
                a++;
                b--;
            }
            if (mostrarPassos) {
                imprimirTrecho(v, inicio, inicio + tam - 1);
            }
        } while (a <= b);

        v[inicio] = v[b];
        v[b] = pivot;
        if (mostrarPassos) {
            imprimirTrecho(v, inicio, inicio + tam - 1);
        }

        quickSort(v, inicio, b - inicio, mostrarPassos);
        quickSort(v, a, tam - (a - inicio), mostrarPassos);

        if (mostrarPassos) {
            imprimirTrecho(v, inicio, inicio + tam - 1);
        }
    }

    private void imprimirTrecho(int[] v, int inicio, int fim) {
        for (int k = inicio; k <= fim; k++) {
            System.out.print(v[k] + " ");
        }
        System.out.println();
    }

    @Override
    public String getNome() {
        return "Quick Sort";
    }
}
