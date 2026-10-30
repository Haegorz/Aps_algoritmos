import java.util.Scanner;

public class Entrada {

    public static int lerInteiro(Scanner scanner, String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor < minimo || valor > maximo) {
                    System.out.println("Digite um valor entre " + minimo + " e " + maximo + ".");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

    public static int lerInteiro(Scanner scanner, String mensagem) {
        return lerInteiro(scanner, mensagem, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static boolean lerSimNao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s")) {
                return true;
            }
            if (resposta.equals("n")) {
                return false;
            }
            System.out.println("Responda com s ou n.");
        }
    }

    public static String lerTexto(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("O texto nao pode ser vazio.");
        }
    }
}
