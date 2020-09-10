package com.example.qrapp.Type_Check;

import com.google.zxing.client.result.GeoParsedResult;

public class GeoCheckClass {

    private static final String TAG ="UriCheckClass";
    private GeoParsedResult mGeoParsedResult ;

    public GeoCheckClass(Object object) {
        this.mGeoParsedResult = (GeoParsedResult) object;
    }

    public String SetGeo(){
        return mGeoParsedResult.getGeoURI() ;
    }
}
