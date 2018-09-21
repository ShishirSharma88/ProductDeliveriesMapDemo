package com.my.shishir.demoapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.my.shishir.demoapp.api.RequestManager;
import com.my.shishir.demoapp.model.ProductData;
import com.my.shishir.demoapp.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainPresenterImpl implements ProductListContract.MainPresenter {

    private static final int LIMIT = 10;
    private static final int THRESHOLD = 10;

    private final ProductListContract.MainView mainView;
    private final RequestManager requestManager;

    private boolean isLoading;
    private int offset;
    private final List<ProductData> productDataList;
    private boolean isAllDataLoaded;

    MainPresenterImpl(ProductListContract.MainView mainView) {
        this.mainView = mainView;
        productDataList = new ArrayList<>();

        RequestManager.ResponseListener<List<ProductData>> responseListener = this;
        requestManager = new RequestManager(responseListener);

    }

    private void startProcess(int offset) {
        requestManager.callApi(LIMIT, offset);
    }

    public void initiate() {
        mainView.showProgress();
        startProcess(offset);
    }

    public void onRefresh() {
        startProcess(offset);
    }

    @Override
    public void onClick(ProductData productData) {
        mainView.launchWeb(productData);
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isAllDataLoaded
                && (visibleItemCount + pastVisibleItems + THRESHOLD >= totalItemCount)
                && !isLoading) {
            isLoading = true;
            mainView.showProgress();

            startProcess(offset);
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        // Not in use as of now
    }

    @Override
    public void onResponse(@NonNull List<ProductData> productDataList, boolean success, int errorType) {
        mainView.hideProgress();

        if (productDataList.isEmpty()) {
            mainView.noData(true);
        } else {
            mainView.noData(false);

            this.productDataList.clear();
            this.productDataList.addAll(productDataList);

            mainView.setAdapter(this.productDataList);
        }

        isLoading = false;

        if (success) {
            if (offset <= productDataList.size()) {
                offset = productDataList.size() + 1;
            }

            return;
        }

        switch (errorType) {
            case Utility.ALL_DATA_LOADED:
                isAllDataLoaded = true;
                break;
            case Utility.OTHER_ERROR_CODE:
                mainView.showMessage(R.string.download_failed);
                break;
            case Utility.NETWORK_ERROR_CODE:
                mainView.showMessage(R.string.no_internet);
                break;
        }
    }
}
