# Sistema de Análise de Performance de Algoritmos de Ordenação

Programa em Java (console) que ordena um vetor de `int` com Insertion Sort, Quick Sort, Heap Sort e Merge Sort e mede o tempo de execução de cada um sobre os mesmos dados. Os algoritmos seguem as implementações do material da disciplina (AlgoritmosC26), portadas de C para Java.

## Compilação e execução

Requer JDK (o `javac` não vem no JRE). Não há `package`; todos os arquivos ficam no mesmo diretório.

```
javac *.java
java Main
```

Se `javac` e `java` forem de versões diferentes (erro `UnsupportedClassVersionError`), compile com `javac --release 17 *.java`.

## Estrutura

```
Main.java                  ponto de entrada, menu em laço, tratamento de exceções
Entrada.java               leitura validada do teclado (Scanner)
AlgoritmoOrdenacao.java    interface comum
InsertionSort.java         implements AlgoritmoOrdenacao
QuickSort.java             implements AlgoritmoOrdenacao
HeapSort.java              implements AlgoritmoOrdenacao
MergeSort.java             implements AlgoritmoOrdenacao
GeradorDados.java          geração e leitura de dados (métodos estáticos)
Benchmark.java             cronometragem, validação, saída em arquivo
BateriaTestes.java         bateria automática (tamanhos x cenários) e exportação do CSV
ResultadoTeste.java        resultado de um teste (modelo de dados + linha do CSV)
```

## Interface `AlgoritmoOrdenacao`

```java
void ordenar(int[] dados, boolean mostrarPassos);
String getNome();
```

- `ordenar` ordena `dados` **in place** (não retorna nada).
- `mostrarPassos` é passado por parâmetro (não é estado global), então cada chamada decide se imprime os passos intermediários.
- `getNome` é usado nas mensagens e para montar o nome dos arquivos de saída.

O `Main` mantém `List<AlgoritmoOrdenacao>` com as quatro implementações e itera chamando o mesmo método em todas (polimorfismo), sem `if/else` por nome de algoritmo.

## Menu e fluxo de `Main`

`main` roda um laço com o menu até a opção `0`. Toda leitura passa por `Entrada`, então uma entrada inválida repete a pergunta em vez de lançar exceção.

| Opção | Ação |
|---|---|
| 1 | `GeradorDados.gerarDadosAleatorios(tamanho, 0, 1_000_000)` |
| 2 | Pede o cenário e chama `gerarDadosOrdenados`, `gerarDadosDecrescentes` ou `gerarDadosSemiOrdenados(tamanho, 0.10)` |
| 3 | `GeradorDados.lerDadosDoUsuario(scanner, tamanho)` |
| 4 | `GeradorDados.lerDadosDeArquivo(caminho)` (dados externos) |
| 5 | `GeradorDados.gerarArquivoCatalogo(caminho, quantidade)` (catálogo simulado de imagens de satélite) |
| 6 | `BateriaTestes.executar(algoritmos, "resultados.csv")` |

As opções 1 a 4 terminam em `executarComparacao`, que:

1. pergunta se mostra o passo a passo (e, se houver mais de 50 elementos, pede confirmação);
2. imprime os 10 primeiros valores e grava o vetor completo em `saida/dados_antes.txt`;
3. para cada algoritmo chama `benchmark.testarAlgoritmo(algoritmo, Benchmark.copiarArray(dados), mostrarPassos)`. A cópia garante que todos recebem a mesma entrada.

O `try/catch` em volta do `switch` trata `FileNotFoundException`, `IOException`, `IllegalArgumentException` e `OutOfMemoryError`; `NoSuchElementException` (fim da entrada padrão) encerra o programa.

## `Entrada`

Métodos estáticos que leem uma linha inteira com `scanner.nextLine()` e validam:

- `lerInteiro(scanner, mensagem, minimo, maximo)`: repete até receber um inteiro dentro do intervalo; existe também a versão sem limites.
- `lerSimNao(scanner, mensagem)`: aceita `s` ou `n`.
- `lerTexto(scanner, mensagem)`: rejeita texto vazio.

Ler sempre a linha inteira evita o problema clássico de misturar `nextInt()` com `nextLine()`.

## `GeradorDados`

Todos os métodos são `static`. Há um único `Random` compartilhado.

| Método | Descrição |
|---|---|
| `gerarDadosAleatorios(tamanho, min, max)` | `min + RANDOM.nextInt(max - min + 1)`, valores em `[min, max]`. |
| `gerarDadosOrdenados(tamanho)` | `dados[i] = i`. |
| `gerarDadosDecrescentes(tamanho)` | `dados[i] = tamanho - i`. |
| `gerarDadosSemiOrdenados(tamanho, p)` | Parte do vetor ordenado e faz `(int)(tamanho * p)` trocas entre duas posições sorteadas. As posições podem repetir, então o percentual real de elementos deslocados varia. |
| `lerDadosDoUsuario(scanner, tamanho)` | Laço com `Entrada.lerInteiro`. |
| `lerDadosDeArquivo(caminho)` | `try-with-resources` com `BufferedReader`. Ignora linhas vazias e as iniciadas por `#`, separa por vírgula, ponto e vírgula ou espaço, e converte com `Integer.parseInt`. Um valor inválido lança `IllegalArgumentException` com o número da linha; arquivo sem valores também lança. |
| `gerarArquivoCatalogo(caminho, quantidade)` | Grava `quantidade` códigos `1_700_000_000 + nextInt(60_000_000)` (timestamps de captura), um por linha, com um cabeçalho `#`. |

## `Benchmark`

**Execução única (opções 1 a 4):** `testarAlgoritmo(algoritmo, dados, mostrarPassos)`.

```java
long inicio = System.nanoTime();
try {
    algoritmo.ordenar(dados, mostrarPassos);
} catch (StackOverflowError e) { ... }
long fim = System.nanoTime();
```

- Só a chamada a `ordenar` fica dentro do intervalo cronometrado. Geração, leitura de dados e gravação de arquivos ficam de fora.
- Se ocorrer `StackOverflowError`, o método avisa e retorna sem imprimir tempo.
- Depois confere com `estaOrdenado` (percorre o vetor procurando `dados[i] > dados[i+1]`) e grava `saida/dados_depois_<nome>.txt`.

**Medição repetida (bateria):**

- `aquecer(algoritmos)`: executa cada algoritmo 5 vezes sobre 2000 valores aleatórios, descartando o tempo. Serve para a JVM compilar (JIT) o código antes das medições que valem.
- `medirNanos`: copia o vetor **antes** de iniciar o cronômetro, mede só `ordenar`, e valida o resultado **depois** de parar o cronômetro. Retorna códigos internos para estouro de pilha e para vetor não ordenado.
- `medirTempoMedio(algoritmo, original, cenario)`: repete `REPETICOES = 5` vezes sobre cópias do mesmo vetor, calcula média e mediana (`Arrays.sort` num clone dos tempos, elemento do meio) e devolve um `ResultadoTeste`. Se qualquer repetição estourar a pilha ou não ordenar, devolve o status correspondente sem tempos.

Constantes: `PASTA_SAIDA = "saida"` (todos os arquivos gerados vão para essa pasta) e `REPETICOES`. `caminhoSaida(nome)` cria a pasta com `mkdirs()` e devolve o caminho. Para mudar o local de saída, basta alterar `PASTA_SAIDA`.

## `BateriaTestes`

`executar(algoritmos, nomeCSV)` percorre `CENARIOS` (Aleatorio, Ordenado, Decrescente, Semi-ordenado) x `TAMANHOS` (100, 1000, 5000, 10000, 50000, 100000) x algoritmos: 96 testes. Para cada combinação cenário/tamanho o vetor é gerado **uma vez**, e os quatro algoritmos recebem cópias dele, ou seja, a comparação é sobre os mesmos dados. O progresso é impresso a cada teste e o resultado final vai para `saida/resultados.csv`.

Formato do CSV: separador `;`, decimais com vírgula (`Locale pt-BR`), para abrir direto no Excel em português.

```
Algoritmo;Cenario;Tamanho;Media_ms;Mediana_ms;Status
Insertion Sort;Aleatorio;100;0,0018;0,0012;OK
Quick Sort;Ordenado;50000;;;ESTOUROU_PILHA
```

`Status` pode ser `OK`, `ESTOUROU_PILHA` ou `NAO_ORDENOU`.

## `ResultadoTeste`

Classe de dados imutável (algoritmo, cenário, tamanho, média, mediana, status). `linhaCSV()` monta a linha do arquivo; para status diferente de `OK` deixa as colunas de tempo vazias.

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
- `atual` é guardado e os maiores que ele são deslocados uma posição à direita (sem trocas); em seguida `atual` é inserido na posição livre.
- A comparação é `>` estrito, então elementos iguais não passam uns pelos outros (estável).
- Com `mostrarPassos`, cada iteração imprime `dados[0..j-1]`, como no material do professor.

### QuickSort

Assinatura interna: `quickSort(int[] v, int inicio, int tam)`, onde `tam` é a **quantidade** de elementos do trecho (não o índice final). `inicio + tam` faz o papel do ponteiro `&v[a]` do código em C.

```java
if (tam <= 1) return;
pivoAleatorioParaInicio(v, inicio, tam);
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

- Pivô escolhido ao acaso: `pivoAleatorioParaInicio` sorteia uma posição do trecho com `ThreadLocalRandom` e troca esse elemento com `v[inicio]`. Depois disso o algoritmo segue exatamente o esquema do professor (pivô na primeira posição), então o particionamento não muda.
- Particionamento de dois índices (estilo Hoare): `a` avança enquanto `v[a] <= pivot`, `b` recua enquanto `v[b] > pivot`, e se `a < b` os elementos são trocados.
- O segundo `while` não precisa de limite inferior: `v[inicio] == pivot` não satisfaz `v[b] > pivot`, então o pivô funciona como sentinela e `b` nunca fica abaixo de `inicio`.
- Ao sair do laço (`a > b`), `b` aponta para a última posição com valor `<= pivot`. A troca `v[inicio] = v[b]; v[b] = pivot` coloca o pivô na posição final.
- Subproblemas: esquerda = `[inicio, b-1]` com `b - inicio` elementos; direita = `[a, inicio+tam-1]` com `tam - (a - inicio)` elementos.
- Não é estável.
- Por que o pivô aleatório: com o pivô fixo no primeiro elemento, vetor ordenado gera partições de tamanho `0` e `tam-1` e o decrescente gera `tam-1` e `0`; a profundidade de recursão vira O(n) e o Java estoura a pilha (`StackOverflowError`) a partir de 50000 elementos. A mediana de três também foi testada e não resolveu o cenário decrescente (408 ms com 50000 elementos e estouro com 100000), porque as partições resultantes ficam com padrões que degradam de novo. Com pivô aleatório nenhum padrão de entrada é sistematicamente ruim: 200000 elementos ordenados, decrescentes, semi-ordenados ou em "organ pipe" levam de 7 a 12 ms, e a profundidade esperada é O(log n).
- Limitação: o laço `v[a] <= pivot` trata todos os valores iguais ao pivô como "menores". Com vetores em que quase todos os valores se repetem (por exemplo, 200000 elementos só entre 0 e 9, ou todos iguais) as partições continuam desbalanceadas, o tempo vira O(n²) e a pilha pode estourar. Isso não ocorre nos cenários da bateria (aleatório, ordenado, decrescente, semi-ordenado).
- Como o pivô é sorteado, o tempo do Quick Sort varia um pouco entre execuções, mesmo com a mesma entrada.

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
| Quick Sort (pivô aleatório) | O(n log n) | O(n log n) | O(n²), raro (muitos valores repetidos) | O(log n) esperado (pilha) | não |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | não |
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | não (nesta versão) |

- Insertion Sort é O(n) no melhor caso porque, com a entrada já ordenada, o `while` interno nunca executa.
- No Quick Sort, o pivô aleatório evita que entradas ordenadas ou decrescentes caiam no pior caso; o O(n²) fica restrito a entradas com muitos valores repetidos (ver a limitação acima).

## Arquivos gerados

Todos na pasta `saida/` (criada automaticamente), dentro do diretório de execução:

- `dados_antes.txt`: vetor completo antes da ordenação
- `dados_depois_insertion_sort.txt`, `dados_depois_quick_sort.txt`, `dados_depois_heap_sort.txt`, `dados_depois_merge_sort.txt`: vetor completo após cada algoritmo
- `catalogo_satelite.txt` (ou o nome escolhido): catálogo simulado gerado pela opção 5
- `resultados.csv`: saída da bateria (opção 6)

## Tratamento de erros

| Situação | Comportamento |
|---|---|
| Letra, texto ou valor fora do intervalo no menu/tamanhos | `Entrada` repete a pergunta |
| Arquivo inexistente | `FileNotFoundException` capturada em `main`, mensagem e volta ao menu |
| Valor não numérico no arquivo | `IllegalArgumentException` com o número da linha |
| Arquivo sem valores | `IllegalArgumentException` |
| Recursão do Quick Sort muito profunda (por exemplo, vetor enorme com quase todos os valores iguais) | `StackOverflowError` capturado; execução única avisa, bateria registra `ESTOUROU_PILHA` |
| Algoritmo que não ordena corretamente | Aviso na execução única; `NAO_ORDENOU` na bateria |
| Memória insuficiente | `OutOfMemoryError` capturado |

## Pontos de atenção na execução

- Com `mostrarPassos = true`, as chamadas de `System.out.print` passam a ser executadas dentro do trecho cronometrado, então o tempo reflete principalmente a E/S. Para medir desempenho, usar `n` ou a bateria (que nunca imprime passos).
- Na execução única (opções 1 a 4) cada algoritmo roda uma vez, sem aquecimento. Para tempos confiáveis, usar a bateria (opção 6), que aquece a JVM e usa média e mediana de 5 repetições.
- Tempos absolutos dependem da máquina; o que se compara é o comportamento entre algoritmos e cenários.
