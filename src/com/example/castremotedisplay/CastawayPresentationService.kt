/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.castremotedisplay

import android.content.Context
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

import com.google.android.gms.cast.CastPresentation
import com.google.android.gms.cast.CastRemoteDisplayLocalService

/**
 * Service to keep the remote display running even when the app goes into the background
 */
class CastawayPresentationService : CastRemoteDisplayLocalService() {

    // First screen
    private var mPresentation: CastPresentation? = null
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        // Audio
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mMediaPlayer?.setVolume(0.1.toFloat(), 0.1.toFloat())
        mMediaPlayer?.isLooping = true
    }

    override fun onCreatePresentation(display: Display) {
        createPresentation(display)
    }

    override fun onDismissPresentation() {
        dismissPresentation()
    }

    private fun dismissPresentation() {
        if (mPresentation != null) {
            mMediaPlayer?.stop()
            mPresentation?.dismiss()
            mPresentation = null
        }
    }

    private fun createPresentation(display: Display) {
        dismissPresentation()
        mPresentation = CastAwayPresentationService(this, display)

        try {
            mPresentation?.show()
            mMediaPlayer?.start()
        } catch (ex: WindowManager.InvalidDisplayException) {
            Log.e(TAG, "Unable to show presentation, display was removed.", ex)
            dismissPresentation()
        }
    }

    /**
     * The presentation to show on the first screen (the TV).
     *
     *
     * Note that this display may have different metrics from the display on
     * which the main activity is showing so we must be careful to use the
     * presentation's own [Context] whenever we load resources.
     *
     */
    private inner class CastAwayPresentationService(context: Context, display: Display) : CastPresentation(context, display) {

        private val TAG = "FirstScreenPresentation"

        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.first_screen_layout_castaway)

            val titleTextView = findViewById<TextView>(R.id.title)
            val typeface = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
            titleTextView.typeface = typeface
            val webView = findViewById<WebView>(R.id.castaway_webview)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("http://www.google.com")
            webView.webViewClient = WebViewController()
        }

        private inner class WebViewController : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    companion object {

        private val TAG = "PresentationService"
    }
}
