package com.laos.hiramoto.ilovelaos;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;

import com.laos.hiramoto.ilovelaos.model.DaoMaster;
import com.laos.hiramoto.ilovelaos.model.DaoSession;
import com.laos.hiramoto.ilovelaos.model.Word;
import com.laos.hiramoto.ilovelaos.model.WordDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WordsFlashFragment extends Fragment {

    @BindView(R.id.adapterFlipper2)AdapterViewFlipper adapterFlipper;
    private boolean isAuto = true;

    private List<Word> wordsList = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFlashFragment newInstance() {
        WordsFlashFragment fragment = new WordsFlashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public WordsFlashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_words_flash,container,false);

        SQLiteDatabase db = new DaoMaster.DevOpenHelper(getActivity(), "laosDb", null).getWritableDatabase();
        DaoSession session = new DaoMaster(db).newSession();
        WordDao dao = session.getWordDao();

        ButterKnife.bind(this, v);
        adapterFlipper.setAdapter(new WordAdapter(getActivity(),dao.loadAll()));
        wordsList = dao.loadAll();
        adapterFlipper.setAutoStart(true);
        adapterFlipper.setFlipInterval(1000);

        return v;
    }

    @OnClick(R.id.switchButton2)
    public  void onSwitch(ImageButton switchButton){

        if(isAuto){
            adapterFlipper.setAutoStart(false);
            adapterFlipper.stopFlipping();
            switchButton.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(),R.drawable.manual));
        }else{
            adapterFlipper.setAutoStart(true);
            adapterFlipper.startFlipping();
            switchButton.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(),R.drawable.auto));
        }
        isAuto = !isAuto;
    }
}
