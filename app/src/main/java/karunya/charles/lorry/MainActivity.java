package karunya.charles.lorry;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.arch.lifecycle.ViewModelProviders;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import karunya.charles.lorry.Adapter.RecAdapter;
import karunya.charles.lorry.DB.Busy;
import karunya.charles.lorry.DB.Local;
import karunya.charles.lorry.ViewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecAdapter mRecAdapter;

    private RecyclerView mRecyclerView;
    private TextView mTotalTextView;
    private ProgressBar mBusySpinnerView;
    private MainActivityViewModel mViewModel;

    private Observer<List<Local>> mLocalListObserver;
    private Observer<Integer> mTotalLocalsObserver;
    private Observer<Busy> mBusyOberver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTotalTextView = findViewById(R.id.totalValue);
        mRecyclerView = findViewById(R.id.locationList);
        mBusySpinnerView = findViewById(R.id.main_busy_spinner);
        mBusySpinnerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecAdapter = new RecAdapter();
        mRecyclerView.setAdapter(mRecAdapter);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mLocalListObserver = new Observer<List<Local>>() {
            @Override
            public void onChanged(@Nullable List<Local> locals) {
                final String TAG = "Main Locals Observer";
                Log.d(TAG,"locals invoked");
                mRecAdapter.setDataset(locals);
            }
        };
        mViewModel.getAllLocals().observe(this, mLocalListObserver);

        mTotalLocalsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                final String TAG = "MainObserver";
                Log.d(TAG,"total invoked");
                mTotalTextView.setText(integer.toString());
            }
        };
        mViewModel.getTotal().observe(this, mTotalLocalsObserver);

        mBusyOberver = new Observer<Busy>() {
            @Override
            public void onChanged(@Nullable Busy aBusy) {
                final String TAG = "MainObserver";
                if(aBusy != null) {
                    Log.d(TAG,String.format("busy invoked. is not null"));
                    if (aBusy.getState()) {
                        mBusySpinnerView.setVisibility(View.VISIBLE);
                    } else {
                        mBusySpinnerView.setVisibility(View.GONE);
                    }
                }else{
                    Log.d(TAG,String.format("busy invoked. is null"));
                }
            }
        };
        mViewModel.isBusy().observe(this,mBusyOberver);
        mViewModel.setBusy(new Busy(false)); // mein Fresse wie aufwendig!!!
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(mRecAdapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"inflating Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_bar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_fetch_data:
                Log.d(TAG,"Deleting all to populate fresh");
                mViewModel.deleteAll();
                mViewModel.fetchAllLocals();
                return true;
            case R.id.menu_delete_all:
                mViewModel.deleteAll();
                return true;
            case R.id.menu_show_at_map:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
