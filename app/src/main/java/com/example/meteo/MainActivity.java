package com.example.meteo;

import static com.example.meteo.ApiClient.API_KEY;
import java.util.List;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.meteo.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTemperature;
    private TextView textViewHumidity;
    private TextView textViewPressure;
    private TextView textViewRessenti;
    private TextView textViewSpeed;
    private TextView textViewDeg;
    private TextView textViewVille;
    private TextView textViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewPressure = findViewById(R.id.textViewPressure);
        textViewRessenti = findViewById(R.id.textViewRessenti);
        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewDeg = findViewById(R.id.textViewDeg);
        textViewVille = findViewById(R.id.textViewVille);
        textViewWeather = findViewById(R.id.textViewWeather);

        afficherMeteo("Paris", "metric");
        // Récupération de la SearchView depuis le layout
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    // Si la recherche est vide, affiche la météo de Paris par défaut
                    query = "Paris";
                }
                afficherMeteo(query, "metric");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Gestion du bouton Switch pour changer les unités de température
        Switch unitSwitch = findViewById(R.id.unitSwitch);
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String units = isChecked ? "imperial" : "metric";
                String ville = textViewVille.getText().toString();
                afficherMeteo(ville, units);
            }
        });
    }

    // Méthode pour obtenir et afficher les informations météorologiques en fonction de la ville
    private void afficherMeteo(String ville, String units) {
        ApiService apiService = ApiClient.createService();
        Call<WeatherResponse> call = apiService.getCurrentWeather(ville, "d0060546f0e76bb2f532096b8a62e825", units);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    // Récupération des données météorologiques
                    double temperature = weatherResponse.getMain().getTemp();
                    int humidite = weatherResponse.getMain().getHumidity();
                    double temperature_min = weatherResponse.getMain().getTemp_min();
                    double temperature_max = weatherResponse.getMain().getTemp_max();
                    int pressure = weatherResponse.getMain().getPressure();
                    double ressenti = weatherResponse.getMain().getFeels_like();
                    double speed = weatherResponse.getWind().getSpeed();
                    int deg = weatherResponse.getWind().getDeg();
                    String nomVille = weatherResponse.getName();

                    int roundedTemperature = (int) Math.round(temperature);
                    int roundedTemperature_min = (int) Math.round(temperature_min);
                    int roundedTemperature_max = (int) Math.round(temperature_max);
                    int roundedRessenti = (int) Math.round(ressenti);

                    textViewTemperature.setText(String.valueOf(roundedTemperature) + (units.equals("metric") ? "°C" : "°F"));
                    textViewHumidity.setText(String.valueOf(humidite) + "%");
                    textViewPressure.setText(String.valueOf(pressure) + " hPa");
                    textViewRessenti.setText(String.valueOf(roundedRessenti) + (units.equals("metric") ? "°C" : "°F"));
                    textViewSpeed.setText(String.valueOf(speed) + (units.equals("metric") ? "m/sec" : "miles/h"));
                    textViewDeg.setText(String.valueOf(deg) + "°N");
                    textViewVille.setText(nomVille);

                    List<Weather> weatherList = weatherResponse.getWeather();
                    if (weatherList != null && !weatherList.isEmpty()) {
                        String description = weatherList.get(0).getDescription();
                        String temperatureMinMax = String.valueOf(roundedTemperature_max) + "° / " + String.valueOf(roundedTemperature_min) + "°";

                        textViewWeather.setText(description + " " + temperatureMinMax);
                    }
                } else {
                    // Gestion des erreurs si la réponse n'est pas réussie
                    // ...
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Gestion de l'échec de la requête
                // ...
            }
        });
    }
}