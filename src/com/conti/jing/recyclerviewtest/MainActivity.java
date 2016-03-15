
package com.conti.jing.recyclerviewtest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final String URL = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";
    private AsyncHttpTask mAsyncHttpTask;
    private RecyclerView mCardListRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> mCardAdapter;
    private List<CardItem> mCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.layout_main_activity);
        initView();
    }

    private void initView() {
        mCardListRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_card_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCardListRecyclerView.setLayoutManager(mLayoutManager);
        mCardListRecyclerView.setHasFixedSize(true);
        mAsyncHttpTask = new AsyncHttpTask();
        mAsyncHttpTask.execute(URL);
        // setStaticData();
    }

    private void setStaticData() {
        mCardList = new ArrayList<CardItem>();
        CardItem[] cardItems = new CardItem[10];
        for (int i = 0; i < cardItems.length; i++) {
            cardItems[i] = new CardItem();
            cardItems[i].setThumbnail("thumbnail" + i);
            cardItems[i].setTitle("title" + i);
            mCardList.add(cardItems[i]);
        }
        mCardAdapter = new RecyclerViewAdapter(mCardList, MainActivity.this);
        mCardListRecyclerView.setAdapter(mCardAdapter);
    }

    // The three types used by an asynchronous task are the following:
    // 1. Params, the type of the parameters sent to the task upon execution.
    // 2. Progress, the type of the progress units published during the background computation.
    // 3. Result, the type of the result of the background computation.
    private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection httpURLConnection = null;
            try {
                // forming the java.net.URL object
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // for Get request
                httpURLConnection.setRequestMethod("GET");
                int statusCode = httpURLConnection.getResponseCode();
                if (statusCode == 200) { // 200 represents HTTP OK
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    parseResponse(response.toString());
                    result = 1; // Successful to fetch data
                } else {
                    result = 0; // Failed to fetch data
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setProgressBarIndeterminateVisibility(false);
            if (result == 1) {
                mCardAdapter = new RecyclerViewAdapter(mCardList, MainActivity.this);
                mCardListRecyclerView.setAdapter(mCardAdapter);
            }
        }

    }

    private void parseResponse(String response) {
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            JSONArray posts = responseJsonObject.optJSONArray("posts");
            if (mCardList == null) {
                mCardList = new ArrayList<CardItem>();
            }
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                CardItem cardItem = new CardItem();
                cardItem.setThumbnail(post.optString("thumbnail"));
                cardItem.setTitle(post.optString("title"));
                mCardList.add(cardItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
