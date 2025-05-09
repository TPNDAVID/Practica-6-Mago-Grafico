import java.io.*;
import java.util.*;

public class Mago {
    private Diccionario diccionario;
    private HashMap<String, Integer> puntajeJugador;
    private int modoDeJuego;
    private List<String> jugadores;
    private Set<Character> letrasActuales;
    private Set<String> palabrasUsadas = new HashSet<>();
    private Set<String> jugadoresQuePasaron = new HashSet<>();
    private int jugadorActual = 0;
    private int rondaActual = 1;

    public Mago(List<String> jugadores, int modoDeJuego) {
        this.jugadores = new ArrayList<>(jugadores);
        this.modoDeJuego = modoDeJuego;
        this.puntajeJugador = new HashMap<>();
        this.diccionario = new Diccionario(modoDeJuego);
        this.letrasActuales = new HashSet<>();

        try {
            diccionario.loadFromFile("src/diccionariopalabrasmago.txt");
        } catch (IOException e) {
            System.err.println("Error al cargar el diccionario: " + e.getMessage());
            System.exit(1);
        }

        jugadores.forEach(p -> puntajeJugador.put(p, 0));
        prepararNuevaRonda();
    }

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

    public ResultadoPalabra procesarPalabra(String jugador, String palabraInput) {
        if (!jugador.equals(getJugadorActual())) {
            return new ResultadoPalabra(false, "No es tu turno", 0);
        }

        String palabra = palabraInput.toUpperCase();
        ResultadoPalabra resultado;

        if (palabrasUsadas.contains(palabra)) {
            resultado = new ResultadoPalabra(false, "Esta palabra ya se usó en esta ronda", 0);
        } else if (!letrasValidas(palabra)) {
            int penalizacion = (modoDeJuego == 1) ? -5 : -10;
            puntajeJugador.merge(jugador, penalizacion, Integer::sum);
            resultado = new ResultadoPalabra(false, "Letras no válidas", penalizacion);
        } else if (!diccionario.contienePalabra(palabra, modoDeJuego)) {
            int penalizacion = (modoDeJuego == 1) ? -5 : -10;
            puntajeJugador.merge(jugador, penalizacion, Integer::sum);
            resultado = new ResultadoPalabra(false, "Palabra no válida", penalizacion);
        } else {
            palabrasUsadas.add(palabra);
            int puntos = diccionario.obtenerPuntos(palabra);
            puntajeJugador.merge(jugador, puntos, Integer::sum);
            resultado = new ResultadoPalabra(true, "Palabra válida", puntos);
        }

        siguienteTurno();

        return resultado;
    }

    public boolean pasarTurno() {
        jugadoresQuePasaron.add(jugadores.get(jugadorActual));

        siguienteTurno();

        if (jugadoresQuePasaron.size() >= jugadores.size()) {
            jugadoresQuePasaron.clear();
            rondaActual++;
            if (!esFinalDelJuego()) {
                prepararNuevaRonda();
            }
            return true;
        }
        return false;
    }

    private void siguienteTurno() {
        jugadorActual = (jugadorActual + 1) % jugadores.size();
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
        Set<Character> letrasDisponibles = getLetrasActuales();
        Iterator<Character> it = palabra.toUpperCase()
                .chars()
                .mapToObj(c -> (char) c)
                .iterator();

        while (it.hasNext()) {
            if (!letrasDisponibles.contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    private Set<Character> generarLetras(int cantidad) {
        Random random = new Random(System.nanoTime());
        Set<Character> letras = new LinkedHashSet<>();
        String vocalesBasicas = "AEIOU";
        String vocalesAcentuadas = "ÁÉÍÓ";
        String consonantes = "BCDFGHJKLMNPRST";
        String consonantesDificiles = "VWXYZ";

        for (int i = 0; i < 4; i++) {
            letras.add(vocalesBasicas.charAt(random.nextInt(vocalesBasicas.length())));
        }

        if (modoDeJuego == 2) {
            letras.add(vocalesAcentuadas.charAt(random.nextInt(vocalesAcentuadas.length())));
            if (random.nextDouble() <= 0.2) {
                letras.add('Ñ');
            }
        }

        String fuenteConsonantes = (modoDeJuego == 1) ? consonantes : consonantes + consonantesDificiles;
        while (letras.size() < cantidad) {
            char c = fuenteConsonantes.charAt(random.nextInt(fuenteConsonantes.length()));
            letras.add(c);
        }

        List<Character> letrasMezcladas = new ArrayList<>(letras);
        Collections.shuffle(letrasMezcladas, random);
        return new LinkedHashSet<>(letrasMezcladas);
    }
}
