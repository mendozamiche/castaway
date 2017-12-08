package com.example.castremotedisplay

import android.os.Handler
import java.net.URL

interface PresentationDelegate {
    fun startPresenting(media: CastawayMedia)
    fun stopPresenting()
}

class Scheduler(val presentationDelegate: PresentationDelegate) {

    val items = ArrayList<CastawayMedia>()
    var itemIterator: Iterator<CastawayMedia>
    lateinit var currentRunnable: Runnable

    val handler = Handler()

    init {
        items.apply {
            add(WebMedia(5, URL("https://thumbs.dreamstime.com/b/christmas-sale-sign-background-green-chalkboard-large-star-says-red-white-capital-letters-branches-evergreen-sprigs-34939191.jpg")))
            add(ImageMedia(5, R.drawable.ad1))
            add(ImageMedia(5, R.drawable.ad2))
            add(ImageMedia(5, R.drawable.ad3))
        }
        itemIterator = items.iterator()
    }

    fun start() {
        if (!itemIterator.hasNext()) {
            itemIterator = items.iterator()
        }

        if (itemIterator.hasNext()) {
            val currentItem = itemIterator.next()

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
