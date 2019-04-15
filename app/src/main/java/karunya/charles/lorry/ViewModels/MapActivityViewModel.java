package karunya.charles.lorry.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Handler;


import java.util.List;

import karunya.charles.lorry.DB.Busy;
import karunya.charles.lorry.DB.Local;
import karunya.charles.lorry.DB.LocalRepo;

public class MapActivityViewModel extends AndroidViewModel implements BaseViewModel {

    private LocalRepo mLocalRepo;
    private LiveData<List<Local>> mLocals;
    private LiveData<Integer> mTotal;
    private LiveData<Busy> mBusy;


    public MapActivityViewModel(Application application){
        super(application);
        mLocalRepo = LocalRepo.getLocalRepo(application);
    }

    public void setBusy(Busy busy){
        mLocalRepo.setBusy(busy);
    }


    public LiveData<Busy> isBusy() {
        if(mBusy == null){
            mBusy =  mLocalRepo.getBusy();
            return mBusy;
        }
        return mBusy;
    }


    public void fetchAllLocals(){
        mLocalRepo.fetchAllLocals();
    }

    public void fetchLatestLocal(){
        mLocalRepo.fetchLatestLocal();
    }



    public LiveData<List<Local>> getAllLocals() {
        if (mLocals == null) {
            mLocals = mLocalRepo.getAllLocals();
            return mLocals;
        }
        return mLocals;
    }

    public LiveData<Integer> getTotal(){
        if(mTotal == null){
            mTotal = mLocalRepo.getTotal();
            return mTotal;
        }
        return mTotal;
    }

    public void insert(Local local){ mLocalRepo.insert(local);}

    public void updateLongitude(String payload, int id) {
        mLocalRepo.updateLongitude(payload, id);
    }

    public void updateLatitude(String payload, int id) {
        mLocalRepo.updateLatitude(payload, id);
    }

    public void deleteAll(){
        mLocalRepo.deleteAll();
    }
    public void delete(int id){
        mLocalRepo.delete(id);
    }

    public void insertAll(List<Local> localList){
        mLocalRepo.insertAll(localList);
    }

}
