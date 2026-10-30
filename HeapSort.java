public class HeapSort implements AlgoritmoOrdenacao {

    @Override
    public void ordenar(int[] dados, boolean mostrarPassos) {
        int tam = dados.length;
        montaHeapMax(dados, tam, mostrarPassos);
        for (int i = tam - 1; i >= 1; i--) {
            int temp = dados[0];
            dados[0] = dados[i];
            dados[i] = temp;
            heapifica(dados, 0, i - 1, mostrarPassos);
        }
    }

    private void montaHeapMax(int[] v, int tam, boolean mostrarPassos) {
        for (int i = tam / 2; i >= 0; i--) {
            heapifica(v, i, tam - 1, mostrarPassos);
        }
    }

    private void heapifica(int[] v, int pai, int tam, boolean mostrarPassos) {
        boolean percorreu = false;
        while ((pai * 2 <= tam) && !percorreu) {
            int folhaMaior;
            if (pai * 2 == tam) {
                folhaMaior = pai * 2;
            } else if (v[pai * 2] > v[pai * 2 + 1]) {
                folhaMaior = pai * 2;
            } else {
                folhaMaior = pai * 2 + 1;
            }

            if (v[pai] < v[folhaMaior]) {
                int temp = v[pai];
                v[pai] = v[folhaMaior];
                v[folhaMaior] = temp;
                pai = folhaMaior;
            } else {
                percorreu = true;
            }

            if (mostrarPassos) {
                for (int k = 0; k < v.length; k++) {
                    System.out.print(v[k] + " ");
                }
                System.out.println();
            }
        }
    }

    @Override
    public String getNome() {
        return "Heap Sort";
    }
}
