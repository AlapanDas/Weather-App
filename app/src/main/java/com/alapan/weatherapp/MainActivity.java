package com.alapan.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView weather;
    Button button;
    private String url="http://api.openweathermap.org/data/2.5/weather";
    private String appid="3e296e84c837be3621f0c16f176ec3a0";
    DecimalFormat df= new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=findViewById(R.id.place);
        weather=findViewById(R.id.weather);
        button=findViewById(R.id.button);
    }

    public void getweather(View view) {
        String  tempurl="";
        String location=city.getText().toString().trim();
        if(location.equals("")) weather.setText("Location cannot be empty!");
        else
        {
            tempurl=url+"?q="+location+"&appid="+appid;
            StringRequest strreq= new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output="";
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("weather");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String desc=jsonObject1.getString("description");
                        JSONObject jsonObject2=jsonObject.getJSONObject("main");
                        double temp=jsonObject2.getDouble("temp") -273.15;
                        double temp_max=jsonObject2.getDouble("temp_max") -273.15;
                        double temp_min=jsonObject2.getDouble("temp_min") -273.15;
                        float pressure=jsonObject2.getInt("pressure");
                        int humidity=jsonObject2.getInt("humidity");
                        weather.setTextColor(Color.rgb(82, 93, 110));
                        output+=" Current Weather of "+location
                                +"\n Description: "+desc.toUpperCase()
                                +"\n Temp: "+df.format(temp)+"°C"
                                +"\n Minimum Temp: "+df.format(temp_min)+"°C"
                                +"\n Maximum Temp: "+df.format(temp_max)+"°C"
                                +"\n Pressure: "+df.format(pressure)+" hPa"
                                +"\n Humidity: "+df.format(humidity)+"%";
                        weather.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue reque= Volley.newRequestQueue(getApplicationContext());
            reque.add(strreq);
        }
    }
}