package za.co.eugenevdm.stockmonitor;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import za.co.eugenevdm.stockmonitor.engine.Utils;

/**
 * A list fragment representing a list of Stocks. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link StockDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class StockListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String TAG = "sm_StockListFragment";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StockListFragment() {
    }

    private List<Stock> stocksList = new ArrayList<Stock>();
    private CustomListAdapter adapter;

    // Custom definitions
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // TODO Investigate Vogella example to find code below
    // private Cursor cursor;
    private SimpleCursorAdapter adapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CustomListAdapter(getActivity(), stocksList);
        setListAdapter(adapter);

        String tag_string_req = "string_req";
        String stocks = "JSE:BAT,JSE:SAB,NASDAQ:TSLA,JSE:SHF,NASDAQ:MSFT,NYSE:ORCL";
        String url = "https://www.google.com/finance/info?infotype=infoquoteall&q=" + stocks;
        final Gson gson = new Gson();

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Updating...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                final String respond = Utils.GoogleRespondToJSON(response);
                // Google returns "// [ { "id": ... } ]".
                // We need to turn them into "[ { "id": ... } ]".
                // http://stackoverflow.com/questions/15158916/convert-json-array-to-a-java-list-object
                TypeToken<List<Stock>> token = new TypeToken<List<Stock>>() {
                };
                List<Stock> serverStockList = gson.fromJson(respond, token.getType());
                pDialog.hide();

                for (Stock serverStock : serverStockList) {
                    //String currencySymbol;
                    String currency;
                    String ex = serverStock.getExchange();
                    String p = serverStock.getPrice();
                    //currencySymbol = serverStock.getCurrencySymbol();
                    if (ex.equals("JSE")) {
                        // TODO Migrate to object method
                        currency = "R ";
                        // TODO Migrate to object method
                        p = p.replace(",", "");
                        float f = Float.parseFloat(p);
                        f = f / 100;
                        p = Float.toString(f);
                        serverStock.setPrice(p);
                    } else {
                        currency = "$";
                    }
                    Stock s = new Stock();
                    s.setGoogleId(serverStock.getGoogleId());
                    s.setName(serverStock.getName());
                    s.setExchange(serverStock.getExchange());
                    s.setTicker(serverStock.getTicker());
                    // Add the currency symbol to the price
                    s.setPrice(s.getCurrencySymbol() + serverStock.getPrice());
                    s.setPe(serverStock.getPe());
                    s.setMarketCap(serverStock.getMarketCap());
                    Float cp = serverStock.getCp();
                    s.setCp(serverStock.getCp());
                    stocksList.add(s);
                    Stock.addItem(new
                            Stock.StockItem(s.getGoogleId(),
                            s.getName(),
                            s.getExchange(),
                            s.getTicker(),
                            s.getPrice(),
                            s.getPe(),
                            s.getMarketCap(),
                            s.getCp()));
                }

                adapter.notifyDat aSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        // Add request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(Stock.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    // Customisation

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {StockTable.COLUMN_ID, StockTable.COLUMN_TICKER};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                MyStockContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter2.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter2.swapCursor(null);
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{StockTable.COLUMN_TICKER};
        // Fields on the UI to which we map
        //int[] to = new int[] { R.id.label };
        int[] to = new int[]{R.id.name};

        getLoaderManager().initLoader(0, null, this);
        adapter2 = new SimpleCursorAdapter(getActivity(), R.layout.list_item, null, from, to, 0);

        setListAdapter(adapter);
    }

}
