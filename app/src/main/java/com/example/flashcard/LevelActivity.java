package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelActivity extends AppCompatActivity {

    private TextView questiontextView, countertextView, difficultytextView;
    private RadioGroup radioGroup;
    private ImageView guess;
    private Button confirmationButton;

    private List<MainActivity.Question> questionList, questionLoseList;
    private int currentQuestionIndex = 0;

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

        initializeViews();
        initializeQuiz();
        setupListeners();
    }

    private void initializeViews() {
        questiontextView = findViewById(R.id.questiontextView);
        countertextView = findViewById(R.id.countertextView);
        difficultytextView = findViewById(R.id.difficultytextView);
        guess = findViewById(R.id.pictureimageView);
        confirmationButton = findViewById(R.id.confirmbutton);
        radioGroup = findViewById(R.id.radiogroup); // Assurez-vous que l'ID est "radiogroup" dans le XML
    }

    private void initializeQuiz() {
        Intent srcIntent = getIntent();
        questionList = srcIntent.getParcelableArrayListExtra("question");
        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Aucune question trouvée pour cette difficulté.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if( srcIntent.getParcelableArrayListExtra("questionLose") == null) {
            questionLoseList = new java.util.ArrayList<MainActivity.Question>();
        }

        String difficulty = questionList.get(0).getDifficulte();

        if ("Hardcore".equalsIgnoreCase(difficulty)) {
            RotateAnimation rotate = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(4000);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setInterpolator(new android.view.animation.LinearInterpolator());
            guess.startAnimation(rotate);
        }

        Collections.shuffle(questionList);
        loadQuestion(currentQuestionIndex);
    }

    private void loadQuestion(int index) {
        MainActivity.Question currentQuestion = questionList.get(index);
        List<MainActivity.Reponse> answers = currentQuestion.getReponses();
        Collections.shuffle(answers);

        difficultytextView.setText(currentQuestion.getDifficulte());
        questiontextView.setText("Que voyez-vous ?");
        guess.setImageResource(currentQuestion.getImage(this));

        String counterText = "Question " + (index + 1) + "/" + questionList.size();
        countertextView.setText(counterText);

        radioGroup.clearCheck();
        radioGroup.removeAllViews();

        for (MainActivity.Reponse answer : answers) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer.getReponce());
            radioButton.setId(View.generateViewId());
            radioGroup.addView(radioButton);
        }
    }

    private void setupListeners() {
        confirmationButton.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Veuillez sélectionner une réponse.", Toast.LENGTH_SHORT).show();
                return;
            }
            checkAnswer(selectedId);
        });
    }

    private void checkAnswer(int selectedId) {
        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswerText = selectedButton.getText().toString();
        List<MainActivity.Reponse> currentAnswers = questionList.get(currentQuestionIndex).getReponses();
        boolean isCorrect = false;
        String correctAnswerText = "";

        for (MainActivity.Reponse answer : currentAnswers) {
            if (answer.isBonneReponce()) {
                correctAnswerText = answer.getReponce();
                if (correctAnswerText.equalsIgnoreCase(selectedAnswerText)) {
                    isCorrect = true;
                }
                break;
            }
        }

        if (isCorrect) {
            showResultPopup("Bonne réponse !", "Félicitations !");
        } else {
            showResultPopup("Oops, dommage...", "La bonne réponse était : " + correctAnswerText);
            questionLoseList.add(questionList.get(currentQuestionIndex));
        }
    }

    private void showResultPopup(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Suivant", (dialog, which) -> {
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questionList.size()) {
                        loadQuestion(currentQuestionIndex);
                    } else {
                        showFinalScorePopup();
                    }
                })
                .show();
    }

    private void showFinalScorePopup() {
        new AlertDialog.Builder(this)
                .setTitle("Quiz terminé !")
                .setMessage("Vous avez répondu à toutes les questions.")
                .setCancelable(false)
                .setPositiveButton("Voir le score finale", (dialog, which) -> {
                    Intent intent = new Intent(LevelActivity.this, ScoreActivity.class);
                    intent.putParcelableArrayListExtra("question", getIntent().getParcelableArrayListExtra("question"));
                    intent.putParcelableArrayListExtra("questionLose", (ArrayList<? extends Parcelable>) questionLoseList);
                    startActivity(intent);
                    finish();
                })
                .show();
    }
}
