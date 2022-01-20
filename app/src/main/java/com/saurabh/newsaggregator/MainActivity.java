package com.saurabh.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.saurabh.newsaggregator.Services.NewsSourceRunnable;
import com.saurabh.newsaggregator.Services.Service;
import com.saurabh.newsaggregator.Services.ServiceSource;
import com.saurabh.newsaggregator.bean.News;
import com.saurabh.newsaggregator.bean.NewsChannelList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private TextView textView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] items;
    private ArrayAdapter<String> arrayAdapter;
    private List<News> list= new ArrayList<>();
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private NewsChannelList[] newsChannelLists;
    private NewsChannelList[] newsChannelListsFilter;
    private NewsSourceRunnable newsService;
    private Menu topicslist, countrieslist, languagelist, optMenu;
    private String[] sourcesList;
    private String type;
    ArrayList<String> mainMenus = new ArrayList<>();
    Map<String, String> countriess;
    Map<String, String> languages;
    Map<Integer, String> newsList = new HashMap<>();
    public Service service;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.Recyclers);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);


        mainMenus.add("Topics");
        mainMenus.add("Countries");
        mainMenus.add("Languages");

        newsList.put(1,"all");
        newsList.put(2,"all");
        newsList.put(3,"all");

        countriess = loadJsonDataList(R.raw.country_codes, "countries");
        languages = loadJsonDataList(R.raw.language_codes, "languages");

        ServiceSource serviceSource = new ServiceSource(this, countriess,languages);
        new Thread(serviceSource).start();

        service = new Service();
        service.setColorList(this);


//        NewsServicesRunnable ServicesRunnable = new NewsServicesRunnable(this);
//        new Thread(ServicesRunnable).start();
        newsService = new NewsSourceRunnable(this);

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " );
            return true;
        }
        Log.d(TAG, "option selected is : " + item.getTitle().toString() + " : " + item.getGroupId());

        updateList(item.getTitle().toString(),item.getGroupId());
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Log.d("Main","Channel"+newsChannelListsFilter[position].getName());
        newsService.setSource(newsChannelListsFilter[position].getId());
        setTitle(newsChannelListsFilter[position].getName());
        new Thread(newsService).start();
        findViewById(R.id.content_frame).setBackgroundColor(Color.parseColor("#ffffff"));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public Map<String, String> loadJsonDataList(int resource, String k) {
        Log.d(TAG, "loadFile: Loading JSON File");
        Map<String, String> data = new HashMap<>();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(resource), StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONObject(sb.toString()).getJSONArray(k);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                data.put(jsonObject.getString("code"), jsonObject.getString("name"));
            }

            getResources().openRawResource(resource).close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File Not Found: JSON File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateMenuList(NewsChannelList[] newsSources) {

        Arrays.stream(newsSources).map(NewsChannelList::getCatagory).distinct().forEach(topic -> {
            SpannableString s = new SpannableString(topic);
            s.setSpan(new ForegroundColorSpan(service.getColorList(topic)), 0, s.length(), 0);
            topicslist.add(1, 0, 0, s);

        });
        Arrays.stream(newsSources).map(NewsChannelList::getCountry).distinct().forEach(country->{
           countrieslist.add(2, 0, 0,country);
        });
        Arrays.stream(newsSources).map(NewsChannelList::getLanguage).distinct().forEach(lang->{
            languagelist.add(3, 0, 0, lang);
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNewsSourceList(NewsChannelList[] newsChannel) {
        this.newsChannelLists = newsChannel;
        this.newsChannelListsFilter = newsChannel;

        sourcesList = new String[newsChannel.length];

        this.setTitle("News Aggregator " + "(" +sourcesList.length+")");
        for (int i = 0; i < sourcesList.length; i++)
            sourcesList[i] = newsChannel[i].getName();

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, sourcesList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView)v.findViewById(android.R.id.text1))
                        .setTextColor(service.getColorList(newsChannel[position].getCatagory()));
                return v;
            }
        };
        mDrawerList.setAdapter(arrayAdapter);
        updateMenuList(newsChannel);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateList(String item, int id) {

        if (id == 0) return;

        String previous = newsList.get(id);
        newsList.put(id, item);

        NewsChannelList[] filtered = Arrays.stream(newsChannelLists)
                .filter(source -> (source.getCatagory().equals(newsList.get(1)) || newsList.get(1).equals("all"))
                        && (source.getCountry().equals(newsList.get(2)) || newsList.get(2).equals("all"))
                        && (source.getLanguage().equals(newsList.get(3)) || newsList.get(3).equals("all")))
        .toArray(NewsChannelList[]::new);

        String []newsSources = Arrays.stream(filtered).map(NewsChannelList::getName).toArray(String[]::new);

        if (newsSources.length == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Sources exist")
                    .setMessage(" No sources exist that\n" +
                            "match the specified Topic, Language and/or\n" +
                            "Country")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newsList.put(id, previous);
                        }
                    }).show();
        } else {
            sourcesList = newsSources;
            NewsChannelList[] file = filtered;
            this.newsChannelListsFilter = filtered;

            arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, sourcesList)  {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView)v.findViewById(android.R.id.text1))
                            .setTextColor(service.getColorList(filtered[position].getCatagory()));
                    return v;
                }
            };

            mDrawerList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            setTitle("News Aggregator " + "(" +sourcesList.length+")");
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        topicslist = menu.addSubMenu("Topics");
        topicslist.add(1, 0, 0, "all");
        countrieslist = menu.addSubMenu("Countries");
        countrieslist.add(2, 0, 0, "all");
        languagelist = menu.addSubMenu("Language");
        languagelist.add(3, 0, 0, "all");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getNewsChannelList(NewsChannelList[] newsChannelList) {
        this.newsChannelLists = newsChannelList;
        this.newsChannelListsFilter = newsChannelList;

        setTitle("News Gateway ("+newsChannelList.length+")");
        items = new String[newsChannelList.length];
        for (int i = 0; i < items.length; i++)
            items[i] = newsChannelList[i].getName();

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, items));
        updateMenuList(newsChannelList);
    }


    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        System.out.println(list.get(position).getUrl());

        String url = list.get(position).getUrl();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }

    public void setArticlesList(ArrayList<News> articles) {
        this.list = articles;

        adapter = new NewsAdapter(this);
        adapter.setList(articles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}