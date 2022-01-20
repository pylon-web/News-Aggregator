package com.saurabh.newsaggregator;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.saurabh.newsaggregator.bean.NewsChannelList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class NewsServicesRunnable implements Runnable{

    private String news = "https://newsapi.org/v2/top-headlines/sources?apiKey=2c48919da85d40348d0485fddb6cf339";
    public static String TAG ="Runnable";
    private MainActivity mainActivity;

    public NewsServicesRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {

        Uri uri = Uri.parse(news);
        String urlToUse = uri.toString();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            Log.d(TAG, "news data: " + stringBuilder.toString());


        }catch (Exception e){
            Log.e(TAG, "run: ", e);
            handleResults(null);
            return;
        }
        handleResults(stringBuilder.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleResults(String s) {
        if (s == null){
            Log.d(TAG, "handleResults: Failure in data download");
//            mainActivity.runOnUiThread(mainActivity::downloadFailed);
            return;
        }
        final NewsChannelList[] newsChannelList = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (newsChannelList != null) mainActivity.getNewsChannelList(newsChannelList);
        });
    }

    private NewsChannelList[] parseJSON(String s) {
        try {

            JSONObject jMain = new JSONObject(s);
            JSONArray jSource = jMain.getJSONArray("sources");
            int sourcesLength = jSource.length();
            NewsChannelList[] sources = new NewsChannelList[sourcesLength];

            for (int i = 0; i < sourcesLength; i++) {
                JSONObject source = (JSONObject) jSource.get(i);
                sources[i] = new NewsChannelList(source.getString("id"),
                        source.getString("name"),
                        source.getString("category"),
                        source.getString("country"),
                        source.getString("language"));
            }
            Log.d("Sources","Error"+sources[0]);


            return sources;


        }
        catch (Exception e){

            Log.d("Runnable","Error"+e);
        }
       return null;
    }
}
