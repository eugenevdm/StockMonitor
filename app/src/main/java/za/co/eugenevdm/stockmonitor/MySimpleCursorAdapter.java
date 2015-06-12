package za.co.eugenevdm.stockmonitor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by eugene on 2015/06/11.
 * <p/>
 * TODO Possible optimisation
 * http://stackoverflow.com/questions/5183813/android-issue-with-newview-and-bindview-in-custom-simplecursoradapter
 */
public class MySimpleCursorAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private int mLayout;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(mLayout, parent, false);

        String name = c.getString(c.getColumnIndex("description"));
        String price = c.getString(c.getColumnIndex("lastPrice"));
        String ticker = c.getString(c.getColumnIndex("ticker"));
        Float changePrice = c.getFloat(c.getColumnIndex("changePrice"));
        Float changePricePercentage = c.getFloat(c.getColumnIndex("changePricePercentage"));

        TextView name_text = (TextView) v.findViewById(R.id.description);
        name_text.setText(name);

        TextView textViewLastPrice = (TextView) v.findViewById(R.id.last_price);
        textViewLastPrice.setText(price);

        TextView textViewTicker = (TextView) v.findViewById(R.id.ticker);
        textViewTicker.setText(ticker);

        if (changePrice != 0) {

            TextView textViewChangePrice = (TextView) v.findViewById(R.id.change_price);
            textViewChangePrice.setText(Float.toString(changePrice));

            TextView textViewChangePricePercentage = (TextView) v.findViewById(R.id.change_price_percentage);
            textViewChangePricePercentage.setText("(" + Float.toString(changePricePercentage) + "%)");

            if (changePrice > 0) {
                textViewChangePricePercentage.setTextColor(Color.GREEN);
            } else if (changePrice < 0) {
                textViewChangePricePercentage.setTextColor(Color.RED);
            } else {
                textViewChangePricePercentage.setTextColor(Color.WHITE);
            }
        } else {
            TextView textViewChangePrice = (TextView) v.findViewById(R.id.change_price);
            textViewChangePrice.setText("");

            TextView textViewChangePricePercentage = (TextView) v.findViewById(R.id.change_price_percentage);
            textViewChangePricePercentage.setText("");
        }

        return v;
    }

    public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mContext = context;
        this.mLayout = layout;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

        String name = c.getString(c.getColumnIndex("description"));
        String price = c.getString(c.getColumnIndex("lastPrice"));
        String ticker = c.getString(c.getColumnIndex("ticker"));
        Float changePrice = c.getFloat(c.getColumnIndex("changePrice"));
        Float changePricePercentage = c.getFloat(c.getColumnIndex("changePricePercentage"));

        TextView name_text = (TextView) v.findViewById(R.id.description);
        name_text.setText(name);

        TextView textViewLastPrice = (TextView) v.findViewById(R.id.last_price);
        textViewLastPrice.setText(price);

        TextView textViewTicker = (TextView) v.findViewById(R.id.ticker);
        textViewTicker.setText(ticker);

        if (changePrice != 0) {

            TextView textViewChangePrice = (TextView) v.findViewById(R.id.change_price);
            // TODO Convert changePrice to Rands and add currency symbol
            textViewChangePrice.setText(Float.toString(changePrice));

            TextView textViewChangePricePercentage = (TextView) v.findViewById(R.id.change_price_percentage);
            textViewChangePricePercentage.setText("(" + Float.toString(changePricePercentage) + "%)");

            if (changePrice > 0) {
                textViewChangePricePercentage.setTextColor(Color.GREEN);
            } else if (changePrice < 0) {
                textViewChangePricePercentage.setTextColor(Color.RED);
            } else {
                textViewChangePricePercentage.setTextColor(Color.WHITE);
            }
        } else {
            TextView textViewChangePrice = (TextView) v.findViewById(R.id.change_price);
            textViewChangePrice.setText("");

            TextView textViewChangePricePercentage = (TextView) v.findViewById(R.id.change_price_percentage);
            textViewChangePricePercentage.setText("");
        }

    }

}