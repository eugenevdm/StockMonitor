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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import za.co.eugenevdm.stockmonitor.engine.Code;
import za.co.eugenevdm.stockmonitor.engine.GoogleStockServer;
import za.co.eugenevdm.stockmonitor.engine.Pair;
import za.co.eugenevdm.stockmonitor.engine.Stock;
import za.co.eugenevdm.stockmonitor.engine.StockNotFoundException;
import za.co.eugenevdm.stockmonitor.engine.Symbol;
import za.co.eugenevdm.stockmonitor.engine.UnitedStatesGoogleFormatCodeLookup;
import za.co.eugenevdm.stockmonitor.engine.Utils;
import za.co.eugenevdm.stockmonitor.engine.currency.Currency;
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
    private final Gson gson = new Gson();
    private static final int SYMBOL_MAX_LENGTH = 17;
    private static final Map<String, String> currencySymbolToCurrencyCodeMap = new HashMap<>();

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

    /*
    06-02 21:08:24.341  24907-24907/za.co.eugenevdm.stockmonitor D/sm_StockDetailFragment﹕ [{id=1019083932318899, t=BAT, e=JSE, l=10,682.00, l_fix=10682.00, l_cur=ZAC10,682.00, s=0, ltt=5:00PM GMT 2, lt=Jun 2, 5:00PM GMT 2, lt_dts=2015-06-02T17:00:22Z, c= 182.00, c_fix=182.00, cp=1.73, cp_fix=1.73, ccol=chg, pcls_fix=10500, eo=, delay=15, op=10,500.00, hi=10,711.00, lo=10,250.00, vo=485,561.00, avvo=642,997.00, hi52=11,665.00, lo52=5,600.00, mc=55.17B, pe=20.39, fwpe=, beta=, eps=5.24, shares=516.49M, inst_own=, name=Brait SE, type=Company}, {id=918586879122938, t=SAB, e=JSE, l=64,859.00, l_fix=64859.00, l_cur=ZAC64,859.00, s=0, ltt=5:00PM GMT 2, lt=Jun 2, 5:00PM GMT 2, lt_dts=2015-06-02T17:00:25Z, c= 324.00, c_fix=324.00, cp=0.50, cp_fix=0.50, ccol=chg, pcls_fix=64535, eo=, delay=15, op=64,100.00, hi=64,859.00, lo=63,368.00, vo=955,935.00, avvo=819,223.00, hi52=68,907.00, lo52=55,403.00, mc=1.05T, pe=26.01, fwpe=, beta=, eps=24.94, shares=1.62B, inst_own=, name=SABMiller plc (S. Africa), type=Company}]
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_detail, container, false);

        // Show the dummy name as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.stock_detail)).setText(mItem.name);
            // Obtain latest quote from Google Finance
            // http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
            String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=JSE%3ABAT,JSE%3ASAB";
            // String url = "http://api.androidhive.info/volley/person_array.json";
            // Tag used to cancel the request
            String tag_string_req = "string_req";
            // Tag used to cancel the request
            String tag_json_arry = "json_array_req";

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    final String respond = Utils.GoogleRespondToJSON(response);
                    // Google returns "// [ { "id": ... } ]".
                    // We need to turn them into "[ { "id": ... } ]".
                    // http://stackoverflow.com/questions/15158916/convert-json-array-to-a-java-list-object
                    TypeToken<List<Contact>> token = new TypeToken<List<Contact>>(){};
                    List<Contact> personList = gson.fromJson(respond, token.getType());


                    //final List<Map> jsonArray = gson.fromJson(respond, List.class);
                    //final JSONArray jsonArray = gson.fromJson(respond, JSONArray.class);
                    Log.d(TAG, personList.toString());

                    // Convert to object
                   // List<Contact> result = new ArrayList<Contact>();
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            result.add(convertContact(jsonArray
//                                    .getJSONObject(i)));
//                        } catch (JSONException e) {
//                        }
//                    }

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

    private final Contact convertContact(JSONObject obj) throws JSONException {
        String t = obj.getString("t");
        String e = obj.getString("e");
        String name = obj.getString("name");
        String type = obj.getString("type");
        return new Contact(t, e, name, type);
    }


}
