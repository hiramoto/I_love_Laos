package com.laos.hiramoto.ilovelaos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class WordsFlashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LoopEngine loopEngine = new LoopEngine();

//    private  int serial = 0;

    private List<words> wordsList = null;
    private int idx = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFlashFragment newInstance(String param1, String param2) {
        WordsFlashFragment fragment = new WordsFlashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WordsFlashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(getActivity(), "laosDb", null).getWritableDatabase();
        DaoSession session = new DaoMaster(db).newSession();
        wordsDao dao = session.getWordsDao();
        wordsList = dao.loadAll();

    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_words_flash,container,false);

        Button start = (Button)v.findViewById(R.id.button);
        Button stop = (Button)v.findViewById(R.id.button2);
        Button next = (Button)v.findViewById(R.id.button7);

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

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                update();
            }
        });

        return v;

    }

    public void update() {

        //TextViewに表示
        StringBuilder text = new StringBuilder();

        if((wordsList.size()-1) <= idx) idx = 0;
        words wordsInfo = wordsList.get(idx);
        idx++;

        text.append(wordsInfo.getLaotian()).append("\n");
        if(! (wordsInfo.getKana() == null)){
            text.append(wordsInfo.getKana()).append("\n");
        }
        text.append(wordsInfo.getJapanese()).append("\n");

        TextView v = (TextView)getActivity().findViewById(R.id.textView);
        v.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "saysettha_ot.ttf"));
        v.setText(String.valueOf(text.toString()));

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

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String str = sharedPreferences.getString("Speed","");

                int i = 0;
                try{
                    i = Integer.parseInt(str);
                }catch(NumberFormatException e){
                    i = 3000;
                }

                WordsFlashFragment.this.update();//自身が発したメッセージを取得してupdateを実行
                sendMessageDelayed(obtainMessage(0), i);//100ミリ秒後にメッセージを出力
            }
        }
    };
}
