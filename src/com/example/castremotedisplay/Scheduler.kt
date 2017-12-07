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
            add(WebMedia(15, URL("https://abetterlookingshop.myshopify.com/")))
            add(WebMedia(10, URL("https://abetterlookingshop.myshopify.com/collections/frontpage/products/messenger-bag?variant=38541589506")))
            add(WebMedia(20, URL("https://cdn.shopify.com/s/files/1/0295/8581/products/original_1024x1024.jpg?v=1473284865")))
            add(WebMedia(15, URL("https://abetterlookingshop.myshopify.com/products/gray-fedora?variant=403285465")))
            add(WebMedia(15, URL("https://cdn.shopify.com/s/files/1/0295/8581/products/gray-fedora_1024x1024.jpeg?v=1384377263")))
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
