package com.example.castremotedisplay

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import java.net.URL


class CastawayView : RelativeLayout {

    private lateinit var imageView: ImageView
    private lateinit var webView: WebView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.web_and_image_view, this, true)

        imageView = findViewById(R.id.castaway_imageview) as ImageView
        webView = findViewById(R.id.castaway_webview) as WebView
    }

    fun updateImage(@DrawableRes drawableResId: Int) {
        imageView.setImageResource(drawableResId)
    }

    fun updateWebView(url: URL) {
        webView.loadUrl(url.toString())
    }

}
