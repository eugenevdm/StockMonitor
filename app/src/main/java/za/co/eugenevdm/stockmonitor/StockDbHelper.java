package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class StockDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stocktable.db";
    private static final int DATABASE_VERSION = 4;

    public StockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        StockDbTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        StockDbTable.onUpgrade(database, oldVersion, newVersion);
    }

//    /**
//     * This method returns all notes from the database
//     */
//    public ArrayList<Stock> getAllStocks() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        ArrayList<Stock> listItems = new ArrayList<>();
//        Cursor cursor = db.rawQuery(
//                "SELECT " +
//                        "_id, description, ticker, lastPrice, ChangePrice, ChangePricePercentage" +
//                        "from " + StockDbTable.TABLE_STOCK,
//                new String[] {});
//        if (cursor.moveToFirst()) {
//            do {
//                Stock stock = new Stock();
//                stock.setDbId(cursor.getInt(cursor.getColumnIndex(StockDbTable.COLUMN_ID)));
//                stock.setName(cursor.getString(cursor.getColumnIndex(StockDbTable.COLUMN_DESCRIPTION)));
//                stock.setTicker(cursor.getString(cursor.getColumnIndex(StockDbTable.COLUMN_TICKER)));
//                stock.setLastPrice(cursor.getString(cursor.getColumnIndex(StockDbTable.COLUMN_LAST_PRICE)));
//                stock.setChangePrice(cursor.getFloat(cursor.getColumnIndex(StockDbTable.COLUMN_CHANGE_PRICE)));
//                stock.setChangePricePercentage(cursor.getFloat(cursor.getColumnIndex(StockDbTable.COLUMN_CHANGE_PRICE_PERCENTAGE)));
//                listItems.add(stock);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return listItems;
//    }
}
