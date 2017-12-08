package com.example.castremotedisplay

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import java.net.URL


class CastawayView : RelativeLayout {

    private lateinit var imageView: ImageView
    private lateinit var webView1: WebView
    private lateinit var webView2: WebView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.web_and_image_view, this, true)

        imageView = findViewById(R.id.castaway_imageview) as ImageView
        webView1 = findViewById(R.id.castaway_webview_1) as WebView
        webView2 = findViewById(R.id.castaway_webview_2) as WebView
        webView1.settings.javaScriptEnabled = true
        webView2.settings.javaScriptEnabled = true
    }

    fun showInImageView(id: Int) {
        webView1.visibility = View.GONE
        webView2.visibility = View.GONE
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(id)
    }

    fun showInWebView(stringUrl: String) {
        imageView.visibility = View.GONE
        if (webView2.visibility == View.VISIBLE) {
            webView1.visibility = View.GONE
            webView2.visibility = View.VISIBLE
            webView2.loadUrl(stringUrl)
            webView2.settings.useWideViewPort = true
            webView2.settings.loadWithOverviewMode = true
        } else {
            webView1.visibility = View.VISIBLE
            webView2.visibility = View.GONE
            webView1.loadUrl(stringUrl)
        }
    }

}
