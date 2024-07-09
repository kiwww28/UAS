package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        try {
            JFrame jFrame = new JFrame();
            jFrame.setTitle("Pokemon");
            jFrame.setSize(600, 400); // Ukuran JFrame yang lebih pas untuk menampung tabel
            jFrame.setLocationRelativeTo(null);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();

            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            String url = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=30";
            URI uri = new URI(url);
            
            // Buat URL dengan parameter limit dan offset
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Deserialisasi JSON Response
            Pokemon jsonObject = gson.fromJson(response.body(), Pokemon.class);
            // Data untuk tabel
            String[] columnNames = {"Name", "URL"};
            Object[][] data = new Object[jsonObject.results.length][2];

            // Mengisi data dari response JSON ke dalam array data
            for (int i = 0; i < jsonObject.results.length; i++) {
                PokemonDetails details = jsonObject.results[i];
                data[i][0] = details.name;
                data[i][1] = details.url;
            }

            // Buat model untuk tabel
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(tableModel);

            // Menambahkan tabel ke dalam panel dengan JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane);

            // Menambahkan panel ke dalam JFrame
            jFrame.add(panel);
            jFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
