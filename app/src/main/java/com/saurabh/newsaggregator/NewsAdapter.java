package com.saurabh.newsaggregator;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.saurabh.newsaggregator.Services.ImageLoderService;
import com.saurabh.newsaggregator.bean.News;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    MainActivity newsReport;
    ArrayList<News> list;

    public NewsAdapter(MainActivity newsReport) {
        this.newsReport = newsReport;

    }

    public void setList(ArrayList<News> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_news_report, parent, false);
        view.setOnClickListener(newsReport);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        News news = list.get(position);
        System.out.println(news.getDate());

        try{

            Date d = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(news.getDate());
            String s2 = (new SimpleDateFormat("MMM dd,yyyy hh:mm")).format(d);
            System.out.println("s2 "+s2);
            holder.Date.setText(s2);
        } catch(Exception e) {
            e.printStackTrace();
        }


        if (!news.getImage().equals("null")) {

            new ImageLoderService(holder.imageView, newsReport).execute(news.getImage());
        }

        if (news.getAuthor().equals("null")){
            news.setAuthor("");
        }
        if (news.getDescription().equals("null")){
            news.setDescription("");
        }
        Log.d("NewsAdapter","Adapter"+news.getDescription());
        holder.Headline.setText(news.getHeadline());
        holder.Author.setText(news.getAuthor());
        holder.Description.setText(news.getDescription());
        holder.Count.setText((position + 1) + " of " + list.size());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Headline, Date, Author, Description, Count;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Headline = itemView.findViewById(R.id.Headline);
            Date = itemView.findViewById(R.id.Date);
            Author = itemView.findViewById(R.id.Auther);
            imageView = itemView.findViewById(R.id.imageView);
            Description = itemView.findViewById(R.id.Article);
            Count = itemView.findViewById(R.id.Count);


        }
    }
}
