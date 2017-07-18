package com.example.aburgess11.foodmood;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.aburgess11.foodmood.models.Config;
import com.example.aburgess11.foodmood.models.Match;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by liangelali on 7/13/17.
 */

public class MatchesListActivity extends AppCompatActivity {
    // constants
    // the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MatchesListActivity";

    // instance fields
    AsyncHttpClient client;
    // the list of currently playing movies
    ArrayList<Match> matches;
    // the recycler view
    RecyclerView rvMatches;
    // the adapter wired to the recycler view
    MatchesAdapter adapter;
    // image config
    Config config;

    // formatting the toolbar items
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    boolean appBarExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);
        // initialize the client
        client = new AsyncHttpClient();
        // init the list of matches
        matches = new ArrayList<>();
        // initialize the adapter -- movies array cannot be reinitialized after this point
        adapter = new MatchesAdapter(matches);

        // resolve the recycler view and connect a layout manager and the adapter
        rvMatches = (RecyclerView) findViewById(R.id.rvMatches);
        rvMatches.setLayoutManager(new LinearLayoutManager(this));
        rvMatches.setNestedScrollingEnabled(false);
        rvMatches.setHasFixedSize(false);
        rvMatches.getLayoutManager().setAutoMeasureEnabled(true);
        rvMatches.setAdapter(adapter);


        // toolbar
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar.setTitleEnabled(false);

        toolbar.setTitle("start");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(MatchesListActivity.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);

                //  Vertical offset == 0 indicates appBar is collapsed.

                int vertOff = Math.abs(verticalOffset);

                if (vertOff != 0) {
                    Toast.makeText(getApplicationContext(), "the thing should be expanded", Toast.LENGTH_SHORT).show();
                    collapsingToolbar.setTitle(getString(R.string.matches_header_expanded));
                }
                if (vertOff == 0) {
                    Toast.makeText(getApplicationContext(), "the thing should be collapsed", Toast.LENGTH_SHORT).show();
                    collapsingToolbar.setTitle(getString(R.string.matches_header_collapsed));
                }

                if (Math.abs(verticalOffset) == 0) {
                    appBarExpanded = true;Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
//                    collapsingToolbar.setTitle(getString(R.string.matches_header_collapsed));
                } else {
                    appBarExpanded = false;
//                    Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_SHORT).show();
//                    collapsingToolbar.setTitle(getString(R.string.matches_header_expanded));
                }
            }
        });

        // get the configuration on app creation
        getConfiguration();
    }



    // get the list of currently playing movies from the API
    private void getNowPlaying() {
        // create the url
        String url = API_BASE_URL + "/movie/now_playing";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, "0bed49764dcc012c08bb5b9dc334e476"); // API key, always required
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++) {
                       Match match = new Match(results.getJSONObject(i));
                        matches.add(match);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(matches.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s matches", results.length()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    // get the configuration free from the API
    private void getConfiguration() {
        // create the url
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, "0bed49764dcc012c08bb5b9dc334e476"); // API key, always required
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get the image base url
                try {
                    config = new Config(response);
                    Log.i(TAG,
                            String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getBackdropSize()));
                    // pass config to adapter
                    adapter.setConfig(config);
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    // handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser) {
            // show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
