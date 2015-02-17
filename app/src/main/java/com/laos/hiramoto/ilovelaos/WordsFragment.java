package com.laos.hiramoto.ilovelaos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class WordsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int idx = 1;

    private OnFragmentInteractionListener mListener;

    private LoopEngine loopEngine = new LoopEngine();

    private  int serial = 0;

    private static final String[] COLUMNS = {"_id", "laotian", "kana", "japanese"};
    SQLiteDatabase db;
    private DataBaseHelper mDbHelper;

    private Cursor findData(int id) {
        Cursor cursor = db.query("words", COLUMNS, "_id=" + id, null, null, null, "_id");
        return cursor;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance(String param1, String param2) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setDatabase();
    }

    private void setDatabase() {
        mDbHelper = new DataBaseHelper(this.getActivity());
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_words,container,false);

        Button start = (Button)v.findViewById(R.id.button);
        Button stop = (Button)v.findViewById(R.id.button2);

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

        Button words = (Button)v.findViewById(R.id.button3);
        words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WordsFragment.this.getActivity().getIntent();
                intent.setClass(WordsFragment.this.getActivity().getApplicationContext(),
                        WordsFlashActivity.class);
                WordsFragment.this.startActivity(intent);
            }
        });

        return v;

    }

    public void update() {

        //TextViewに表示
        StringBuilder text = new StringBuilder();

        try{
            serial++;
            Cursor cursor = findData(serial);
            boolean hasdata = cursor.moveToFirst();
            if(hasdata){
                cursor.moveToFirst();
                text.append(cursor.getString(1));
                text.append("\n");
                text.append(cursor.getString(2));
                text.append("\n");
                text.append(cursor.getString(3));
                text.append("\n");
            }else{
                serial = 0;
            }
        }finally{
            //db.close();
        }
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
                WordsFragment.this.update();//自信が発したメッセージを取得してupdateを実行
                sendMessageDelayed(obtainMessage(0), 1000);//100ミリ秒後にメッセージを出力
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
