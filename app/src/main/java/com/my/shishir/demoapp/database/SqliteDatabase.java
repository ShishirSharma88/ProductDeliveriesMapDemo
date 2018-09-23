package com.my.shishir.demoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.shishir.demoapp.model.Location;
import com.my.shishir.demoapp.model.ProductData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ProductDetails";
    private static final String TABLE_NAME = "Detail";
    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LNG = "longtude";
    private static final String KEY_ADD = "address";

    private static final String[] COLUMNS = {KEY_ID, KEY_DESCRIPTION, KEY_IMAGE_URL,
            KEY_LAT, KEY_LNG, KEY_ADD};

    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_LAT + " DOUBLE,"
                + KEY_LNG + " DOUBLE,"
                + KEY_ADD + " TEXT )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    // This is not in use but here in case you need it in future
    public void deleteProduct(ProductData productData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(productData.getId())});
        db.close();
    }

    // This is not in use but here in case you need it in future
    public ProductData getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        ProductData productData = new ProductData();
        productData.setId(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)));
        productData.setDescription(cursor.getString(1));
        productData.setImageUrl(cursor.getString(2));

        Location location = new Location();
        location.setLat(cursor.getDouble(3));
        location.setLng(cursor.getDouble(4));
        location.setAddress(cursor.getString(5));

        productData.setLocation(location);

        cursor.close();
        db.close();

        return productData;
    }


    public List<ProductData> getAllData() {

        List<ProductData> productDataList = new LinkedList<>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ProductData productData;

        if (cursor.moveToFirst()) {
            do {
                productData = new ProductData();
                productData.setId(Integer.parseInt(cursor.getString(0)));
                productData.setDescription(cursor.getString(1));
                productData.setImageUrl(cursor.getString(2));

                Location location = new Location();
                location.setLat(cursor.getDouble(3));
                location.setLng(cursor.getDouble(4));
                location.setAddress(cursor.getString(5));

                productData.setLocation(location);
                productDataList.add(productData);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return productDataList;
    }

    public void addProduct(ProductData productData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, productData.getId());
            values.put(KEY_DESCRIPTION, productData.getDescription());
            values.put(KEY_IMAGE_URL, productData.getImageUrl());
            values.put(KEY_ADD, productData.getLocation().getAddress());
            values.put(KEY_LAT, productData.getLocation().getLat());
            values.put(KEY_LNG, productData.getLocation().getLng());

            // insert
            db.insertOrThrow(TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("addProduct : error ", e.toString());
        }
    }

    // This is not in use but here in case you need it in future
    public int updateProduct(ProductData productData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, productData.getId());
        values.put(KEY_DESCRIPTION, productData.getDescription());
        values.put(KEY_IMAGE_URL, productData.getImageUrl());
        values.put(KEY_ADD, productData.getLocation().getAddress());
        values.put(KEY_LAT, productData.getLocation().getLat());
        values.put(KEY_LNG, productData.getLocation().getLng());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[]{String.valueOf(productData.getId())});

        db.close();

        return i;
    }
}
