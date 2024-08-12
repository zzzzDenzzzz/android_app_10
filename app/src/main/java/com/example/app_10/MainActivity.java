package com.example.app_10;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        errorTextView = findViewById(R.id.errorTextView);

        fetchData("https://api.adviceslip.com/advice", textView1);
        fetchData("https://catfact.ninja/fact", textView2);
    }

    private void fetchData(String urlString, TextView tw) {
        Thread thread = new Thread(() -> {
            String result;
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    result = stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "Ошибка: " + e.getMessage();
                String finalResult1 = result;
                runOnUiThread(() -> {
                    errorTextView.setText(finalResult1);
                    errorTextView.setVisibility(View.VISIBLE);
                });
            }

            String finalResult = result;
            runOnUiThread(() -> tw.setText(finalResult));
        });
        thread.start();
    }
}