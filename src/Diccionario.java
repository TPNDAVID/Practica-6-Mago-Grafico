import java.io.*;
import java.util.*;

public class Diccionario {
    private HashMap<String, Integer> palabras = new HashMap<>();

    public Diccionario(int mode) {
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

    public void addWord(String palabra) throws IOException {
        String palabraUpper = palabra.toUpperCase().trim();
        if (!palabras.containsKey(palabraUpper)) {
            int puntos = calcularPuntos(palabraUpper);
            palabras.put(palabraUpper, puntos);
            saveToFile("src/diccionariopalabrasmago.txt", palabraUpper);
        }
    }

    private void saveToFile(String filename, String nuevaPalabra) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.newLine();
            writer.write(nuevaPalabra);
        }
    }

}