package com.laos.hiramoto.ilovelaos;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class CharacterActivity extends ActionBarActivity {

    private LoopEngine loopEngine = new LoopEngine();

    private  int serial = 0;

    private static final String[] COLUMNS = { "character", "char_hatsuon","word", "word_hatsuon","meaning"};
    SQLiteDatabase db;
    private DataBaseHelper mDbHelper;

    private Cursor findData(int id) {
        Cursor cursor = db.query("characters", COLUMNS, "_id=" + id, null, null, null, "_id");
        return cursor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        setDatabase();


        Button start = (Button)findViewById(R.id.button5);
        Button stop = (Button)findViewById(R.id.button6);
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                loopEngine.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener(){
            public  void  onClick(View v){
                loopEngine.stop();
            }
        });

    }

    private void setDatabase() {
        mDbHelper = new DataBaseHelper(this);
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    //一定時間後にupdateを呼ぶためのオブジェクト
    class LoopEngine extends Handler {
        private boolean isUpdate;
        public void start(){
            this.isUpdate = true;
            handleMessage(new Message());
        }
        public void stop(){
            this.isUpdate = false;
        }
        @Override
        public void handleMessage(Message msg) {
            this.removeMessages(0);//既存のメッセージは削除
            if(this.isUpdate){
                updateView();//自信が発したメッセージを取得してupdateを実行
                sendMessageDelayed(obtainMessage(0), 1000);//100ミリ秒後にメッセージを出力
            }
        }
    };

    private void updateView() {

        //TextViewに表示
        StringBuilder text = new StringBuilder();

        try{
            serial++;
            Cursor cursor = findData(serial);
            boolean hasdata = cursor.moveToFirst();
            if(hasdata){
                cursor.moveToFirst();
                text.append(cursor.getString(0));
                text.append("\n");
                text.append(cursor.getString(1));
                text.append("\n");
                text.append(cursor.getString(2));
                text.append("\n");
                text.append(cursor.getString(3));
                text.append("\n");
                text.append(cursor.getString(4));
            }else{
                serial = 0;
            }
        }finally{
            //db.close();
        }
        TextView v = (TextView)findViewById(R.id.textView2);
        v.setTypeface(Typeface.createFromAsset(getAssets(), "saysettha_ot.ttf"));
        v.setText(String.valueOf(text.toString()));

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
