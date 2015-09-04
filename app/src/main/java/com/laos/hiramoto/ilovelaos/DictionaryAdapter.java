package com.laos.hiramoto.ilovelaos;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiramoto on 2015/08/29.
 */
public class DictionaryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    List<dictionary> wordsList = new ArrayList<>();

    public DictionaryAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setWordsList(List<dictionary> wordsList) {
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
        return wordsList.get(position).get_id();
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
