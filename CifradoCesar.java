import javax.swing.*;
import java.awt.*;

public class CifradoCesar extends JFrame {
    
    private final char[][] matrizMayus = {
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'},
        {'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q'},
        {'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}
    };

    private final char[][] matrizMinus = {
        {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'},
        {'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q'},
        {'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}
    };

    private final char[][] matrizNum = {
        {'0', '1'}, {'2', '3'}, {'4', '5'}, {'6', '7'}, {'8', '9'}
    };

    private final char[][] matrizSimbolos = {
        {' ', '!', '"', '#', '$', '%'},
        {'&', '\'', '(', ')', '*', '+'},
        {',', '-', '.', '/', ':', ';'},
        {'<', '=', '>', '?', '@', '['},
        {'\\', ']', '^', '_', '{', '}'}
    };

    private final Color fondoBlanco = new Color(255, 255, 255);
    private final Color azulBoton = new Color(0, 51, 153);
    private final Color textoNegro = new Color(20, 20, 20);
    private final Font fuenteEtiqueta = new Font("Segoe UI", Font.BOLD, 14);

    public CifradoCesar() {
        setTitle("SISTEMA CRIPTOGRÁFICO - ALTO CONTRASTE");
        setSize(650, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(fondoBlanco);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("ENCRIPTAR", crearPanelClaro(true));
        pestañas.addTab("DESENCRIPTAR", crearPanelClaro(false));

        add(pestañas);
    }

    private JPanel crearPanelClaro(boolean esCifrado) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(fondoBlanco);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTexto = new JLabel(esCifrado ? "TEXTO DE ENTRADA:" : "CÓDIGO A DESCIFRAR:");
        lblTexto.setForeground(textoNegro);
        lblTexto.setFont(fuenteEtiqueta);
        
        JTextArea areaEntrada = new JTextArea(8, 20);
        estilizarArea(areaEntrada);
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JLabel lblLlave = new JLabel("LLAVE (ROTACIÓN):");
        lblLlave.setFont(fuenteEtiqueta);

        JTextField txtLlave = new JTextField();
        txtLlave.setBackground(new Color(240, 245, 255));
        txtLlave.setFont(new Font("Consolas", Font.BOLD, 18));
        txtLlave.setBorder(BorderFactory.createLineBorder(azulBoton));
        txtLlave.setMaximumSize(new Dimension(100, 40));

        // --- CORRECCIÓN DEL BOTÓN ---
        JButton btnAccion = new JButton(esCifrado ? "EJECUTAR CIFRADO" : "EJECUTAR DESCIFRADO");
        btnAccion.setFocusPainted(false);
        btnAccion.setBackground(azulBoton);
        btnAccion.setForeground(Color.WHITE); // Texto blanco para máximo contraste
        btnAccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccion.setMaximumSize(new Dimension(300, 50));

        JProgressBar barraCarga = new JProgressBar(0, 100);
        barraCarga.setForeground(new Color(0, 153, 76)); // Verde para la carga
        barraCarga.setVisible(false);

        JTextArea areaSalida = new JTextArea(10, 20);
        estilizarArea(areaSalida);
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(245, 245, 245));
        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(azulBoton), "RESULTADO", 0, 0, fuenteEtiqueta, azulBoton));

        btnAccion.addActionListener(e -> {
            try {
                String texto = areaEntrada.getText();
                int llave = Integer.parseInt(txtLlave.getText());
                btnAccion.setEnabled(false);
                barraCarga.setVisible(true);
                barraCarga.setValue(0);

                Timer t = new Timer(10, null);
                t.addActionListener(evt -> {
                    barraCarga.setValue(barraCarga.getValue() + 5);
                    if (barraCarga.getValue() >= 100) {
                        t.stop();
                        areaSalida.setText(procesarTexto(texto, esCifrado ? llave : -llave));
                        btnAccion.setEnabled(true);
                        barraCarga.setVisible(false);
                    }
                });
                t.start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: LA LLAVE DEBE SER UN NÚMERO");
            }
        });

        panel.add(lblTexto); panel.add(Box.createVerticalStrut(10));
        panel.add(scrollEntrada); panel.add(Box.createVerticalStrut(15));
        panel.add(lblLlave); panel.add(Box.createVerticalStrut(10));
        panel.add(txtLlave); panel.add(Box.createVerticalStrut(20));
        panel.add(btnAccion); panel.add(Box.createVerticalStrut(15));
        panel.add(barraCarga); panel.add(Box.createVerticalStrut(15));
        panel.add(scrollSalida);

        return panel;
    }

    private void estilizarArea(JTextArea area) {
        area.setBackground(new Color(252, 252, 252));
        area.setForeground(Color.BLACK);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    private String procesarTexto(String texto, int desplazamiento) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (Character.isWhitespace(c)) { sb.append(c); continue; }
            char p = aplicarDesplazamiento2D(c, matrizMayus, desplazamiento);
            if (p == '\0') p = aplicarDesplazamiento2D(c, matrizMinus, desplazamiento);
            if (p == '\0') p = aplicarDesplazamiento2D(c, matrizNum, desplazamiento);
            if (p == '\0') p = aplicarDesplazamiento2D(c, matrizSimbolos, desplazamiento);
            sb.append(p != '\0' ? p : c);
        }
        return sb.toString();
    }

    private char aplicarDesplazamiento2D(char c, char[][] matriz, int desplazamiento) {
        int filas = matriz.length, columnas = matriz[0].length, total = filas * columnas;
        for (int f = 0; f < filas; f++) {
            for (int col = 0; col < columnas; col++) {
                if (matriz[f][col] == c) {
                    int n = (f * columnas + col + desplazamiento) % total;
                    if (n < 0) n += total;
                    return matriz[n / columnas][n % columnas];
                }
            }
        }
        return '\0';
    }

    public static void main(String[] args) {
        // Forzamos el LookAndFeel para que los colores se respeten
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new CifradoCesar().setVisible(true));
    }
}