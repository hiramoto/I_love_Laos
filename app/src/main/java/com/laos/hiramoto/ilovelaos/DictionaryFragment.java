package com.laos.hiramoto.ilovelaos;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DictionaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DictionaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DictionaryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    private List<dictionary> dicList;
    private String lastWords = "";

    public static DictionaryFragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_dictionary,container,false);

        TextWatcher watchHandler = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showResult();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        ((EditText)v.findViewById(R.id.editText)).addTextChangedListener(watchHandler);

        //選択時のイベントセット
        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        return v;
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //キーボード表示中の場合は隠して処理終了
        InputMethodManager inputMethodManager  = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //Viewにフォーカスを移す
        view.requestFocus();
        return;

        //TODO:長押しでActivity起動するように変える
        //view.findViewById(R.id.typeLaoScript);
        //String  itemValue    = ((TextView) view.findViewById(R.id.wordLao)).getText().toString();
        //TODO:別Activityでデータを表示する。
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

    private void showResult(){

        String param = ((EditText)getActivity().findViewById(R.id.editText)).getText().toString();

        ArrayList<dictionary> newList = new ArrayList<>();
        //データがあって、前回と同じ文字をもっているならそこから絞り込み
        //なければ新規取得。
        if( param.startsWith(lastWords)
                && dicList != null
                && dicList.size() > 0){

            for (int i = 0; i < dicList.size(); i++) {
                String yomi = dicList.get(i).getYomi();
                if (yomi.contains(param)){
                    newList.add(dicList.get(i));
                }
            }
            dicList = newList;

            if (dicList.size() != 0) return;

        }

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(getActivity(), "laosDb", null).getWritableDatabase();
        DaoSession session = new DaoMaster(db).newSession();
        dictionaryDao dao = session.getDictionaryDao();
        de.greenrobot.dao.query.Query<dictionary> query = dao.queryBuilder().where(
                dictionaryDao.Properties.Yomi.like(param)
        ).orderAsc(
                dictionaryDao.Properties.Yomi).build();
        dicList = query.list();

        lastWords = param;

        DictionaryAdapter adapter = new DictionaryAdapter(this.getActivity().getApplicationContext());
        adapter.setWordsList(dicList);
        ((ListView)getActivity().findViewById(R.id.listView)).setAdapter(adapter);

    }
}
