package com.laos.hiramoto.ilovelaos;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hiramoto on 2015/08/20.
 */
public class FileLoader {
    private static String fileNameChar = "characters.csv";
    private static String fileNameDic = "dictionary.csv";
    private static String fileNameWords = "words.csv";


    private static  String delimiter = ",";
    private static Long i = 0L;
    private static String line = "";

    static DaoSession getDaoSession(Activity act){
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        return new DaoMaster(db).newSession();
    }

    static BufferedReader getBufferReader(Context ctx, String  fileName) throws IOException{
        try
        {
            AssetManager asset = ctx.getResources().getAssets();
            InputStream stream = asset.open(fileName);
            return new BufferedReader(new InputStreamReader(stream));
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    static void loadCharacterData(Context ctx, Activity act){
        List<characters> charsList = new ArrayList<>();

        charactersDao charDao = getDaoSession(act).getCharactersDao();
        Long count = charDao.count();

        try {

            BufferedReader reader = getBufferReader(ctx,fileNameChar);

            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                characters chars = new characters(i
                        ,result[0]
                        ,Integer.parseInt(result[1])
                        ,result[2]
                        ,result[3]
                        ,result[4]
                        ,result[5]
                        ,result[6]
                        ,result[7]
                        ,result[8]);
                charsList.add(chars);
            }
            reader.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        charDao.insertInTx(charsList);

    }

    static void loadDictionaryData(Context ctx,Activity act){
        List<dictionary> dicList = new ArrayList<>();

        dictionaryDao dicDao = getDaoSession(act).getDictionaryDao();
        Long count = dicDao.count();
        try {

            BufferedReader reader = getBufferReader(ctx,fileNameDic);

            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                dictionary dic = new dictionary(i
                        ,result[0]
                        ,result[1]
                        ,result[2]
                        ,result[3]);
                dicList.add(dic);
            }
            reader.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        dicDao.insertInTx(dicList);

    }

    static void loadWordData(Context ctx,Activity act){
        List<words> wordsList = new ArrayList<>();

        wordsDao wordsDao = getDaoSession(act).getWordsDao();
        Long count = wordsDao.count();

        try {

            BufferedReader reader = getBufferReader(ctx,fileNameWords);

            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                words words = new words(i
                        ,result[0]
                        ,result[1]
                        ,result[2]
                        ,result[3]
                        ,result[4]
                        ,0);
                //TODO:最後のIntegerの利用方法を検討する。
                wordsList.add(words);
            }
            reader.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        wordsDao.insertInTx(wordsList);
    }

    static void loadData(Context ctx,Activity act) throws IOException,SQLException {

        //Upgradeの際にデータはすべて消されるため、件数チェックで確認
        //wordsはカスタマイズできるようにする予定なので、件数チェックから外す
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        DaoSession daoSession = new DaoMaster(db).newSession();

        if(daoSession.getCharactersDao().count() !=0
                || daoSession.getDictionaryDao().count() != 0){
            return;
        }

        loadCharacterData(ctx,act);
        loadDictionaryData(ctx,act);
        loadWordData(ctx,act);

    }
}
