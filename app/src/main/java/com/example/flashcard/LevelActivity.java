package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public LinearLayout layout;
    public RadioGroup radioGroup;


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
        layout = findViewById(R.id.radioGroupContainer); // ton conteneur dans le XML
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL); // oriente verticalement les boutons de réponse

        List<response> reponse = srcIntent.getStringArrayListExtra("reponse");

        for (int i = 0; i < reponse.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText();
            radioButton.setId();
            radioGroup.addView(radioButton);
        }

        layout.addView(radioGroup);
    }
}