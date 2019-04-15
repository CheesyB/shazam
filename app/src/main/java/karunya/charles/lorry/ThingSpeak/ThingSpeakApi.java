package karunya.charles.lorry.ThingSpeak;



import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import karunya.charles.lorry.DB.Local;


public class ThingSpeakApi {

    private static final String TAG = "ThingSpeak API";


    private static final String THINGSPEAK_CHANNEL_ID = "744150";
    private static final String THINGSPEAK_API_KEY = "TIO7505TKCYF5Y9J1"; //Read
    private static final String THINGSPEAK_API_KEY_STRING = "WK2GFICTP06W7CTU"; //Write?!
    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_LATITUDE = "field1";
    private static final String THINGSPEAK_LONGITUDE = "field2";
    private static final String THINGSPEAK_TIMESTAMP = "created_at";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?"; //??
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    private static final String THINGSPEAK_FEED_ALL = "/feed.json";

    MutableLiveData<List<Local>> mAllLocals;
    MutableLiveData<Local> mNewestLocal;

    MutableLiveData<Boolean> mFetching;


    public MutableLiveData<List<Local>> getAllLocals() {
        return mAllLocals;
    }

    public MutableLiveData<Local> getNewestLocal() {
        return mNewestLocal;
    }

    public MutableLiveData<Boolean> getFetching() {
        return mFetching;
    }

    public ThingSpeakApi() {
        mAllLocals = new MutableLiveData<>();
        mNewestLocal = new MutableLiveData<>();
        mFetching = new MutableLiveData<>();
        mFetching.setValue(false);
    }

    public void fetchAllLocals(){
        new FetchAllLocations().execute();
    }

/*
    public void fetchLatestLocation(){
        new FetchLatestLocation().execute();
    }
*/

    // Okay Charles:) what's better having two distinct AsynkTasks sitting around
    // or one with more logic. Best would be to make an base class an inherit from it-> to lazy
    // right now....
    private class FetchAllLocations extends AsyncTask<Void,Void,String> {

        //https://thingspeak.com/channels/744150/feed.json
        protected void onPreExecute() {
            Log.d(TAG, "started fetching all data");
            mFetching.setValue(Boolean.TRUE);
        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEED_ALL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            mFetching.setValue(Boolean.FALSE);
            if (response == null) {
                Log.d(TAG, "This should not supposed to happen!!!!");
                return;
            }
            try {
                Log.d(TAG, "We made it to post execution");
                Log.d(TAG, String.format("This is the response Sting: %s", response));
                JSONObject channel = new JSONObject(response);
                JSONArray feeds = channel.getJSONArray("feeds");
                List<Local> fetchedLocals = new ArrayList<>();
                for(int i = 0; i < feeds.length(); i++) {
                    JSONObject feed = feeds.getJSONObject(i);
                    String longitude = feed.getString(THINGSPEAK_LONGITUDE);
                    String latitude = feed.getString(THINGSPEAK_LATITUDE);
                    //String timestamp = feed.getString(THINGSPEAK_TIMESTAMP);

                    Local tmpLocal = new Local(Double.parseDouble(longitude),
                            Double.parseDouble(latitude));
                    fetchedLocals.add(tmpLocal);
                }
                mAllLocals.setValue(fetchedLocals);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

   /* private class FetchLatestLocation extends AsyncTask<Void,Void,String> {

        protected void onPreExecute() {
            Log.d(TAG, "started fetching");
            mViewModel.setState(Boolean.TRUE);

        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            mViewModel.setState(false);
            if (response == null) {
                Log.d(TAG, "This should not supposed to happen!!!!");
                return;
            }
            try {
                Log.d(TAG, "We made it to post execution");
                Log.d(TAG, String.format("This is the response Sting: %s", response));
                JSONObject feed = (JSONObject) new JSONTokener(response).nextValue();
                String longitude = feed.getString(THINGSPEAK_LONGITUDE);
                String latitude = feed.getString(THINGSPEAK_LATITUDE);
                //String timestamp = feed.getString(THINGSPEAK_TIMESTAMP);
                Local tmpLocal = new Local(Double.parseDouble(longitude),
                        Double.parseDouble(latitude));
                mViewModel.insert(tmpLocal);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}