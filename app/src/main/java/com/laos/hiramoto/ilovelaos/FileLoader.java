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

    static void loadData(Context ctx,Activity act) throws IOException,SQLException {
        InputStream stream = null;
        BufferedReader reader = null;
        List<characters> charsList = new ArrayList<>();


        try {

            SQLiteDatabase db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
            DaoSession daoSession = new DaoMaster(db).newSession();
            daoSession.getCharactersDao().deleteAll();
            daoSession.getDictionaryDao().deleteAll();
            daoSession.getWordsDao().deleteAll();



            AssetManager asset = ctx.getResources().getAssets();
            stream = asset.open("characters.csv");

            reader = new BufferedReader(new InputStreamReader(stream));

            Long i = 0L;
            for( String line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(",");
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

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        DaoSession daoSession = new DaoMaster(db).newSession();

        charactersDao charDao = daoSession.getCharactersDao();
        charDao.insertInTx(charsList);


        List<dictionary> dicList = new ArrayList<>();
        try {
            AssetManager asset = ctx.getResources().getAssets();
            stream = asset.open("dictionary.csv");

            reader = new BufferedReader(new InputStreamReader(stream));

            Long i = 0L;
            for( String line = ""; (line = reader.readLine()) != null; i++)
            {
                String [] result = line.split(",");
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

        db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
        dictionaryDao dicDao = daoSession.getDictionaryDao();
        dicDao.insertInTx(dicList);


        List<words> wordsList = new ArrayList<words>();
        try {
            AssetManager asset = ctx.getResources().getAssets();
            stream = asset.open("words.csv");

            reader = new BufferedReader(new InputStreamReader(stream));

            Long i = 0L;
            for( String line = ""; (line = reader.readLine()) != null;i++ )
            {
                String [] result = line.split(",");
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

        db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
        wordsDao wordsDao = daoSession.getWordsDao();
        wordsDao.insertInTx(wordsList);

    }
}
