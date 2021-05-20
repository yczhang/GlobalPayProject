package com.imobile3.groovypayments.ui.orderentry;

import com.imobile3.groovypayments.BuildConfig;
import com.imobile3.groovypayments.MainApplication;
import com.imobile3.groovypayments.concurrent.GroovyExecutors;
import com.imobile3.groovypayments.data.ProductRepository;
import com.imobile3.groovypayments.data.Result;
import com.imobile3.groovypayments.data.model.Product;
import com.imobile3.groovypayments.network.WebServiceManager;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.ProductCollection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ViewModel serves as an async bridge between the View (Activity, Fragment)
 * and our backing data repository (Database).
 */
public class OrderEntryViewModel extends ViewModel {

    private ProductRepository mRepository;

    OrderEntryViewModel(ProductRepository repository) {
        mRepository = repository;
    }

    public LiveData<List<Product>> getProducts() {
        // Caller should observe this object for changes. When the data has finished
        // async loading, the observer can react accordingly.
        final MutableLiveData<List<Product>> observable =
                new MutableLiveData<>(new ArrayList<>());

        GroovyExecutors.getInstance().getDiskIo().execute(() -> {
            Result<List<Product>> result = mRepository.getDataSource().loadProducts();
            if (result instanceof Result.Success) {
                List<Product> resultSet = ((Result.Success<List<Product>>)result).getData();
                observable.postValue(resultSet);
            } else {
                // TODO: Return an error message appropriate for the UI.
                observable.postValue(new ArrayList<>());
            }
        });

        return observable;
    }
}
