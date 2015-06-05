package za.co.eugenevdm.stockmonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock implements Serializable {

    private String id;
    private String e;
    private String t;
    private String name;
    private String l;

    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, StockItem> ITEM_MAP = new HashMap<String, StockItem>();

    void setId(String id) { this.id = id; }

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

//    public void addStock(String id, String name, String e, String t, String l) {
//        this.id = id;
//        this.name = name;
//        this.e = e;
//        this.t = t;
//        this.l = l;
//    }

    public static void addItem(StockItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
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

    public static class StockItem {
        public String id;
        public String name;
        public String exchange;
        public String ticker;
        public String price;

        public StockItem(String id, String name, String exchange, String ticker, String price) {
            this.id = id;
            this.name = name;
            this.exchange = exchange;
            this.ticker = ticker;
            this.price = price;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}

