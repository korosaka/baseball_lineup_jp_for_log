package com.websarva.wings.android.dasenapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUsing {

    private DatabaseHelper helper;
    private String playerName;
    private String playerPosition;

    public DatabaseUsing(Context context) {
        //データベースからデータを取り出してきて表示する処理
        //データベースヘルパーオブジェクト生成
        helper = new DatabaseHelper(context);
    }

    /**
     * スタメン情報を取得
     *
     * @param k 1 -> DH無し(9)
     *          2 -> DH制(10)
     *          3 -> 全員打(10)
     *          4 -> 全員打(11)
     *          5 -> 全員打(12)
     *          6 -> 全員打(13)
     *          7 -> 全員打(14)
     *          8 -> 全員打(15)
     */
    public void getPlayersInfo(int k) {

        int players = 9;
        switch (k) {
            case 1:
                players = 9;
                break;
            case 2:
                players = 10;
                break;
            case 3:
                players = 10;
                break;
            case 4:
                players = 11;
                break;
            case 5:
                players = 12;
                break;
            case 6:
                players = 13;
                break;
            case 7:
                players = 14;
                break;
            case 8:
                players = 15;
                break;
        }

        // DH制の場合は10人分のデータ
        int pitcher = 0;
        if (k == 0) {
            pitcher = 1;
        }


        for (int j = 0; j < players; j++) {
            getDatabaseInfo(k, j);
        }
    }

    /**
     * DBから登録データ取得
     */
    private void getDatabaseInfo(int version, int num) {
        //ヘルパーから接続オブジェクト取得
        SQLiteDatabase dbR = helper.getReadableDatabase();
        try {
            //SQL文字列作成➡︎検索・表示
            //SELECT文の文字列作成
            String sqlSelect = "SELECT playerName,position FROM lineup WHERE number = " + (version * 100 + num);
            //sql実行（カーソルオブジェクト（実行結果そのもの？）が戻り値）
            Cursor cursor = dbR.rawQuery(sqlSelect, null);
            //cursor.moveToNext() ➡︎データない時（移動ができなかった時）はfalseになる
            if (cursor.moveToNext()) {
                //カラムのインデックス値取得
                int idxName = cursor.getColumnIndex("playerName");
                int idxPosition = cursor.getColumnIndex("position");
                //カラムのインデックス値を元に実際のデータ取得
                playerName = cursor.getString(idxName);
                playerPosition = cursor.getString(idxPosition);
                //名前の空白登録は未登録にする
                if (playerName.equals("")) {
                    playerName = "-----";
                }
            }
            //データが無かった時
            else {
                playerName = "-----";
                playerPosition = "----";
            }
            setPlayerCachedInfo(version, num, playerName, playerPosition);
        }
        //catchないとエラー出る　
        catch (Exception e) {
            //エラーメッセージ出す
            Log.e("キャッチ", "エラー", e);
        } finally {
            //解放
            dbR.close();
        }

    }

    /**
     * 1つ(1人)ずつデータベースに登録されている情報をキャッシュ
     *
     * @param version
     * @param num
     * @param name
     * @param position
     */
    private void setPlayerCachedInfo(int version, int num, String name, String position) {
        switch (version) {
            case 1:
                CachedPlayerNamesInfo.instance.setNameNormal(num, name);
                CachedPlayerPositionsInfo.instance.setPositionNormal(num, position);
                break;
            case 2:
                CachedPlayerNamesInfo.instance.setNameDh(num, name);
                CachedPlayerPositionsInfo.instance.setPositionDh(num, position);
                break;
            case 3:
                CachedPlayerNamesInfo.instance.setNameAll10(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll10(num, position);
                break;
            case 4:
                CachedPlayerNamesInfo.instance.setNameAll11(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll11(num, position);
                break;
            case 5:
                CachedPlayerNamesInfo.instance.setNameAll12(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll12(num, position);
                break;
            case 6:
                CachedPlayerNamesInfo.instance.setNameAll13(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll13(num, position);
                break;
            case 7:
                CachedPlayerNamesInfo.instance.setNameAll14(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll14(num, position);
                break;
            case 8:
                CachedPlayerNamesInfo.instance.setNameAll15(num, name);
                CachedPlayerPositionsInfo.instance.setPositionAll15(num, position);
                break;
        }

    }


}
