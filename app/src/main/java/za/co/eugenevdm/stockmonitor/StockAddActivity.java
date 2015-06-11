package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import za.co.eugenevdm.stockmonitor.engine.Utils;

/*
 * StockAddActivity allows to add a new stock item or to change an existing stock
 */
public class StockAddActivity extends Activity {
    private static final String TAG = "sm_StockAddActivity";
    private Spinner mCategorySpinner;
    private EditText mTickerText;
    private EditText mDescriptionText;
    private EditText mLastPrice;
    private EditText mChangePrice;
    private EditText mChangePricePercentage;

    private Uri stockUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.stock_edit);

        mCategorySpinner = (Spinner) findViewById(R.id.category);
        mTickerText = (EditText) findViewById(R.id.stock_edit_ticker);
        mDescriptionText = (EditText) findViewById(R.id.stock_edit_description);
        mLastPrice = (EditText) findViewById(R.id.stock_edit_last_price);
        mChangePrice = (EditText) findViewById(R.id.stock_edit_change_price);
        mChangePricePercentage = (EditText) findViewById(R.id.stock_edit_change_price_percentage);

        Button confirmButton = (Button) findViewById(R.id.stock_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        stockUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(StockContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            stockUri = extras
                    .getParcelable(StockContentProvider.CONTENT_ITEM_TYPE);

            fillData(stockUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTickerText.getText().toString())) {
                    makeToast();
                } else {
                    // Get essential stock data online
                    getStockInfo(mTickerText.getText().toString());
                    //setResult(RESULT_OK);
                    //finish();
                }
            }

        });
    }

    void getStockInfo(String symbol) {
        String tag_string_req = "string_req";

        String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=" + symbol;

        final Gson gson = new Gson();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Retrieving stock information...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                final String respond = Utils.GoogleRespondToJSON(response);
                TypeToken<List<Stock>> token = new TypeToken<List<Stock>>() {
                };
                List<Stock> serverStockList = gson.fromJson(respond, token.getType());
                pDialog.hide();
                for (Stock serverStock : serverStockList) {
                    String ex = serverStock.getExchange();
                    String p = serverStock.getPrice();
                    if (ex.equals("JSE")) {
                        p = p.replace(",", "");
                        float f = Float.parseFloat(p);
                        f = f / 100;
                        p = Float.toString(f);
                        serverStock.setPrice(p);
                    }
                    Stock s = new Stock();
                    s.setGoogleId(serverStock.getGoogleId());
                    s.setName(serverStock.getName());
                    s.setExchange(serverStock.getExchange());
                    s.setTicker(serverStock.getTicker());
                    s.setPrice(s.getCurrencySymbol() + serverStock.getPrice());
                    s.setPe(serverStock.getPe());
                    s.setMarketCap(serverStock.getMarketCap());
                    s.setCp(serverStock.getCp());

                    mLastPrice.setText(s.getPrice());
                    mChangePrice.setText(s.getCp().toString());
                    mChangePricePercentage.setText(s.getCp().toString());

                }

                //adapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        // Add request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fillData(Uri uri) {
        String[] projection = {StockDbTable.COLUMN_TICKER,
                StockDbTable.COLUMN_DESCRIPTION, StockDbTable.COLUMN_CATEGORY};
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(StockDbTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategorySpinner.getCount(); i++) {

                String s = (String) mCategorySpinner.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategorySpinner.setSelection(i);
                }
            }

            mTickerText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(StockDbTable.COLUMN_TICKER)));
            mDescriptionText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(StockDbTable.COLUMN_DESCRIPTION)));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(StockContentProvider.CONTENT_ITEM_TYPE, stockUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) mCategorySpinner.getSelectedItem();
        String ticker = mTickerText.getText().toString();
        String description = mDescriptionText.getText().toString();

        // only save if either summary or description
        // is available

        if (description.length() == 0 && ticker.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(StockDbTable.COLUMN_CATEGORY, category);
        values.put(StockDbTable.COLUMN_TICKER, ticker);
        values.put(StockDbTable.COLUMN_DESCRIPTION, description);

        if (stockUri == null) {
            // New stock
            stockUri = getContentResolver().insert(StockContentProvider.CONTENT_URI, values);
        } else {
            // Update stock
            getContentResolver().update(stockUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(StockAddActivity.this, "Please add a ticker",
                Toast.LENGTH_LONG).show();
    }
} 