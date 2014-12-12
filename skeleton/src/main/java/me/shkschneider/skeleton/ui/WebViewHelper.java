package me.shkschneider.skeleton.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import org.apache.http.protocol.HTTP;

import me.shkschneider.skeleton.SkeletonApplication;
import me.shkschneider.skeleton.helper.AndroidHelper;
import me.shkschneider.skeleton.data.FileHelper;
import me.shkschneider.skeleton.helper.LogHelper;

public class WebViewHelper {

    public static final String META_VIEWPORT = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=yes\">";
    public static final String CHARSET = HTTP.UTF_8;
    public static final String MIME_TYPE = "text/html";

    public static WebView fromUrl(@NonNull final String url) {
        final WebView webView = new WebView(SkeletonApplication.CONTEXT);
        webView.loadUrl(url);
        return webView;
    }

    public static WebView fromAsset(@NonNull final String asset) {
        final WebView webView = new WebView(SkeletonApplication.CONTEXT);
        webView.loadDataWithBaseURL(FileHelper.PREFIX_ASSETS, asset, MIME_TYPE, CHARSET, "");
        return webView;
    }

    public static WebView fromRaw(@NonNull final String raw) {
        final WebView webView = new WebView(SkeletonApplication.CONTEXT);
        webView.loadDataWithBaseURL(FileHelper.join(FileHelper.PREFIX_RES, "raw"), raw, MIME_TYPE, CHARSET, "");
        return webView;
    }

    public static WebView fromHtml(@NonNull final String source) {
        final WebView webView = new WebView(SkeletonApplication.CONTEXT);
        webView.loadData(source, MIME_TYPE, CHARSET);
        return webView;
    }

    @SuppressLint("AddJavascriptInterface") // Dangerous below API-17
    public static boolean javascriptInterface(@NonNull final WebView webView, @NonNull final Object javascriptInterface, @NonNull final String name) {
        webView.addJavascriptInterface(javascriptInterface, name);
        return true;
    }

    @SuppressLint("AddJavascriptInterface") // Dangerous below API-17
    public static boolean javascriptInterface(@NonNull final WebView webView, @NonNull final Object javascriptInterface) {
        return javascriptInterface(webView, javascriptInterface, AndroidHelper.PLATFORM);
    }

    public static boolean back(@NonNull final WebView webView) {
        if (! webView.canGoBack()) {
            LogHelper.info("WebView cannot go back");
            return false;
        }

        webView.goBack();
        return true;
    }

    public static boolean forward(@NonNull final WebView webView) {
        if (! webView.canGoForward()) {
            LogHelper.info("WebView cannot go forward");
            return false;
        }

        webView.goForward();
        return true;
    }

    public static String original(@NonNull final WebView webView) {
        return webView.getOriginalUrl();
    }

}