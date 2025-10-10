package com.example.flashcard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.SerializedName; // IMPORT TRÈS IMPORTANT

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public Button startbutton, aproposbutton, selectbutton;

    public static class Reponse implements Parcelable {
        @SerializedName("reponce")
        private String reponce;

        @SerializedName("bonnereponce")
        private boolean bonnereponce;

        public Reponse() {} // Constructeur vide obligatoire pour Gson

        public String getReponce() {
            return reponce;
        }

        public boolean isBonneReponce() {
            return bonnereponce;
        }

        protected Reponse(Parcel in) {
            reponce = in.readString();
            bonnereponce = in.readByte() != 0;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(reponce);
            dest.writeByte((byte) (bonnereponce ? 1 : 0));
        }
    }


    public static class Question implements Parcelable {
        @SerializedName("difficulte")
        private String difficulte;

        @SerializedName("image")
        private String image;

        @SerializedName("imagereponce")
        private String imagereponce;

        @SerializedName("reponses")
        private List<Reponse> reponses;

        public Question() {} // Constructeur vide obligatoire pour Gson

        public String getDifficulte() {
            return difficulte;
        }

        public void setDifficulte(String difficulte) {
            this.difficulte = difficulte;
        }

        public List<Reponse> getReponses() {
            return reponses;
        }

        public int getImage(Context context) {
            return context.getResources().getIdentifier(image, "drawable", context.getPackageName());
        }

        public int getImageReponce(Context context) {
            return context.getResources().getIdentifier(imagereponce, "drawable", context.getPackageName());
        }

        protected Question(Parcel in) {
            difficulte = in.readString();
            image = in.readString();
            imagereponce = in.readString();
            reponses = in.createTypedArrayList(Reponse.CREATOR);
        }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(difficulte);
            dest.writeString(image);
            dest.writeString(imagereponce);
            dest.writeTypedList(reponses);
        }
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

        Intent musicServiceIntent = new Intent(this, MusicService.class);
        startService(musicServiceIntent);

        Button stopMusicButton = findViewById(R.id.stop_music_button);
        stopMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(musicServiceIntent);
            }
        });


        startbutton = findViewById(R.id.startbutton);
        startbutton.setOnClickListener(view -> {
            showDifficultyPopup();
        });

        aproposbutton = findViewById(R.id.aproposbutton);
        aproposbutton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });

        selectbutton = findViewById(R.id.selectbutton);
        selectbutton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListLevelActivity.class);
            List<Question> toutesLesQuestions = chargerQuestionsJson();
            intent.putParcelableArrayListExtra("question", new ArrayList<>(toutesLesQuestions));
            startActivity(intent);
        });
    }

    private void showDifficultyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisis la difficulté");

        CharSequence[] options = {"Facile", "Moyen", "Difficile", "Hardcore"};

        builder.setItems(options, (dialog, which) -> {
            List<Question> toutesLesQuestions = chargerQuestionsJson();

            if (toutesLesQuestions == null || toutesLesQuestions.isEmpty()) {
                Log.e("JSON_ERROR", "question vide");
                return;
            }

            ArrayList<Question> questionsForLevel;
            String difficultePourJson;

            if (options[which].equals("Hardcore")) {
                difficultePourJson = "difficile";
                questionsForLevel = toutesLesQuestions.stream()
                        .filter(q -> q.getDifficulte() != null && q.getDifficulte().equalsIgnoreCase(difficultePourJson))
                        .collect(Collectors.toCollection(ArrayList::new));

                for (Question q : questionsForLevel) {
                    q.setDifficulte("Hardcore");
                }
            } else {
                String[] difficultiesInJson = {"facile", "moyen", "difficile"};
                difficultePourJson = difficultiesInJson[which];
                questionsForLevel = toutesLesQuestions.stream()
                        .filter(q -> q.getDifficulte() != null && q.getDifficulte().equalsIgnoreCase(difficultePourJson))
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            Intent intent = new Intent(MainActivity.this, LevelActivity.class);
            intent.putParcelableArrayListExtra("question", questionsForLevel);
            startActivity(intent);

            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private List<Question> chargerQuestionsJson() {
        InputStream inputStream = getResources().openRawResource(R.raw.questions);
        Reader reader = new InputStreamReader(inputStream);

        Type questionListType = new TypeToken<ArrayList<Question>>(){}.getType();
        Gson gson = new Gson();

        List<Question> questions = gson.fromJson(reader, questionListType);
        return questions != null ? questions : new ArrayList<>();
    }
}
