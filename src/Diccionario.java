import java.io.*;
import java.util.*;

public class Diccionario {
    private HashMap<String, Integer> palabras = new HashMap<>();
    private String archivoDiccionario;

    public Diccionario(int mode, String filename) {
        this.archivoDiccionario = filename;
        try {
            loadFromFile(filename);
        } catch (IOException e) {
            System.err.println("Error al cargar el diccionario: " + e.getMessage());
        }
    }

    // Método para añadir palabras
    public synchronized void añadirPalabra(String palabra) {
        String palabraUpper = palabra.toUpperCase().trim();
        if (!palabras.containsKey(palabraUpper)) {
            int puntos = calcularPuntos(palabraUpper);
            palabras.put(palabraUpper, puntos);
            guardarDiccionario();
        }
    }

    private void guardarDiccionario() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDiccionario, true))) {
            for (Map.Entry<String, Integer> entry : palabras.entrySet()) {
                if (!entry.getKey().isEmpty()) {
                    writer.write(entry.getKey());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar en el diccionario: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.lines()
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> {
                        String palabra = line.trim().toUpperCase();
                        int puntos = calcularPuntos(palabra);
                        palabras.put(palabra, puntos);
                    });
        }
    }

    private int calcularPuntos(String palabra) {
        String vocales = "AEIOUÁÉÍÓÚ";
        long numVocales = palabra.chars()
                .filter(c -> vocales.indexOf(c) != -1)
                .count();
        return (int) (numVocales * 5 + (palabra.length() - numVocales) * 3);
    }

    public boolean contienePalabra(String palabra, int mode) {
        String palabraUpper = palabra.toUpperCase();
        if (!palabras.containsKey(palabraUpper)) return false;

        if (mode == 1 && (palabraUpper.matches(".[ÁÉÍÓÚÑ]."))) {
            return false;
        }
        return true;
    }

    public int obtenerPuntos(String palabra) {
        return palabras.getOrDefault(palabra.toUpperCase(), 0);
    }
}