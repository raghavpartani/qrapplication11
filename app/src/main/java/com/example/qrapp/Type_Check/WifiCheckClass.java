package com.example.qrapp.Type_Check;

import com.google.zxing.client.result.WifiParsedResult;

public class WifiCheckClass {

    private static final String TAG ="WifiCheckClass";

    private WifiParsedResult mWifiParsedResult ;

    public WifiCheckClass(Object object){
        this.mWifiParsedResult = (WifiParsedResult)object;
    }

    public String SetWifiSSID(){
        return mWifiParsedResult.getSsid() ;
    }
    public String SetWifiPassword(){
        return mWifiParsedResult.getPassword();
    }

}
