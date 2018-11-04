package com.websarva.wings.android.dasenapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FieldActivity extends AppCompatActivity {
    //広告ビュー
    private AdView mAdView;
    //各ポジションのテキスト
    private TextView position1;
    private TextView position2;
    private TextView position3;
    private TextView position4;
    private TextView position5;
    private TextView position6;
    private TextView position7;
    private TextView position8;
    private TextView position9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        //広告処理
        MobileAds.initialize(this, "ca-app-pub-6298264304843789~9524433477");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //ポジション紐付け
        position1 = findViewById(R.id.pitcher);
        position2 = findViewById(R.id.catcher);
        position3 = findViewById(R.id.first);
        position4 = findViewById(R.id.second);
        position5 = findViewById(R.id.third);
        position6 = findViewById(R.id.shortStop);
        position7 = findViewById(R.id.left);
        position8 = findViewById(R.id.center);
        position9 = findViewById(R.id.right);

    }
}
