package com.saurabh.newsaggregator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.saurabh.newsaggregator.bean.News;

import java.util.ArrayList;
import java.util.List;

public class NewsReport extends AppCompatActivity{

    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private List<News> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        recyclerView = findViewById(R.id.Recycler);

        Bundle bundle = getIntent().getBundleExtra("List");
        list = (ArrayList<News>)bundle.getSerializable("List");


        //adapter = new NewsAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }


}