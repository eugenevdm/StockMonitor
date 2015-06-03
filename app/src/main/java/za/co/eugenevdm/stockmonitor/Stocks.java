package za.co.eugenevdm.stockmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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
 * Created by eugene on 2015/06/03.
 */
public class Stocks extends ArrayList<Stock> {

    private static final String TAG = "sm_getAllStocks";

//    public void getAllStocks(Activity ctx) {
//
//
//        // Tag used to cancel the request
//        String tag_string_req = "string_req";
//
//        final ProgressDialog pDialog = new ProgressDialog(ctx);
//        pDialog.setMessage("Loading...");
//        pDialog.show();



}

/*

Documentation

http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
http://www.survivingwithandroid.com/2013/11/android-volley-dynamic-listview.html
http://stackoverflow.com/questions/28120029/how-can-i-return-value-from-function-onresponse-of-volley

*/

