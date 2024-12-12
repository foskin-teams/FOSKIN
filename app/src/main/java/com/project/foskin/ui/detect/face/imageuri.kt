package com.project.foskin.ui.detect.face

import android.util.Log

class imageuri {
    companion object{
        var EXTRA_IMAGE_URI_FRONT = "EXTRA_IMAGE_URI_FRONT"
        var EXTRA_IMAGE_URI_LEFT = "EXTRA_IMAGE_URI_LEFT"
        var EXTRA_IMAGE_URI_RIGHT = "EXTRA_IMAGE_URI_RIGHT"

        fun printUri() {
            Log.d("ImageUri", "Front URI: $EXTRA_IMAGE_URI_FRONT")
            Log.d("ImageUri", "Left URI: $EXTRA_IMAGE_URI_LEFT")
            Log.d("ImageUri", "Right URI: $EXTRA_IMAGE_URI_RIGHT")
        }
    }
}