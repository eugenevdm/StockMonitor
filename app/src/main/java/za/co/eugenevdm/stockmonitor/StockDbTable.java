package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StockDbTable {

    // Database table
    public static final String TABLE_STOCK = "stock";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TICKER = "ticker";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LAST_PRICE = "lastPrice";
    public static final String COLUMN_CHANGE_PRICE = "changePrice";
    public static final String COLUMN_CHANGE_PRICE_PERCENTAGE = "changePricePercentage";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STOCK
            + "("
            + COLUMN_ID                         + " integer primary key autoincrement, "
            + COLUMN_CATEGORY                   + " text, "
            + COLUMN_TICKER                     + " text, "
            + COLUMN_DESCRIPTION                + " text, "
            + COLUMN_LAST_PRICE                 + " real, "
            + COLUMN_CHANGE_PRICE               + " real, "
            + COLUMN_CHANGE_PRICE_PERCENTAGE    + " real "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(StockDbTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(database);
    }
}