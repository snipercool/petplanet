package be.example.petplanet.petplanet.Activities;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TemperatureClass {
    private float temperature;
    private String date;

    // Array
    private List<Integer> temperatures = new ArrayList<>();

    //Default onstructor
    public TemperatureClass(){
    }

    public List<Integer> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Integer> temperatures) {
        this.temperatures = temperatures;
    }

    //Setters

    public void setTemperature(float temperature){
        this.temperature = temperature;
    }

    public void setDate(String date){
        this.date = date;
    }

    //Getters

    public float getTemperature(){
        return temperature;
    }

    public String getDate(){
        return date;
    }

    public void addTemperatureToArray(String temperature){
        float stringToFloar = Float.parseFloat(temperature);
        int round = Math.round(stringToFloar);
        temperatures.add(round);
    }
}
