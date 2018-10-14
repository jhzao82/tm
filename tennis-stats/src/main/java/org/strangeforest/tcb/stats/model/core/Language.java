package org.strangeforest.tcb.stats.model.core;

public enum Language {
    ZH("简体中文"),
    TW("繁体中文"),
    EN("English");
    public final String text;

    Language(String text) {
        this.text = text;
    }
}
