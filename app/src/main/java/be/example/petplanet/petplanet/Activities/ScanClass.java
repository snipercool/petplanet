package be.example.petplanet.petplanet.Activities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScanClass {
    // Array - Object

    private List<Object> products = new ArrayList<>();
    private List<Object> resultsQr = new ArrayList<>();

    // Default constructor
    public ScanClass() {
    }

    // Constructor
    public ScanClass(List<Object> products, List<Object> resultsQr) {
        this.products = products;
        this.resultsQr = resultsQr;
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

    public Integer initiate(){
        ArrayList<Integer> productIds = this.getIdsOfResultsOfProducts();
        ArrayList<Integer> qrIds = this.getIdsOfResultsOfQr();

        ArrayList<Integer> comparison = this.compareQrWithProducts(productIds, qrIds);
        ArrayList<Integer> ratings = getRatingsOfProducts(comparison);
        Integer score = getTotalScoreOfRatings(ratings);

        return score;
    }

    //Methods
    //TODO: totaal van ratings moet afgetrokken worden van totaal

    private ArrayList<Integer> getIdsOfResultsOfQr() {
        ArrayList<Integer> resultIds = new ArrayList<>();
        String strResults = resultsQr.toString();

        try{
            JSONArray arrAllResults = new JSONArray(strResults);
            for(int i = 0; i < arrAllResults.length(); i++){
                JSONArray arrResults = arrAllResults.optJSONArray(i);
                for(int j = 0; j < arrResults.length(); j++){
                    JSONObject objResult = arrResults.getJSONObject(j);
                    resultIds.add(objResult.getInt("id"));
                }
            }
        }
        catch(JSONException err){
            Log.d("Error", err.toString());
        }

        return resultIds;
    }

    /*
    * Get only the ids of all products
    * */

    private ArrayList<Integer> getIdsOfResultsOfProducts(){
        ArrayList<Integer> productIds = new ArrayList<>();

        for(Object product : this.products){
            Map<String, String> p = (Map<String, String>) product;
            //String omzetten naar Integer
            try{
                String value = String.valueOf(p.get("id"));
                productIds.add(Integer.parseInt(value));
            }catch(NumberFormatException nfe){
                nfe.printStackTrace();
            }
        }

        return productIds;
    }

    /*
    * Extra check to make sure that all integers match.
    * */

    private ArrayList<Integer> compareQrWithProducts(ArrayList<Integer> productsIds, ArrayList<Integer> qrIds){
        ArrayList<Integer> foundProductIds = new ArrayList<>();

        for(int i = 0; i < qrIds.size(); i++){
            if(qrIds.isEmpty() || productsIds.isEmpty()){
                //show error
            }
            //Als gelijk voeg toe rating toe
            else if(productsIds.contains(qrIds.get(i))){
                foundProductIds.add(qrIds.get(i));
            }
        }

        return foundProductIds;
    }

    private ArrayList<Integer> getRatingsOfProducts(ArrayList<Integer> comparisonIds) {

        //All ratings of product

        ArrayList<Integer> productRatings = new ArrayList<>();

        for (Object product : this.products) {
            Map<String, String> p = (Map<String, String>) product;

            //String omzetten naar Integer
            try {
                String value = String.valueOf(p.get("rating"));
                productRatings.add(Integer.parseInt(value));
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        //All found ratings
        ArrayList<Integer> foundRatings = new ArrayList<>();

        for (int i = 0; i < productRatings.size(); i++) {
            for (int j = 0; j < comparisonIds.size(); j++) {
                int value = comparisonIds.get(j);
                if (i == value) {
                    foundRatings.add(productRatings.get(i));
                }
            }
        }

        return foundRatings;
    }

    public Integer getTotalScoreOfRatings(ArrayList<Integer> ratings){
        Integer score = 0;

        for(int i = 0; i < ratings.size(); i++){
            score += ratings.get(i);
        }

        /*
        * Division of 8 due to a too strong qr code.
        * */

        score /= 8;
        Log.i("score", String.valueOf(score));
        return score;
    }
}
