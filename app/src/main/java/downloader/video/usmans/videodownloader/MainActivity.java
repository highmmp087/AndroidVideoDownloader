package downloader.video.usmans.videodownloader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.AdSettings;
//import com.facebook.ads.AdSize;
//import com.facebook.ads.AdView;
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

import downloader.video.usmans.videodownloader.Tasks.GeneratingDownloadLinks;
import downloader.video.usmans.videodownloader.adapter.SitesAdapter;
import downloader.video.usmans.videodownloader.constants.iConstants;
import downloader.video.usmans.videodownloader.utils.AdBlock;
import downloader.video.usmans.videodownloader.helper.BottomNavigationViewHelper;
import downloader.video.usmans.videodownloader.utils.IOUtils;
import downloader.video.usmans.videodownloader.utils.iUtils;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;


public class MainActivity extends AppCompatActivity
        implements iConstants , NavigationView.OnNavigationItemSelectedListener  {
     private String postUrl = "http://www.google.com";
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    private EditText SearchText;
    final Activity activity = this;
    FloatingActionButton fab;
    private boolean AdblockEnabled = true;
    private BottomNavigationView bottomNavigationView;
    private CountDownTimer timer;
    private Boolean SearchHasFocus = false;
    private View bottomSheet;
    private View webViewCon;
    SharedPreferences sharedPrefs;
    String URL;
    GridView HomeView;
//    private AdView adView;
//    private InterstitialAd interstitialAd;
    private InterstitialAd mInterstitialAd;

    PublisherInterstitialAd mPublisherInterstitialAd;
    public static boolean mAdIsLoading = false;

    public static final int REQUEST_PERMISSION_CODE = 1001;
    public static final String REQUEST_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppLovinSdk.initializeSdk(MainActivity.this);

//        MobileAds.initialize(this, ADMOB_APP_ID);
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_ID);
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded(){
//                if (!mAdIsLoading) {
//                    mInterstitialAd.show();
//                    if(AppLovinInterstitialAd.isAdReadyToDisplay(MainActivity.this)){
//            // An ad is available to display.  It's safe to call show.
//                     AppLovinInterstitialAd.show(MainActivity.this);
//                    Log.d("APPLOVIN ADS Ready=====>","YES");
//                }
//                else{
//            // No ad is available to display.  Perform failover logic...
//                Log.d("Not Ready=====>","YES");
//
//                }
//                    mAdIsLoading = true;
//                }
//
//            }
//
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SearchText=(EditText) findViewById(R.id.SearchText);
        SearchText.setSelectAllOnFocus(true);
         bottomSheet = findViewById(R.id.design_bottom_sheet);
           HomeView = (GridView) findViewById(R.id.HomePage);
        webViewCon=(View)findViewById(R.id.webViewCon);
//        bottomNavigationView;
         bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        OnPrepareBottomNav(bottomNavigationView.getMenu());
        HomeView.setAdapter(new SitesAdapter(this));
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
//        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
//        interstitialAd = new InterstitialAd(this, FACEBOOK_INTERSTITIAL_ID);
//
//        adView = new AdView(this, FACEBOOK_BANNER_ID, AdSize.BANNER_320_50);
//
//        adViewContainer.addView(adView);
//        AdSettings.addTestDevice("d07d57d86bba7e633a9f017d267f2949");
//        adView.loadAd();
        /**
         * On Click event for Single Gridview Item
         * */
        HomeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
        webView.loadUrl(HomePageURI[position]);
        SwithcView(true);
            }
        });
        AdblockEnabled=sharedPrefs.getBoolean("ADBLOCK",true);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_home:
                                SwithcView(false);
                                break;
                            case R.id.action_bookmark:
                                iUtils.bookmarkUrl(MainActivity.this,webView.getUrl());
                                if(iUtils.isBookmarked(MainActivity.this,webView.getUrl())){

                                    item.setIcon(R.drawable.ic_bookmark_grey_800_24dp);
                                    item.getIcon().setAlpha(255);
                                    iUtils.ShowToast(MainActivity.this,"Bookmarked");
                                }else{
                                    item.setIcon(R.drawable.ic_bookmark_border_grey_800_24dp);
                                    item.getIcon().setAlpha(130);

                                }
                                break;
                            case R.id.action_back:
                                back();
                                break;
                            case R.id.action_forward:
                                forward();

                                break;
                        }
                        return true;
                    }
                });


        //WebView
        initWebView();

     //   webView.loadUrl(postUrl);


        SearchText.setText(webView.getUrl());
        SearchText.setSelected(false);
        isNeedGrantPermission();
        //Floating Button :)

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneratingDownloadLinks.Start(MainActivity.this,webView.getUrl(),webView.getTitle());
//                loadInterstitialAd();
                //        // For interstitials
//        if(AppLovinInterstitialAd.isAdReadyToDisplay(MainActivity.this)){
//            // An ad is available to display.  It's safe to call show.
//            AppLovinInterstitialAd.show(MainActivity.this);
//            Log.d("APPLOVIN ADS Ready=====>","YES");
//        }
//        else{
//            // No ad is available to display.  Perform failover logic...
//            Log.d("Not Ready=====>","YES");
//
//        }





            }
        });
        if(intent.hasExtra("URL")){
            URL = extras.getString("URL");

            if(!URL.equals("")){
                LoadFromIntent(URL);
            }}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Search Text
        SearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ( (i == EditorInfo.IME_ACTION_DONE) || ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN ))){
                    String url = SearchText.getText().toString();
                      //  iUtils.ShowToast(MainActivity.this,url);
                    SearchText.clearFocus();
                    SwithcView(true);
                    InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
                    if(iUtils.checkURL(url)){
                        if(!url.contains("http://") || !url.contains("https://")) {
                            if(url.contains("http//") || url.contains("https//") ){
                                url=url.replace("http//","http://");
                                url=url.replace("https//","https://");
                            }else {
                                url = "http://" + url;
                            }
                        }
                        webView.loadUrl(url);
                        SearchText.setText(webView.getUrl());
                    }else{
                        String Searchurl = String.format(SEARCH_ENGINE,url);
                        webView.loadUrl(Searchurl);
                        SearchText.setText(webView.getUrl());
                    }
                    return true;
                }
                return false;
            }


        });

        timer = new CountDownTimer(2000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try{
                    UpdateUi();
                  //  iUtils.ShowToast(MainActivity.this,"working");

                }catch(Exception e){
                    Log.e("TimerError", "Error: " + e.toString());
                }
            }
        }.start();

        SearchText.setOnFocusChangeListener(focusListener);
    }
    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                SearchHasFocus = true;
            } else {
                SearchHasFocus = false;
            }
        }
    };
    public void UpdateUi(){
        String WebUrl = webView.getUrl();
        String SearchUrl = SearchText.getText().toString();

if(!SearchHasFocus) {
    if (!WebUrl.equals(SearchUrl)) {

        SearchText.setText(WebUrl);

    }
    //iUtils.ShowToast(MainActivity.this,WebUrl);
//    DownloadHandle(false, WebUrl);
//    for (int i = 0; i < SITES_URL.length; i++) {
//        if (WebUrl.contains(SITES_URL[i])) {
//            DownloadHandle(true, WebUrl);
//        }
//    }


    OnPrepareBottomNav(bottomNavigationView.getMenu());
}
        timer.start();
    }

    private  void  LoadFromIntent(String url){
        SwithcView(true);
        webView.loadUrl(url);
    }
    private void SwithcView(Boolean show){

        if(show){
            webViewCon.setVisibility(View.VISIBLE);
            HomeView.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else{
            webViewCon.setVisibility(View.GONE);
            HomeView.setVisibility(View.VISIBLE);
            webView.stopLoading();
            webView.loadUrl("about:blank");
            SearchText.setText("");
            fab.setVisibility(View.GONE);
        }


    }
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                OnPrepareBottomNav(bottomNavigationView.getMenu());

            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(url.startsWith("intent://")){
                    try {
                        Intent furl = Intent.parseUri(url, 1);
                        if (getPackageManager().getLaunchIntentForPackage(furl.getPackage()) != null) {
                            startActivity(furl);
                            return true;
                        }
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse("market://details?id=" + furl.getPackage()));
                        startActivity(intent);
                        return true;
                    } catch (URISyntaxException e) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e.printStackTrace();
                    }

                }else if (url.startsWith("market://")) {
                    try {
                        Intent r1 = Intent.parseUri(url, 1);
                        if (r1 == null) {
                            return true;
                        }
                        startActivity(r1);
                        return true;
                    } catch (Throwable e22) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e22.printStackTrace();
                        return true;
                    }
                }else{
                SearchText.setText(url);
                webView.loadUrl(url);}
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url=request.getUrl().toString();
                if(url.startsWith("intent://")){
                    try {
                        Intent furl = Intent.parseUri(url, 1);
                        if (getPackageManager().getLaunchIntentForPackage(furl.getPackage()) != null) {
                            startActivity(furl);
                            return true;
                        }
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse("market://details?id=" + furl.getPackage()));
                        startActivity(intent);
                        return true;
                    } catch (URISyntaxException e) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e.printStackTrace();
                    }

                }else if (url.startsWith("market://")) {
                    try {
                        Intent r1 = Intent.parseUri(url, 1);
                        if (r1 == null) {
                            return true;
                        }
                        startActivity(r1);
                        return true;
                    } catch (Throwable e22) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e22.printStackTrace();
                        return true;
                    }
                }else{
                    SearchText.setText(url);
                    webView.loadUrl(url);}
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                  //  view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");

                OnPrepareBottomNav(bottomNavigationView.getMenu());


            }

            @Deprecated
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {



                if (AdblockEnabled && new  AdBlock(MainActivity.this).isAd(url)) {
                    return new WebResourceResponse(
                            "text/plain",
                            "UTF-8",
                            new ByteArrayInputStream("".getBytes())
                    );
                }

                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (AdblockEnabled &&  new  AdBlock(MainActivity.this).isAd(request.getUrl().toString())) {
                        return new WebResourceResponse(
                                "text/plain",
                                "UTF-8",
                                new ByteArrayInputStream("".getBytes())
                        );
                    }
                }

                return super.shouldInterceptRequest(view, request);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
                OnPrepareBottomNav(bottomNavigationView.getMenu());
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(),
                "android");
        webView.setWebChromeClient(new WebChromeClient() {});

        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void onUrlChange(String url) {

        }
    }
//    private void loadInterstitialAd() {
//        interstitialAd.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                // Interstitial displayed callback
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                // Interstitial dismissed callback
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Show the ad when it's done loading.
//                interstitialAd.show();
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        });
//
//        // For auto play video ads, it's recommended to load the ad
//        // at least 30 seconds before it is shown
//        interstitialAd.loadAd();
//    }



    private  void GetMedia(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter URL to get media ");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Get", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //iUtils.ShowToast(MainActivity.this,input.getText().toString());
                String url = input.getText().toString();
                if(iUtils.checkURL(url)) {
                    GeneratingDownloadLinks.Start(MainActivity.this, input.getText().toString(), "");
                    // For interstitials


                }else{
                    iUtils.ShowToast(MainActivity.this,URL_NOT_SUPPORTED);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(webView.canGoBack()){

                back();

            }else{

                webView.loadUrl("");
                webView.stopLoading();
                super.onBackPressed();}
        }
    }
    public  void DownloadHandle(boolean show , final String postUrl){

        if(show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.VISIBLE);
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.GONE);
                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public boolean OnPrepareBottomNav(Menu menu){
      //  Log.e("ERROR BOOKMARK",webView.getUrl());
   if (!iUtils.isBookmarked(this,SearchText.getText().toString())) {
       menu.getItem(1).setIcon(R.drawable.ic_bookmark_border_grey_800_24dp);
       menu.getItem(1).getIcon().setAlpha(130);
   } else {
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_grey_800_24dp);
       menu.getItem(1).getIcon().setAlpha(255);

   }

        if (!webView.canGoBack()) {
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        } else {
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }

        if (!webView.canGoForward()) {
            menu.getItem(3).setEnabled(false);
            menu.getItem(3).getIcon().setAlpha(130);
        } else {
            menu.getItem(3).setEnabled(true);
            menu.getItem(3).getIcon().setAlpha(255);
        }



        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

         return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_downloads) {

         startActivity(new Intent("android.intent.action.VIEW_DOWNLOADS"));
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void back() {
        if (webView.canGoBack()) {
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();

            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();

            webView.goBack();
            SearchText.setText(historyUrl);

        }
    }

    private void forward() {
        if (webView.canGoForward()) {
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()+1).getUrl();
            webView.goForward();
            SearchText.setText(webView.getUrl());

        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }

    private boolean isNeedGrantPermission() {
        try {
            if (IOUtils.hasMarsallow()) {
                if (ContextCompat.checkSelfPermission(this, REQUEST_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUEST_PERMISSION)) {
                        final String msg = String.format(getString(R.string.format_request_permision), getString(R.string.app_name));

                        AlertDialog.Builder localBuilder = new AlertDialog.Builder(MainActivity.this);
                        localBuilder.setTitle("Permission Required!");
                        localBuilder
                                .setMessage(msg).setNeutralButton("Grant",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface paramAnonymousDialogInterface,
                                            int paramAnonymousInt) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{REQUEST_PERMISSION}, REQUEST_PERMISSION_CODE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface paramAnonymousDialogInterface,
                                            int paramAnonymousInt) {


                                        paramAnonymousDialogInterface.dismiss();
                                        finish();
                                    }
                                });
                        localBuilder.show();

                    }
                    else {
                        ActivityCompat.requestPermissions(this, new String[]{REQUEST_PERMISSION}, REQUEST_PERMISSION_CODE);
                    }
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                else {
                    iUtils.ShowToast(MainActivity.this,getString(R.string.info_permission_denied));

                    finish();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            iUtils.ShowToast(MainActivity.this,getString(R.string.info_permission_denied));
            finish();
        }

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_downloads) {
            startActivity(new Intent("android.intent.action.VIEW_DOWNLOADS"));
        } else if (id == R.id.nav_bookmarks) {
            Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_get_media) {
            GetMedia();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out best video downloader at: https://play.google.com/store/apps/details?id="+MainActivity.this.getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",EMAIL_ID, null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }else if (id == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        AdblockEnabled=sharedPrefs.getBoolean("ADBLOCK",true);
        webView.onResume();
        webView.resumeTimers();


    }
    @Override
    public void onPause(){
        super.onPause();
        // put your code here...
        webView.onPause();
        webView.pauseTimers();

    }
    @Override
    public void onStop(){
        super.onStop();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView=null;
     }
}
