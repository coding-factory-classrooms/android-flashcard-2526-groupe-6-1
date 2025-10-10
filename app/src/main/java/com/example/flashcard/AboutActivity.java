package com.example.flashcard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionText = findViewById(R.id.appVersion);

        try {
            // Récupèration dynamique  de la version du ficheier gradle
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            versionText.setText("Version " + version);

        } catch (PackageManager.NameNotFoundException e) {
            versionText.setText("Version inconnue");
        }
    }
}