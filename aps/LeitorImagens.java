package aps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LeitorImagens {

    public static List<FotoItem> carregarFotosDeDiretorio(String caminhoPasta) {
        List<FotoItem> fotos = new ArrayList<>();
        File pasta = new File(caminhoPasta);

        if (!pasta.exists() || !pasta.isDirectory()) return fotos;

        File[] arquivos = pasta.listFiles();
        if (arquivos == null || arquivos.length == 0) return fotos;

        for (File f : arquivos) {
            if (f.isFile() && eImagem(f.getName())) {
                try {
                    fotos.add(new FotoItem(f));
                } catch (Exception ignored) {}
            }
        }
        return fotos;
    }

    private static boolean eImagem(String nome) {
        String nomeLower = nome.toLowerCase();
        return nomeLower.endsWith(".jpg") || nomeLower.endsWith(".jpeg") || 
               nomeLower.endsWith(".png") || nomeLower.endsWith(".bmp") || 
               nomeLower.endsWith(".webp");
    }
}