package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class LevelActivity extends AppCompatActivity {

    public TextView questiontextView;
    public String question;
    public  int choix;
    public RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent srcIntent = getIntent();

        //modification du nom de la question
        questiontextView = findViewById(R.id.questiontextView);
        question = srcIntent.getStringExtra("question_text");
        questiontextView.setText(question);

        //modification des reponses
        List<String> reponse = srcIntent.getStringArrayListExtra("reponse");

            radioButton = findViewById(R.id.responseradioButton);
            radioButton.setText(reponse.get(0));
            radioButton = findViewById(R.id.responseradioButton2);
            radioButton.setText(reponse.get(1));
            radioButton = findViewById(R.id.responseradioButton3);
            radioButton.setText(reponse.get(2));
            radioButton = findViewById(R.id.responseradioButton4);
            radioButton.setText(reponse.get(3));
    }
}