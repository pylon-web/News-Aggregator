package com.saurabh.newsaggregator.Services;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.saurabh.newsaggregator.MainActivity;
import com.saurabh.newsaggregator.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Service {


    Map<String, Integer> color;


    public String getResultData(String s){

        String urlToUse = Uri.parse(s).toString();
        StringBuilder stringBuilder = new StringBuilder();
        try {

            HttpsURLConnection conn = (HttpsURLConnection) new URL(urlToUse).openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d("Services", "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                return null;
            }
            BufferedReader breader = new BufferedReader((new InputStreamReader(conn.getInputStream())));

            String linelist;
            while ((linelist = breader.readLine()) != null) {
                stringBuilder.append(linelist).append('\n');
            }

            Log.d("Services", "news data: " + stringBuilder.toString());


        }catch (Exception e){
            Log.e("Services", "run: ", e);

            return null;
        }
        return stringBuilder.toString();
    }

    public void setColorList(MainActivity mainActivity) {
        color = new HashMap<>();
        Resources r = mainActivity.getResources();

        color.put("technology", r.getColor(R.color.technology));//Technology
        color.put("general", r.getColor(R.color.general));//general
        color.put("sports", r.getColor(R.color.sports));//sports
        color.put("science", r.getColor(R.color.science));//science
        color.put("business", r.getColor(R.color.business));//business
        color.put("entertainment", r.getColor(R.color.entertainment));//entertainment
        color.put("health", r.getColor(R.color.health));//health

    }

    public int getColorList(String data) {
        return color.get(data);//getData
    }

}
