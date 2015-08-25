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
import java.util.List;


public class CharacterActivity extends ActionBarActivity {

    private LoopEngine loopEngine = new LoopEngine();

    private  int serial = 0;

    private List<characters> charList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, "laosDb", null).getWritableDatabase();
        DaoSession session = new DaoMaster(db).newSession();
        charactersDao dao = session.getCharactersDao();
        charList = dao.loadAll();

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
                updateView();//自身が発したメッセージを取得してupdateを実行
                sendMessageDelayed(obtainMessage(0), 1000);//100ミリ秒後にメッセージを出力
            }
        }
    };

    private void updateView() {

        //TextViewに表示
        StringBuilder text = new StringBuilder();

        if(charList.size()-1 < serial) serial = 0;
        characters charInfo = charList.get(serial);
        serial++;

        text.append(charInfo.getCharacter()).append("\n");
        text.append(charInfo.getChar_hatsuon()).append("\n");
        text.append(charInfo.getWord()).append("\n");
        text.append(charInfo.getWord_hatsuon()).append("\n");
        text.append(charInfo.getMeaning()).append("\n");

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
