package com.example.castremotedisplay

import java.net.URL

/**
 * Created by krisorr on 2017-12-07.
 */


interface  CastawayMedia {
     var duration : Long
}

data class WebMedia(override var duration: Long, val url: URL): CastawayMedia

data class ImageMedia(override var duration: Long, val imageName: String): CastawayMedia