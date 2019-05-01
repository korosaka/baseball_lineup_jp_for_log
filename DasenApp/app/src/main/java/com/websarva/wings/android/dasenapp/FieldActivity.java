package com.websarva.wings.android.dasenapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private TextView[] dh = new TextView[6];


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
        dh[0] = findViewById(R.id.dh1);
        dh[1] = findViewById(R.id.dh2);
        dh[2] = findViewById(R.id.dh3);
        dh[3] = findViewById(R.id.dh4);
        dh[4] = findViewById(R.id.dh5);
        dh[5] = findViewById(R.id.dh6);

        int playerNumber = 0;
        switch (CurrentOrderVersion.instance.getCurrentVersion()) {
            case FixedWords.DEFAULT:
                playerNumber = 9;
                break;
            case FixedWords.DH:
                playerNumber = 10;
                break;
            case FixedWords.ALL10:
                playerNumber = 10;
                break;
            case FixedWords.ALL11:
                playerNumber = 11;
                break;
            case FixedWords.ALL12:
                playerNumber = 12;
                break;
            case FixedWords.ALL13:
                playerNumber = 13;
                break;
            case FixedWords.ALL14:
                playerNumber = 14;
                break;
            case FixedWords.ALL15:
                playerNumber = 15;
                break;
        }

        //ある打順の守備位置dataがどこかのポジションと合致すれば、その打順登録名を守備フィールドに
        for (int i = 0; i < playerNumber; i++) {
            switch (CachedPlayerPositionsInfo.instance.getAppropriatePosition(i)) {
                case "(投)":
                    position1.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(捕)":
                    position2.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(一)":
                    position3.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(二)":
                    position4.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(三)":
                    position5.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(遊)":
                    position6.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(左)":
                    position7.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(中)":
                    position8.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                case "(右)":
                    position9.setText(CachedPlayerNamesInfo.instance.getAppropriateName(i) + " (" + (i + 1) + ")");
                    break;
                default:
                    break;
            }
        }


    }

    //戻るボタン
    public void onClickBack(View view) {
        finish();
    }
}
