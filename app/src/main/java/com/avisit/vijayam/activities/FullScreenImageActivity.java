package com.avisit.vijayam.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import com.avisit.vijayam.R;

public class FullScreenImageActivity extends ActionBarActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_full_screen_image);
    String str = getIntent().getStringExtra("imageName");
    WebView localWebView = (WebView)findViewById(R.id.webview);
    localWebView.getSettings().setSupportZoom(true);
    localWebView.getSettings().setBuiltInZoomControls(true);
    localWebView.loadDataWithBaseURL("file:///android_asset/", "<html><head><meta name='viewport' content='target-densitydpi=device-dpi,initial-scale=1,minimum-scale=1,user-scalable=yes'/></head><body><img src=\"" + str + "\"/></body></html>", "text/html", "utf-8", null);
  }
}