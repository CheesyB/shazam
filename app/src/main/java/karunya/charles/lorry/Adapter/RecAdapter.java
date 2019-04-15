package karunya.charles.lorry.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import karunya.charles.lorry.DB.Local;
import karunya.charles.lorry.R;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {

    private static final String TAG = "RecAdapter";

    private List<Local> mLocals;

    public RecAdapter(){
        mLocals = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecAdapter.RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,
                parent,
                false);
        return new RecViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.RecViewHolder holder, int position) {
        holder.setLocal(mLocals.get(position));

    }

    @Override
    public int getItemCount() {
        if(mLocals != null){
            Log.d(TAG, String.format("size of mLocals: %d", mLocals.size()));
            return mLocals.size();
        }else{
            Log.d(TAG,"mLocals is null, but initialized in Constructor?!");
            return 0;
        }
    }

    public void setDataset(List<Local> local){
        mLocals = local;
        notifyDataSetChanged();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "RecViewHolder";
        public TextView mLongi;
        public TextView mLati;

        public RecViewHolder(@NonNull View itemView) {
            super(itemView);

            mLongi  = (TextView)itemView.findViewById(R.id.itemValueLongitude);
            mLati = (TextView)itemView.findViewById(R.id.itemValueLatitude);
            if(mLati == null || mLongi == null){
                Log.d(TAG,"Something is wrong here! Null!");
            }
        }

        public void setLocal(Local local ){
            mLongi.setText(local.getLongitude().toString());
            mLati.setText(local.getLatitude().toString());
        }

    }

}
