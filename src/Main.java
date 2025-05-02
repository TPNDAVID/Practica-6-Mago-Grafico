public class Main {
    public static void main(String[] args) {
        // Inicia la interfaz gráfica Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GUISwing(); // Llama a tu clase de interfaz gráfica
        });
    }
}