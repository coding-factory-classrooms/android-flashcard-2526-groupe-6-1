package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
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

        //récupération de l'intent
        Intent srcIntent = getIntent();

        //modification du nom de la question
        questiontextView = findViewById(R.id.questiontextView);
        questiontextView.setText("Que voyez-vous ?");


        //Positionnement des réponses dans le layout
        layout = findViewById(R.id.radioGroupContainer); // conteneur dans le XML
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL); // oriente verticalement les boutons de réponse

        //récupération des questions et réponse
        List<MainActivity.Question> listquestion = srcIntent.getParcelableArrayListExtra("question");
        MainActivity.Question q = listquestion.get(0);
        List<MainActivity.Reponse> reponses = q.getReponses();

        for (int i = 0; i < reponses.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(reponses.get(i).getReponce());
            radioButton.setId(View.generateViewId()); // pour donner un ID unique automatiquement
            radioGroup.addView(radioButton);
        }
        layout.addView(radioGroup);

    }
}