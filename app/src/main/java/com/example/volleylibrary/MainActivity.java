package com.example.volleylibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView country, tempNum, wCondition;
    Button button;
    EditText inputText;
    ImageView weatherImage;

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
        inputText = findViewById(R.id.cityInput);
        country = findViewById(R.id.countryName);
        tempNum = findViewById(R.id.temp);
        weatherImage = findViewById(R.id.imageView);
        wCondition = findViewById(R.id.condition);

        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = inputText.getText().toString().trim();

                fetchData(city);
            }
        });
    }
    private void fetchData(String city) {
        String API_KEY = BuildConfig.WEATHER_API;
        String url = "https://api.weatherapi.com/v1/current.json?key=" + API_KEY + "&q="+city + "&aqi=no";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject location = response.getJSONObject("location");
                            JSONObject current = response.getJSONObject("current");
                            JSONObject condition = current.getJSONObject("condition");

                            String countryFetch = location.getString("country");
                            String temp = current.getString("temp_c");
                            String condition_text = condition.getString("text");
                            String imageUrl = condition.getString("icon");

                            country.setText(countryFetch);
                            tempNum.setText(temp + "°");
                            wCondition.setText(condition_text);

                            Picasso.get().load("https:" + imageUrl).resize(64, 64).centerCrop().into(weatherImage);

                        } catch (JSONException e) {
                            country.setText("Something went wrong here");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                country.setText("Something went wrong");

            }
        });

        requestQueue.add(jsonRequest);

    }

}