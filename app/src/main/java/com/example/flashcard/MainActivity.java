package com.example.flashcard;

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
        private String difficulte;
        private int image;
        private List<Reponse> reponses;

        // Constructor
        public Question(String difficulte, int image, List<Reponse> reponses) {
            this.difficulte = difficulte;
            this.image = image;
            this.reponses = reponses;
        }


        public String getdifficulte() { return difficulte; }
        public int getImage() { return image; }
        public List<Reponse> getReponses() { return reponses; }

        // Parcel constructor
        protected Question(Parcel in) {
            difficulte = in.readString();
            image = in.readInt();
            reponses = in.createTypedArrayList(Reponse.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(difficulte);
            dest.writeInt(image);
            dest.writeTypedList(reponses);
        }

        @Override
        public int describeContents() {
            return 0;
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
    private void showDifficultyPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisis la difficulté");

        CharSequence[] options = {"Facile", "Moyen", "Difficile"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent;

                List<Reponse> reponseslist = new ArrayList<>();
                List<Question> questionslist = new ArrayList<>();

                reponseslist.add(new Reponse("cheval",true));
                reponseslist.add(new Reponse("chien",false));
                reponseslist.add(new Reponse("chat",false));

                questionslist.add(new Question("facille", 0, reponseslist));
                switch (which) {
                    case 0: // Facile
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("question", (Serializable) questionslist);
                        break;
                    case 1: // Moyen
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("question", (Serializable) questionslist);
                        break;
                    case 2: // Difficile
                        intent = new Intent(MainActivity.this, LevelActivity.class);
                        intent.putExtra("question", (Serializable) questionslist);
                        break;
                    default:
                        return; // ne rien faire si problème
                }

                startActivity(intent);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}