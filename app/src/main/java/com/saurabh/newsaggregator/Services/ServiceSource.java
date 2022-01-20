package com.saurabh.newsaggregator.Services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.saurabh.newsaggregator.MainActivity;
import com.saurabh.newsaggregator.bean.NewsChannelList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class ServiceSource extends Service implements Runnable {

    MainActivity  mainActivity;
    Map<String, String> countries;
    Map<String, String> languages;

    public ServiceSource(MainActivity mainActivity, Map<String, String> countries, Map<String, String> languages) {
        this.mainActivity = mainActivity;
        this.countries = countries;
        this.languages = languages;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResult(String result) {
        if (result.isEmpty()) {
            Log.d("Source Runnable", "handleResults: Failure in data download");
            //mainActivity.runOnUiThread(mainActivity::apiFailedToFetch);
            return;
        }

        NewsChannelList[] newsSource = parseJSON(result);

        mainActivity.runOnUiThread(() -> {
            if (newsSource != null) mainActivity.setNewsSourceList(newsSource);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NewsChannelList[] parseJSON(String s) {
        try {

            JSONArray jSources = new JSONObject(s).getJSONArray("sources");

            NewsChannelList[] newsSources = new NewsChannelList[jSources.length()];

            for (int i = 0; i < jSources.length(); i++) {
                JSONObject sourcenews = (JSONObject) jSources.get(i);
                newsSources[i] = new NewsChannelList(sourcenews.getString("id"),
                        sourcenews.getString("name"),
                        sourcenews.getString("category"),
                        countries.get(sourcenews.getString("country").toUpperCase()),
                        languages.get(sourcenews.getString("language").toUpperCase())
                );
            }

            return newsSources;
        } catch (Exception e) {
            Log.d("Source Runnable", "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return new NewsChannelList[]{};
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        handleResult(getResultData("https://newsapi.org/v2/top-headlines/sources?apiKey=2c48919da85d40348d0485fddb6cf339"));
    }
}