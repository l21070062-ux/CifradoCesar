import javax.swing.*;
import java.awt.*;

public class CifradoCesarClasico extends JFrame {
    
    // Matriz  (3x9 = 27 posiciones)
    private final char[][] matriz = {
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'},
        {'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q'},
        {'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', ' '}
    };

   
    private final Color fondoBlanco = new Color(255, 255, 255);
    private final Color fondoGrisClaro = new Color(245, 248, 250);
    private final Color azulPrincipal = new Color(0, 102, 204);
    private final Color azulOscuro = new Color(20, 40, 80);
    private final Font fuenteModerna = new Font("Segoe UI", Font.BOLD, 13);

    public CifradoCesarClasico() {
        setTitle("SISTEMA CRIPTOGRÁFICO - MATRIZ 3x9");
        setSize(650, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(fondoBlanco);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setBackground(fondoGrisClaro);
        pestañas.setForeground(azulOscuro);

        pestañas.addTab("ENCRIPTAR", crearPanelClaro(true));
        pestañas.addTab("DESENCRIPTAR", crearPanelClaro(false));

        add(pestañas);
    }

    private JPanel crearPanelClaro(boolean esCifrado) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(fondoBlanco);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ENTRADA ---
        JLabel lblTexto = new JLabel(esCifrado ? "TEXTO DE ENTRADA (MAYÚSCULAS):" : "CÓDIGO A DESCIFRAR:");
        lblTexto.setForeground(azulOscuro);
        lblTexto.setFont(fuenteModerna);
        
        JTextArea areaEntrada = new JTextArea(8, 20);
        estilizarArea(areaEntrada);
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createLineBorder(azulPrincipal));

        // --- LLAVE ---
        JLabel lblLlave = new JLabel("LLAVE (ROTACIÓN):");
        lblLlave.setForeground(azulOscuro);
        lblLlave.setFont(fuenteModerna);

        JTextField txtLlave = new JTextField();
        txtLlave.setBackground(fondoGrisClaro);
        txtLlave.setForeground(azulOscuro);
        txtLlave.setCaretColor(azulPrincipal);
        txtLlave.setFont(fuenteModerna);
        txtLlave.setBorder(BorderFactory.createLineBorder(azulPrincipal));
        txtLlave.setMaximumSize(new Dimension(100, 30));

        
        JButton btnAccion = new JButton(esCifrado ? "EJECUTAR CIFRADO" : "EJECUTAR DESCIFRADO");
        btnAccion.setFocusPainted(false);
        btnAccion.setBackground(azulPrincipal); 
        btnAccion.setForeground(azulOscuro); // Cambiado a azul oscuro para legibilidad
        btnAccion.setFont(fuenteModerna);
        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));

     
        JProgressBar barraCarga = new JProgressBar(0, 100);
        barraCarga.setForeground(azulPrincipal);
        barraCarga.setBackground(fondoGrisClaro);
        barraCarga.setStringPainted(true);
        barraCarga.setVisible(false);
        barraCarga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

      
        JTextArea areaSalida = new JTextArea(10, 20);
        estilizarArea(areaSalida);
        areaSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(azulPrincipal), "RESULTADO", 0, 0, fuenteModerna, azulPrincipal));

        // Lógica del Botón
        btnAccion.addActionListener(e -> {
            try {
                String texto = areaEntrada.getText().toUpperCase();
                int llave = Integer.parseInt(txtLlave.getText());
                int llaveFinal = esCifrado ? llave : -llave;

                btnAccion.setEnabled(false);
                barraCarga.setVisible(true);
                barraCarga.setValue(0);

                Timer t = new Timer(15, null);
                t.addActionListener(evt -> {
                    barraCarga.setValue(barraCarga.getValue() + 5);
                    if (barraCarga.getValue() >= 100) {
                        t.stop();
                        areaSalida.setText(aplicarLogicaOriginal(texto, llaveFinal));
                        btnAccion.setEnabled(true);
                        barraCarga.setVisible(false);
                    }
                });
                t.start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ERROR: LA LLAVE DEBE SER UN NÚMERO");
            }
        });

        // Ensamblaje
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
        area.setBackground(fondoGrisClaro);
        area.setForeground(azulOscuro);
        area.setCaretColor(azulPrincipal);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }

    private String aplicarLogicaOriginal(String texto, int desplazamiento) {
        StringBuilder sb = new StringBuilder();
        int total = 27; 

        for (char c : texto.toCharArray()) {
            if (c == '\n' || c == '\r') {
                sb.append(c);
                continue;
            }

            int posLineal = -1;
            for (int f = 0; f < 3; f++) {
                for (int col = 0; col < 9; col++) {
                    if (matriz[f][col] == c) {
                        posLineal = (f * 9) + col;
                        break;
                    }
                }
            }

            if (posLineal != -1) {
                int nuevaPos = (posLineal + desplazamiento) % total;
                if (nuevaPos < 0) nuevaPos += total; 
                sb.append(matriz[nuevaPos / 9][nuevaPos % 9]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new CifradoCesarClasico().setVisible(true));
    }

}
