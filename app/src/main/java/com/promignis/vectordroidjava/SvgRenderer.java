package com.promignis.vectordroidjava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Path;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by sahebjot.singh on 01/08/17.
 */

public class SvgRenderer {

    private WebView browser;
    private Activity ctx;
    private ImageView container;
    private XmlPullParser parser;

    SvgRenderer(Activity ctx, ImageView container) {
        this.ctx = ctx;
        this.container = container;
        setupWebView();
    }

    private void setupWebView() {
        browser = new WebView(ctx.getApplicationContext());
        browser.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        browser.setWebChromeClient(new WebChromeClient());
        WebViewClient webViewClient = new WebViewClient();
        browser.setWebViewClient(webViewClient);
        try {
            initXmlParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public XmlPullParser createParser(String svg) {

        InputStream svgStream = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                svgStream = new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));
            } else {
                svgStream = new ByteArrayInputStream(svg.getBytes("UTF-8"));
            }

            parser.setInput(svgStream, null);
            parser.nextTag();
            Log.d("svg!", svg);
            parser.require(XmlPullParser.START_TAG, null, "svg");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parser;
    }

    public void startRender(String svg) {
        parser = createParser(svg);

        try {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                render(parser);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start xml parser

    }

    public void render(XmlPullParser parser) {
        Log.d("svg!", parser.getName());
        switch (parser.getName()) {
            case "path":
                String pathData = parser.getAttributeValue(null, "d");
                String fill = parser.getAttributeValue(null, "fill");
                Path p = new Path();
//                readPath(pathData, p);
                break;
            default:
                break;
        }
    }

//    private void readPath(String data, Path p) {
//        try {
//            String[] tokens = data.split("[ ,]");
//            int i = 0;
//            while (i < tokens.length) {
//                String token = tokens[i++];
//                if (token.equals("M")) {
//                    float x = Float.valueOf(tokens[i++]);
//                    float y = Float.valueOf(tokens[i++]);
//                    p.moveTo(x, y);
//                } else
//                if (token.equals("L")) {
//                    float x = Float.valueOf(tokens[i++]);
//                    float y = Float.valueOf(tokens[i++]);
//                    p.lineTo(x, y);
//                } else
//                if (token.equals("C")) {
//                    float x1 = Float.valueOf(tokens[i++]);
//                    float y1 = Float.valueOf(tokens[i++]);
//                    float x2 = Float.valueOf(tokens[i++]);
//                    float y2 = Float.valueOf(tokens[i++]);
//                    float x3 = Float.valueOf(tokens[i++]);
//                    float y3 = Float.valueOf(tokens[i++]);
//                    p.cubicTo(x1, y1, x2, y2, x3, y3);
//                } else
//                if (token.equals("z")) {
//                    p.close();
//                } else {
//                    throw new RuntimeException("unknown command [" + token + "]");
//                }
//            }
//        } catch (IndexOutOfBoundsException e) {
//            throw new RuntimeException("bad data ", e);
//        }
//    }

    public void initXmlParser() throws XmlPullParserException {
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    }

    public void addJsToWebView(final String js) {
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(browser != null) {
                    browser.loadUrl("javascript:" + js);
                }
            }
        });
    }

    public void loadUrl(String url) {
        browser.loadUrl(url);
    }

    @SuppressLint("JavascriptInterface")
    public void addJsInterface(Object object, String reference) {
        browser.addJavascriptInterface(object, reference);
    }
}
