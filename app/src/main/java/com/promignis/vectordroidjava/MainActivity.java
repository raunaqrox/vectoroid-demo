package com.promignis.vectordroidjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private SvgRenderer svgRenderer;
    private SvgJSInterface svgJSInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        svgJSInterface = new SvgJSInterface(this);
        ImageView parent = findViewById(R.id.parent);
        svgRenderer = new SvgRenderer(this, parent);
        svgRenderer.addJsInterface(svgJSInterface, "SVG");
        svgRenderer.loadUrl("http://172.16.21.96:8080");
    }

    public void renderSvg(String svg) {
        this.svgRenderer.startRender(svg);
    }
}
