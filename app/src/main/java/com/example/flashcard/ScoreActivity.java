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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    public static class Question {
        String questionText;

        public Question(String questionText) {
            this.questionText = questionText;
        }
    }

    private int score;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.scoreTextView);
        Button shareButton = findViewById(R.id.shareButton);

        // Hardcoded lists pour test
        ArrayList<Question> listQuestion = new ArrayList<>();
        listQuestion.add(new Question("Question 1"));
        listQuestion.add(new Question("Question 2"));
        listQuestion.add(new Question("Question 3"));
        listQuestion.add(new Question("Question 4"));

        ArrayList<Question> questionPerdu = new ArrayList<>();
        questionPerdu.add(new Question("Question 2"));
        questionPerdu.add(new Question("Question 4"));

        totalQuestions = listQuestion.size();
        score = totalQuestions - questionPerdu.size();

        scoreTextView.setText("Score : " + score + " / " + totalQuestions);

        // Bouton partage
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareScore();
            }
        });
    }

    private void shareScore() {
        String message = "J'ai eu " + score + "/" + totalQuestions + " au quiz !";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Partager via");
        startActivity(shareIntent);
    }
}


