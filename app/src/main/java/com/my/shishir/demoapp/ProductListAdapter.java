package com.my.shishir.demoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.shishir.demoapp.model.ProductData;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.my.shishir.demoapp.utility.Utility.imageRounder;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private final List<ProductData> productDataList;
    private Picasso picasso;
    private final ProductListContract.MainPresenter mainPresenter;

    ProductListAdapter(List<ProductData> mainData, MainPresenterImpl mainPresenter) {
        this.productDataList = mainData;
        this.mainPresenter = mainPresenter;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        ButterKnife.bind(this, view);

        // This is here because we do not want to use context from views or activity instead we can
        // get it from the view
        if (picasso == null) {
            picasso = new Picasso.Builder(parent.getContext()).build();
        }
        return new ProductViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        final Context context = holder.productLayout.getContext();
        final int adapterPosition = holder.getAdapterPosition();

        final String description = productDataList.get(adapterPosition).getDescription();
        final String address = productDataList.get(adapterPosition).getLocation().getAddress();

        Object imageUrl = productDataList.get(adapterPosition).getImageUrl();
        imageUrl = imageUrl != null ? imageUrl : "";

        holder.descriptionTextView.setText(!TextUtils.isEmpty(description) ? description
                : context.getString(R.string.no_description));
        holder.addressTextView.setText(!TextUtils.isEmpty(address) ? address
                : context.getString(R.string.no_address));
        holder.productImage.setImageDrawable(context
                .getResources().getDrawable(R.drawable.ic_launcher_background, context.getTheme()));

        holder.productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onClick(productDataList.get(adapterPosition));
            }
        });

        // This is the default visibility to avoid flickering of images or may be miss positioning
        // of them while loading or scrolling.
        holder.productImage.setVisibility(View.GONE);

        final Object finalImageUrl = imageUrl;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty((String) finalImageUrl)) {
                    holder.productImage.setVisibility(View.GONE);
                } else {
                    loadImage((String) finalImageUrl, holder.productImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    // Picasso load images only when the view is visible, So it can be like
    // lazy loading or loading when required
    private void loadImage(String finalImageUrl, final ImageView imageView) {
        picasso.load(finalImageUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setVisibility(View.VISIBLE);
                        imageRounder(((BitmapDrawable) imageView.getDrawable()).getBitmap(),
                                imageView.getResources(),
                                new ProcessedBitmapListener() {
                                    @Override
                                    public void onProcessDone(RoundedBitmapDrawable roundedBitmapDrawable) {
                                        imageView.setImageDrawable(roundedBitmapDrawable);
                                    }
                                });
                    }

                    @Override
                    public void onError(Exception e) {
                        imageView.setVisibility(View.GONE);
                    }
                });
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_product_item)
        CardView productLayout;

        @BindView(R.id.text_description)
        TextView descriptionTextView;

        @BindView(R.id.text_address)
        TextView addressTextView;

        @BindView(R.id.image_product)
        ImageView productImage;

        ProductViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface ProcessedBitmapListener {
        void onProcessDone(RoundedBitmapDrawable roundedBitmapDrawable);
    }

}