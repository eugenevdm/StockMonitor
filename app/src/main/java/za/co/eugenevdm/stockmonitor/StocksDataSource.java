package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StocksDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TICKER };

    public StocksDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Stock createStock(String ticker) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TICKER, ticker);
        long insertId = database.insert(MySQLiteHelper.TABLE_STOCKS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_STOCKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Stock newStock = cursorToStock(cursor);
        cursor.close();
        return newStock;
    }

    public void deleteStock(Stock stock) {
        long id = stock.getDbId();
        System.out.println("Stock deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_STOCKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<Stock>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_STOCKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Stock stock = cursorToStock(cursor);
            stocks.add(stock);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stocks;
    }

    private Stock cursorToStock(Cursor cursor) {
        Stock stock = new Stock();
        stock.setDbId(cursor.getLong(0));
        stock.setTicker(cursor.getString(1));
        return stock;
    }
} 
