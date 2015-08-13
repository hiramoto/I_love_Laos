package com.laos.hiramoto.ilovelaos;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Hiramoto on 2015/06/04.
 */
public class DictionaryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<WordEntry> wordsList = new ArrayList<WordEntry>();

    public DictionaryAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setDatabase();
    }

    private void setDatabase() {
        mDbHelper = new DataBaseHelper(context.getApplicationContext());
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    private static final String[] COLUMNS = {"_id", "yomi", "wordJpn", "wordLao", "typeLaoScript"};
    SQLiteDatabase db;
    private DataBaseHelper mDbHelper;

    public ArrayList<WordEntry> findData(String param) {
        if (param == null || param.length() <= 0){
            return new ArrayList<WordEntry>();
        }

        ArrayList<WordEntry> newList = new ArrayList<WordEntry>();
        //データがあって、前回と同じ文字をもっているならそこから絞り込み
        //なければ新規取得。
        if( wordsList != null && wordsList.size() > 0){
            String firstWord = wordsList.get(0).getYomi();
            for (int i = 0; i < wordsList.size(); i++) {
                String yomi = wordsList.get(i).yomi;
                if (yomi.contains(param)){
                    newList.add(wordsList.get(i));
                }
            }
        }else {
            try{
                Cursor cursor = db.query("dictionary", COLUMNS, "yomi like ?", new String[]{param + "%"}, null, null, "_id");
                boolean hasData = cursor.moveToFirst();
                while (hasData && !cursor.isAfterLast()){
                    WordEntry entry = new WordEntry();
                    entry.setId(cursor.getInt(0));
                    entry.setYomi(cursor.getString(1));
                    entry.setWordJpn(cursor.getString(2));
                    entry.setWordLao(cursor.getString(3));
                    entry.setTypeLaoScript(cursor.getString(4));
                    newList.add(entry);
                    cursor.moveToNext();
                }
            }finally{
                //db.close();
            }
        }
        wordsList = newList;
        return newList;
    }




    public void setWordsList(ArrayList<WordEntry> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return wordsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.resultrow,parent,false);

        ((TextView)convertView.findViewById(R.id.yomi)).setText(wordsList.get(position).getYomi());
        ((TextView)convertView.findViewById(R.id.wordJpn)).setText(wordsList.get(position).getWordJpn());
        ((TextView)convertView.findViewById(R.id.wordLao)).setText(wordsList.get(position).getWordLao());
        ((TextView)convertView.findViewById(R.id.typeLaoScript)).setText(wordsList.get(position).getTypeLaoScript());
        TextView v = (TextView)convertView.findViewById(R.id.wordLao);
        v.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "saysettha_ot.ttf"));
        v.setText(wordsList.get(position).getWordLao());

        return convertView;
    }
}
