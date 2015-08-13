package com.laos.hiramoto.ilovelaos;

/**
 * Created by Hiramoto on 2015/06/04.
 */
public class WordEntry {

    int id;
    String yomi = null;
    String wordJpn = null;
    String wordLao = null;
    String typeLaoScript = null;

    public String getYomi() {
        return yomi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    public String getWordJpn() {
        return wordJpn;
    }

    public void setWordJpn(String wordJpn) {
        this.wordJpn = wordJpn;
    }

    public String getWordLao() {
        return wordLao;
    }

    public void setWordLao(String wordLao) {
        this.wordLao = wordLao;
    }

    public String getTypeLaoScript() {
        return typeLaoScript;
    }

    public void setTypeLaoScript(String typeLaoScript) {
        this.typeLaoScript = typeLaoScript;
    }

}
