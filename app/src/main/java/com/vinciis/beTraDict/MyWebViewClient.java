package com.vinciis.beTraDict;

//Create a class that extends WebViewClient//

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
//Implement shouldOverrideUrlLoading//
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
return false;
    }
}