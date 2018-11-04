package com.websarva.wings.android.dasenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FieldActivity extends AppCompatActivity {
    //広告ビュー
    private AdView mAdView;
    //戻るボタン
    private Button backButton;
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

        backButton = findViewById(R.id.back);

        //インテントobject
        Intent intent = getIntent();
        //data取得
        String positions[] = intent.getStringArrayExtra("positions");
        String names[] = intent.getStringArrayExtra("names");

        //ある打順の守備位置dataがどこかのポジションと合致すれば、その打順登録名を守備フィールドに
        for(int i = 0;i < 9;i++){
            switch (positions[i]){
                case "(投)":
                    position1.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(捕)":
                    position2.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(一)":
                    position3.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(二)":
                    position4.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(三)":
                    position5.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(遊)":
                    position6.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(左)":
                    position7.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(中)":
                    position8.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                case "(右)":
                    position9.setText(names[i] + " (" + (i + 1) + ")");
                    break;
                default:
                    break;
            }
        }


    }
    //戻るボタン
    public void onClickBack(View view){
        finish();
    }
}
