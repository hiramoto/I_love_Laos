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

import butterknife.ButterKnife;

/**
 * Created by Hiramoto on 2015/08/29.
 */
public class CharacterAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    List<characters> charList = new ArrayList<>();

    public CharacterAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CharacterAdapter(Context context,List<characters> charList) {
        this(context);
        this.charList = charList;
    }

    @Override
    public int getCount() {
        return charList.size();
    }

    @Override
    public Object getItem(int position) {
        return charList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return charList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.character_area,parent,false);

        TextView laoLetter = ButterKnife.findById(convertView,R.id.textView4);
        TextView pronunciation = ButterKnife.findById(convertView, R.id.textView8);
        TextView word = ButterKnife.findById(convertView, R.id.textView9);
        TextView meaning = ButterKnife.findById(convertView, R.id.textView11);
        TextView laoWord = ButterKnife.findById(convertView,R.id.textView2);

        laoLetter.setText(charList.get(position).getCharacter());
        pronunciation.setText(charList.get(position).getChar_hatsuon());
        laoWord.setText(charList.get(position).getWord());
        word.setText(charList.get(position).getWord_hatsuon());
        meaning.setText(charList.get(position).getMeaning());

        return convertView;
    }
}
