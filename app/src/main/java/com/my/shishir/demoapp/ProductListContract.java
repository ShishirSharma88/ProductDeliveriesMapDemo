package com.my.shishir.demoapp;

import com.my.shishir.demoapp.api.RequestManager;
import com.my.shishir.demoapp.model.ProductData;

import java.util.List;

interface ProductListContract {
    interface MainView {
        void showProgress();
        void hideProgress();
        void setAdapter(List<ProductData> mainDataList);
        void showMessage(int message);
        void launchWeb(ProductData productData);
        void noData(boolean shouldShow);
    }

    interface MainPresenter extends RequestManager.ResponseListener<List<ProductData>> {
        void onClick(ProductData productData);

    }
}
