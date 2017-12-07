package com.example.castremotedisplay

import android.os.Handler
import java.net.URL

interface PresentationDelegate {
    fun startPresenting(media: CastawayMedia)
    fun stopPresenting()
}


class Scheduler(val presentationDelegate: PresentationDelegate){

    val items = ArrayList<CastawayMedia>()
    var itemIterator : Iterator<CastawayMedia>
    lateinit var currentRunnable : Runnable

    val handler = Handler()

    init {
        items.apply {
            add(WebMedia(5, URL("http://www.google.com")))
            add(ImageMedia(10, R.drawable.cat))
            add(WebMedia(5, URL("http://www.shopify.com")))
        }
        itemIterator = items.iterator()
    }


    fun start() {

        if (itemIterator.hasNext()) {
            val currentItem = itemIterator.next()

            presentationDelegate.startPresenting(currentItem)

            handler.postDelayed({
                presentationDelegate.stopPresenting()
                start()
            }, currentItem.duration)
        }

    }

    fun stop() {
        presentationDelegate.stopPresenting()
        handler.removeCallbacks(currentRunnable)
    }

}
