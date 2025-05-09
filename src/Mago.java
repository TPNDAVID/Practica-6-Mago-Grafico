import java.io.*;
import java.util.*;

public class Mago {
    private Diccionario diccionario;
    private HashMap<String, Integer> puntajeJugador;
    private int modoDeJuego;
    private List<String> jugadores;
    private Set<Character> letrasActuales;
    private Set<String> palabrasUsadas = new HashSet<>();
    private int jugadorActual = 0;
    private int rondaActual = 1;
    private String ultimaPalabraInvalida; // Nueva variable para guardar la última palabra inválida

    public Mago(List<String> jugadores, int modoDeJuego) {
        this.jugadores = new ArrayList<>(jugadores);
        this.modoDeJuego = modoDeJuego;
        this.puntajeJugador = new HashMap<>();
        this.diccionario = new Diccionario(modoDeJuego, "src/Diccionario.txt"); // Modificado para pasar el nombre del archivo
        this.letrasActuales = new HashSet<>();

        try {
            diccionario.loadFromFile("src/Diccionario.txt");
        } catch (IOException e) {
            System.err.println("Error al cargar el diccionario: " + e.getMessage());
            System.exit(1);
        }

        jugadores.forEach(p -> puntajeJugador.put(p, 0));
        prepararNuevaRonda();
    }

    // Metodo nuevo para añadir palabras al diccionario
    public boolean añadirPalabraAlDiccionario(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return false;
        }

        // Verificar que la palabra no existe ya
        if (diccionario.contienePalabra(palabra, modoDeJuego)) {
            return false;
        }

        // Verificar que usa letras válidas
        if (!letrasValidas(palabra)) {
            return false;
        }

        // Añadir la palabra
        diccionario.añadirPalabra(palabra);
        return true;
    }

    // Metodo para obtener la última palabra inválida
    public String getUltimaPalabraInvalida() {
        return ultimaPalabraInvalida;
    }

    // Metodo para acceder al diccionario
    public Diccionario getDiccionario() {
        return diccionario;
    }

    // Modificación del metodo procesarPalabra
    public ResultadoPalabra procesarPalabra(String jugador, String palabraInput) {
        if (!jugador.equals(getJugadorActual())) {
            return new ResultadoPalabra(false, "No es tu turno", 0);
        }

        String palabra = palabraInput.toUpperCase();
        ultimaPalabraInvalida = null; // Resetear la última palabra inválida

        if (palabrasUsadas.contains(palabra)) {
            return new ResultadoPalabra(false, "Esta palabra ya se usó en esta ronda", 0);
        }

        if (!letrasValidas(palabra)) {
            int penalizacion = (modoDeJuego == 1) ? -5 : -10;
            puntajeJugador.merge(jugador, penalizacion, Integer::sum);
            return new ResultadoPalabra(false, "Letras no válidas", penalizacion);
        }

        if (!diccionario.contienePalabra(palabra, modoDeJuego)) {
            int penalizacion = (modoDeJuego == 1) ? -5 : -10;
            puntajeJugador.merge(jugador, penalizacion, Integer::sum);
            ultimaPalabraInvalida = palabra; // Guardar la palabra inválida
            return new ResultadoPalabra(false, "Palabra no válida", penalizacion);
        }

        palabrasUsadas.add(palabra);
        int puntos = diccionario.obtenerPuntos(palabra);
        puntajeJugador.merge(jugador, puntos, Integer::sum);

        return new ResultadoPalabra(true, "Palabra válida", puntos);
    }

    // Resto de los métodos permanecen igual...
    public Set<Character> getLetrasActuales() {
        return Collections.unmodifiableSet(letrasActuales);
    }

    public HashMap<String, Integer> getPuntuaciones() {
        return new HashMap<>(puntajeJugador);
    }

    public String getJugadorActual() {
        return jugadores.get(jugadorActual);
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public boolean esFinalDelJuego() {
        return rondaActual > 3;
    }

    public void prepararNuevaRonda() {
        letrasActuales = generarLetras(10);
        palabrasUsadas.clear();
    }

    public void iniciarJuego() {
        prepararNuevaRonda();
    }

    public void pasarTurno() {
        palabrasUsadas.clear();
        siguienteTurno();
    }

    private void siguienteTurno() {
        jugadorActual++;
        if (jugadorActual >= jugadores.size()) {
            jugadorActual = 0;
            rondaActual++;
            if (!esFinalDelJuego()) {
                prepararNuevaRonda();
            }
        }
    }

    public static class ResultadoPalabra {
        public final boolean valida;
        public final String mensaje;
        public final int puntos;

        public ResultadoPalabra(boolean valida, String mensaje, int puntos) {
            this.valida = valida;
            this.mensaje = mensaje;
            this.puntos = puntos;
        }
    }

    private boolean letrasValidas(String palabra) {
        for (char letra : palabra.toCharArray()) {
            if (!letrasActuales.contains(Character.toUpperCase(letra))) {
                return false;
            }
        }
        return true;
    }

    private Set<Character> generarLetras(int cantidad) {
        Random random = new Random();
        Set<Character> letras = new HashSet<>();
        String vocalesBasicas = "AEIOU";
        String vocalesAcentuadas = "ÁÉÍÓ";
        String consonantes = "BCDFGHJKLMNPRST";
        String consonantesDificiles = "VWXYZ";
        String consonantesExpertas = "BCDFGHJKLMNPQRSTVWXYZ";

        for (int i = 0; i < 4; i++) {
            letras.add(vocalesBasicas.charAt(random.nextInt(vocalesBasicas.length())));
        }

        if (modoDeJuego == 1) {
            letras.add(consonantesDificiles.charAt(random.nextInt(consonantesDificiles.length())));
            if (random.nextDouble() <= 0.10) {
                letras.add('W');
                letras.add('X');
                letras.add('Y');
                letras.add('Z');
            }
        }

        if (modoDeJuego == 2) {
            letras.add(vocalesAcentuadas.charAt(random.nextInt(vocalesAcentuadas.length())));
            if (random.nextDouble() <= 0.2) {
                letras.add('Ñ');
            }
        }

        String fuenteConsonantes = (modoDeJuego == 2) ? consonantesExpertas : consonantes;
        while (letras.size() < cantidad) {
            char c = fuenteConsonantes.charAt(random.nextInt(fuenteConsonantes.length()));

            letras.add(c);
        }
        return letras;
    }
}