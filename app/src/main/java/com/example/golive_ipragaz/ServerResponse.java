package com.example.golive_ipragaz;

import com.google.gson.annotations.SerializedName;

class ServerResponse {
    // variable name should be same as in the json response from php
//    @SerializedName("result")
//    String result;

    @SerializedName("result")
    String result;
    String getMessage() {
        return result;
    }
//    String getSuccess() {
//        return sayac;
//    }
}