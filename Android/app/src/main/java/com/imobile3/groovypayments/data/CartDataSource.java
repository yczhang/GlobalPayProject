package com.imobile3.groovypayments.data;

import com.imobile3.groovypayments.data.model.Cart;

import androidx.annotation.WorkerThread;

import java.util.List;

public class CartDataSource {

    public CartDataSource() {
    }

    @WorkerThread
    public Result<List<Cart>> loadCarts() {
        List<Cart> results =
                DatabaseHelper.getInstance().getDatabase().getCartDao().getCarts();
        return new Result.Success<>(results);
    }

    @WorkerThread
    public void addCart(Cart cart) {
        DatabaseHelper.getInstance().getDatabase().getCartDao().insertCarts(cart);
    }

    @WorkerThread
    public void deleteAllCard() {
        DatabaseHelper.getInstance().getDatabase().getCartDao().deleteAll();
    }
}
