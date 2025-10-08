package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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

            showDifficultyPopup();
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
    private void showDifficultyPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisis la difficulté");

        CharSequence[] options = {"Facile", "Moyen", "Difficile"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Selon l'index, lancer une activité différente
                Intent intent;
                String question = "Quelle est la capitale de la France ?";
                ArrayList<String> answer = new ArrayList<>();
                answer.add(0, "response 1");
                answer.add(1, "response 2");
                answer.add(2, "response 3");
                answer.add(3, "response 4");
                switch (which) {
                    case 0: // Facile
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("difficulty", 3);
                        intent.putExtra("question_text", question);
                        intent.putExtra("reponse", answer);
                        break;
                    case 1: // Moyen
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("difficulty", 5);
                        intent.putExtra("question_text", question);
                        intent.putExtra("reponse", answer);
                        break;
                    case 2: // Difficile
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("difficulty", 10);
                        intent.putExtra("question_text", question);
                        intent.putExtra("reponse", answer);
                        break;
                    default:
                        return; // ne rien faire si problème
                }

                startActivity(intent);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}