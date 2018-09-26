package com.zhuorui.iphotoviewlibrary.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.PhotoView
import com.zhuorui.iphotoviewlibrary.ImageLoader

class PhotoViewAdapter(context: Context, imageLoader: ImageLoader, onClickDismiss: View.OnClickListener) : PagerAdapter() {

    private var imageUrls: ArrayList<Any>? = null
    private var context: Context? = null
    private var imageLoader: ImageLoader? = null
    private var onClickDismiss: View.OnClickListener? = null

    init {
        this.context = context
        this.imageLoader = imageLoader
        this.onClickDismiss = onClickDismiss
    }

    private var mChildCount = 0

    override fun getCount(): Int = mChildCount

    override fun notifyDataSetChanged() {
        mChildCount = imageUrls?.size ?: 0
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

    override fun isViewFromObject(view: View, o: Any): Boolean = view === o

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        container.removeView(o as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val img = imageUrls?.get(position)
        val photoView = PhotoView(context)
        container.addView(photoView)
        imageLoader?.loadImage(context!!, img!!, photoView)
        photoView.setOnClickListener {
            onClickDismiss?.onClick(it)
        }
        return photoView
    }

    fun setData(imageUrls: ArrayList<Any>) {
        this.imageUrls = imageUrls
        notifyDataSetChanged()
    }

    fun getData(): List<Any>? = imageUrls

    fun removeData(position: Int) {
        this.imageUrls?.removeAt(position)
        notifyDataSetChanged()
    }


}
