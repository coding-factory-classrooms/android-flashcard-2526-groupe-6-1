package com.example.flashcard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.Button;
import java.io.Serializable;
import java.util.Arrays;

public class ScoreActivity extends AppCompatActivity {

    // Classe Question complète
    public static class Question implements Serializable {
        String difficulty;
        String questionText;
        ArrayList<String> answers;
        String correctAnswer;

        public Question(String difficulty, String questionText, ArrayList<String> answers, String correctAnswer) {
            this.difficulty = difficulty;
            this.questionText = questionText;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }
    }

    private int score;
    private int totalQuestions;
    private String difficulte = "Moyen"; // simulée pour test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        Button shareButton = findViewById(R.id.shareButton);
        Button replayButton = findViewById(R.id.replayButton);

        // Simuler une liste de questions complètes
        ArrayList<Question> listQuestion = new ArrayList<>();
        listQuestion.add(new Question(
                "Moyen",
                "Quelle est la capitale de la France ?",
                new ArrayList<>(Arrays.asList("Paris", "Lyon", "Marseille", "Nice")),
                "Paris"
        ));
        listQuestion.add(new Question(
                "Moyen",
                "Combien font 5 + 3 ?",
                new ArrayList<>(Arrays.asList("6", "8", "9", "7")),
                "8"
        ));
        listQuestion.add(new Question(
                "Moyen",
                "Quelle est la couleur du ciel ?",
                new ArrayList<>(Arrays.asList("Bleu", "Vert", "Rouge", "Noir")),
                "Bleu"
        ));
        listQuestion.add(new Question(
                "Moyen",
                "Quel est le résultat de 2 x 2 ?",
                new ArrayList<>(Arrays.asList("2", "4", "6", "8")),
                "4"
        ));

        // Simuler les questions perdues (ratées)
        ArrayList<Question> questionPerdu = new ArrayList<>();
        questionPerdu.add(listQuestion.get(1)); // mauvaise réponse à 2ème question
        questionPerdu.add(listQuestion.get(3)); // mauvaise réponse à 4ème question

        totalQuestions = listQuestion.size();
        score = totalQuestions - questionPerdu.size();

        scoreTextView.setText("Score : " + score + " / " + totalQuestions);

        // Bouton partager
        shareButton.setOnClickListener(view -> shareScore());

        // Bouton rejouer : on renvoie seulement les questions ratées
        replayButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("replayQuestions", questionPerdu);
            startActivity(intent);
        });
    }

    // Message de partage incluant la difficulté
    private void shareScore() {
        String message = "J'ai eu " + score + "/" + totalQuestions + " au quiz !\n"
                + "Difficulté : " + difficulte + "\n"
                + "🔥 Et toi, tu fais mieux ?";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Partager via");
        startActivity(shareIntent);
    }
}


