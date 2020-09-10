package com.example.qrapp.Type_Check;

import com.google.zxing.client.result.EmailAddressParsedResult;

public class EmailCheckClass {
    private static final String TAG ="EmailCheckClass";

    private EmailAddressParsedResult mEmailAddressParsedResult ;

    public EmailCheckClass(Object object){
        this.mEmailAddressParsedResult = (EmailAddressParsedResult)object;
    }

     public String SetEmailTo(){
       return mEmailAddressParsedResult.getTos()[0] ;
    }
    public String SetEmailSubject(){
        return mEmailAddressParsedResult.getSubject();
    }

    public String SetEmailBody(){
        return mEmailAddressParsedResult.getBody();
    }
}

