package com.example.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    private MemoryGameView memoryGameView;
    private TextView statusText;
    private Button restartButton;

//    git
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gán các view từ layout sau setContentView
        memoryGameView = findViewById(R.id.memoryGameView);
        statusText = findViewById(R.id.statusText);
        restartButton = findViewById(R.id.restartButton);
        startButton = findViewById(R.id.startButton);

        // Gán sự kiện khi trò chơi kết thúc
        memoryGameView.setGameEndListener(() -> {
            statusText.setText("게임이 끝났습니다");
            restartButton.setVisibility(View.VISIBLE);
        });

        // Nút restart
        restartButton.setOnClickListener(v -> {
            memoryGameView.restartGame();
            statusText.setText("");
            restartButton.setVisibility(View.GONE);
        });

        // Nút bắt đầu (start)
        startButton.setOnClickListener(v -> {
            memoryGameView.restartGame();
        });
    }
}
