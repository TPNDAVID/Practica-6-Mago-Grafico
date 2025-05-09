import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class ConsolaGrafica {
    private JFrame frame;
    private JTextArea infoArea;
    private JTextField inputField;
    private Mago mago;
    private List<String> jugadores;
    private int modoJuego;
    private JPanel letrasPanel;
    private JLabel lblJugador;
    private JLabel lblRonda;

    private final Font FUENTE_BOTONES = new Font("Arial", Font.BOLD, 30);

    public ConsolaGrafica() {
        crearPantallaInicial();
    }

    private void crearPantallaInicial() {
        frame = new JFrame("El Mago de las Palabras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Imagen del fondo
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/MAGOIMAGEN.png"));
        backgroundLabel.setLayout(new BorderLayout());

        // Panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(240, 0, 0, 0));

        // Botones
        JButton btnIniciar = crearBoton("Jugar");
        btnIniciar.addActionListener(e -> mostrarConfiguracionJugadores());

        JButton btnSalir = crearBoton("Salir");
        btnSalir.addActionListener(e -> confirmarSalida());

        // Centrado y espaciado
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.add(btnIniciar);
        panelBotones.add(Box.createRigidArea(new Dimension(0, 6)));
        panelBotones.add(btnSalir);

        // Ensamblado final
        backgroundLabel.add(panelBotones, BorderLayout.CENTER);
        frame.add(backgroundLabel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(220, 70));
        boton.setFont(FUENTE_BOTONES);
        boton.setBackground(new Color(251, 181, 45));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return boton;
    }

    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
                frame,
                "¿Quieres salir del juego?",
                "Salir",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void mostrarConfiguracionJugadores() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblTitulo = new JLabel("CONFIGURACIÓN DEL JUEGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelJugadores.setMaximumSize(new Dimension(350, 100));

        JLabel lblNumJugadores = new JLabel("Número de jugadores (2-4)", SwingConstants.CENTER);
        lblNumJugadores.setFont(new Font("Arial", Font.BOLD, 22));
        lblNumJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelJugadores.add(lblNumJugadores);
        panelJugadores.add(Box.createRigidArea(new Dimension(0, 10)));

        // Campo de texto
        JTextField txtJugadores = new JTextField();
        txtJugadores.setFont(new Font("Arial", Font.PLAIN, 20));
        txtJugadores.setHorizontalAlignment(JTextField.CENTER);
        txtJugadores.setMaximumSize(new Dimension(150, 35));
        txtJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtJugadores.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        panelJugadores.add(txtJugadores);
        panelPrincipal.add(panelJugadores);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel panelModo = new JPanel();
        panelModo.setLayout(new BoxLayout(panelModo, BoxLayout.Y_AXIS));
        panelModo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelModo.setMaximumSize(new Dimension(350, 120));

        JLabel lblModoJuego = new JLabel("Modo de juego", SwingConstants.CENTER);
        lblModoJuego.setFont(new Font("Arial", Font.BOLD, 22));
        lblModoJuego.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelModo.add(lblModoJuego);
        panelModo.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelRadios.setOpaque(false);

        ButtonGroup grupoModo = new ButtonGroup();

        // Radio button Regular
        JRadioButton rbRegular = new JRadioButton("Regular", true);
        rbRegular.setFont(new Font("Arial", Font.PLAIN, 20));
        rbRegular.setBackground(new Color(240, 240, 240)); // Fondo claro
        rbRegular.setFocusPainted(false);

        // Radio button Experto
        JRadioButton rbExperto = new JRadioButton("Experto");
        rbExperto.setFont(new Font("Arial", Font.PLAIN, 20));
        rbExperto.setBackground(new Color(240, 240, 240));
        rbExperto.setFocusPainted(false);

        grupoModo.add(rbRegular);
        grupoModo.add(rbExperto);

        panelRadios.add(rbRegular);
        panelRadios.add(rbExperto);

        panelModo.add(panelRadios);
        panelPrincipal.add(panelModo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton btnContinuar = new JButton("CONTINUAR");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 22));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinuar.setMaximumSize(new Dimension(250, 50));
        btnContinuar.setBackground(new Color(251, 181, 45));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnContinuar.setFocusPainted(false);

        btnContinuar.addActionListener(e -> {
            try {
                int numJugadores = Integer.parseInt(txtJugadores.getText());
                if (numJugadores < 2 || numJugadores > 4) {
                    JOptionPane.showMessageDialog(frame, "Deben ser entre 2 y 4 jugadores", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                modoJuego = rbRegular.isSelected() ? 1 : 2;
                mostrarIngresoNombres(numJugadores);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                txtJugadores.setText("");
                txtJugadores.requestFocus();
            }
        });

        panelPrincipal.add(btnContinuar);

        frame.getContentPane().removeAll();
        frame.add(new JScrollPane(panelPrincipal));
        frame.revalidate();
        frame.repaint();
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
        mostrarPantallaJuego();
    }

    private void mostrarPantallaJuego() {
        frame.getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout());
        lblRonda = new JLabel("Ronda: " + mago.getRondaActual() + "/3");
        lblJugador = new JLabel("Turno de: " + mago.getJugadorActual());
        topPanel.add(lblRonda);
        topPanel.add(lblJugador);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel lblLetras = new JLabel("Letras disponibles:");
        lblLetras.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(lblLetras);

        letrasPanel = new JPanel();
        actualizarLetras();
        centerPanel.add(letrasPanel);

        inputField = new JTextField(20);
        inputField.setMaximumSize(new Dimension(300, 30));
        centerPanel.add(inputField);

        JPanel buttonPanel = new JPanel();
        JButton btnEnviar = new JButton("Enviar palabra");
        btnEnviar.addActionListener(this::procesarPalabra);

        JButton btnPasar = new JButton("Pasar turno");
        btnPasar.addActionListener(e -> pasarTurno());

        JButton btnAnadir = new JButton("Añadir al diccionario");

        buttonPanel.add(btnEnviar);
        buttonPanel.add(btnPasar);
        buttonPanel.add(btnAnadir);

        centerPanel.add(buttonPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior
        infoArea = new JTextArea(8, 30);
        infoArea.setEditable(false);
        mainPanel.add(new JScrollPane(infoArea), BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void actualizarLetras() {
        letrasPanel.removeAll();
        for (Character letra : mago.getLetrasActuales()) {
            JLabel lbl = new JLabel(String.valueOf(letra));
            lbl.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            lbl.setFont(new Font("Arial", Font.BOLD, 24));
            letrasPanel.add(lbl);
        }
        letrasPanel.revalidate();
        letrasPanel.repaint();
    }

    private void procesarPalabra(ActionEvent e) {
        String palabra = inputField.getText().trim();
        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Debes ingresar una palabra");
            return;
        }

        Mago.ResultadoPalabra resultado = mago.procesarPalabra(mago.getJugadorActual(), palabra);

        String mensaje = mago.getJugadorActual() + " escribió: '" + palabra + "' - ";
        if (resultado.valida) {
            mensaje += "VÁLIDA (+" + resultado.puntos + " puntos)";
        } else {
            mensaje += "INVÁLIDA (" + resultado.mensaje + ", " + resultado.puntos + " puntos)";
            // Activar botón de añadir solo si la palabra no existe
            if (resultado.mensaje.equals("Palabra no válida")) {
                btnAnadir.setEnabled(true);
                palabraTemporal = palabra; // Guardamos la palabra para posible añadido
            }
        }
        actualizarInfo(mensaje);

        inputField.setText("");
        actualizarInfo("Puntuaciones: " + mago.getPuntuaciones());
        actualizarLetras();

        // Actualizar información del turno
        lblJugador.setText("Turno de: " + mago.getJugadorActual());
        lblRonda.setText("Ronda: " + mago.getRondaActual() + "/3");
    }

    private void pasarTurno() {
        mago.pasarTurno();
        actualizarInfo(mago.getJugadorActual() + " ha pasado su turno");

        if (mago.esFinalDelJuego()) {
            mostrarResultadosFinales();
        } else {
            actualizarLetras();
            lblJugador.setText("Turno de: " + mago.getJugadorActual());
            lblRonda.setText("Ronda: " + mago.getRondaActual() + "/3");
            inputField.setText("");
        }
    }

    private void mostrarResultadosFinales() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("RESULTADOS FINALES", SwingConstants.CENTER));

        List<Map.Entry<String, Integer>> resultados = new ArrayList<>(mago.getPuntuaciones().entrySet());
        resultados.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        for (Map.Entry<String, Integer> entry : resultados) {
            panel.add(new JLabel(entry.getKey() + ": " + entry.getValue() + " puntos"));
        }

        JOptionPane.showMessageDialog(frame, panel, "Juego Terminado", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
        new ConsolaGrafica();
    }

    private void actualizarInfo(String mensaje) {
        infoArea.append(mensaje + "\n");
    }

    private void añadirAlDiccionario(ActionEvent e) {
        if (palabraTemporal != null && !palabraTemporal.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "¿Deseas añadir '" + palabraTemporal + "' al diccionario?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                mago.getDiccionario().añadirPalabra(palabraTemporal);
                actualizarInfo("Palabra '" + palabraTemporal + "' añadida al diccionario");
                btnAnadir.setEnabled(false);
                palabraTemporal = null;
            }
        }
    }


}