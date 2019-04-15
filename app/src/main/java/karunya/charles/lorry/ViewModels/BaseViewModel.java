package karunya.charles.lorry.ViewModels;

import android.arch.lifecycle.LiveData;

import java.util.List;

import karunya.charles.lorry.DB.Busy;
import karunya.charles.lorry.DB.Local;

public interface BaseViewModel {

    LiveData<Busy> isBusy();

    LiveData<List<Local>> getAllLocals();

    LiveData<Integer> getTotal();

    void setBusy(Busy busy);

    void fetchAllLocals();

    void insertAll(List<Local> localList);

    void insert(Local local);

    void updateLongitude(String payload, int id);

    void updateLatitude(String payload, int id);

    void deleteAll();

    void delete(int id);
}
