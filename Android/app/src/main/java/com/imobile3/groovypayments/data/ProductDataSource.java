package com.imobile3.groovypayments.data;

import com.imobile3.groovypayments.data.entities.ProductEntity;
import com.imobile3.groovypayments.data.model.Product;

import androidx.annotation.WorkerThread;

import java.util.List;

public class ProductDataSource {

    public ProductDataSource() {
    }

    @WorkerThread
    public Result<List<Product>> loadProducts() {
        List<Product> results =
                DatabaseHelper.getInstance().getDatabase().getProductDao().getProducts();
        return new Result.Success<>(results);
    }

    @WorkerThread
    public void addProduct(Product product) {
        DatabaseHelper.getInstance().getDatabase().getProductDao().insertProducts((ProductEntity) product);
    }

    @WorkerThread
    public void deleteAll() {
        DatabaseHelper.getInstance().getDatabase().getProductDao().deleteAllProducts();
    }
}
