package be.example.petplanet.petplanet.Activities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScanClass {
    // Array
    private List<Object> products = new ArrayList<>();
    private List<Object> resultsQr = new ArrayList<>();

    // Default constructor
    public ScanClass() {
    }

    // Constructor

    public ScanClass(List<Object> products, List<Object> resultsQr) {
        this.products = products;
        this.resultsQr = resultsQr;

        this.cleanupResultsQr();
    }

    // Getters

    public List<Object> getProducts() {
        return products;
    }

    public List<Object> getResultsQr() {
        return resultsQr;
    }

    // Setters

    public void setProducts(List<Object> products) {
        this.products = products;
    }

    public void setResultsQr(List<Object> resultsQr) {
        this.resultsQr = resultsQr;
    }

    //Methods
    //TODO: scan resultaten moeten enkel de id's van de producten weergeven
    //TODO: producten resultaten moeten vergeleken worden met id's scan
    //TODO: alle ratings moeten ergens opgeslagen worden
    //TODO: totaal van ratings moet afgetrokken worden van totaal

    private void cleanupResultsQr() {
        ArrayList<Integer> resultIds = new ArrayList<>();

        for(Object product : this.resultsQr){
            Map<String, String> p = (Map<String, String>) product;
            //String omzetten naar Integer
            resultIds.add(Integer.parseInt(p.get("id")));
            Log.i("test", p.get("id"));
        }
    }

    //Dees werkt: lees alle ids van productlijst
    private void cleanupProducts(){
        ArrayList<Integer> productIds = new ArrayList<>();

        for(Object product : this.products){
            Map<String, String> p = (Map<String, String>) product;
            //String omzetten naar Integer
            productIds.add(Integer.parseInt(p.get("id")));
        }
    }
}
