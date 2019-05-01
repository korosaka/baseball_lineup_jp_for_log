package com.websarva.wings.android.dasenapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    //選択した打順
    TextView tvSelectNum;
    //入力欄
    public EditText etName;
    //登録ボタン
    Button record;
    //    キャンセルボタン
    Button cancel;
    // 入れ替えボタン
    Button replace;
    // 入れ替え中フラグ
    Boolean isReplacing = false;
    // １つ目入れ替え選択フラグ
    Boolean isFirstReplaceClicked = false;
    //スタメンタイトル
    TextView title;
    //各打順の数字配列
    int numbers[] = new int[19];
    //グローバル変数i（データベースへの登録・検索で使う）
    int i = 0;
    //サブオーダー選択時に+10される
    int k = 0;
    //各打順の名前
    TextView[] name_tv = new TextView[9];
    //スピナーオブジェクト
    Spinner spinner;
    //クリアボタン（現在上部に入力中のものを未入力状態に戻す（選択打順も））
    Button clear;
    //各打順のポジション
    TextView[] position_tv = new TextView[9];
    //各打順の名前,ポジション用配列
    String[] names;
    String[] positions;


    int firstClicked = -1;
    private DatabaseUsing databaseUsing;
    private NormalLineupFragment normalLineupFragment;
    private DhLineupFragment dhLineupFragment;

    private Fragment currentFragment;

    //ここからmain
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAdsense();

        databaseUsing = new DatabaseUsing(this);
        for (int version = 1; version < 3; version++) {
            databaseUsing.getPlayersInfo(version);
        }

        bindLayout();
        setEdit();
        setOrderFragment();
    }

    private void setAdsense() {
        //広告処理
        MobileAds.initialize(this, "ca-app-pub-6298264304843789~9524433477");
        //広告ビュー
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void bindLayout() {
        //上記のグローバルフィールド紐付け
        tvSelectNum = findViewById(R.id.selectNum);
        etName = findViewById(R.id.etName);
        record = findViewById(R.id.record);
        cancel = findViewById(R.id.cancel);
        replace = findViewById(R.id.replace);
        clear = findViewById(R.id.clear);
        spinner = findViewById(R.id.position);
        title = findViewById(R.id.title);
    }

    private void setEdit() {
        //EditText入力不可に
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.setEnabled(false);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean flag) {
//                フォーカスを取得→キーボード表示
                if (flag) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, 0);
                }
//                フォーカス外れる→キーボード非表示
                else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private void setOrderFragment() {
        normalLineupFragment = NormalLineupFragment.newInstance();
        dhLineupFragment = DhLineupFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.lineup_container, normalLineupFragment);
        transaction.add(R.id.lineup_container, dhLineupFragment);
        transaction.show(normalLineupFragment);
        transaction.hide(dhLineupFragment);
        transaction.commit();

        currentFragment = normalLineupFragment;
    }

    //以下１〜９番の打順ボタン処理⬇
    public void onClick1(View view) {
        commonMethod(0);
    }

    public void onClick2(View view) {
        commonMethod(1);
    }

    public void onClick3(View view) {
        commonMethod(2);
    }

    public void onClick4(View view) {
        commonMethod(3);
    }

    public void onClick5(View view) {
        commonMethod(4);
    }

    public void onClick6(View view) {
        commonMethod(5);
    }

    public void onClick7(View view) {
        commonMethod(6);
    }

    public void onClick8(View view) {
        commonMethod(7);
    }

    public void onClick9(View view) {
        commonMethod(8);
    }

    public void onClickP(View view) {
        commonMethod(9);
    }


    //打順ボタン共通メソッド（打順・登録状態表示、EditText・登録/クリアボタンの有効化、データベース用の数字登録）
    public void commonMethod(int j) {
        // 入れ替え時のクリックと処理区別
        if (isReplacing) {
            replaceMethod(j);

        } else {
            // 通常時の打順選択
            selectNum(j);
        }
    }


    private void replaceMethod(int j) {
        // 入れ替え時
        if (!isFirstReplaceClicked) {
            // 1つめ選択時
            selectFirstReplacing(j);
        } else {
            // 2つめ選択時
            if (j == firstClicked) {
                // 同じボタンがクリックされた →　元に戻す
                cancelFirstClick(j);
            } else {
                // 異なるボタン →入れ替え処理
                // DB/Layout内で入れ替え
                replacing2players(firstClicked, j);
                putSettingBack();
            }
        }
    }

    private void selectFirstReplacing(int num) {
        changeButtonColor(num);
        firstClicked = num;
        isFirstReplaceClicked = true;
    }

    private void cancelFirstClick(int num) {
        setButtonDefault(num);
        isFirstReplaceClicked = false;
        firstClicked = -1;
    }

    public void replacing2players(int firstSelected, int secondSelected) {

        // 最初に選択した選手のところに後から選択した選手を上書き
        databaseUsing.setDatabaseInfo(firstSelected, CachedPlayerNamesInfo.instance.getAppropriateName(secondSelected)
                , CachedPlayerPositionsInfo.instance.getAppropriatePosition(secondSelected));

        // 後に選択した選手の場所に最初の選手を登録
        databaseUsing.setDatabaseInfo(secondSelected, CachedPlayerNamesInfo.instance.getAppropriateName(firstSelected)
                , CachedPlayerPositionsInfo.instance.getAppropriatePosition(firstSelected));

        // キャッシュデータもデータベースの内容に合わせる(入れ替え後のデータに更新する)
        databaseUsing.getDatabaseInfo(CurrentOrderVersion.instance.getCurrentVersion(), firstSelected);
        databaseUsing.getDatabaseInfo(CurrentOrderVersion.instance.getCurrentVersion(), secondSelected);

        // TextViewも更新
        changeText(firstSelected, secondSelected);
    }

    private void changeText(int firstSelected, int secondSelected) {
        switch (CurrentOrderVersion.instance.getCurrentVersion()) {
            case FixedWords.DEFAULT:
                normalLineupFragment.changeData(firstSelected, CachedPlayerNamesInfo.instance.getAppropriateName(firstSelected)
                        , CachedPlayerPositionsInfo.instance.getAppropriatePosition(firstSelected));
                normalLineupFragment.changeData(secondSelected, CachedPlayerNamesInfo.instance.getAppropriateName(secondSelected)
                        , CachedPlayerPositionsInfo.instance.getAppropriatePosition(secondSelected));
                break;
            case FixedWords.DH:
                dhLineupFragment.changeData(firstSelected, CachedPlayerNamesInfo.instance.getAppropriateName(firstSelected)
                        , CachedPlayerPositionsInfo.instance.getAppropriatePosition(firstSelected));
                dhLineupFragment.changeData(secondSelected, CachedPlayerNamesInfo.instance.getAppropriateName(secondSelected)
                        , CachedPlayerPositionsInfo.instance.getAppropriatePosition(secondSelected));
                break;
            case FixedWords.ALL10:
                break;
            case FixedWords.ALL11:
                break;
            case FixedWords.ALL12:
                break;
            case FixedWords.ALL13:
                break;
            case FixedWords.ALL14:
                break;
            case FixedWords.ALL15:
                break;
        }

    }

    private void putSettingBack() {
        // 色々戻す

        replace.setEnabled(false);
        cancelFirstClick(firstClicked);
        isReplacing = false;
        replace.setEnabled(true);
        cancel.setEnabled(false);
        if (k == 0) title.setText(R.string.title);
        else title.setText(R.string.subtitle);
    }

    private void selectNum(int num) {

        readyInputtingName(num, CachedPlayerPositionsInfo.instance.getAppropriatePosition(num)
                , CachedPlayerNamesInfo.instance.getAppropriateName(num));
        i = num;
    }

    //文字列からスピナーをセットするメソッド（上記メソッドで使用）
    private void setSpinner(Spinner spinner, String position) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(position)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    private void readyInputtingName(int num, String position, String name) {
        spinner.setEnabled(true);
        //numbersは表示打順のためkを反映させない
        String number = String.valueOf(num + 1) + "番";
        tvSelectNum.setText(number);
        //下記メソッド使用
        setSpinner(spinner, position);
        etName.setText(name);
        if (etName.getText().toString().equals("-----")) etName.setText("");
        etName.setEnabled(true);
        etName.setFocusable(true);
        etName.setFocusableInTouchMode(true);
        etName.requestFocus();
        record.setEnabled(true);
        cancel.setEnabled(true);
        clear.setEnabled(true);
        replace.setEnabled(false);

        // DH制の投手の場合のみ対応
        if (CurrentOrderVersion.instance.getCurrentVersion() == FixedWords.DH && num == 9) {
            tvSelectNum.setText("P");
            setSpinner(spinner, "----");
            spinner.setEnabled(false);
        }
    }

    //登録ボタン押した処理
    public void onClickSave(View view) {
        //入力文字列取得
        String playerName = etName.getText().toString();
        if (playerName.equals("")) playerName = "-----";
        //ポジション取得
        String position = (String) spinner.getSelectedItem();

        databaseUsing.setDatabaseInfo(i, playerName, position);

        switch (CurrentOrderVersion.instance.getCurrentVersion()) {
            //画面のメンバー表に反映（１〜９番まで）
            case FixedWords.DEFAULT:
                normalLineupFragment.changeData(i, playerName, position);
                CachedPlayerNamesInfo.instance.setNameNormal(i, playerName);
                CachedPlayerPositionsInfo.instance.setPositionNormal(i, position);
                break;
            case FixedWords.DH:
                dhLineupFragment.changeData(i, playerName, position);
                CachedPlayerNamesInfo.instance.setNameDh(i, playerName);
                CachedPlayerPositionsInfo.instance.setPositionDh(i, position);
                break;
        }

        setLayoutDefault();
    }

    private void setLayoutDefault() {
        //それぞれ初期状態に戻す
        tvSelectNum.setText(getString(R.string.current_num));
        etName.setText("");
        spinner.setSelection(0);
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.setEnabled(false);
        record.setEnabled(false);
        cancel.setEnabled(false);
        clear.setEnabled(false);
        replace.setEnabled(true);
    }

    //クリアボタン処理
    public void onClickClear(View view) {
        //入力名をクリア状態に
        etName.setText("");
        //スピナー（守備位置）を未選択状態に戻す
        spinner.setSelection(0);
    }

    //    キャンセルボタン処理
    public void onClickCancel(View view) {

        // 入れ替えボタンクリック時のキャンセルor入力中のキャンセル？

        if (isReplacing) {
            // ボタン色戻し
            if (isFirstReplaceClicked) cancelFirstClick(firstClicked);
            //入れ替えボタン戻し
            replace.setEnabled(true);
            //入れ替えフラグ戻し
            isReplacing = false;
        }
        //それぞれ初期状態に戻す
        setLayoutDefault();
    }

    // 入れ替えボタン処理
    public void onClickReplace(View view) {

        // 入れ替えクリックされているフラグ
        isReplacing = true;
        // 入れ替えボタンはenable(false)に
        replace.setEnabled(false);
        // キャンセルはできるように
        cancel.setEnabled(true);
        // タイトルが『２つボタンクリック』になる
        title.setText(R.string.replace_title);

    }


    //オプションメニュー追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //メニューインフレター取得
        MenuInflater inflater = getMenuInflater();
        //オプションメニュー用.xmlファイルをインフレート（メニュー部品をJavaオブジェクトに）
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        //親クラスの同名メソッドで返却
        return super.onCreateOptionsMenu(menu);
    }

    //オプションメニューを選択した時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //選択されたオプションメニューのID取得
        int itemId = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String[] spinnerResource = getResources().getStringArray(R.array.positions);

        //IDのR値による処理分岐
        switch (itemId) {
            //オーダー選択の場合
            case R.id.oder:
                transaction.hide(dhLineupFragment);
                transaction.show(normalLineupFragment);
                transaction.commit();
                CurrentOrderVersion.instance.setCurrentVersion(FixedWords.DEFAULT);
                currentFragment = normalLineupFragment;
                break;
            //DHの場合
            case R.id.dh:
                transaction.hide(normalLineupFragment);
                transaction.show(dhLineupFragment);
                transaction.commit();
                CurrentOrderVersion.instance.setCurrentVersion(FixedWords.DH);
                spinnerResource = getResources().getStringArray(R.array.positions_dh);
                currentFragment = dhLineupFragment;
                break;
            case R.id.field:
                //遷移先に送るデータ（各守備位置・名前）
                String[] positionIntent = new String[9];
                String[] nameIntent = new String[9];
                //送るデータ（9人分）を抽出（正規orサブ）
                for (int i = 0; i < 9; i++) {
                    positionIntent[i] = positions[i + k];
                    nameIntent[i] = names[i + k];
                }
                //フィールド画面へ
                Intent intent = new Intent(MainActivity.this, FieldActivity.class);
                intent.putExtra("positions", positionIntent);
                intent.putExtra("names", nameIntent);
                startActivity(intent);
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerResource);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //上部入力欄初期状態へ
        setLayoutDefault();

        if (isFirstReplaceClicked) {
            cancelFirstClick(firstClicked);
        }

        isReplacing = false;

        //親クラス同名メソッドで戻り値返却
        return super.onOptionsItemSelected(item);
    }

    private void changeButtonColor(int num) {
        switch (CurrentOrderVersion.instance.getCurrentVersion()) {
            case FixedWords.DEFAULT:
                normalLineupFragment.changeButtonColor(num);
                break;
            case FixedWords.DH:
                dhLineupFragment.changeButtonColor(num);
                break;
            case FixedWords.ALL10:
                break;
            case FixedWords.ALL11:
                break;
            case FixedWords.ALL12:
                break;
            case FixedWords.ALL13:
                break;
            case FixedWords.ALL14:
                break;
            case FixedWords.ALL15:
                break;
        }
    }

    private void setButtonDefault(int num) {
        switch (CurrentOrderVersion.instance.getCurrentVersion()) {
            case FixedWords.DEFAULT:
                normalLineupFragment.setButtonDefault(num);
                break;
            case FixedWords.DH:
                dhLineupFragment.setButtonDefault(num);
                break;
            case FixedWords.ALL10:
                break;
            case FixedWords.ALL11:
                break;
            case FixedWords.ALL12:
                break;
            case FixedWords.ALL13:
                break;
            case FixedWords.ALL14:
                break;
            case FixedWords.ALL15:
                break;
        }
    }

}
