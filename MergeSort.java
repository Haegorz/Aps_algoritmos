public class MergeSort implements AlgoritmoOrdenacao {

    @Override
    public void ordenar(int[] dados, boolean mostrarPassos) {
        mergeSort(dados, 0, dados.length - 1, mostrarPassos);
    }

    private void mergeSort(int[] vetor, int comeco, int fim, boolean mostrarPassos) {
        if (comeco < fim) {
            int meio = (fim + comeco) / 2;

            if (mostrarPassos) {
                for (int i = comeco; i <= meio; i++) {
                    System.out.print(vetor[i] + " ");
                }
                System.out.println();
            }
            mergeSort(vetor, comeco, meio, mostrarPassos);

            if (mostrarPassos) {
                for (int i = meio + 1; i <= fim; i++) {
                    System.out.print(vetor[i] + " ");
                }
                System.out.println();
            }
            mergeSort(vetor, meio + 1, fim, mostrarPassos);

            merge(vetor, comeco, meio, fim, mostrarPassos);
        }
    }

    private void merge(int[] vetor, int comeco, int meio, int fim, boolean mostrarPassos) {
        int posicao1 = comeco, posicao2 = meio + 1, posicaoAux = 0;
        int tam = fim - comeco + 1;
        int[] vetAux = new int[tam];

        while (posicao1 <= meio && posicao2 <= fim) {
            if (vetor[posicao1] < vetor[posicao2]) {
                vetAux[posicaoAux] = vetor[posicao1];
                posicao1++;
            } else {
                vetAux[posicaoAux] = vetor[posicao2];
                posicao2++;
            }
            posicaoAux++;
        }
        while (posicao1 <= meio) {
            vetAux[posicaoAux] = vetor[posicao1];
            posicaoAux++;
            posicao1++;
        }
        while (posicao2 <= fim) {
            vetAux[posicaoAux] = vetor[posicao2];
            posicaoAux++;
            posicao2++;
        }

        for (posicaoAux = comeco; posicaoAux <= fim; posicaoAux++) {
            vetor[posicaoAux] = vetAux[posicaoAux - comeco];
            if (mostrarPassos) {
                System.out.print(vetor[posicaoAux] + " ");
            }
        }
        if (mostrarPassos) {
            System.out.println();
        }
    }

    @Override
    public String getNome() {
        return "Merge Sort";
    }
}
