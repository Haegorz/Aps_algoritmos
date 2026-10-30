public class InsertionSort implements AlgoritmoOrdenacao {

    @Override
    public void ordenar(int[] dados, boolean mostrarPassos) {
        int tam = dados.length;
        int j;
        for (j = 1; j < tam; j++) {
            int atual = dados[j];
            int i = j - 1;
            while (i >= 0 && dados[i] > atual) {
                dados[i + 1] = dados[i];
                i--;
            }
            dados[i + 1] = atual;

            if (mostrarPassos) {
                imprimirTrecho(dados, 0, j - 1);
            }
        }
        if (mostrarPassos) {
            imprimirTrecho(dados, 0, j - 1);
        }
    }

    private void imprimirTrecho(int[] dados, int inicio, int fim) {
        for (int k = inicio; k <= fim; k++) {
            System.out.print(dados[k] + " ");
        }
        System.out.println();
    }

    @Override
    public String getNome() {
        return "Insertion Sort";
    }
}
