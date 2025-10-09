package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

public class LevelActivity extends AppCompatActivity {

    public TextView questiontextView, countertextView, difficultytextView;
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

        //Nombre de question
        difficultytextView = findViewById(R.id.difficultytextView);
        difficultytextView.setText(listquestion.get(0).getdifficulte());


        //affichage des réponses
        for (int i = 0; i < reponses.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(reponses.get(i).getReponce());
            radioButton.setId(View.generateViewId()); // pour donner un ID unique automatiquement
            radioGroup.addView(radioButton);
        }
        layout.addView(radioGroup);
        //bouton de confirmation
        Button confirmation = findViewById(R.id.confirmbutton);
        confirmation.setOnClickListener(view ->{
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) { // -1 = rien sélectionné
                RadioButton selectedButton = findViewById(selectedId);
                String buttonchose = selectedButton.getText().toString();
                MainActivity.Reponse resultat = null;
                int indexTrouve = -1;

                for (int i = 0; i < reponses.size(); i++) {
                    if (reponses.get(i).getReponce().equalsIgnoreCase(buttonchose)) {
                        indexTrouve = i;
                        break;
                    }
                }
                if(reponses.get(indexTrouve).isBonneReponce()){
                    String message = "Bonne réponse!";
                    showResultPopup(message,"La bonne réponse est bien " + reponses.get(indexTrouve).getReponce());
                }else{
                    String message = "Oops dommage";
                    String reponse = "";
                    for (MainActivity.Reponse rep : reponses) {
                        if (rep.isBonneReponce()) {
                            reponse = rep.getReponce();
                            break;
                        }
                    }
                    showResultPopup(message, "La bonne réponse était " + reponse);
                }
            }

        });


    }

    private void showResultPopup(String message, String reponse){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message)
                .setMessage(reponse);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}