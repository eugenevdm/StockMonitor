package za.co.eugenevdm.stockmonitor;

import android.view.View;
import android.widget.TextView;

/**
 * Created by eugene on 2015/06/07.
 */
class ViewHolder {

    TextView name=null;
    TextView price=null;
    TextView cp=null;

    ViewHolder(View row) {
        this.name=(TextView)row.findViewById(R.id.name);
        this.price=(TextView)row.findViewById(R.id.price);
        this.cp=(TextView)row.findViewById(R.id.cp);
    }
}