package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public Button startbutton, aproposbutton, selectbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startbutton = findViewById(R.id.startbutton);
        startbutton.setOnClickListener(view ->{
             Intent intent = new Intent(this, LevelActivity.class);


             startActivity(intent);
         });

        aproposbutton = findViewById(R.id.aproposbutton);
        aproposbutton.setOnClickListener(view ->{
            Intent intent = new Intent(this, AboutActivity.class);

            startActivity(intent);
        });

        selectbutton= findViewById(R.id.selectbutton);
        selectbutton.setOnClickListener(view ->{
            Intent intent = new Intent(this, ListLevelActivity.class);

            startActivity(intent);
        });
    }
}