package aps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FotoItem implements Comparable<FotoItem> {
    private String nome;
    private LocalDateTime dataModificacao;
    private long tamanhoBytes;

    public FotoItem(File arquivo) throws IOException {
        this.nome = arquivo.getName();
        this.tamanhoBytes = arquivo.length();
        BasicFileAttributes attrs = Files.readAttributes(arquivo.toPath(), BasicFileAttributes.class);
        this.dataModificacao = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
    }

    public String getNome() { return nome; }
    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public long getTamanhoBytes() { return tamanhoBytes; }

    @Override
    public int compareTo(FotoItem outro) {
        return this.dataModificacao.compareTo(outro.dataModificacao);
    }
}