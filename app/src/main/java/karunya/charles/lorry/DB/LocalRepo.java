package karunya.charles.lorry.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import karunya.charles.lorry.ThingSpeak.ThingSpeakApi;


public class LocalRepo {

    private static final String TAG = "LocalRepo";
    private static volatile LocalRepo INSTANCE;

    private LocalDatabase mDatabase;
    private LocalDao mLocalDao;
    private ThingSpeakApi mThingSpeak;
    private Observer<List<Local>> mThingSpeakAllLocalsObserver;
    private Observer<Boolean> mThingFetchingObserver;


    public static LocalRepo getLocalRepo(Application application) {
        if(INSTANCE == null){
            Log.d(TAG,"First call");
            INSTANCE = new LocalRepo(application);
            return INSTANCE;
        }
        Log.d(TAG,"Consecutive calls");
        return INSTANCE;
    }

    private LocalRepo(Application application ) {
        mDatabase = LocalDatabase.getDatabase(application.getApplicationContext());
        mLocalDao = mDatabase.localDao();
        mThingSpeak = new ThingSpeakApi();
        mThingSpeakAllLocalsObserver = new Observer<List<Local>>() {
            @Override
            public void onChanged(@Nullable List<Local> locals) {
                final String TAG = "Main Locals Observer";
                Log.d(TAG,"invoked");
                insertAll(locals);
            }
        };
        // I think is okay to observe forever because when repo is gone ThinkSpeak is gone, too.
        mThingSpeak.getAllLocals().observeForever(mThingSpeakAllLocalsObserver);

        mThingFetchingObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                final String TAG = " Observer";
                Log.d(TAG,"invoked fetching");
                setBusy(new Busy(aBoolean.booleanValue()));
            }
        };
        mThingSpeak.getFetching().observeForever(mThingFetchingObserver);
    }

    public LiveData<Busy> getBusy(){return mLocalDao.isBusy();}
    public void setBusy(Busy busy){new setBusyAsyncTask(mLocalDao,busy).execute();}

    public void fetchAllLocals(){
        mThingSpeak.fetchAllLocals();
    }

    public LiveData<Integer> getTotal(){
       return mLocalDao.getTotal();
    }
    public LiveData<List<Local>> getAllLocals() {
        return mLocalDao.getAllLoacals();
    }

    public void insert(Local local){
        new insertAsyncTask(mLocalDao).execute(local);
    }
    public void insertAll(List<Local> localList){
        new insertAllAsyncTask(mLocalDao, localList).execute();
    }

    public void delete(int id){
        new deleteAsyncTask(mLocalDao).execute(id);
    }
    public void deleteAll(){
        new deleteAllAsyncTask(mLocalDao).execute();
    }

    public void updateLatitude(String payload, int id){
        new updateLatitudeAsyncTask(mLocalDao,payload,id).execute();
    }
    public void updateLongitude(String payload, int id){
        new updateLongitudeAsyncTask(mLocalDao,payload,id).execute();
    }
    private static class updateLatitudeAsyncTask extends AsyncTask<Void,Void,Void> {

        private LocalDao myAsyncLocalDao;
        private String payload;
        private int mId;

        public updateLatitudeAsyncTask(LocalDao dao, String editedText, int id) {
            myAsyncLocalDao = dao;
            payload = editedText;
            mId = id;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            myAsyncLocalDao.updateLatiutude(payload, mId);
            return null;
        }
    }


    private static class setBusyAsyncTask extends AsyncTask<Void,Void,Void> {

        private LocalDao myAsyncLocalDao;
        private Busy mBusy;
        public setBusyAsyncTask(LocalDao dao, Busy busy) {
            myAsyncLocalDao = dao;
            mBusy = busy;

        }
        @Override
        protected Void doInBackground(final Void... params) {
            myAsyncLocalDao.setBusy(mBusy);
            return null;
        }
    }
    private static class updateLongitudeAsyncTask extends AsyncTask<Void,Void,Void> {

        private LocalDao myAsyncLocalDao;
        private String payload;
        private int mId;

        public updateLongitudeAsyncTask(LocalDao dao, String editedText, int id) {
            myAsyncLocalDao = dao;
            payload = editedText;
            mId = id;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            myAsyncLocalDao.updateLongitude(payload, mId);
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<Local,Void,Void> {

        private LocalDao myAsyncLocalDao;

        public insertAsyncTask(LocalDao dao){
            myAsyncLocalDao = dao;
        }

        @Override
        protected Void doInBackground(final Local... params){
            myAsyncLocalDao.insert(params[0]);
            return null;
        }
    }
    private static class deleteAsyncTask extends AsyncTask<Integer,Void,Void> {

        private LocalDao myAsyncLocalDao;

        public deleteAsyncTask(LocalDao dao){
            myAsyncLocalDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params){
            myAsyncLocalDao.delete(params[0]);
            return null;
        }
    }
    private static class deleteAllAsyncTask extends AsyncTask<Void,Void,Void> {

        private LocalDao myAsyncLocalDao;

        public deleteAllAsyncTask(LocalDao dao){
            myAsyncLocalDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params){
            myAsyncLocalDao.deleteAll();
            return null;
        }
    }
    private static class insertAllAsyncTask extends AsyncTask<Void,Void,Void> {

        private LocalDao myAsyncLocalDao;
        private List<Local> mLocalList;

        public insertAllAsyncTask(LocalDao dao, List<Local> localList){
            myAsyncLocalDao = dao;
            mLocalList = localList;
            Log.d(TAG,"potentially slow operation");
        }

        @Override
        protected Void doInBackground(Void... params){
            for(Local local: mLocalList) {
                myAsyncLocalDao.insert(local);
            }
            return null;
        }
    }




}
