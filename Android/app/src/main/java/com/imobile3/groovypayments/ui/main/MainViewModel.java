package com.imobile3.groovypayments.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imobile3.groovypayments.MainApplication;
import com.imobile3.groovypayments.concurrent.GroovyExecutors;
import com.imobile3.groovypayments.data.ProductDataSource;
import com.imobile3.groovypayments.data.ProductRepository;
import com.imobile3.groovypayments.data.model.Product;
import com.imobile3.groovypayments.network.WebServiceManager;
import com.stripe.exception.StripeException;
import com.stripe.model.ProductCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewModel extends ViewModel {

    public void getProducts() {
        GroovyExecutors.getInstance().getNetwork().execute(() -> {
            try {
                WebServiceManager.getInstance().init(MainApplication.getInstance().getWebServiceConfig());

                Map<String, Object> params = new HashMap<>();

                params.put("limit", 20);

                ProductCollection products = com.stripe.model.Product.list(params);

                ProductRepository repository = ProductRepository.getInstance(new ProductDataSource());

                for ( com.stripe.model.Product product : products.getData()) {

                    Product newProduct = new Product();

                    newProduct.setName(product.getName());
                    newProduct.setDesc(product.getDescription());

                    if(product.getImages() != null && product.getImages().size() > 0) {
                        newProduct.setImageName(product.getImages().get(0));
                    }

                    newProduct.setProductId(product.getId());

                    repository.getDataSource().addProduct(newProduct);
                }

            } catch (StripeException e) {
                e.printStackTrace();
            }
        });
    }
}
