package com.mobfox.mfx4demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import com.mobfox.android.MobfoxSDK;
import com.mobfox.android.MobfoxSDK.*;

// added for MoPub
import com.mobfox.android.core.gdpr.GDPRParams;


public class MainActivity extends AppCompatActivity {

    private final Activity self = this;

    private LinearLayout relBanner;

    //creating variables for our layout
    private LinearLayout    linNative;
    private ImageView iconNative, mainNative;
    private TextView titleNative, descNative, ratingNative, sponsoredNative;
    private Button ctaNative;

    private Button          btnBannerSmall;
    private Button          btnBannerLarge;
    private Button          btnBannerVideo;

    private Button          btnInterstitialHtml;
    private Button          btnInterstitialVideo;

    private Button          btnNative;

    //###########################################################################################
    //###########################################################################################
    //#####                                                                                 #####
    //#####   A p p   U I   s t u f f                                                       #####
    //#####                                                                                 #####
    //###########################################################################################
    //###########################################################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        relBanner = (LinearLayout)findViewById(R.id.banner);

        // MobFox SDK init:
        initMobFoxSDK();

        initBannerButtons();

        initInterstitial();

        initNative();

        //startMobFoxLargeBanner();
        //startMobFoxHtmlInterstitial();
        //startMobFoxNative();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MobfoxSDK.onPause(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MobfoxSDK.onResume(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        clearAllAds();

        MobfoxSDK.onDestroy(this);
    }

    //===========================================================================================

    private void clearAllAds()
    {
        clearMobFoxBanners();
        clearMobFoxInterstitials();
        clearMobFoxNatives();
    }

    //===========================================================================================

    private void initBannerButtons()
    {
        btnBannerSmall = (Button)findViewById(R.id.btnBannerSmall);
        btnBannerSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading banner...",Toast.LENGTH_SHORT).show();

                startMobFoxSmallBanner();
            }
        });

        btnBannerLarge = (Button)findViewById(R.id.btnBannerLarge);
        btnBannerLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading banner...",Toast.LENGTH_SHORT).show();

                startMobFoxLargeBanner();
            }
        });

        btnBannerVideo = (Button)findViewById(R.id.btnBannerVideo);
        btnBannerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading banner...",Toast.LENGTH_SHORT).show();

                startMobFoxVideoBanner();
            }
        });
    }

    //===========================================================================================

    private void initInterstitial()
    {
        btnInterstitialHtml = (Button)findViewById(R.id.btnInterstitialHtml);
        btnInterstitialHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading interstitial...",Toast.LENGTH_SHORT).show();

                startMobFoxHtmlInterstitial();
            }
        });

        btnInterstitialVideo = (Button)findViewById(R.id.btnInterstitialVideo);
        btnInterstitialVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading interstitial...",Toast.LENGTH_SHORT).show();

                startMobFoxVideoInterstitial();
            }
        });
    }

    //===========================================================================================

    private void initNative()
    {
        linNative         = findViewById(R.id.linNative);
        iconNative        = findViewById(R.id.iconNative);
        mainNative        = findViewById(R.id.mainNative);

        titleNative       = findViewById(R.id.titleNative);
        descNative        = findViewById(R.id.descNative);
        ratingNative      = findViewById(R.id.ratingNative);
        sponsoredNative   = findViewById(R.id.sponsoredNative);

        ctaNative         = findViewById(R.id.ctaNative);

        titleNative.setVisibility(View.GONE);
        descNative.setVisibility(View.GONE);
        ratingNative.setVisibility(View.GONE);
        sponsoredNative.setVisibility(View.GONE);
        ctaNative.setVisibility(View.GONE);
        iconNative.setVisibility(View.GONE);
        mainNative.setVisibility(View.GONE);

        btnNative = (Button)findViewById(R.id.btnNative);
        btnNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Loading native...",Toast.LENGTH_SHORT).show();

                startMobFoxNative();
            }
        });
    }

    //###########################################################################################
    //###########################################################################################
    //#####                                                                                 #####
    //#####   M o b f o x                                                                   #####
    //#####                                                                                 #####
    //###########################################################################################
    //###########################################################################################

    public static final String MOBFOX_HASH_BANNER_HTML  = "fe96717d9875b9da4339ea5367eff1ec";
    public static final String MOBFOX_HASH_BANNER_VIDEO = "80187188f458cfde788d961b6882fd53";
    public static final String MOBFOX_HASH_INTER_HTML   = "267d72ac3f77a3f447b32cf7ebf20673";
    public static final String MOBFOX_HASH_INTER_VIDEO  = "80187188f458cfde788d961b6882fd53";
    public static final String MOBFOX_HASH_NATIVE       = "a764347547748896b84e0b8ccd90fd62";

    private MFXBanner       mMFXBannerAd       = null;
    private MFXInterstitial mMFXInterstitialAd = null;
    private MFXNative       mMFXNativeAd       = null;

    //===========================================================================================

    private void initMobFoxSDK()
    {
        MobfoxSDK.init(this);

        MobfoxSDK.setGDPR(true);
        MobfoxSDK.setGDPRConsentString(GDPRParams.GDPR_DEFAULT_MOBFOX_CONSENT_STRING);

        MobfoxSDK.setDemoAge("32");
        MobfoxSDK.setDemoGender("male");
        MobfoxSDK.setDemoKeywords("basketball,tennis");
        MobfoxSDK.setLatitude(32.455666);
        MobfoxSDK.setLongitude(32.455666);
    }

    //===========================================================================================

    private void startMobFoxSmallBanner()
    {
        clearAllAds();

        mMFXBannerAd = MobfoxSDK.createBanner(MainActivity.this,320,50, MOBFOX_HASH_BANNER_HTML,bannerListener);
        MobfoxSDK.setBannerFloorPrice(mMFXBannerAd,0.036f);
        MobfoxSDK.loadBanner(mMFXBannerAd);
    }

    private void startMobFoxLargeBanner()
    {
        clearAllAds();

        mMFXBannerAd = MobfoxSDK.createBanner(MainActivity.this,300,250, MOBFOX_HASH_BANNER_HTML,bannerListener);
        MobfoxSDK.loadBanner(mMFXBannerAd);
    }

    private void startMobFoxVideoBanner()
    {
        clearAllAds();

        mMFXBannerAd = MobfoxSDK.createBanner(MainActivity.this,300,250, MOBFOX_HASH_BANNER_VIDEO,bannerListener);
        MobfoxSDK.loadBanner(mMFXBannerAd);
    }

    private void clearMobFoxBanners()
    {
        if (mMFXBannerAd!=null)
        {
            MobfoxSDK.releaseBanner(mMFXBannerAd);
            mMFXBannerAd = null;
        }

        relBanner = (LinearLayout)findViewById(R.id.banner);
        relBanner.removeAllViews();
    }

    //-------------------------------------------------------------------------------------------

    private MFXBannerListener bannerListener = new MFXBannerListener() {

        @Override
        public void onBannerLoadFailed(MFXBanner banner, String code) {
            Toast.makeText(MainActivity.this,"onBannerLoadFailed: "+code,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBannerLoaded(MFXBanner banner) {
            Toast.makeText(MainActivity.this,"onBannerLoaded",Toast.LENGTH_SHORT).show();

            MobfoxSDK.addBannerViewTo(banner, relBanner);
        }

        @Override
        public void onBannerShown(MFXBanner banner) {
            Toast.makeText(MainActivity.this,"onBannerShown",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBannerClosed(MFXBanner banner) {
            Toast.makeText(MainActivity.this,"onBannerClosed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBannerFinished(MFXBanner banner) {
            Toast.makeText(MainActivity.this,"onBannerFinished",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBannerClicked(MFXBanner banner, String url) {
            Toast.makeText(MainActivity.this,"onBannerClicked",Toast.LENGTH_SHORT).show();
        }
    };

    //===========================================================================================

    private void startMobFoxHtmlInterstitial()
    {
        clearAllAds();

        mMFXInterstitialAd = MobfoxSDK.createInterstitial(MainActivity.this,
                MOBFOX_HASH_INTER_HTML,
                interstitialListener);
        MobfoxSDK.loadInterstitial(mMFXInterstitialAd);
    }

    private void startMobFoxVideoInterstitial()
    {
        clearAllAds();

        mMFXInterstitialAd = MobfoxSDK.createInterstitial(MainActivity.this,
                MOBFOX_HASH_INTER_VIDEO,
                interstitialListener);
        MobfoxSDK.loadInterstitial(mMFXInterstitialAd);
    }

    private void clearMobFoxInterstitials()
    {
        if (mMFXInterstitialAd!=null)
        {
            MobfoxSDK.releaseInterstitial(mMFXInterstitialAd);
            mMFXInterstitialAd = null;
        }
    }

    //-------------------------------------------------------------------------------------------

    private MFXInterstitialListener interstitialListener = new MFXInterstitialListener() {
        @Override
        public void onInterstitialLoaded(MFXInterstitial interstitial) {
            Toast.makeText(MainActivity.this,"onInterstitialLoaded",Toast.LENGTH_SHORT).show();

            MobfoxSDK.showInterstitial(mMFXInterstitialAd);
        }

        @Override
        public void onInterstitialLoadFailed(MFXInterstitial interstitial, String code) {
            Toast.makeText(MainActivity.this,"onInterstitialLoadFailed: "+code,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialClosed(MFXInterstitial interstitial) {
            Toast.makeText(MainActivity.this,"onInterstitialClosed",Toast.LENGTH_SHORT).show();

            MobfoxSDK.releaseInterstitial(mMFXInterstitialAd);
        }

        @Override
        public void onInterstitialClicked(MFXInterstitial interstitial, String url) {
            Toast.makeText(MainActivity.this,"onInterstitialClicked",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialShown(MFXInterstitial interstitial) {
            Toast.makeText(MainActivity.this,"onInterstitialShown",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialFinished(MFXInterstitial interstitial) {
            Toast.makeText(MainActivity.this,"onInterstitialFinished",Toast.LENGTH_SHORT).show();
        }
    };

    //===========================================================================================

    private void startMobFoxNative()
    {
        clearAllAds();

        mMFXNativeAd = MobfoxSDK.createNative(MainActivity.this,
                MOBFOX_HASH_NATIVE,
                nativeListener);
        MobfoxSDK.loadNative(mMFXNativeAd);
    }

    private void clearMobFoxNatives()
    {
        if (mMFXNativeAd!=null)
        {
            updateNativeText(titleNative    , null);
            updateNativeText(descNative     , null);
            updateNativeText(ratingNative   , null);
            updateNativeText(sponsoredNative, null);
            updateNativeText(ctaNative      , null);

            updateNativeImage(iconNative, null);
            updateNativeImage(mainNative, null);

            MobfoxSDK.releaseNative(mMFXNativeAd);
            mMFXNativeAd = null;
        }
    }

    //-------------------------------------------------------------------------------------------

    private void updateNativeText(TextView tv, String value)
    {
        if ((value==null) || (value.length()==0))
        {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(value);
        }
    }

    private void updateNativeImage(ImageView iv, Bitmap value)
    {
        if (value==null)
        {
            iv.setVisibility(View.GONE);
        } else {
            iv.setVisibility(View.VISIBLE);
            iv.setImageBitmap(value);
        }
    }

    private MFXNativeListener nativeListener = new MFXNativeListener() {
        @Override
        public void onNativeLoaded(MFXNative aNative) {
            Toast.makeText(self, "on native loaded", Toast.LENGTH_SHORT).show();

            Map<String, String> textItems = MobfoxSDK.getNativeTexts(mMFXNativeAd);

            updateNativeText(titleNative    , textItems.get("title"));
            updateNativeText(descNative     , textItems.get("desc"));
            updateNativeText(ratingNative   , textItems.get("rating"));
            updateNativeText(sponsoredNative, textItems.get("sponsored"));
            updateNativeText(ctaNative      , textItems.get("ctatext"));

            MobfoxSDK.loadNativeImages(mMFXNativeAd);

            MobfoxSDK.registerNativeForInteraction(self, mMFXNativeAd, linNative);

            ctaNative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobfoxSDK.callToActionClicked(mMFXNativeAd);
                }
            });
        }

        @Override
        public void onNativeImagesReady(MFXNative aNative) {
            Toast.makeText(self, "on images ready", Toast.LENGTH_SHORT).show();

            Map<String, Bitmap> imageItems = MobfoxSDK.getNativeImageBitmaps(mMFXNativeAd);

            updateNativeImage(iconNative, imageItems.get("icon"));
            updateNativeImage(mainNative, imageItems.get("main"));
        }

        @Override
        public void onNativeLoadFailed(MFXNative aNative, String code) {
            Toast.makeText(self, "MFXNative error: "+code, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNativeClicked(MFXNative aNative) {
            Toast.makeText(self, "MFXNative clicked", Toast.LENGTH_SHORT).show();
        }
    };

    //###########################################################################################
}
