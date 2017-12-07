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
            add(WebMedia(5, URL("https://abetterlookingshop.myshopify.com/")))
            add(ImageMedia(5, R.drawable.cat))
            add(WebMedia(5, URL("https://agoodlookingshop.myshopify.com/")))
            add(WebMedia(5, URL("https://canadianmade.co/")))
        }
        itemIterator = items.iterator()
    }


    fun start() {
        val currentItem: CastawayMedia

        if (!itemIterator.hasNext()) {
            itemIterator = items.iterator()
        }

        if (itemIterator.hasNext()) {
            currentItem = itemIterator.next()

            presentationDelegate.startPresenting(currentItem)

            handler.postDelayed({
                presentationDelegate.stopPresenting()
                start()
            }, currentItem.duration * 1000)
        }

    }

    fun stop() {
        presentationDelegate.stopPresenting()
        handler.removeCallbacks(currentRunnable)
    }

}
