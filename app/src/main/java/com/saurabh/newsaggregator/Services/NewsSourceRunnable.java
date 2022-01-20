package com.saurabh.newsaggregator.Services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.saurabh.newsaggregator.MainActivity;
import com.saurabh.newsaggregator.bean.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsSourceRunnable implements Runnable{

    private static final String TAG = "News Service Runnable";
    MainActivity mainActivity;
    String source;

    public NewsSourceRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setSource(String source) {
        this.source = source;
    }

    private ArrayList<News> parseJSON(String s) {
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONArray newsSources = new JSONObject(s).getJSONArray("articles");

            for (int i = 0; i < newsSources.length(); i++) {
                JSONObject article = (JSONObject) newsSources.get(i);
                newsList.add(new News(article.getString("title"),
                        article.getString("author"),
                        article.getString("description"),
                        article.getString("publishedAt"),
                        article.getString("urlToImage"),
                        article.getString("url")
                ));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return newsList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResult(String result) {
        if (result.isEmpty()) {
            Log.d(TAG, "handleResults: Failure in data download");
            //mainActivity.runOnUiThread(mainActivity::apiFailedToFetch);
            return;
        }

        ArrayList<News> articles;
        articles = parseJSON(result);
        mainActivity.runOnUiThread(() -> {
            if (articles != null) mainActivity.setArticlesList(articles);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        Service service = new Service();

        String API_URL = "https://newsapi.org/v2/top-headlines?sources="+source+"&apiKey=2c48919da85d40348d0485fddb6cf339";
        System.out.println(service.getResultData(API_URL));
        handleResult(service.getResultData(API_URL));
    }
}
