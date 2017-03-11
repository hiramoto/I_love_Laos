package com.laos.hiramoto.ilovelaos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.laos.hiramoto.ilovelaos.model.Character;
import com.laos.hiramoto.ilovelaos.model.Word;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Hiramoto on 2015/08/29.
 */
public class WordAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater = null;
    private List<Word> wordList = new ArrayList<>();

    private WordAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected WordAdapter(Context context, List<Word> wordList) {
        this(context);
        this.wordList = wordList;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return wordList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.word_area,parent,false);

        TextView laoLetter = ButterKnife.findById(convertView,R.id.txtLao);
        TextView roman = ButterKnife.findById(convertView, R.id.txtRoman);
        TextView kana = ButterKnife.findById(convertView, R.id.txtKana);
        TextView japanese = ButterKnife.findById(convertView,R.id.txtJapanese);

        laoLetter.setText(wordList.get(position).getLaotian());
        roman.setText(wordList.get(position).getRoman());
        kana.setText(wordList.get(position).getKana());
        japanese.setText(wordList.get(position).getJapanese());

        return convertView;
    }
}
