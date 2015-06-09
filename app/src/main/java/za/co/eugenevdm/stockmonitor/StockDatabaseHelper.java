package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StockDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stocktable.db";
    private static final int DATABASE_VERSION = 1;

    public StockDatabaseHelper(Context context) {
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
}
