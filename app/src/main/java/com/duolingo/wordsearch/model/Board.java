package com.duolingo.wordsearch.model;

import java.util.List;
import java.util.Map;

/**
 * Created by brad on 7/29/17.
 */

public class Board {

    private String source_language;
    private String word;
    private List<List<String>> character_grid;
    private Map<String, String> word_locations;
    private String target_language;

    public String getSource_language() {
        return source_language;
    }

    public String getWord() {
        return word;
    }

    public List<List<String>> getCharacter_grid() {
        return character_grid;
    }

    public Map<String, String> getWord_locations() {
        return word_locations;
    }

    public String getTarget_language() {
        return target_language;
    }

    public void setSource_language(String source_language) {
        this.source_language = source_language;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCharacter_grid(List<List<String>> character_grid) {
        this.character_grid = character_grid;
    }

    public void setWord_locations(Map<String, String> word_locations) {
        this.word_locations = word_locations;
    }

    public void setTarget_language(String target_language) {
        this.target_language = target_language;
    }
}
