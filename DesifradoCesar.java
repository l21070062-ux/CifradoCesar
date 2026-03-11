import java.util.Scanner;

public class DesifradoCesar {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String ALFABETO = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        boolean continuar = true;

        System.out.println("=== SISTEMA DE DECIFRADO CÉSAR v2.0 ===");

        while (continuar) {
            System.out.print("\nIntroduce el mensaje cifrado: ");
            String mensaje = sc.nextLine().toUpperCase();

            System.out.println("\n--- Probando las 27 rotaciones posibles ---");
            
            for (int llave = 0; llave < ALFABETO.length(); llave++) {
                StringBuilder resultado = new StringBuilder();
                for (char c : mensaje.toCharArray()) {
                    int pos = ALFABETO.indexOf(c);
                    if (pos != -1) {
                        // Formula: (Posición - Llave + Tamaño) % Tamaño
                        int nuevaPos = (pos - llave + ALFABETO.length()) % ALFABETO.length();
                        resultado.append(ALFABETO.charAt(nuevaPos));
                    } else {
                        resultado.append(c);
                    }
                }
                System.out.printf("[%02d] -> %s%n", llave, resultado);
            }

            System.out.println("\n-------------------------------------------");
            System.out.print("¿Deseas descifrar otro mensaje? (S/N): ");
            String respuesta = sc.nextLine().trim().toUpperCase();
            
            if (respuesta.equals("N")) {
                continuar = false;
                System.out.println("Cerrando sesión... Conexión segura finalizada.");
            }
        }
        sc.close();
    }
}