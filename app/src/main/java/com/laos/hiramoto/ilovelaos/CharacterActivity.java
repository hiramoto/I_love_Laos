package com.laos.hiramoto.ilovelaos;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CharacterActivity extends AppCompatActivity {
    @BindView(R.id.adapterFlipper)AdapterViewFlipper adapterFlipper;
    private boolean isAuto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        ButterKnife.bind(this);

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, "laosDb", null).getWritableDatabase();
        DaoSession session = new DaoMaster(db).newSession();
        charactersDao dao = session.getCharactersDao();
        adapterFlipper.setAdapter(new CharacterAdapter(this,dao.loadAll()));
        adapterFlipper.setAutoStart(true);
        adapterFlipper.setFlipInterval(1000);

    }

    @OnClick(R.id.switchButton)
    public  void onSwitch(ImageButton switchButton){

        if(isAuto){
            adapterFlipper.setAutoStart(false);
            adapterFlipper.stopFlipping();
            switchButton.setImageDrawable(getDrawable(R.drawable.manual));
        }else{
            adapterFlipper.setAutoStart(true);
            adapterFlipper.startFlipping();
            switchButton.setImageDrawable(getDrawable(R.drawable.auto));
        }
        isAuto = !isAuto;
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
