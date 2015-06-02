package za.co.eugenevdm.stockmonitor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import za.co.eugenevdm.stockmonitor.engine.Code;
import za.co.eugenevdm.stockmonitor.engine.GoogleStockServer;
import za.co.eugenevdm.stockmonitor.engine.Stock;
import za.co.eugenevdm.stockmonitor.engine.StockNotFoundException;
import za.co.eugenevdm.stockmonitor.stock.StockObject;

/**
 * A fragment representing a single Stock detail screen.
 * This fragment is either contained in a {@link StockListActivity}
 * in two-pane mode (on tablets) or a {@link StockDetailActivity}
 * on handsets.
 */
public class StockDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "sm_StockDetailFragment";

    /**
     * The dummy name this fragment is presenting.
     */
    private StockObject.StockItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StockDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy name specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load name from a name provider.
            mItem = StockObject.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_detail, container, false);

        // Show the dummy name as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.stock_detail)).setText(mItem.name);
            // Obtain latest quote from Google Finance
            GoogleStockServer server = new GoogleStockServer();
            List codes = Arrays.asList("A", "B", "C");
            //try {
                //List test = server.getStocks(codes);
            //} catch (StockNotFoundException e) {
                //e.printStackTrace();
            //}
//            //Stock stock = new Stock();
//            Code code = new Code("abc123");
//            Stock stock = server.getStock();

            // http://www.androidhive.info/2014/05/android-working-with-volley-library-1/


            String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=JSE%3ABAT,JSE%3ASAB";
            //String url = "http://api.androidhive.info/volley/person_array.json";

            // Tag used to cancel the request
            String  tag_string_req = "string_req";



            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    pDialog.hide();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            });

// Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


            ((TextView) rootView.findViewById(R.id.stock_price)).setText(mItem.ticker);
        }

        return rootView;
    }
}
