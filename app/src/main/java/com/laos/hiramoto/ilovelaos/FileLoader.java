package com.laos.hiramoto.ilovelaos;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.laos.hiramoto.ilovelaos.model.Character;
import com.laos.hiramoto.ilovelaos.model.CharacterDao;
import com.laos.hiramoto.ilovelaos.model.DaoMaster;
import com.laos.hiramoto.ilovelaos.model.DaoSession;
import com.laos.hiramoto.ilovelaos.model.Dictionary;
import com.laos.hiramoto.ilovelaos.model.DictionaryDao;
import com.laos.hiramoto.ilovelaos.model.Word;
import com.laos.hiramoto.ilovelaos.model.WordDao;

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

    private static DaoSession getDaoSession(Activity act){
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(act, "laosDb", null).getWritableDatabase();
        return new DaoMaster(db).newSession();
    }

    private static BufferedReader getBufferReader(Context ctx, String  fileName) throws IOException{
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

    private static void loadCharacterData(Context ctx, Activity act){
        List<Character> charsList = new ArrayList<>();

        CharacterDao charDao = getDaoSession(act).getCharacterDao();
        Long count = charDao.count();

        try {

            BufferedReader reader = getBufferReader(ctx,fileNameChar);

            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                Character chars = new Character(i
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

    private static void loadDictionaryData(Context ctx,Activity act){
        List<Dictionary> dicList = new ArrayList<>();

        DictionaryDao dicDao = getDaoSession(act).getDictionaryDao();
        Long count = dicDao.count();
        try {

            BufferedReader reader = getBufferReader(ctx,fileNameDic);

            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                Dictionary dic = new Dictionary(i
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

    /***
     *
     * @param reader
     * @param count startCount
     * @return List<Word>
     */
    static List<Word> getWordList(BufferedReader reader, Long count){
        List<Word> wordsList = new ArrayList<>();
        try {
            for(i = count== 0L ? 0L : count,line = ""; (line = reader.readLine()) != null; i++ )
            {
                String [] result = line.split(delimiter);
                Word words = new Word(i
                        ,result[0]
                        ,result[1]
                        ,result[2]
                        ,result[3]
                        ,result[4]
                        ,0);
                //TODO:最後のIntegerの利用方法を検討する。
                wordsList.add(words);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return wordsList;
    }

    private static void loadWordData(Context ctx,Activity act){
        List<Word> wordsList = new ArrayList<>();

        WordDao wordsDao = getDaoSession(act).getWordDao();
        Long count = wordsDao.count();

        try {
            BufferedReader reader = getBufferReader(ctx,fileNameWords);
            wordsList = getWordList(reader,count);
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

        if(daoSession.getCharacterDao().count() !=0
                || daoSession.getDictionaryDao().count() != 0){
            return;
        }

        loadCharacterData(ctx,act);
        loadDictionaryData(ctx,act);
        loadWordData(ctx,act);

    }
}
