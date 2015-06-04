package za.co.eugenevdm.stockmonitor;

import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.eugenevdm.stockmonitor.engine.Utils;

/**
 * Helper class for stock
 *
 *
 */
public class StockObject {

    private static final String TAG = "sm_StockObject";
    /**
     * An array of stock items.
     */
    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, StockItem> ITEM_MAP = new HashMap<String, StockItem>();

    static {

        getAllStocks(new VolleyCallback() {
            @Override
            public void onSuccess(List<Stock> result) {
                addItems(result);
            }
        });

        // Add 3 sample items.
        //addItem(new StockItem("1", "Brait SE", "JSE:BAT" , "1"));
        //addItem(new StockItem("2", "SABMiller plc (S. Africa)", "JSE:SAB", "2"));
        //addItem(new StockItem("3", "Steinhoff International Holdings Limited", "JSE:SHF", "3"));
    }



    public static void getAllStocks(final VolleyCallback callback) {

        String tag_string_req = "string_req";
        String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=JSE%3ABAT,JSE%3ASAB";
        final Gson gson = new Gson();

        //final ProgressDialog pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Loading...");
        //pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                final String respond = Utils.GoogleRespondToJSON(response);
                // Google returns "// [ { "id": ... } ]".
                // We need to turn them into "[ { "id": ... } ]".
                // http://stackoverflow.com/questions/15158916/convert-json-array-to-a-java-list-object
                TypeToken<List<Stock>> token = new TypeToken<List<Stock>>() {};
                List<Stock> stockList = gson.fromJson(respond, token.getType());
                //pDialog.hide();
                callback.onSuccess(stockList);
                //VolleyLog.d(TAG, response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public interface VolleyCallback {
        void onSuccess(List<Stock> result);
    }

    public static void addItems(List<Stock> stockList) {
        for(Stock stock : stockList) {
            addItem(new StockItem(stock.getId(),stock.getName(),stock.getTicker(), stock.getPrice()));
            // some code here
        }
    }

    private static void addItem(StockItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class StockItem {
        public String id;
        public String name;
        public String exchange;
        public String ticker;
        public String price;

        public StockItem(String id, String name, String ticker, String price) {
            this.id = id;
            this.name = name;
            this.ticker = ticker;
            this.ticker = price;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

