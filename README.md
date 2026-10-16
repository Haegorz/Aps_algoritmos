# APS olvao

Programa em Java (console) que ordena um vetor de `int` com Insertion Sort, Quick Sort, Heap Sort e Merge Sort e mede o tempo de execução de cada um sobre os mesmos dados. Os algoritmos seguem as implementações do material da disciplina, portadas de C para Java.

## Estrutura

```
Main.java                  ponto de entrada, menu, orquestração
AlgoritmoOrdenacao.java    interface comum
InsertionSort.java         implements AlgoritmoOrdenacao
QuickSort.java             implements AlgoritmoOrdenacao
HeapSort.java              implements AlgoritmoOrdenacao
MergeSort.java             implements AlgoritmoOrdenacao
GeradorDados.java          geração e leitura de dados (métodos estáticos)
Benchmark.java             cronometragem, validação, saída em arquivo
```

## Interface `AlgoritmoOrdenacao`

```java
void ordenar(int[] dados, boolean mostrarPassos);
String getNome();
```

- `ordenar` ordena `dados` **in place** (não retorna nada).
- `mostrarPassos` é passado por parâmetro e não é estado global, então cada chamada decide se imprime os passos intermediários.
- `getNome` é usado pelo `Benchmark` para as mensagens e para montar o nome do arquivo de saída.

O `Main` mantém `List<AlgoritmoOrdenacao>` com as quatro implementações e itera chamando o mesmo método em todas (polimorfismo), sem `if/else` por nome de algoritmo.

## Fluxo de `Main.main`

1. Lê `opcao` com `Scanner.nextInt()` e faz `switch`:
   - `1`: `GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000)`
   - `2`: pede o cenário e chama `gerarDadosOrdenados`, `gerarDadosDecrescentes` ou `gerarDadosSemiOrdenados(tamanho, 0.10)`
   - `3`: `GeradorDados.lerDadosDoUsuario(scanner, tamanho)`
   - `4`: `GeradorDados.lerDadosDeArquivo(caminho)`
2. Lê `s/n` para `mostrarPassos` (`equalsIgnoreCase("s")`).
3. Imprime os 10 primeiros valores e grava o vetor completo em `dados_antes.txt`.
4. Para cada algoritmo da lista chama `benchmark.testarAlgoritmo(algoritmo, Benchmark.copiarArray(dados), mostrarPassos)`. A cópia garante que todos recebem a mesma entrada e que o vetor original não é alterado.

## `GeradorDados`

Todos os métodos são `static`. Há um único `Random` compartilhado (`private static final`).

| Método | Descrição |
|---|---|
| `gerarDadosAleatorios(tamanho, min, max)` | `min + RANDOM.nextInt(max - min + 1)`, valores em `[min, max]`. |
| `gerarDadosOrdenados(tamanho)` | `dados[i] = i`. |
| `gerarDadosDecrescentes(tamanho)` | `dados[i] = tamanho - i` (valores de `tamanho` a `1`). |
| `gerarDadosSemiOrdenados(tamanho, p)` | Parte do vetor ordenado e faz `(int)(tamanho * p)` trocas entre duas posições sorteadas. O número de trocas é proporcional a `p`, mas o percentual real de elementos deslocados varia, pois as posições sorteadas podem repetir. |
| `lerDadosDoUsuario(scanner, tamanho)` | Laço com `scanner.nextInt()`. |
| `lerDadosDeArquivo(caminho)` | `BufferedReader`, `readLine()` até `null`; faz `trim()`, ignora linhas vazias e as que começam com `#`, converte com `Integer.parseInt` e acumula em `List<Integer>`; ao final copia para `int[]` (unboxing). |

## `Benchmark`

`testarAlgoritmo(algoritmo, dados, mostrarPassos)`:

```java
long inicio = System.nanoTime();
try {
    algoritmo.ordenar(dados, mostrarPassos);
} catch (StackOverflowError e) { ... }
long fim = System.nanoTime();
double tempoMs = (fim - inicio) / 1_000_000.0;
```

- A cronometragem envolve **somente** a chamada a `ordenar`. Geração/leitura dos dados e gravação de arquivos ficam fora do intervalo.
- `System.nanoTime()` é monotônico e adequado para medir intervalos (diferente de `currentTimeMillis`).
- Se ocorrer `StackOverflowError`, a flag `estourouPilha` faz o método retornar sem imprimir tempo nem validar.
- Em seguida chama `estaOrdenado(dados)`, que percorre o vetor e retorna `false` se existir `dados[i] > dados[i+1]`.
- Grava o resultado em `dados_depois_<nome>.txt`, onde `<nome>` é `getNome().toLowerCase().replace(" ", "_")`.

Métodos auxiliares estáticos: `estaOrdenado`, `salvarArrayEmArquivo` (`BufferedWriter`, um valor por linha), `imprimirArray(dados, limite)` (imprime só os `limite` primeiros) e `copiarArray` (cópia elemento a elemento).

## Algoritmos

### InsertionSort

```java
for (j = 1; j < tam; j++) {
    int atual = dados[j];
    int i = j - 1;
    while (i >= 0 && dados[i] > atual) {
        dados[i + 1] = dados[i];
        i--;
    }
    dados[i + 1] = atual;
}
```

- Invariante: antes de cada iteração de `j`, `dados[0..j-1]` está ordenado.
- O elemento `atual` é guardado e os maiores que ele são deslocados uma posição à direita (sem trocas), e então `atual` é inserido na posição livre.
- A comparação é `>` estrito, então elementos iguais não passam uns pelos outros (estável).
- Com `mostrarPassos`, cada iteração imprime `dados[0..j-1]`, igual ao material do professor (o índice `j` não entra no print daquele passo).

### QuickSort

Assinatura interna: `quickSort(int[] v, int inicio, int tam)`, onde `tam` é a **quantidade** de elementos do trecho (não o índice final). `inicio + tam` faz o papel do ponteiro `&v[a]` do código em C.

```java
if (tam <= 1) return;
int pivot = v[inicio];
int a = inicio + 1;
int b = inicio + tam - 1;
do {
    while (a < inicio + tam && v[a] <= pivot) a++;
    while (v[b] > pivot) b--;
    if (a < b) { troca(v[a], v[b]); a++; b--; }
} while (a <= b);
v[inicio] = v[b];
v[b] = pivot;
quickSort(v, inicio, b - inicio);
quickSort(v, a, tam - (a - inicio));
```

- Pivô fixo no primeiro elemento. Particionamento de dois índices (estilo Hoare): `a` avança enquanto `v[a] <= pivot`, `b` recua enquanto `v[b] > pivot`, e se `a < b` os elementos são trocados.
- O segundo `while` não precisa de limite inferior, pois `v[inicio] == pivot` não satisfaz `v[b] > pivot`; o pivô funciona como sentinela e `b` nunca fica abaixo de `inicio`.
- Ao sair do laço (`a > b`), `b` aponta para a última posição com valor `<= pivot`. A troca `v[inicio] = v[b]; v[b] = pivot` coloca o pivô na posição final.
- Subproblemas: esquerda = `[inicio, b-1]` com `b - inicio` elementos; direita = `[a, inicio+tam-1]` com `tam - (a - inicio)` elementos.
- Não é estável.
- Profundidade de recursão: com pivô no primeiro elemento, vetor já ordenado gera partições de tamanho `0` e `tam-1`; vetor decrescente gera `tam-1` e `0`. Nos dois casos a profundidade é O(n). O Java não faz otimização de recursão em cauda, então vetores grandes nesses cenários podem lançar `StackOverflowError` (tratado no `Benchmark`).

### HeapSort

```java
montaHeapMax(v, tam);
for (int i = tam - 1; i >= 1; i--) {
    troca(v[0], v[i]);
    heapifica(v, 0, i - 1);
}
```

- `montaHeapMax` chama `heapifica(v, i, tam-1)` para `i` de `tam/2` até `0`, construindo o max-heap.
- No laço principal, o maior elemento (`v[0]`) vai para a posição `i`, o heap passa a ser `v[0..i-1]` e é restaurado com `heapifica`.
- **Em `heapifica(v, pai, tam)`, o parâmetro `tam` é o último índice válido do heap**, não a quantidade de elementos.
- `heapifica` é iterativa (sift-down): enquanto `pai*2 <= tam` e não houve parada, escolhe o filho maior entre `pai*2` e `pai*2+1` (se `pai*2 == tam` só existe um filho), troca com o pai se `v[pai] < v[folhaMaior]` e desce para esse filho; caso contrário marca `percorreu = true`.
- A indexação de filhos `pai*2` / `pai*2+1` é a de um heap com raiz no índice 1, aplicada a um vetor que começa em 0. Para `pai = 0`, `pai*2` é o próprio 0, então a raiz só tem de fato o filho `1`; a partir do índice 1 os filhos de `i` são `2i` e `2i+1`. O resultado continua sendo um heap válido sobre essa árvore e o algoritmo ordena corretamente, mas a árvore não é a usual `2i+1 / 2i+2`.
- Não usa recursão nem memória auxiliar, e não é estável.

### MergeSort

```java
mergeSort(v, comeco, fim):
    if (comeco < fim) {
        meio = (fim + comeco) / 2;
        mergeSort(v, comeco, meio);
        mergeSort(v, meio + 1, fim);
        merge(v, comeco, meio, fim);
    }
```

- `merge` cria `vetAux = new int[fim - comeco + 1]`, percorre as duas metades ordenadas `[comeco, meio]` e `[meio+1, fim]` com `posicao1` e `posicao2`, copia o menor para `vetAux`, esgota a metade restante e copia `vetAux` de volta para `vetor[comeco..fim]`.
- A comparação é `vetor[posicao1] < vetor[posicao2]` (estrita). Em empate o elemento da direita é copiado primeiro, então esta versão **não é estável**.
- Profundidade da recursão: O(log n).
- Um `new int[...]` por chamada de `merge` gera muitas alocações pequenas. O custo assintótico não muda, mas há pressão sobre o coletor de lixo e isso entra no tempo medido.

## Complexidade

| Algoritmo | Melhor | Médio | Pior | Memória extra | Estável |
|---|---|---|---|---|---|
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) | sim |
| Quick Sort (pivô = 1º) | O(n log n) | O(n log n) | O(n²) | O(log n) médio, O(n) pior (pilha) | não |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | não |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | não (nesta versão) |

- Insertion Sort é O(n) no melhor caso porque, com a entrada já ordenada, o `while` interno nunca executa.
- No Quick Sort, os cenários ordenado e decrescente deste projeto coincidem com o pior caso do pivô fixo no primeiro elemento.

## Arquivos gerados

Gravados no diretório de execução:

- `dados_antes.txt`: vetor completo antes da ordenação
- `dados_depois_insertion_sort.txt`, `dados_depois_quick_sort.txt`, `dados_depois_heap_sort.txt`, `dados_depois_merge_sort.txt`: vetor completo após cada algoritmo

## Pontos de atenção na execução

- Com `mostrarPassos = true`, as chamadas de `System.out.print` passam a ser executadas dentro do trecho cronometrado, então o tempo medido reflete principalmente a E/S. Para medir desempenho, usar `n`.
- Há uma única execução por algoritmo, na ordem Insertion, Quick, Heap, Merge. Como a JVM compila o código sob demanda (JIT) e o coletor de lixo atua de forma não determinística, os tempos de uma execução isolada têm variação.
- Entradas inválidas não são tratadas: `Scanner.nextInt()` pode lançar `InputMismatchException`, `Integer.parseInt` pode lançar `NumberFormatException` na leitura do arquivo, um tamanho negativo lança `NegativeArraySizeException` e um caminho inexistente lança `FileNotFoundException` (que sobe por `throws IOException` até o `main`).
