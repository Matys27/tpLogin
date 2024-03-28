package com.matys.tplogin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Récupérer l'email depuis l'Intent
        String email = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView userInfoTextView = (TextView) findViewById(R.id.userInfoTextView);
        userInfoTextView.setText(email);


        // Appeler la méthode fetchUserInfo pour obtenir les informations de l'utilisateur
        fetchUserInfo(email);
    }

    private void fetchUserInfo(final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://gsbcr.alwaysdata.net/Api/loginAPI.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Construire la requête JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", email);

                    // Écrire la requête JSON dans le flux de sortie
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(jsonParam.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // Obtenir la réponse du serveur
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    conn.disconnect();

                    // Analyser la réponse JSON
                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                    // Vérifier le statut de la réponse
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        // Extraire les informations de l'utilisateur
                        JSONObject userObject = jsonObject.getJSONObject("user");

                        final int idUser = userObject.getInt("id_user");
                        final String nom = userObject.getString("nom");
                        final String prenom = userObject.getString("prenom");
                        final String userEmail = userObject.getString("email");
                        final String role = userObject.getString("role");
                        final int idRegion = userObject.getInt("id_region");

                        // Afficher les informations de l'utilisateur dans l'interface utilisateur
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                TextView userInfoTextView = findViewById(R.id.userInfoTextView);
                                userInfoTextView.setText("User Info:\nID_USER: " + idUser + "\nNom: " + nom + "\nPrénom: " + prenom + "\nEmail: " + userEmail + "\nRole: " + role + "\nID_REGION: " + idRegion);
                            }
                        });
                    } else {
                        // Afficher un message d'erreur si la requête échoue
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Erreur: Impossible de récupérer les informations de l'utilisateur", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
