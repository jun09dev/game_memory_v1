package com.example.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MemoryGameView memoryGameView;
    private TextView statusText;
    private Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memoryGameView = findViewById(R.id.memoryGameView);
        statusText = findViewById(R.id.statusText);
        restartButton = findViewById(R.id.restartButton);

        memoryGameView.setGameEndListener(() -> {
            statusText.setText("Trò chơi kết thúc!");
            restartButton.setVisibility(View.VISIBLE);
        });

        restartButton.setOnClickListener(v -> {
            memoryGameView.restartGame();
            statusText.setText("");
            restartButton.setVisibility(View.GONE);
        });
    }
}