package com.imobile3.groovypayments.ui.main;

import androidx.lifecycle.ViewModel;

import com.imobile3.groovypayments.MainApplication;
import com.imobile3.groovypayments.concurrent.GroovyExecutors;
import com.imobile3.groovypayments.data.CartDataSource;
import com.imobile3.groovypayments.data.CartRepository;
import com.imobile3.groovypayments.data.ProductDataSource;
import com.imobile3.groovypayments.data.ProductRepository;
import com.imobile3.groovypayments.data.model.Cart;
import com.imobile3.groovypayments.data.model.Product;
import com.imobile3.groovypayments.network.WebServiceManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Order;
import com.stripe.model.OrderCollection;
import com.stripe.model.OrderItem;
import com.stripe.model.ProductCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public void getCarts() {
        GroovyExecutors.getInstance().getNetwork().execute(() -> {
            WebServiceManager.getInstance().init(MainApplication.getInstance().getWebServiceConfig());

            try {

                Map<String, Object> params = new HashMap<>();

                params.put("limit", 20);

                OrderCollection orders = com.stripe.model.Order.list(params);

                CartRepository repository = CartRepository.getInstance(new CartDataSource());

                repository.getDataSource().deleteAllCard();

                for ( com.stripe.model.Order order : orders.getData()) {

                    Cart newOrder = new Cart();

                    List<OrderItem> items = order.getItems();

                    if(items.size() > 0) {
                        newOrder.setTaxTotal(items.get(0).getAmount());
                    } else {
                        newOrder.setTaxTotal(0);
                    }

                    newOrder.setDateCreated(new Date(order.getCreated() * 1000));

                    repository.getDataSource().addCart(newOrder);
                }

            } catch (StripeException e) {
                e.printStackTrace();
            }
        });
    }
}
