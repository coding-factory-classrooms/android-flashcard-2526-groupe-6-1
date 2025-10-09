package com.example.flashcard;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    // Classe Question simple interne à ScoreActivity
    public static class Question {
        String questionText;

        public Question(String questionText) {
            this.questionText = questionText;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.scoreTextView);

        // Liste complète de questions (hardcodée)
        ArrayList<Question> listQuestion = new ArrayList<>();
        listQuestion.add(new Question("Question 1"));
        listQuestion.add(new Question("Question 2"));
        listQuestion.add(new Question("Question 3"));
        listQuestion.add(new Question("Question 4"));

        // Liste des questions perdues (hardcodée)
        ArrayList<Question> questionPerdu = new ArrayList<>();
        questionPerdu.add(new Question("Question 2"));
        questionPerdu.add(new Question("Question 4"));

        // Calcul du score : total - perdu
        int score = listQuestion.size() - questionPerdu.size();

        // Affichage
        scoreTextView.setText("Score : " + score + " / " + listQuestion.size());
    }
}

