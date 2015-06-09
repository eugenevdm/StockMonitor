package za.co.eugenevdm.stockmonitor;

/**
 * List of stocks
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Stock> stockItems;

    public CustomListAdapter(Activity activity, List<Stock> movieItems) {
        this.activity = activity;
        this.stockItems = movieItems;
    }

    @Override
    public int getCount() {
        return stockItems.size();
    }

    @Override
    public Object getItem(int location) {
        return stockItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView cp = (TextView) convertView.findViewById(R.id.cp);

        // Get row data
        Stock s = stockItems.get(position);

        name.setText(s.getName());
        price.setText(s.getPrice());
        if (s.getCp() < 0) {
            cp.setTextColor(Color.RED);
        } else if (s.getCp() > 0) {
            cp.setTextColor(Color.GREEN);
        } else {
            cp.setTextColor(Color.WHITE);
        }
        cp.setText(s.getCp() + "%");
        return convertView;
    }

}