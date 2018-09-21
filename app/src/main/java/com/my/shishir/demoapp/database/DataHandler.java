package com.my.shishir.demoapp.database;

import android.os.AsyncTask;
import android.util.Log;

import com.my.shishir.demoapp.AppContext;
import com.my.shishir.demoapp.listener.TaskListener;
import com.my.shishir.demoapp.model.ProductData;

import java.util.List;

public class DataHandler extends AsyncTask<Void, Void, Boolean> {

    private final List<ProductData> productDataList;
    private final TaskListener<List<ProductData>> taskListener;
    private List<ProductData> allProductList;
    private boolean isSuccessful = false;
    private final int errorType;

    public DataHandler(List<ProductData> productDataList,
                       TaskListener<List<ProductData>> taskListener, int errorType) {
        this.productDataList = productDataList;
        this.taskListener = taskListener;
        this.errorType = errorType;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            SqliteDatabase sqliteDatabase = new SqliteDatabase(AppContext.getContext());
            if (productDataList != null && !productDataList.isEmpty()) {
                for (ProductData productData : productDataList) {
                    sqliteDatabase.addProduct(productData);
                }

                isSuccessful = true;
            }
            allProductList = sqliteDatabase.getAllData();
        } catch (Exception e) {
            Log.e("DataHandler : doInbackground-", e.toString());
        }

        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (aBoolean) {
            taskListener.onSuccess(allProductList);
        } else {
            taskListener.onFailure(allProductList, errorType);
        }
    }
}
