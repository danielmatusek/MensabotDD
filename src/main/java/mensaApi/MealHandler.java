package mensaApi;
import var.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MealHandler {

    private OpenMensaConn openMensaConn;

    public static final String openMensa_canteens = "http://openmensa.org/api/v2/canteens";

    public MealHandler(){
        openMensaConn = OpenMensaConn.getInstance();
    }

    public ArrayList<String> getMeals(int mensaId, String datum) throws Exception {

        ArrayList<String> meals = new ArrayList<>();

        if (isCanteenOpened(mensaId)) {

            JSONArray todaysMeals = new JSONArray(openMensaConn.getResponseString(
                    openMensaConn.openConnection(openMensa_canteens + "/" + mensaId + "/days/te" + datum + "/meals")));

            for(int i = 0; i < todaysMeals.length(); i++){
                JSONObject meal = todaysMeals.getJSONObject(i);
                JSONObject prices = meal.getJSONObject("prices");
                if(!prices.isNull("students")) {
                    meals.add("*" + meal.getString("category") + "*: " + meal.getString("name") + ", Preis: "
                            + prices.getDouble("students") + " EUR");
                } else {
                    meals.add("*" + meal.getString("category") + "*: " + meal.getString("name")
                            + "(keine Preisangabe)");
                }
            }
            return meals;
        }
        else {
            System.out.println ("canteen closed");
            meals.add("Heute ist die Mensa geschlossen");

            return meals;
        }
    }

    public Boolean isCanteenOpened (int mensaId) throws Exception {

        HttpURLConnection con = openMensaConn.openConnection(openMensa_canteens + "/" + mensaId + "/days");
        JSONArray allDays = new JSONArray(openMensaConn.getResponseString(con));

        if(allDays.getJSONObject(0).getBoolean("closed")){
            return false;
        }
        else return true;
    }

    public String getMealsForSending(int mensaId, String mensaName, String date) throws Exception {

        ArrayList meals = getMeals(mensaId, date);
        String output = Constants.todayMenu + mensaName + ": \n\n";

        for(int i = 0; i < meals.size(); i++){
            output = output + meals.get(i) + "\n\n";
        }

        return output;
    }
}
