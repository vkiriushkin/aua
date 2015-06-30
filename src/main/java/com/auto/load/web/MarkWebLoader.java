package com.auto.load.web;

public class MarkWebLoader {

    private static MarkWebLoader webLoader;

    private MarkWebLoader() {}

    public static MarkWebLoader getInstance() {
        if (webLoader == null)
            webLoader = new MarkWebLoader();

        return webLoader;
    }
}
