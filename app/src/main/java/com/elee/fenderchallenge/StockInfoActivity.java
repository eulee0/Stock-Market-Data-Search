package com.elee.fenderchallenge;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity that shows detailed Stock Data.
 * This activity starts on ListView element click, providing details on the company's stock.
 *
 * @author Eugene Lee
 */
public class StockInfoActivity extends AppCompatActivity {

    /**
     * URL that returns response when using the Stock Quote API.
     */
    private static final String QUOTE_URL = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info_actvity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String stockSymbol = getIntent().getExtras().getString("symbol");
        String json = MainActivity.getJsonFromURL(QUOTE_URL, stockSymbol);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String stockStatus = jsonObject.getString("Status");
            String stockName = jsonObject.getString("Name");
            String stockLastPrice = Double.toString(jsonObject.getDouble("LastPrice"));
            String stockTimestamp = jsonObject.getString("Timestamp");
            String stockHigh = Double.toString(jsonObject.getDouble("High"));
            String stockLow = Double.toString(jsonObject.getDouble("Low"));
            String stockOpen = Double.toString(jsonObject.getDouble("Open"));

            if (!stockStatus.equals("SUCCESS")) {
                Toast.makeText(getBaseContext(), "API error, status: failure", Toast.LENGTH_LONG).show();
            }

            TextView symbolTextView = (TextView) findViewById(R.id.stockSymbol);
            TextView nameTextView = (TextView) findViewById(R.id.stockName);
            TextView priceTextView = (TextView) findViewById(R.id.stockPrice);
            TextView timestampTextView = (TextView) findViewById(R.id.stockTimestamp);
            TextView highTextView = (TextView) findViewById(R.id.stockHigh);
            TextView lowTextView = (TextView) findViewById(R.id.stockLow);
            TextView openTextView = (TextView) findViewById(R.id.stockOpen);

            Resources res = getResources();
            symbolTextView.setText(stockSymbol);
            nameTextView.setText(res.getString(R.string.stock_name, stockName));
            priceTextView.setText(res.getString(R.string.stock_last_price, stockLastPrice));
            timestampTextView.setText(res.getString(R.string.stock_timestamp, stockTimestamp));
            highTextView.setText(res.getString(R.string.stock_high, stockHigh));
            lowTextView.setText(res.getString(R.string.stock_low, stockLow));
            openTextView.setText(res.getString(R.string.stock_open, stockOpen));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
