package com.matys.tplogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    // Déclaration de l'URL de l'API
    private static final String API_URL = "https://gsbcr.alwaysdata.net/Api/loginAPI.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Appel de l'API pour l'authentification
                authenticateUser(email, password);
            }
        });
    }

    private void authenticateUser(String email, String password) {
        // Création des données JSON pour l'authentification
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
            requestData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Création de la file d'attente de requêtes Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Création de la requête JSON POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, API_URL, requestData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Vérification de la réponse de l'API
                            boolean success = response.getBoolean("success");

                            if (success) {
                                // Connexion réussie, passer à l'activité suivante avec les données de l'utilisateur
                                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                                intent.putExtra("token", response.getString("token"));
                                intent.putExtra("mdp", response.getString("mot_de_passe"));
                                intent.putExtra("email", response.getString("email"));
                                intent.putExtra("role", response.getString("role"));
                                intent.putExtra("id_region", response.getString("id_region"));
                                intent.putExtra("id_user", response.getString("id_user"));
                                startActivity(intent);
                            } else {
                                // Afficher un message d'erreur en cas d'échec de connexion
                                Toast.makeText(MainActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Erreur de réponse de l'API", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestion des erreurs
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Erreur de connexion à l'API", Toast.LENGTH_SHORT).show();
                    }
                });

        // Ajout de la requête à la file d'attente
        queue.add(jsonObjectRequest);
    }
}