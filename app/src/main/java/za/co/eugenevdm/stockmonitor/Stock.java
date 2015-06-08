package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock implements Serializable {
    // JSON values
    private long DbId;
    private String googleId;        // Google ID
    private String name;            // Name
    private String e;               // Exchange
    private String t;               // Ticker
    private String l;               // Price (last price)
    private String currencySymbol;        // Currency symbol, derived
    private String pe;              // PE
    private String mc;              // Market cap
    private Float cp;              // Change percentage (from last day)
    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

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

    void setPrice(String price) {
        this.l = price;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public void setMarketCap(String mc) {
        this.mc = mc;
    }

    public void setCp(Float cp) {
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

    String getPrice() {
        // Check which exchange and set currencySymbol symbol
        return l;
//        if (e.equals("")) {
//            return l;
//        } else if (e.equals("JSE")) {
//            this.currencySymbol = "R ";
//            l = convertToCents(l);
//            //return l;
//        } else if (e.equals("NASDAQ") || e.equals("NYSE")) {
//            this.currencySymbol = "$";
//            //return l;
//        }
//        return l;
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

    Float getCp() {
        return cp;
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

        public StockItem(String id,
                         String name,
                         String exchange,
                         String ticker,
                         String price,
                         String pe,
                         String market_cap,
                         Float cp) {
            this.id                 = id;
            this.name               = name;
            this.exchange           = exchange;
            this.ticker             = ticker;
            this.exchange_ticker    = this.exchange + ":" + this.ticker;
            this.price              = price;
            this.pe                 = pe;
            this.market_cap         = market_cap;
            this.cp                 = cp;
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

}

