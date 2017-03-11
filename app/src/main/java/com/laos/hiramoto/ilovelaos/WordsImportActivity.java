package com.laos.hiramoto.ilovelaos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.laos.hiramoto.ilovelaos.model.DaoMaster;
import com.laos.hiramoto.ilovelaos.model.Word;
import com.laos.hiramoto.ilovelaos.model.WordDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class WordsImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_import);

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

        // ダイアログの設定
        alertDialog.setTitle("I Love Laos 単語 インポート");      //タイトル設定
        alertDialog.setMessage("単語リストを置き換えますか？");  //内容(メッセージ)設定

        // OKボタンの設定
        alertDialog.setPositiveButton("置き換え", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OKボタン押下時の処理
                String result = isImportSucceed(true) ? "取込成功しました。":"取込失敗しました。";
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });

        // SKIPボタンの設定
        alertDialog.setNeutralButton("追加", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // SKIPボタン押下時の処理
                String result = isImportSucceed(false) ? "取込成功しました。":"取込失敗しました。";
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });

        // NGボタンの設定
        alertDialog.setNegativeButton("中止", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // NGボタン押下時の処理
                Toast.makeText(getApplicationContext(), "取込処理を中止しました。", Toast.LENGTH_LONG).show();
            }
        });
        // ダイアログの作成と描画
        alertDialog.show();
    }


    private boolean isImportSucceed(Boolean isAppend){

        List<Word> wordsList;

        // データ取得
        try{
            SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, "laosDb", null).getWritableDatabase();
            Long count = (new DaoMaster(db).newSession()).getWordDao().count();

            // 形式チェック
            InputStream stream = getContentResolver().openInputStream(getIntent().getData());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            wordsList = FileLoader.getWordList(reader, count);

        }catch(IOException e){
            Toast.makeText(this, "ファイルの読み込みに失敗しました。", Toast.LENGTH_LONG).show();
            return false;
        }

        // データ投入
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, "laosDb", null).getWritableDatabase();
        db.beginTransaction();
        try{
            WordDao wordsDao = (new DaoMaster(db).newSession()).getWordDao();
            // 上書きの場合は、全削除
            if(!isAppend) wordsDao.deleteAll();
            wordsDao.insertInTx(wordsList);
            db.setTransactionSuccessful();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return  false;
        }finally {
            db.endTransaction();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_words_import, menu);
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
