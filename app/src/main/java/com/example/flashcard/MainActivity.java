package com.example.flashcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;import java.io.Reader;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public Button startbutton, aproposbutton, selectbutton;



    public static class Reponse implements Parcelable {
        private boolean bonnereponce;
        private String reponce;

        public Reponse(String reponce, boolean bonnereponce) {
            this.reponce = reponce;
            this.bonnereponce = bonnereponce;
        }

        public boolean isBonneReponce() {
            return bonnereponce;
        }

        public String getReponce() {
            return reponce;
        }

        protected Reponse(Parcel in) {
            bonnereponce = in.readByte() != 0; // boolean stored as byte
            reponce = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (bonnereponce ? 1 : 0));
            dest.writeString(reponce);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Reponse> CREATOR = new Creator<Reponse>() {
            @Override
            public Reponse createFromParcel(Parcel in) {
                return new Reponse(in);
            }

            @Override
            public Reponse[] newArray(int size) {
                return new Reponse[size];
            }
        };
    }


    public static class Question implements Parcelable {
        //correspondre EXACTEMENT aux clés du JSON
        private String difficulte;
        private String image;
        private List<Reponse> reponses;
        private String imagereponce;


        // Constructeur vide nécessaire pour Gson
        public Question() {}

        public Question(String difficulte, String image, List<Reponse> reponses, String imagereponce) {
            this.difficulte = difficulte;
            this.image = image;
            this.reponses = reponses;
            this.imagereponce = imagereponce;
        }


        public String getdifficulte() { return difficulte; }
        public List<Reponse> getReponses() { return reponses; }


        public int getImage(Context context) {
            return context.getResources().getIdentifier(image, "drawable", context.getPackageName());
        }

        public int getImagereponce(Context context) {
            return context.getResources().getIdentifier(imagereponce, "drawable", context.getPackageName());
        }


        protected Question(Parcel in) {
            difficulte = in.readString();
            image = in.readString();
            reponses = in.createTypedArrayList(Reponse.CREATOR);
            imagereponce = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(difficulte);
            dest.writeString(image);
            dest.writeTypedList(reponses);
            dest.writeString(imagereponce);
        }

        @Override
        public int describeContents() { return 0; }

        public static final Creator<Question> CREATOR = new Creator<Question>() {
            @Override
            public Question createFromParcel(Parcel in) {
                return new Question(in);
            }

            @Override
            public Question[] newArray(int size) {
                return new Question[size];
            }
        };
    }

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
    private void showDifficultyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisis la difficulté");

        CharSequence[] options = {"Facile", "Moyen", "Difficile"};
        String[] difficultiesInJson = {"facile", "moyen", "difficile"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<Question> toutesLesQuestions = chargerQuestionsJson();


                String difficulteChoisie = difficultiesInJson[which];
                ArrayList<Question> questionsFiltrees = toutesLesQuestions.stream()
                        .filter(q -> q.getdifficulte().equalsIgnoreCase(difficulteChoisie))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (!questionsFiltrees.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, LevelActivity.class);

                    intent.putParcelableArrayListExtra("questions", questionsFiltrees);
                    startActivity(intent);
                } else {
                    // Gérer le cas où aucune question n'est trouvée pour cette difficulté
                    //toast("Aucune question trouvée pour cette difficulté.");
                }

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private List<Question> chargerQuestionsJson() {
        // Ouvre le flux de lecture pour le fichier JSON
        InputStream inputStream = getResources().openRawResource(R.raw.questions);
        Reader reader = new InputStreamReader(inputStream);

        // Prépare Gson pour convertir le JSON en une liste de Questions
        Gson gson = new Gson();
        Type questionListType = new TypeToken<ArrayList<Question>>(){}.getType();

        // Convertit et retourne la liste. Retourne une liste vide en cas d'erreur.
        List<Question> questions = gson.fromJson(reader, questionListType);
        return questions != null ? questions : new ArrayList<>();
    }

}