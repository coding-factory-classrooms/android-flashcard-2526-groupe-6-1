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
import java.util.List;

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
    private List<MainActivity.Question> totalQuestions, questionPerdu;
    private String difficulte; // simulée pour test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        Button shareButton = findViewById(R.id.shareButton);
        Button replayButton = findViewById(R.id.replayButton);
        // recuperation de la list de question de la page LevelActivity
        Intent srcintent = getIntent();
        totalQuestions = srcintent.getParcelableArrayListExtra("question");
        questionPerdu = srcintent.getParcelableArrayListExtra("questionLose");

        // calcul du score
        score = totalQuestions.size() - questionPerdu.size();
        difficulte = totalQuestions.get(0).getDifficulte();

        questionPerdu = null;

        scoreTextView.setText("Score : " + score + " / " + totalQuestions.size());

        // Bouton partager
        shareButton.setOnClickListener(view -> shareScore());

        // Bouton rejouer : on renvoie seulement les questions ratées
//        replayButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LevelActivity.class);
//            intent.putParcelableArrayListExtra("replayQuestions", questionPerdu);
//            startActivity(intent);
//        });
    }

    // fonction du boutton partage
    private void shareScore() {
        String message = "J'ai eu " + score + "/" + totalQuestions.size() + " au quiz !\n"
                + "Difficulté : " + difficulte + "\n"
                + "🔥 Et toi, tu fais mieux ?";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Partager via");
        startActivity(shareIntent);
    }
}


