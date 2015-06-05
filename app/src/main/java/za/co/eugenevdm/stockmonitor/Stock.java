package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock implements Serializable {

    private String id;      // Google ID
    private String e;       // Exchange
    private String t;       // Ticker
    private String name;    // Name
    private String l;       // Price (last price)
    private String pe;      // PE
    private String mc;      // Market cap
    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

    /**
     * A map of stock items, by ID.
     */
    public static Map<String, StockItem> ITEM_MAP = new HashMap<String, StockItem>();

    public static void addItem(StockItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    void setId(String id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    void setExchange(String exchange) {
        this.e = exchange;
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

    public void setMc(String mc) {
        this.mc = mc;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getExchange() {
        return e;
    }

    String getTicker() {
        return t;
    }

    String getPrice() {
        return l;
    }

    String getPe() {
        return pe;
    }

    String getMarketCap() {
        return mc;
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

        public StockItem(String id, String name, String exchange, String ticker, String price, String pe, String market_cap) {
            this.id = id;
            this.name = name;
            this.exchange = exchange;
            this.ticker = ticker;
            this.exchange_ticker = this.exchange + ":" + this.ticker;
            this.price = price;
            this.pe = pe;
            this.market_cap = market_cap;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}

