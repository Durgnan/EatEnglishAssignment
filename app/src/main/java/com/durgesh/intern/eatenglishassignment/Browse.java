package com.durgesh.intern.eatenglishassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class Browse extends AppCompatActivity {
    Context context;
    WebView myWebView;
    Toolbar toolbar;
    String id,uname;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        context = getApplicationContext();
        myWebView = findViewById(R.id.webView);
        staticated.context=this.context;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        myWebView.setScrollbarFadingEnabled(false);
        myWebView.setWebViewClient(mWebViewClient);
        myWebView.addJavascriptInterface(new MyJavaScriptInterface() ,"android");
//        myWebView.setWebViewClient(new WebViewClient() {
//
//        });
        staticated.webView=myWebView;
        myWebView.loadUrl(getString(R.string.site_name));

        Bundle bundle = getIntent().getExtras();
         id = bundle.getString("id");
        uname = bundle.getString("user");
        staticated.id=id;
        staticated.uname=uname;






    }
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");


        }
        @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("Do.php")||url.contains("ReadNotes.php"))
            {
                myWebView.setVisibility(View.INVISIBLE);
                TextView t5 = findViewById(R.id.text5);
                t5.setVisibility(View.VISIBLE);

            }


            return false;
            }
    };




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id1=item.getItemId();
        if (id1 == R.id.userdata)
        {
            Intent i = new Intent(Browse.this,Login.class).putExtra("id",id).putExtra("user",uname);
            startActivity(i);
        }
        if (id1 == R.id.logout)
        {
            Toast.makeText(Browse.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();

            Intent i = new Intent(Browse.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id1 == R.id.settings)
        {
            Intent i = new Intent(Browse.this,Settings.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        myWebView.setVisibility(View.VISIBLE);
        myWebView.goBack();
        TextView t5 = findViewById(R.id.text5);
        t5.setVisibility(View.INVISIBLE);

        super.onResume();
    }

}
class MyJavaScriptInterface {
    JSONArray jsonArray=null;

    @JavascriptInterface
    public void onUrlChange(String url) {
        Log.e("hydrated", "onUrlChange" + url);
        if (url.contains("Do.php") || url.contains("ReadNotes.php"))
        {
            try
            {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line);
                    }
                    String result = sBuilder.toString();
                result=result.replaceAll("\uFEFF","" );
                result=result.replaceAll("\\s+"," " );



                    try {
                        jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                           // Log.e("Message",jsonObj.toString());
                        }
                    }catch (JSONException j){
                        Toast.makeText(staticated.context,j.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e("Error",j.toString());
                    }
                    Log.e("Message",jsonArray.length()+"");
                    staticated.jsonArray1=jsonArray;
                   /* for (int i=0;i<jsonArray.length();i++)
                    {
                        Log.e("Question",jsonArray.getJSONObject(i).getString("q2"));
                        Log.e("Answer",jsonArray.getJSONObject(i).getString("a2"));
                    }*/

                    if (url.contains("Do.php")) {
                       // staticated.webView.loadUrl("about:blank");
                        Intent i  = new Intent(staticated.context,ShowContent.class).putExtra("url","DO").putExtra("json",jsonArray.toString()).putExtra("id",staticated.id).putExtra("user",staticated.uname);
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            staticated.context.startActivity(i);


                    }
                if (url.contains("ReadNotes.php"))
                {
                   // staticated.webView.loadUrl("about:blank");
                    Intent i  = new Intent(staticated.context,ShowContent.class).putExtra("url","READ").putExtra("json",jsonArray.toString()).putExtra("id",staticated.id).putExtra("user",staticated.uname);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    staticated.context.startActivity(i);
                }




            }catch (Exception e) {
                Toast.makeText(staticated.context,e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Error",e.toString());
            }
        }
        return ;
    }
    @JavascriptInterface
    public void showHTML(String html) {
       // Log.e("hydrated",html);
    }
}
class staticated{
    static Context context;
    static WebView webView;
    static String id,uname;
    static JSONArray jsonArray1;
}

