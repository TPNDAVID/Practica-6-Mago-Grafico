import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class GUISwing {
    private JFrame frame;
    private JTextArea infoArea;
    private JTextField inputField;
    private Mago mago;
    private List<String> jugadores;
    private int modoJuego;
    private int jugadorActual = 0;
    private int rondaActual = 1;

    public GUISwing() {
        crearPantallaInicial();
    }

    private void crearPantallaInicial() {
        frame = new JFrame("Mago de Palabras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("MAGO DE PALABRAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // Botón de inicio
        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setPreferredSize(new Dimension(150, 40));
        btnIniciar.addActionListener(e -> mostrarConfiguracionJugadores());

        panel.add(btnIniciar, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void mostrarConfiguracionJugadores() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configuración básica
        panel.add(new JLabel("Número de jugadores (2-4):"));
        JTextField txtJugadores = new JTextField();
        panel.add(txtJugadores);

        panel.add(new JLabel("Modo de juego:"));
        JPanel panelModo = new JPanel();
        ButtonGroup grupoModo = new ButtonGroup();
        JRadioButton rbRegular = new JRadioButton("Regular", true);
        JRadioButton rbExperto = new JRadioButton("Experto");
        grupoModo.add(rbRegular);
        grupoModo.add(rbExperto);
        panelModo.add(rbRegular);
        panelModo.add(rbExperto);
        panel.add(panelModo);

        // Botón continuar
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.addActionListener(e -> {
            try {
                int numJugadores = Integer.parseInt(txtJugadores.getText());
                if (numJugadores < 2 || numJugadores > 4) {
                    JOptionPane.showMessageDialog(frame, "Deben ser entre 2 y 4 jugadores");
                    return;
                }
                modoJuego = rbRegular.isSelected() ? 1 : 2;
                mostrarIngresoNombres(numJugadores);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingresa un número válido");
            }
        });

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.add(btnContinuar, BorderLayout.SOUTH);
        frame.revalidate();
    }

    private void mostrarIngresoNombres(int numJugadores) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField[] camposJugadores = new JTextField[numJugadores];
        for (int i = 0; i < numJugadores; i++) {
            panel.add(new JLabel("Jugador " + (i + 1) + ":"));
            camposJugadores[i] = new JTextField();
            panel.add(camposJugadores[i]);
        }

        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.addActionListener(e -> {
            jugadores = new ArrayList<>();
            for (JTextField campo : camposJugadores) {
                if (campo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Todos los jugadores deben tener nombre");
                    return;
                }
                jugadores.add(campo.getText().trim());
            }
            iniciarJuego();
        });

        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.add(btnIniciar, BorderLayout.SOUTH);
        frame.revalidate();
    }

    private void iniciarJuego() {
        mago = new Mago(jugadores, modoJuego);
        mago.iniciarJuego(); // Asegura que las letras estén generadas
        mostrarPantallaJuego();
    }

    private void mostrarPantallaJuego() {
        frame.getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Información
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Ronda: " + rondaActual + "/3"));
        topPanel.add(new JLabel("Turno: " + jugadores.get(jugadorActual)));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central - Letras y entrada
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel lblLetras = new JLabel("Letras disponibles:");
        lblLetras.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblLetras);

        JPanel letrasPanel = new JPanel();
        for (Character letra : mago.getLetrasActuales()) {
            JLabel lbl = new JLabel(String.valueOf(letra));
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            lbl.setFont(new Font("Arial", Font.BOLD, 24));
            letrasPanel.add(lbl);
        }
        centerPanel.add(letrasPanel);

        inputField = new JTextField(20);
        inputField.setMaximumSize(new Dimension(300, 30));
        centerPanel.add(inputField);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(this::procesarPalabra);
        centerPanel.add(btnEnviar);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior - Información
        infoArea = new JTextArea(5, 30);
        infoArea.setEditable(false);
        mainPanel.add(new JScrollPane(infoArea), BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.revalidate();
    }

    private void procesarPalabra(ActionEvent e) {
        String palabra = inputField.getText().trim();
        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Debes ingresar una palabra");
            return;
        }

        String jugador = jugadores.get(jugadorActual);
        mago.procesarPalabra(jugador, palabra);
        actualizarInfo("Jugador " + jugador + ": " + palabra);

        siguienteTurno();
    }

    private void siguienteTurno() {
        jugadorActual++;
        if (jugadorActual >= jugadores.size()) {
            jugadorActual = 0;
            rondaActual++;

            if (rondaActual > 3) {
                mostrarResultados();
                return;
            }

            mago.prepararNuevaRonda();
            actualizarInfo("\n--- COMIENZA RONDA " + rondaActual + " ---");
        }

        mostrarPantallaJuego();
    }

    private void mostrarResultados() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("RESULTADOS FINALES"));

        // Ordenar jugadores por puntaje
        List<Map.Entry<String, Integer>> resultados = new ArrayList<>(mago.getPuntuaciones().entrySet());
        resultados.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        for (Map.Entry<String, Integer> entry : resultados) {
            panel.add(new JLabel(entry.getKey() + ": " + entry.getValue() + " puntos"));
        }

        JOptionPane.showMessageDialog(frame, panel, "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
        new GUISwing(); // Reiniciar juego
    }

    private void actualizarInfo(String mensaje) {
        infoArea.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUISwing::new);
    }
}