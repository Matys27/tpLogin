package com.matys.tplogin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity {

    private TextView userInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userInfoTextView = findViewById(R.id.userInfoTextView);

        // Récupération des données de l'intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String token = extras.getString("token");
            String mdp = extras.getString("mdp");
            String email = extras.getString("email");
            String role = extras.getString("role");
            String idRegion = extras.getString("id_region");
            String idUser = extras.getString("id_user");

            // Affichage des informations de l'utilisateur dans le TextView
            String userInfo = "Token: " + token + "\nMot de passe: " + mdp + "\nEmail: " + email
                    + "\nRole: " + role + "\nID Région: " + idRegion + "\nID Utilisateur: " + idUser;
            userInfoTextView.setText(userInfo);
        }
    }
}