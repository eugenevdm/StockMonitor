package za.co.eugenevdm.stockmonitor;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.eugenevdm.stockmonitor.engine.Utils;

public class Stock implements Serializable {
    // JSON values
    private long DbId;
    private String googleId;        // Google ID
    private String name;            // Name
    private String e;               // Exchange
    private String t;               // Ticker
    private String l;               // Price (last price)
    private String currencySymbol;  // Currency symbol, derived
    private String pe;              // PE
    private String mc;              // Market cap
    private Float cp;               // Change price percentage (from last read)
    private Float c;                // Change price
    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

    private static final String TAG = "sm_StockObject";

    /**
     * A map of stock items, by ID.
     */
    public static Map<String, StockItem> ITEM_MAP = new HashMap<String, StockItem>();

    public static void addItem(StockItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    void setDbId(long id) {
        this.DbId = id;
    }

    void setGoogleId(String id) {
        this.googleId = id;
    }

    void setName(String name) {
        this.name = name;
    }

    void setExchange(String exchange) {
        this.e = exchange;
        // When setting the exchange, also set currency symbol
        if (e.equals("JSE")) {
            this.currencySymbol = "R ";
        } else if (e.equals("NASDAQ") || e.equals("NYSE")) {
            this.currencySymbol = "$";
        }
    }

    void setTicker(String ticker) {
        this.t = ticker;
    }

    void setLastPrice(String price) {
        this.l = price;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public void setMarketCap(String mc) {
        this.mc = mc;
    }

    public void setChangePrice(Float c) {
        this.c = c;
    }

    public void setChangePricePercentage(Float cp) {
        this.cp = cp;
    }

    long getDbId() { return DbId; }

    String getGoogleId() {
        return googleId;
    }

    String getName() { return name; }

    String getExchange() {
        return e;
    }

    String getTicker() {
        return t;
    }

    String getLastPrice() {
        return l;
    }

    String getCurrencySymbol() {
        return currencySymbol;
    }

    String getPe() {
        if (this.pe.equals("")) {
            return "-";
        } else {
            return pe;
        }
    }

    String getMarketCap() {
        return mc;
    }

    Float getChangePrice() {
        return cp;
    }

    Float getChangePricePercentage() {
        return c;
    }

    public static class StockItem {
        public String id;
        public String name;
        public String exchange;
        public String ticker;
        public String exchange_ticker;
        public String price;
        public String pe;
        public String market_cap;
        public float cp;
        public float c;

        public StockItem(String id,
                         String name,
                         String exchange,
                         String ticker,
                         String price,
                         String pe,
                         String market_cap,
                         Float cp,
                         Float c
                         ) {
            this.id                 = id;
            this.name               = name;
            this.exchange           = exchange;
            this.ticker             = ticker;
            this.exchange_ticker    = this.exchange + ":" + this.ticker;
            this.price              = price;
            this.pe                 = pe;
            this.market_cap         = market_cap;
            this.cp                 = cp;
            this.c                  = c;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static String convertToRands(String l) {
        l = l.replace(",", "");
        float f = Float.parseFloat(l);
        f = f / 100;
        l = Float.toString(f);
        return l;
    }

    void getJsonStocksFromGoogle(Context ctx, final List<Stock> stocksList, final MySimpleCursorAdapter adapter) {
        String tag_string_req = "string_req";
        String stocks = "JSE:BAT,JSE:SAB,NASDAQ:TSLA,JSE:SHF,NASDAQ:MSFT,NYSE:ORCL";
        String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=" + stocks;
        final Gson gson = new Gson();

        final ProgressDialog pDialog = new ProgressDialog(ctx);
        pDialog.setMessage("Updating...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                final String respond = Utils.GoogleRespondToJSON(response);
                // Google returns "// [ { "id": ... } ]".
                // We need to turn them into "[ { "id": ... } ]".
                // http://stackoverflow.com/questions/15158916/convert-json-array-to-a-java-list-object
                TypeToken<List<Stock>> token = new TypeToken<List<Stock>>() {
                };
                List<Stock> serverStockList = gson.fromJson(respond, token.getType());
                pDialog.hide();

                for (Stock serverStock : serverStockList) {
                    String ex = serverStock.getExchange();
                    String p = serverStock.getLastPrice();
                    if (ex.equals("JSE")) {
                        // TODO Migrate to object method
                        p = p.replace(",", "");
                        float f = Float.parseFloat(p);
                        f = f / 100;
                        p = Float.toString(f);
                        serverStock.setLastPrice(p);
                    }
                    Stock s = new Stock();
                    s.setGoogleId(serverStock.getGoogleId());
                    s.setName(serverStock.getName());
                    s.setExchange(serverStock.getExchange());
                    s.setTicker(serverStock.getTicker());
                    // Add the currency symbol to the price
                    s.setLastPrice(s.getCurrencySymbol() + serverStock.getLastPrice());
                    s.setPe(serverStock.getPe());
                    s.setMarketCap(serverStock.getMarketCap());
                    s.setChangePricePercentage(serverStock.getChangePrice());
                    stocksList.add(s); // This is used by the listview
                    Stock.addItem(new
                            Stock.StockItem(s.getGoogleId(),
                            s.getName(),
                            s.getExchange(),
                            s.getTicker(),
                            s.getLastPrice(),
                            s.getPe(),
                            s.getMarketCap(),
                            s.getChangePrice(),
                            s.getChangePricePercentage()
                    ));
                }

                adapter.notifyDataSetChanged();

                //return serverStockList;
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

}

