package com.tadpolechain;

/**
 * Created by lsq on 2018/4/10.
 */
public interface TCTCallback {

    public void success();

    public void error(int errCode, String msg);
}
