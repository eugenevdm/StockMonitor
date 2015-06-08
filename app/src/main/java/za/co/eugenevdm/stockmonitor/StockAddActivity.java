package za.co.eugenevdm.stockmonitor;

/**
 * Created by eugene on 2015/06/08.
 */
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * StockAddActivity allows to add a new stock item or to change an existing stock
 */
public class StockAddActivity extends Activity {
    private Spinner mCategorySpinner;
    private EditText mTickerText;
    private EditText mDescriptionText;

    private Uri stockUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.stock_edit);

        mCategorySpinner = (Spinner) findViewById(R.id.category);
        mTickerText = (EditText) findViewById(R.id.stock_edit_ticker);
        mDescriptionText = (EditText) findViewById(R.id.stock_edit_description);
        Button confirmButton = (Button) findViewById(R.id.stock_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        stockUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyStockContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            stockUri = extras
                    .getParcelable(MyStockContentProvider.CONTENT_ITEM_TYPE);

            fillData(stockUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTickerText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { StockTable.COLUMN_TICKER,
                StockTable.COLUMN_DESCRIPTION, StockTable.COLUMN_CATEGORY };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(StockTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategorySpinner.getCount(); i++) {

                String s = (String) mCategorySpinner.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategorySpinner.setSelection(i);
                }
            }

            mTickerText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(StockTable.COLUMN_TICKER)));
            mDescriptionText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(StockTable.COLUMN_DESCRIPTION)));

            // always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyStockContentProvider.CONTENT_ITEM_TYPE, stockUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) mCategorySpinner.getSelectedItem();
        String ticker = mTickerText.getText().toString();
        String description = mDescriptionText.getText().toString();

        // only save if either summary or description
        // is available

        if (description.length() == 0 && ticker.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(StockTable.COLUMN_CATEGORY, category);
        values.put(StockTable.COLUMN_TICKER, ticker);
        values.put(StockTable.COLUMN_DESCRIPTION, description);

        if (stockUri == null) {
            // New stock
            stockUri = getContentResolver().insert(MyStockContentProvider.CONTENT_URI, values);
        } else {
            // Update stock
            getContentResolver().update(stockUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(StockAddActivity.this, "Please add a ticker",
                Toast.LENGTH_LONG).show();
    }
} 