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
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.cast.CastPresentation
import com.google.android.gms.cast.CastRemoteDisplayLocalService
import android.widget.ProgressBar


/**
 * Service to keep the remote display running even when the app goes into the background
 */
class CastawayPresentationService : CastRemoteDisplayLocalService(), PresentationDelegate {

    // First screen
    private var mPresentation: CastPresentation? = null
    private var mMediaPlayer: MediaPlayer? = null
    lateinit private var mScheduler: Scheduler


    override fun onCreate() {
        super.onCreate()
        mScheduler = Scheduler(this)

        // Audio
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mMediaPlayer?.setVolume(0.1.toFloat(), 0.1.toFloat())
        mMediaPlayer?.isLooping = true
        mScheduler.start()
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
        mPresentation = CastAwayPresentation(this, display)

        try {
            mPresentation?.show()
            mMediaPlayer?.start()
        } catch (ex: WindowManager.InvalidDisplayException) {
            Log.e(TAG, "Unable to show presentation, display was removed.", ex)
            dismissPresentation()
        }
    }

    override fun startPresenting(media: CastawayMedia) {
        when (media) {
            is WebMedia -> showInWebView(media)
            is ImageMedia -> showInImageView(media)
        }
    }

    private fun showInWebView(media: WebMedia) {
        mPresentation?.apply {
            val castawayView = findViewById<CastawayView>(R.id.castaway_view)
            castawayView.showInWebView(media.url.toString())
        }
    }

    private fun showInImageView(media: ImageMedia) {
        mPresentation?.apply {
            val castAwayPresentation = mPresentation as CastAwayPresentation
            castAwayPresentation.castawayView.showInImageView(media.id)
        }
    }

    override fun stopPresenting() {
        // Nothing to do for now
    }

    companion object {

        private val TAG = "PresentationService"
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
    private inner class CastAwayPresentation(context: Context, display: Display) : CastPresentation(context, display) {

        private val TAG = "FirstScreenPresentation"

        lateinit var castawayView: CastawayView
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.first_screen_layout_castaway)

            castawayView = findViewById<CastawayView>(R.id.castaway_view)

//            val webView = findViewById<WebView>(R.id.castaway_webview)
//            webView.settings.javaScriptEnabled = true
//            webView.webViewClient = WebViewController()
        }

        private inner class WebViewController : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.visibility = View.GONE
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val animation = AnimationUtils.loadAnimation(baseContext, android.R.anim.slide_in_left)
                view?.startAnimation(animation)
                view?.visibility = View.VISIBLE;
                super.onPageFinished(view, url)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }
        }
    }
}
