package be.example.petplanet.petplanet.Activities;

public class TemperatureClass {
    private String day;
    private float temperatureInside;
    private float temperatureOutside;

    //Default onstructor
    public TemperatureClass(){
    }

    public TemperatureClass(String day, float temperatureInside, float temperatureOutside) {
        this.day = day;
        this.temperatureInside = temperatureInside;
        this.temperatureOutside = temperatureOutside;
    }

    //Setters

    public void setDay(String day){
        this.day = day;
    }

    public void setTemperatureInside(float temperatureInside) {
        this.temperatureInside = temperatureInside;
    }

    public void setTemperatureOutside(float temperatureOutside) {
        this.temperatureOutside = temperatureOutside;
    }

    //Getters

    public String getDay(){
        return this.day;
    }

    public float getTemperatureInside(){
        return this.temperatureInside;
    }

    public float getTemperatureOutside() {
        return this.temperatureOutside;
    }
}
