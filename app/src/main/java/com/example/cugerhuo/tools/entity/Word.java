package com.example.cugerhuo.tools.entity;

public class Word {
    String synonym;
    double weight;

    public Word(String synonym, double weight, String tag, String word) {
        this.synonym = synonym;
        this.weight = weight;
        this.tag = tag;
        this.word = word;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    String tag;
    String word;
}
