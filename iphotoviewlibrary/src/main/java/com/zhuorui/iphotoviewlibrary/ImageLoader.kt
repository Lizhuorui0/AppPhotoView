package com.zhuorui.iphotoviewlibrary

import android.content.Context
import android.widget.ImageView

abstract class ImageLoader {

     abstract fun loadImage(context: Context, img: Any, view: ImageView)

}
