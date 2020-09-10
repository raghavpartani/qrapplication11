package com.example.qrapp.Type_Check;

import com.google.zxing.client.result.URIParsedResult;

public class UriCheckClass {
    private static final String TAG ="UriCheckClass";
    private URIParsedResult  mURIParsedResult ;

    public UriCheckClass(Object object) {
        this.mURIParsedResult = (URIParsedResult) object;
    }

    public String SetUri(){
        return mURIParsedResult.getURI() ;
    }
}
