package aps;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterfaceGrafica extends JFrame {

    private boolean modoEscuro = true;

    private JPanel mainContainer, headerPanel;
    private JLabel titleLabel, lblInstrucao, lblCombo, lblStatusFotos;
    private JButton btnTema, btnOrdenar, btnExemplo, btnLimparManual, btnSelecionarPasta, btnCriarPastaExemplo, btnLimparFotos, btnRodarBenchmark, btnLimparBenchmark;
    private JTabbedPane tabbedPane;
    private JTextField txtEntradaNumeros;
    private JComboBox<String> comboAlgoritmos;
    private JTextArea txtResultadoManual;
    private JTable tabelaFotos, tabelaBenchmark;
    private DefaultTableModel modelFotos, modelBenchmark;
    private JScrollPane scrollManual, scrollFotos, scrollBenchmark;
    private JPanel panelManual, panelTopManual, panelFotos, infoPanelFotos, panelTopFotos, panelBenchmark, panelTopBenchmark;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_DARK = new Color(30, 41, 59);
    private final Color INPUT_DARK = new Color(15, 23, 42);
    private final Color TEXT_DARK = new Color(255, 255, 255);
    private final Color TEXT_MUTED_DARK = new Color(203, 213, 225);
    private final Color BTN_DARK = new Color(51, 65, 85);
    private final Color BTN_PRIMARY_DARK = new Color(37, 99, 235);
    private final Color BTN_DANGER_DARK = new Color(220, 38, 38);

    private final Color BG_LIGHT = new Color(241, 245, 249);
    private final Color CARD_LIGHT = new Color(255, 255, 255);
    private final Color INPUT_LIGHT = new Color(248, 250, 252);
    private final Color TEXT_LIGHT = new Color(15, 23, 42);
    private final Color TEXT_MUTED_LIGHT = new Color(71, 85, 105);
    private final Color BTN_LIGHT = new Color(226, 232, 240);
    private final Color BTN_PRIMARY_LIGHT = new Color(29, 78, 216);
    private final Color BTN_DANGER_LIGHT = new Color(185, 28, 28);

    public InterfaceGrafica() {
        setTitle("APS - Sistema de Análise de Desempenho de Algoritmos de Ordenação");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainContainer = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        titleLabel = new JLabel("APS - Análise & Ordenação de Dados");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        btnTema = new JButton("[ Modo Escuro ]");
        btnTema.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTema.setFocusPainted(false);
        btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.addActionListener(e -> alternarTema());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnTema, BorderLayout.EAST);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelManual = criarPainelManual();
        panelFotos = criarPainelImagens();
        panelBenchmark = criarPainelBenchmark();

        tabbedPane.addTab("  1. Teste Manual  ", panelManual);
        tabbedPane.addTab("  2. Imagens por Data  ", panelFotos);
        tabbedPane.addTab("  3. Bateria de Testes (Benchmark)  ", panelBenchmark);

        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(tabbedPane, BorderLayout.CENTER);

        add(mainContainer);
        aplicarTema();
    }

    private JPanel criarPainelManual() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelTopManual = new JPanel(new GridLayout(3, 1, 8, 8));
        lblInstrucao = new JLabel("Digite os números inteiros separados por espaço ou vírgula:");
        lblInstrucao.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtEntradaNumeros = new JTextField("45, 12, 89, 3, 27, 100, 1, 64");
        txtEntradaNumeros.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtEntradaNumeros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setOpaque(false);

        lblCombo = new JLabel("Algoritmo:");
        lblCombo.setFont(new Font("Segoe UI", Font.BOLD, 13));

        comboAlgoritmos = new JComboBox<>(new String[]{"QuickSort", "HeapSort", "InsertionSort", "MergeSort", "BubbleSort", "SelectionSort"});
        comboAlgoritmos.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnOrdenar = new JButton("[>] Ordenar Agora");
        btnOrdenar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOrdenar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExemplo = new JButton("[+] Restaurar Exemplo");
        btnExemplo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExemplo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLimparManual = new JButton("[x] Limpar");
        btnLimparManual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimparManual.setCursor(new Cursor(Cursor.HAND_CURSOR));

        controls.add(lblCombo);
        controls.add(comboAlgoritmos);
        controls.add(btnOrdenar);
        controls.add(btnExemplo);
        controls.add(btnLimparManual);

        panelTopManual.add(lblInstrucao);
        panelTopManual.add(txtEntradaNumeros);
        panelTopManual.add(controls);

        txtResultadoManual = new JTextArea();
        txtResultadoManual.setEditable(false);
        txtResultadoManual.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtResultadoManual.setMargin(new Insets(12, 12, 12, 12));

        scrollManual = new JScrollPane(txtResultadoManual);
        panel.add(panelTopManual, BorderLayout.NORTH);
        panel.add(scrollManual, BorderLayout.CENTER);

        btnExemplo.addActionListener(e -> txtEntradaNumeros.setText("45, 12, 89, 3, 27, 100, 1, 64"));
        btnLimparManual.addActionListener(e -> {
            txtEntradaNumeros.setText("");
            txtResultadoManual.setText("");
        });

        btnOrdenar.addActionListener(e -> {
            String linha = txtEntradaNumeros.getText().trim();
            if (linha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite ao menos um número!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] tokens = linha.split("[,\\s]+");
            List<Integer> lista = new ArrayList<>();
            for (String t : tokens) {
                try { lista.add(Integer.parseInt(t.trim())); } catch (NumberFormatException ignored) {}
            }
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum número válido inserido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int[] arr = lista.stream().mapToInt(i -> i).toArray();
            String algSel = (String) comboAlgoritmos.getSelectedItem();

            long inicio = System.nanoTime();
            BateriaTestes.executarAlgoritmoPublico(algSel, arr);
            long fim = System.nanoTime();

            StringBuilder sb = new StringBuilder();
            sb.append("=========================================================\n");
            sb.append("                 RESULTADO DA ORDENAÇÃO                  \n");
            sb.append("=========================================================\n");
            sb.append(String.format("▸ Algoritmo Utilizado : %s\n", algSel));
            sb.append(String.format("▸ Qtd. de Elementos   : %d\n", arr.length));
            sb.append(String.format("▸ Tempo de Execução   : %.4f ms (%d ns)\n", (fim - inicio) / 1_000_000.0, (fim - inicio)));
            sb.append("---------------------------------------------------------\n");
            sb.append("▸ Vetor Ordenado:\n[");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]).append(i < arr.length - 1 ? ", " : "");
            }
            sb.append("]\n=========================================================\n");
            txtResultadoManual.setText(sb.toString());
        });

        return panel;
    }

    private JPanel criarPainelImagens() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelTopFotos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        btnSelecionarPasta = new JButton("[ Pasta ] Selecionar Pasta do Computador...");
        btnSelecionarPasta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSelecionarPasta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCriarPastaExemplo = new JButton("[+] Usar Pasta Exemplo (Simulada)");
        btnCriarPastaExemplo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCriarPastaExemplo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLimparFotos = new JButton("[x] Limpar Tabela");
        btnLimparFotos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimparFotos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblStatusFotos = new JLabel("Nenhuma pasta selecionada.");
        lblStatusFotos.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panelTopFotos.add(btnSelecionarPasta);
        panelTopFotos.add(btnCriarPastaExemplo);
        panelTopFotos.add(btnLimparFotos);

        infoPanelFotos = new JPanel(new BorderLayout());
        infoPanelFotos.add(panelTopFotos, BorderLayout.NORTH);
        infoPanelFotos.add(lblStatusFotos, BorderLayout.SOUTH);
        lblStatusFotos.setBorder(new EmptyBorder(10, 5, 0, 0));

        panel.add(infoPanelFotos, BorderLayout.NORTH);

        modelFotos = new DefaultTableModel(new String[]{"Nome do Arquivo", "Data de Modificação", "Tamanho (Bytes)"}, 0);
        tabelaFotos = new JTable(modelFotos);
        tabelaFotos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaFotos.setRowHeight(26);

        scrollFotos = new JScrollPane(tabelaFotos);
        panel.add(scrollFotos, BorderLayout.CENTER);

        btnSelecionarPasta.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                processarEExibirFotos(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        btnCriarPastaExemplo.addActionListener(e -> processarEExibirFotos(criarPastaComImagensExemplo()));

        btnLimparFotos.addActionListener(e -> {
            modelFotos.setRowCount(0);
            lblStatusFotos.setText("Tabela de arquivos limpa.");
        });

        return panel;
    }

    private void processarEExibirFotos(String caminhoPasta) {
        List<FotoItem> fotos = LeitorImagens.carregarFotosDeDiretorio(caminhoPasta);
        if (fotos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma imagem válida (.jpg, .png, etc.) encontrada nesta pasta.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        FotoItem[] arrFotos = fotos.toArray(new FotoItem[0]);
        long inicio = System.nanoTime();
        AlgoritmosOrdenacao.quickSortObjetos(arrFotos, 0, arrFotos.length - 1);
        long fim = System.nanoTime();

        modelFotos.setRowCount(0);
        for (FotoItem f : arrFotos) {
            modelFotos.addRow(new Object[]{f.getNome(), f.getDataModificacao(), f.getTamanhoBytes()});
        }
        lblStatusFotos.setText(String.format("✓ %d imagens ordenadas por data com sucesso em %.4f ms", arrFotos.length, (fim - inicio) / 1_000_000.0));
    }

    private String criarPastaComImagensExemplo() {
        File pasta = new File("imagens_exemplo_aps");
        if (!pasta.exists()) pasta.mkdirs();
        String[] nomes = {"foto_ferias_2022.jpg", "foto_formatura_2023.png", "viagem_2024.jpg", "foto_perfil_2025.png"};
        long tempoAtual = System.currentTimeMillis();
        long umDiaMs = 86400000L;

        for (int i = 0; i < nomes.length; i++) {
            File f = new File(pasta, nomes[i]);
            try {
                if (!f.exists()) f.createNewFile();
                f.setLastModified(tempoAtual - ((i + 1) * 30 * umDiaMs));
            } catch (IOException ignored) {}
        }
        return pasta.getAbsolutePath();
    }

    private JPanel criarPainelBenchmark() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelTopBenchmark = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        btnRodarBenchmark = new JButton("[>] Executar Bateria de Testes Completa (100 a 100.000)");
        btnRodarBenchmark.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRodarBenchmark.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLimparBenchmark = new JButton("[x] Limpar Tabela de Resultados");
        btnLimparBenchmark.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimparBenchmark.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelTopBenchmark.add(btnRodarBenchmark);
        panelTopBenchmark.add(btnLimparBenchmark);
        panel.add(panelTopBenchmark, BorderLayout.NORTH);

        modelBenchmark = new DefaultTableModel(new String[]{"Cenário", "Tamanho (N)", "Algoritmo", "Tempo Médio (ms)", "Tempo Médio (ns)"}, 0);
        tabelaBenchmark = new JTable(modelBenchmark);
        tabelaBenchmark.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaBenchmark.setRowHeight(26);

        scrollBenchmark = new JScrollPane(tabelaBenchmark);
        panel.add(scrollBenchmark, BorderLayout.CENTER);

        btnLimparBenchmark.addActionListener(e -> modelBenchmark.setRowCount(0));

        btnRodarBenchmark.addActionListener(e -> rodarBenchmarkComModal());

        return panel;
    }

    private void rodarBenchmarkComModal() {
        JDialog dialog = new JDialog(this, "Executando Testes", true);
        dialog.setSize(450, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("⚡ Executando Bateria de Testes (Benchmark)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblStatus = new JLabel("Iniciando...");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JProgressBar progress = new JProgressBar(0, 108);
        progress.setStringPainted(true);

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblStatus, BorderLayout.CENTER);
        p.add(progress, BorderLayout.SOUTH);

        dialog.add(p);

        new Thread(() -> {
            modelBenchmark.setRowCount(0);
            int[] tamanhos = {100, 1000, 5000, 10000, 50000, 100000};
            String[] algoritmos = {"InsertionSort", "QuickSort", "HeapSort", "MergeSort", "BubbleSort", "SelectionSort"};
            int repeticoes = 5;
            int total = 108;
            int contador = 0;

            for (BateriaTestes.Cenario cenario : BateriaTestes.Cenario.values()) {
                for (int n : tamanhos) {
                    for (String alg : algoritmos) {
                        contador++;
                        final int c = contador;
                        final String statusStr = String.format("Executando: %s | %s | N = %,d", alg, cenario, n);

                        SwingUtilities.invokeLater(() -> {
                            progress.setValue(c);
                            progress.setString(c + " / " + total + " (" + (c * 100 / total) + "%)");
                            lblStatus.setText(statusStr);
                        });

                        if ((alg.equals("BubbleSort") || alg.equals("SelectionSort") || alg.equals("InsertionSort")) && n > 50000) {
                            SwingUtilities.invokeLater(() -> modelBenchmark.addRow(new Object[]{cenario, n, alg, "N/A (Muito Lento)", "N/A"}));
                            continue;
                        }

                        long somaTempo = 0;
                        for (int r = 0; r < repeticoes; r++) {
                            int[] vetor = BateriaTestes.gerarVetorPublico(n, cenario);
                            long inicio = System.nanoTime();
                            BateriaTestes.executarAlgoritmoPublico(alg, vetor);
                            long fim = System.nanoTime();
                            somaTempo += (fim - inicio);
                        }

                        long tempoMedioNs = somaTempo / repeticoes;
                        double tempoMedioMs = tempoMedioNs / 1_000_000.0;
                        final double ms = tempoMedioMs;
                        final long ns = tempoMedioNs;

                        SwingUtilities.invokeLater(() -> modelBenchmark.addRow(new Object[]{cenario, n, alg, String.format("%.4f ms", ms), ns}));
                    }
                }
            }

            BateriaTestes.executarBateriaEExportarCSV("resultados_aps.csv");

            SwingUtilities.invokeLater(() -> {
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✓ Bateria de testes concluída!\n✓ Resultados salvos no arquivo 'resultados_aps.csv'.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();

        dialog.setVisible(true);
    }

    private void alternarTema() {
        modoEscuro = !modoEscuro;
        btnTema.setText(modoEscuro ? "[ Modo Escuro ]" : "[ Modo Claro ]");
        aplicarTema();
    }

    private void aplicarTema() {
        Color bg = modoEscuro ? BG_DARK : BG_LIGHT;
        Color card = modoEscuro ? CARD_DARK : CARD_LIGHT;
        Color inputBg = modoEscuro ? INPUT_DARK : INPUT_LIGHT;
        Color text = modoEscuro ? TEXT_DARK : TEXT_LIGHT;
        Color textMuted = modoEscuro ? TEXT_MUTED_DARK : TEXT_MUTED_LIGHT;
        Color btnBg = modoEscuro ? BTN_DARK : BTN_LIGHT;
        Color btnPrimaryBg = modoEscuro ? BTN_PRIMARY_DARK : BTN_PRIMARY_LIGHT;
        Color btnDangerBg = modoEscuro ? BTN_DANGER_DARK : BTN_DANGER_LIGHT;

        mainContainer.setBackground(bg);
        headerPanel.setBackground(bg);
        titleLabel.setForeground(text);

        btnTema.setBackground(btnBg);
        btnTema.setForeground(text);

        tabbedPane.setBackground(bg);
        tabbedPane.setForeground(text);

        panelManual.setBackground(card);
        panelTopManual.setBackground(card);
        lblInstrucao.setForeground(text);
        lblCombo.setForeground(text);

        txtEntradaNumeros.setBackground(inputBg);
        txtEntradaNumeros.setForeground(text);
        txtEntradaNumeros.setCaretColor(text);

        comboAlgoritmos.setBackground(inputBg);
        comboAlgoritmos.setForeground(text);

        btnOrdenar.setBackground(btnPrimaryBg);
        btnOrdenar.setForeground(Color.WHITE);

        btnExemplo.setBackground(btnBg);
        btnExemplo.setForeground(text);

        btnLimparManual.setBackground(btnDangerBg);
        btnLimparManual.setForeground(Color.WHITE);

        txtResultadoManual.setBackground(inputBg);
        txtResultadoManual.setForeground(text);
        txtResultadoManual.setCaretColor(text);
        scrollManual.getViewport().setBackground(inputBg);

        panelFotos.setBackground(card);
        infoPanelFotos.setBackground(card);
        panelTopFotos.setBackground(card);

        btnSelecionarPasta.setBackground(btnBg);
        btnSelecionarPasta.setForeground(text);

        btnCriarPastaExemplo.setBackground(btnPrimaryBg);
        btnCriarPastaExemplo.setForeground(Color.WHITE);

        btnLimparFotos.setBackground(btnDangerBg);
        btnLimparFotos.setForeground(Color.WHITE);

        lblStatusFotos.setForeground(textMuted);

        estilizarTabela(tabelaFotos, scrollFotos, inputBg, text, modoEscuro);

        panelBenchmark.setBackground(card);
        panelTopBenchmark.setBackground(card);

        btnRodarBenchmark.setBackground(btnPrimaryBg);
        btnRodarBenchmark.setForeground(Color.WHITE);

        btnLimparBenchmark.setBackground(btnDangerBg);
        btnLimparBenchmark.setForeground(Color.WHITE);

        estilizarTabela(tabelaBenchmark, scrollBenchmark, inputBg, text, modoEscuro);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void estilizarTabela(JTable tabela, JScrollPane scroll, Color inputBg, Color textColor, boolean isDark) {
        tabela.setBackground(inputBg);
        tabela.setForeground(textColor);
        tabela.setSelectionBackground(isDark ? new Color(51, 65, 85) : new Color(226, 232, 240));
        tabela.setSelectionForeground(textColor);
        tabela.setGridColor(isDark ? new Color(71, 85, 105) : new Color(203, 213, 225));

        scroll.getViewport().setBackground(inputBg);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(isDark ? new Color(15, 23, 42) : new Color(226, 232, 240));
        header.setForeground(textColor);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(inputBg);
                    c.setForeground(textColor);
                } else {
                    c.setBackground(isDark ? new Color(51, 65, 85) : new Color(203, 213, 225));
                    c.setForeground(textColor);
                }
                return c;
            }
        };

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }
}