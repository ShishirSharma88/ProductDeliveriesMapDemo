package com.my.shishir.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.my.shishir.demoapp.listener.ScrollListenerForPagination;
import com.my.shishir.demoapp.model.ProductData;
import com.my.shishir.demoapp.utility.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements
        ProductListContract.MainView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.progress_download_content)
    ProgressBar progressBar;

    @BindView(R.id.list_product)
    RecyclerView listProducts;

    @BindView(R.id.swipe_layout_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.text_no_data)
    TextView textNoData;

    private Unbinder unbinder;
    private MainPresenterImpl mainPresenterImpl;
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mainPresenterImpl = new MainPresenterImpl(this);
        mainPresenterImpl.initiate();

        listProducts.setLayoutManager(layoutManager);
        listProducts.addOnScrollListener(new ScrollListenerForPagination(mainPresenterImpl));

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setAdapter(List<ProductData> productDataList) {
        if (productListAdapter == null) {
            productListAdapter = new ProductListAdapter(productDataList, mainPresenterImpl);
            listProducts.setAdapter(productListAdapter);
            return;
        }

        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(this,
                getResources().getString(message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launchWeb(ProductData productData) {
        startActivity(new Intent(this,
                MapActivity.class).putExtra(Utility.KEY_PRODUCT_DETAIL, productData));
    }

    @Override
    public void noData(boolean shouldShow) {
        textNoData.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mainPresenterImpl.onRefresh();
    }
}
