package com.elee.fenderchallenge;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.elee.fenderchallenge.ListView.StockAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity launched at application start.
 *
 * @author Eugene Lee
 */
public class MainActivity extends AppCompatActivity {

    /**
     * URL that returns response when using the Company Lookup API.
     */
    private static final String LOOKUP_URL = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=";

    List<String> stockSymbol;
    List<String> stockName;
    List<String> stockExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getBaseContext(), "Touch a search result for more information", Toast.LENGTH_LONG).show();
        final ListView listView = (ListView) findViewById(R.id.stock_item_listview);
        final EditText editText = (EditText) findViewById(R.id.search_bar);
        final Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editText.getText().toString();
                if (isAcceptable(userInput)) {
                    stockSymbol = getAttributesFromJson(getJsonFromURL(LOOKUP_URL, userInput), "Symbol");
                    stockName = getAttributesFromJson(getJsonFromURL(LOOKUP_URL, userInput), "Name");
                    stockExchange = getAttributesFromJson(getJsonFromURL(LOOKUP_URL, userInput), "Exchange");
                    listView.setAdapter(new StockAdapter(getBaseContext(), stockSymbol, stockName, stockExchange));
                } else {
                    Toast.makeText(getBaseContext(), "Must contain some alphanumeric value", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String symbol = stockSymbol.get(position);
                Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
                intent.putExtra("symbol", symbol);
                startActivity(intent);
            }
        });
    }

    /**
     * This method determines whether the user input is not just whitespace.
     *
     * @param input User input provided in the EditText.
     * @return True/False.
     */
    protected boolean isAcceptable(String input) {
        return !(input.trim().isEmpty());
    }

    /**
     * This method retrieves the API result using an asynchronous task by concatenating the URL and the input.
     *
     * @param modaURL The API URL, whether or not it is a Lookup or a Quote
     * @param input   User input provided in the EditText
     * @return The retrieved JSON as a String.
     */
    protected static String getJsonFromURL(String modaURL, String input) {
        String newURL = modaURL + input;
        String json = "";
        PullDataTask task = new PullDataTask();
        try {
            json = task.execute(newURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * This method pulls a specific JSON attribute from the JSON and makes a simple list of them.
     *
     * @param json      JSON format as a String
     * @param attribute Common attribute name from every JSON object
     * @return List of all values belonging to the attribute key.
     */
    protected List<String> getAttributesFromJson(String json, String attribute) {
        List<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i).get(attribute).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            Toast.makeText(getBaseContext(), "No search results!", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    /**
     * Class providing an asynchronous task for retrieving data from a URL.
     */
    private static class PullDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String json = "";
            try {
                URL url = new URL(params[0]);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    json += line;
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }
}