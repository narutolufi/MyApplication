package com.rnkj.rain.request.results;

import java.util.List;

/**
 * Created by francis.
 */
public interface IResponseAction<T> {

     void onSuccess(T entity);

     void onSuccess(List<T> entities);

     void onFailure(T entity);
}
