package com.promignis.vectordroidjava;

import android.app.Activity;

/**
 * Created by sahebjot.singh on 01/08/17.
 */

public class SvgJSInterface {

    private Activity ctx;

    SvgJSInterface(Activity ctx) {
        this.ctx = ctx;
    }


    @android.webkit.JavascriptInterface
    public void render(String svgString) {
        ((MainActivity)ctx).renderSvg(svgString);
    }
}
