package com.websarva.wings.android.dasenapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    //広告ビュー
    private AdView mAdView;
    //選択した打順
    TextView tvSelectNum;
    //入力欄
    public EditText etName;
    //登録ボタン
    Button record;
    //スタメンタイトル
    TextView title;
    //各打順の数字配列
    int numbers[] = new int[19];
    //グローバル変数i（データベースへの登録・検索で使う）
    int i = 0;
    //サブオーダー選択時に+10される
    int k = 0;
    //各打順の名前
    TextView name1;
    TextView name2;
    TextView name3;
    TextView name4;
    TextView name5;
    TextView name6;
    TextView name7;
    TextView name8;
    TextView name9;
    //スピナーオブジェクト
    Spinner spinner;
    //クリアボタン（現在上部に入力中のものを未入力状態に戻す（選択打順も））
    Button clear;
    //各打順のポジション
    TextView position1;
    TextView position2;
    TextView position3;
    TextView position4;
    TextView position5;
    TextView position6;
    TextView position7;
    TextView position8;
    TextView position9;
    //各打順の名前,ポジション用配列
    String[] names;
    String[] positions;

    //ここからmain
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //広告処理
        MobileAds.initialize(this, "ca-app-pub-6298264304843789~9524433477");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //上記のグローバルフィールド紐付け
        tvSelectNum = findViewById(R.id.selectNum);
        etName = findViewById(R.id.etName);
        record = findViewById(R.id.record);
        clear = findViewById(R.id.clear);
        title = findViewById(R.id.title);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
        name6 = findViewById(R.id.name6);
        name7 = findViewById(R.id.name7);
        name8 = findViewById(R.id.name8);
        name9 = findViewById(R.id.name9);
        position1 = findViewById(R.id.position1);
        position2 = findViewById(R.id.position2);
        position3 = findViewById(R.id.position3);
        position4 = findViewById(R.id.position4);
        position5 = findViewById(R.id.position5);
        position6 = findViewById(R.id.position6);
        position7 = findViewById(R.id.position7);
        position8 = findViewById(R.id.position8);
        position9 = findViewById(R.id.position9);
        //打順配列に打順番号入れる(1~19番)
        for(int i = 0;i < 19;i++){
            numbers[i] = i + 1;
        }
        //スピナー紐付け
        spinner = findViewById(R.id.position);
        //EditText入力不可に
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.setEnabled(false);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view,boolean flag){
//                フォーカスを取得→キーボード表示
                if(flag){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view,0);
                }
//                フォーカス外れる→キーボード非表示
                else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);

                }

            }
        });

        //データベースからデータを取り出してきて表示する処理
        //データベースヘルパーオブジェクト生成
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        //データベースに保存してある名前・守備位置を入れる変数用意
        String name;
        String position;
        names = new String[19];
        positions = new String[19];
        //9人分のデータ取り出し
        for (int j = 0;j < 9;j++) {
            //ヘルパーから接続オブジェクト取得
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                //SQL文字列作成➡︎検索・表示（１〜９番）
                //SELECT文の文字列作成
                String sqlSelect = "SELECT playername,position FROM batting WHERE number = " + numbers[j];
                //sql実行（カーソルオブジェクト（実行結果そのもの？）が戻り値）
                Cursor cursor = db.rawQuery(sqlSelect, null);
                //cursor.moveToNext() ➡︎データない時（移動ができなかった時）はfalseになる
                if(cursor.moveToNext())
                {
                    //カラムのインデックス値取得
                    int idxName = cursor.getColumnIndex("playername");
                    int idxPosition = cursor.getColumnIndex("position");
                    //カラムのインデックス値を元に実際のデータ取得
                    name = cursor.getString(idxName);
                    position = cursor.getString(idxPosition);
                    //名前の空白登録は未登録にする
                    if(name.equals("")){
                        name = "-----";
                    }
                }
                //データが無かった時
                else {
                    name = "-----";
                    position = "----";
                }
                //各打順の名前・ポジションを配列に格納しておく
                names[j] = name;
                positions[j] = position;
            }
            //catchないとエラー出る　
            catch (Exception e) {
                //エラーメッセージ出す
                Log.e("キャッチ", "エラー", e);
            } finally {
                //解放
                db.close();
            }
        }
        name1.setText(names[0]);
        name2.setText(names[1]);
        name3.setText(names[2]);
        name4.setText(names[3]);
        name5.setText(names[4]);
        name6.setText(names[5]);
        name7.setText(names[6]);
        name8.setText(names[7]);
        name9.setText(names[8]);
        position1.setText(positions[0]);
        position2.setText(positions[1]);
        position3.setText(positions[2]);
        position4.setText(positions[3]);
        position5.setText(positions[4]);
        position6.setText(positions[5]);
        position7.setText(positions[6]);
        position8.setText(positions[7]);
        position9.setText(positions[8]);
    }
    //以下１〜９番の打順ボタン処理⬇
    public void onClick1(View view){
        commonMethod(0);
    }
    public void onClick2(View view){
        commonMethod(1);
    }
    public void onClick3(View view){
        commonMethod(2);
    }
    public void onClick4(View view){
        commonMethod(3);
    }
    public void onClick5(View view){
        commonMethod(4);
    }
    public void onClick6(View view){
        commonMethod(5);
    }
    public void onClick7(View view){
        commonMethod(6);
    }
    public void onClick8(View view) {
        commonMethod(7);
    }
    public void onClick9(View view){
        commonMethod(8);
    }
    //打順ボタン共通メソッド（打順・登録状態表示、EditText・登録/クリアボタンの有効化、データベース用の数字登録）
    public void commonMethod(int j){
        //numbersは表示打順のためkを反映させない
        String number = String.valueOf(numbers[j]) + "番";
        tvSelectNum.setText(number);
        //下記メソッド使用
        setSpinner(spinner,positions[j + k]);
        etName.setText(names[j + k]);
        if(etName.getText().toString().equals("-----")){
            etName.setText("");
        }
        etName.setEnabled(true);
        etName.setFocusable(true);
        etName.setFocusableInTouchMode(true);
        etName.requestFocus();
        record.setEnabled(true);
        clear.setEnabled(true);
        i = j;
    }
    //文字列からスピナーをセットするメソッド（上記メソッドで使用）
    public void setSpinner(Spinner spinner,String position){
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        for(int i = 0; i < adapter.getCount(); i++){
            if(adapter.getItem(i).equals(position)){
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }


    //登録ボタン押した処理
    public void onClickSave(View view){
        //入力文字列取得
        String playerName = etName.getText().toString();
        if(playerName.equals("")){
            playerName = "-----";
        }
        //ポジション取得
        String position = (String) spinner.getSelectedItem();
        //データベースヘルパー作成
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        //接続オブジェクト
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            //今あるデータ削除➡その後インサート
            //削除用文字列
            String sqlDelete = "DELETE FROM batting WHERE number = ?";
            //上記文字列からPreparedStatement取得（SQLを実行するためのインターフェース）
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            //変数バインド（数字は何番目の？に入れるか,？に入れるもの）（kはオプション選択によって変わる）
            stmt.bindLong(1,numbers[i + k]);
            //削除SQL実行
            stmt.executeUpdateDelete();

            //インサート用文字
            //テーブルの内容等変える場合は、新たなテーブル名で作り直さないとエラーになる（詳細はDatabaseHelperクラスへ）
            String sqlInsert = "INSERT INTO batting(_id, number, playername, position) VALUES(?,?,?,?)";
            //PreparedStatement取得 (stmtは削除で使ったものの再利用)
            stmt = db.compileStatement(sqlInsert);
            //変数バインド（数字は何番目の？に入れるか,？に入れるもの）
            //_id(primary key) は設定不要らしい
            //サブオーダー選択時は打順は11~19番でデータベースに登録される
            stmt.bindLong(2,numbers[i + k]);
            stmt.bindString(3,playerName);
            stmt.bindString(4,position);
            //インサートSQL実行
            stmt.executeInsert();
        }
        catch (Exception e){
            Log.d("error","例外発生");
        }
        finally {
            //データ接続プブジェクト解放
            db.close();
        }
        //それぞれ初期状態に戻す
        tvSelectNum.setText(getString(R.string.current_num));
        etName.setText("");
        spinner.setSelection(0);
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.setEnabled(false);
        record.setEnabled(false);
        clear.setEnabled(false);

        //画面のメンバー表に反映（１〜９番まで）
        switch (i){
            case 0:
                name1.setText(playerName);
                position1.setText(position);
                names[0 + k] = playerName;
                positions[0 + k] = position;
                break;
            case 1:
                name2.setText(playerName);
                position2.setText(position);
                names[1 + k] = playerName;
                positions[1 + k] = position;
                break;
            case 2:
                name3.setText(playerName);
                position3.setText(position);
                names[2 + k] = playerName;
                positions[2 + k] = position;
                break;
            case 3:
                name4.setText(playerName);
                position4.setText(position);
                names[3 + k] = playerName;
                positions[3 + k] = position;
                break;
            case 4:
                name5.setText(playerName);
                position5.setText(position);
                names[4 + k] = playerName;
                positions[4 + k] = position;
                break;
            case 5:
                name6.setText(playerName);
                position6.setText(position);
                names[5 + k] = playerName;
                positions[5 + k] = position;
                break;
            case 6:
                name7.setText(playerName);
                position7.setText(position);
                names[6 + k] = playerName;
                positions[6 + k] = position;
                break;
            case 7:
                name8.setText(playerName);
                position8.setText(position);
                names[7 + k] = playerName;
                positions[7 + k] = position;
                break;
            case 8:
                name9.setText(playerName);
                position9.setText(position);
                names[8 + k] = playerName;
                positions[8 + k] = position;
                break;
        }
    }

    //キャンセルボタン処理
    public void onClickClear(View view){
        //入力名をクリア状態に
        etName.setText("");
        //スピナー（守備位置）を未選択状態に戻す
        spinner.setSelection(0);
    }

    //オプションメニュー追加
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //メニューインフレター取得
        MenuInflater inflater = getMenuInflater();
        //オプションメニュー用.xmlファイルをインフレート（メニュー部品をJavaオブジェクトに）
        inflater.inflate(R.menu.menu_options_menu_list,menu);
        //親クラスの同名メソッドで返却
        return super.onCreateOptionsMenu(menu);
    }

    //オプションメニューを選択した時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //選択されたオプションメニューのID取得
        int itemId = item.getItemId();
        //IDのR値による処理分岐
        switch (itemId) {
                //オーダー選択の場合
            case R.id.oder:
                k = 0;
                break;
                //サブオーダーの場合
            case R.id.subOder:
                //データベース上の打順が11〜19番を対象に
                k = 10;
                break;
            case R.id.field:
                //遷移先に送るデータ（各守備位置・名前）
                String[] positionIntent = new String[9];
                String[] nameIntent = new String[9];
                //送るデータ（9人分）を抽出（正規orサブ）
                for (int i = 0;i < 9;i++){
                    positionIntent[i] = positions[i + k];
                    nameIntent[i] = names[i + k];
                }
                //フィールド画面へ
                Intent intent = new Intent(MainActivity.this,FieldActivity.class);
                intent.putExtra("positions",positionIntent);
                intent.putExtra("names",nameIntent);
                startActivity(intent);
                break;
        }

        //以下はmainで行った処理とほとんど同じ
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        String name;
        String position;
        for (int j = 0;j < 9;j++) {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                //SQL文字列作成➡︎検索・表示（1~9番 or 11~19番）(mainメソッドとほぼ同じ)
                String sqlSelect = "SELECT playername,position FROM batting WHERE number = " + numbers[j + k];
                Cursor cursor = db.rawQuery(sqlSelect, null);
                if(cursor.moveToNext())
                {
                    int idxName = cursor.getColumnIndex("playername");
                    int idxPosition = cursor.getColumnIndex("position");
                    name = cursor.getString(idxName);
                    position = cursor.getString(idxPosition);
                    if(name.equals("")){
                        name = "-----";
                    }
                }
                else {
                    name = "-----";
                    position = "----";
                }
                names[j + k] = name;
                positions[j + k] = position;
            }
            catch (Exception e) {
                Log.e("キャッチ2", "エラー2", e);
            }
            finally {
                db.close();
            }
        }
        name1.setText(names[0 + k]);
        name2.setText(names[1 + k]);
        name3.setText(names[2 + k]);
        name4.setText(names[3 + k]);
        name5.setText(names[4 + k]);
        name6.setText(names[5 + k]);
        name7.setText(names[6 + k]);
        name8.setText(names[7 + k]);
        name9.setText(names[8 + k]);
        position1.setText(positions[0 + k]);
        position2.setText(positions[1 + k]);
        position3.setText(positions[2 + k]);
        position4.setText(positions[3 + k]);
        position5.setText(positions[4 + k]);
        position6.setText(positions[5 + k]);
        position7.setText(positions[6 + k]);
        position8.setText(positions[7 + k]);
        position9.setText(positions[8 + k]);
        //見出し変更
        if(k == 0){
            title.setText("~Starting Member~");
        }
        else{
            title.setText("~Starting Member~（サブ）");
        }
        //上部入力欄初期状態へ
        tvSelectNum.setText(getString(R.string.current_num));
        etName.setText("");
        spinner.setSelection(0);
        etName.setFocusable(false);
        etName.setFocusableInTouchMode(false);
        etName.setEnabled(false);
        record.setEnabled(false);
        clear.setEnabled(false);
        //親クラス同名メソッドで戻り値返却
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//
//        etName.setFocusable(false);
//        etName.setFocusableInTouchMode(false);
//    }
//
//    @Override
//    public void onRestart(){
//        super.onRestart();
//
//        etName.setFocusable(false);
//        etName.setFocusableInTouchMode(false);
//    }
}
