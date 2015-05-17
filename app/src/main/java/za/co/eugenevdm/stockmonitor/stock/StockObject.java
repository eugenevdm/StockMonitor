package za.co.eugenevdm.stockmonitor.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for stock
 *
 *
 */
public class StockObject {

    /**
     * An array of stock items.
     */
    public static List<StockItem> ITEMS = new ArrayList<StockItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, StockItem> ITEM_MAP = new HashMap<String, StockItem>();

    static {
        // Add 3 sample items.
        addItem(new StockItem("1", "Brait SE", "JSE:BAT"));
        addItem(new StockItem("2", "SABMiller plc (S. Africa)", "JSE:SAB"));
        addItem(new StockItem("3", "Steinhoff International Holdings Limited", "JSE:SHF"));
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
        public String ticker;

        public StockItem(String id, String name, String ticker) {
            this.id = id;
            this.name = name;
            this.ticker = ticker;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
