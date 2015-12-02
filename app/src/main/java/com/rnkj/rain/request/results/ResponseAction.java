package com.rnkj.rain.request.results;

import com.rnkj.rain.bean.IEntity;

import java.util.List;

/**
 * Created by francis.
 */
public abstract class ResponseAction implements IResponseAction<IEntity> {

    @Override
    public void onSuccess(IEntity entity) {
        anyWay();
    }

    @Override
    public void onSuccess(List<IEntity> entities) {
        anyWay();
    }

    @Override
    public void onFailure(IEntity entity) {
        anyWay();
    }

    private void anyWay() {
    }
}
