package karunya.charles.lorry;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import karunya.charles.lorry.DB.Busy;
import karunya.charles.lorry.DB.Local;
import karunya.charles.lorry.ViewModels.MapActivityViewModel;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MapActivity";



    private GoogleMap mMap;

    private TextView mTotalTextView;
    private ProgressBar mBusySpinnerView;
    private MapActivityViewModel mViewModel;

    private Observer<List<Local>> mLocalListObserver;
    private Observer<Integer> mTotalLocalsObserver;
    private Observer<Busy> mBusyOberver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mTotalTextView = findViewById(R.id.totalValue);
        mBusySpinnerView = findViewById(R.id.main_busy_spinner);
        mBusySpinnerView.setVisibility(View.GONE);

        mViewModel = ViewModelProviders.of(this).get(MapActivityViewModel.class);



        mTotalLocalsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                final String TAG = "Map Total Observer";
                Log.d(TAG, "invoked");
                mTotalTextView.setText(integer.toString());
            }
        };
        mViewModel.getTotal().observe(this, mTotalLocalsObserver);

        mBusyOberver = new Observer<Busy>() {
            @Override
            public void onChanged(@Nullable Busy aBusy) {
                final String TAG = "Map Busy Observer";
                Log.d(TAG, "invoked");
                if (aBusy.getState()) {
                    mBusySpinnerView.setVisibility(View.VISIBLE);
                } else {
                    mBusySpinnerView.setVisibility(View.GONE);
                }
            }
        };
        mViewModel.isBusy().observe(this, mBusyOberver);
        mViewModel.setBusy(new Busy(false));
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "inflating Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_fetch_data:
                Log.d(TAG, "Deleting all to populate fresh");
                mViewModel.deleteAll();
                mViewModel.fetchAllLocals();
                return true;
            case R.id.menu_delete_all:
                mViewModel.deleteAll();
                return true;
            case R.id.go_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLocations(List<Local> locals){
        Integer total = mViewModel.getTotal().getValue();

        if(locals != null){
            for(int i = 0; i < locals.size(); i++){
                Local loc = locals.get(i);
                LatLng currentMark = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentMark)
                        .title(String.format("Loaction%d",i)));
            }
            Local lastLocal = locals.get(locals.size() - 1);
            LatLng lastMark = new LatLng(lastLocal.getLatitude(), lastLocal.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMark,15.0f));
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "onMapsReady ");
        mLocalListObserver = new Observer<List<Local>>() {
            @Override
            public void onChanged(@Nullable List<Local> locals) {
                final String TAG = "Map Locals Observer";
                if(locals.size() > 0) { //needed because setLocation uses .size() -1
                    Log.d(TAG, "invoked");
                    setLocations(locals);
                }
            }
        };
        mViewModel.getAllLocals().observe(this, mLocalListObserver);

    }
}
