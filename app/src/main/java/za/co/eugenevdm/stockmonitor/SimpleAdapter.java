package za.co.eugenevdm.stockmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SimpleAdapter extends ArrayAdapter<Stock> {

    private List<Stock> itemList;
    private Context context;

    public SimpleAdapter(List<Stock> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Stock getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Stock c = itemList.get(position);
        TextView text = (TextView) v.findViewById(R.id.name);
        text.setText(c.getName());

        TextView text1 = (TextView) v.findViewById(R.id.price);
        text1.setText(c.getTicker());

//        TextView text2 = (TextView) v.findViewById(R.id.E);
//        text2.setText(c.getExchange());
//
//        TextView text3 = (TextView) v.findViewById(R.id.type);
//        text3.setText(c.getType());

        return v;

    }

    public List<Stock> getItemList() {
        return itemList;
    }

    public void setItemList(List<Stock> itemList) {
        this.itemList = itemList;
    }

}
