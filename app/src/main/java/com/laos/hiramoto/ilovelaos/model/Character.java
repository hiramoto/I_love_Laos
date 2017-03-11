package com.laos.hiramoto.ilovelaos.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Entity mapped to table "CHARACTERS".
 */

@Entity
public class Character {

    @Id
    private long _id;
    /** Not-null value. */
    private String category;
    private Integer idx;
    private String character;
    private String char_hatsuon;
    private String char_pronun;
    private String word;
    private String word_hatsuon;
    private String word_pronun;
    private String meaning;
    @Generated(hash = 1062346835)
    public Character(long _id, String category, Integer idx, String character, String char_hatsuon,
            String char_pronun, String word, String word_hatsuon, String word_pronun, String meaning) {
        this._id = _id;
        this.category = category;
        this.idx = idx;
        this.character = character;
        this.char_hatsuon = char_hatsuon;
        this.char_pronun = char_pronun;
        this.word = word;
        this.word_hatsuon = word_hatsuon;
        this.word_pronun = word_pronun;
        this.meaning = meaning;
    }
    @Generated(hash = 1853959157)
    public Character() {
    }
    public long get_id() {
        return this._id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Integer getIdx() {
        return this.idx;
    }
    public void setIdx(Integer idx) {
        this.idx = idx;
    }
    public String getCharacter() {
        return this.character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    public String getChar_hatsuon() {
        return this.char_hatsuon;
    }
    public void setChar_hatsuon(String char_hatsuon) {
        this.char_hatsuon = char_hatsuon;
    }
    public String getChar_pronun() {
        return this.char_pronun;
    }
    public void setChar_pronun(String char_pronun) {
        this.char_pronun = char_pronun;
    }
    public String getWord() {
        return this.word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getWord_hatsuon() {
        return this.word_hatsuon;
    }
    public void setWord_hatsuon(String word_hatsuon) {
        this.word_hatsuon = word_hatsuon;
    }
    public String getWord_pronun() {
        return this.word_pronun;
    }
    public void setWord_pronun(String word_pronun) {
        this.word_pronun = word_pronun;
    }
    public String getMeaning() {
        return this.meaning;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

}