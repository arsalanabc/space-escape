package com.a.spaceescape2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 


public class webpage extends Activity  {
	 /** Called when the activity is first created. */
	private WebView webView;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
 
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
	  webView.loadUrl("https://csclub.uwaterloo.ca/~a42ahmed/mobile/first.php");
	  
	  if(webView.getUrl() == "https://csclub.uwaterloo.ca/~a42ahmed/mobile/home.php")
	  	{
		  webView.loadUrl("https://csclub.uwaterloo.ca/~a42ahmed/mobile/home.php");
	  	}
	  
	  
	 
	}
    
	 
  
}



