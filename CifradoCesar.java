import javax.swing.*;

public class CesarMini {
    public static void main(String[] args) {
        String abc = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ ", res, t;
        int k, c;
        do {
            t = JOptionPane.showInputDialog("Texto:").toUpperCase();
            k = Integer.parseInt(JOptionPane.showInputDialog("Llave:"));
            res = "";
            for (char x : t.toCharArray()) {
                int p = abc.indexOf(x), n = (p + k) % 28;
                res += (p < 0) ? x : abc.charAt(n < 0 ? n + 28 : n);
            }
            
            JOptionPane.showMessageDialog(null, new JTextField(res), "Copiable", 1);
            c = JOptionPane.showConfirmDialog(null, "¿Otro?", "", 0);
        } while (c == 0);
    }
}
