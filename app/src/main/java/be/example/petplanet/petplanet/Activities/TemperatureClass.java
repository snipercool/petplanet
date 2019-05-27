package be.example.petplanet.petplanet.Activities;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TemperatureClass {
    private String date;
    private float temperature;

    //Default onstructor
    public TemperatureClass(){
    }

    public TemperatureClass(String date, float temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    public void setDate(String date){
        this.date = date;
    }

    //Getters

    public float getTemperature(){
        return this.temperature;
    }

    public String getDate(){
        return this.date;
    }
}
