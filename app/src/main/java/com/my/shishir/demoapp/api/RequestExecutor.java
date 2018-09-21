package com.my.shishir.demoapp.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.my.shishir.demoapp.database.DataHandler;
import com.my.shishir.demoapp.listener.TaskListener;
import com.my.shishir.demoapp.model.ProductData;
import com.my.shishir.demoapp.utility.Utility;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class is responsible for server communication
 */
class RequestExecutor {

    private final String TAG = getClass().getName();
    private final TaskListener<List<ProductData>> taskListener;
    private final Retrofit retrofit;
    private final int limit;
    private final int offset;
    private final boolean isNetworkAvailable;

    RequestExecutor(@NonNull TaskListener<List<ProductData>> taskListener,
                    @NonNull Retrofit retrofit, int limit, int offset, boolean isNetworkAvailable) {
        this.taskListener = taskListener;
        this.retrofit = retrofit;
        this.limit = limit;
        this.offset = offset;
        this.isNetworkAvailable = isNetworkAvailable;
    }

    void execute() {
        if (!isNetworkAvailable) {
            new DataHandler(null, taskListener, Utility.NETWORK_ERROR_CODE)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        RetroInterface retroInterface = retrofit.create(RetroInterface.class);

        Observable<List<ProductData>> observable = retroInterface.getData(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<List<ProductData>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError : " + e.toString());
                new DataHandler(null, taskListener, Utility.OTHER_ERROR_CODE)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onNext(List<ProductData> datumList) {
                Log.i(TAG, "onNext : ");
                new DataHandler(datumList, taskListener,
                        (datumList.isEmpty() ? Utility.ALL_DATA_LOADED : Utility.NO_ERROR_CODE))
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }
}
