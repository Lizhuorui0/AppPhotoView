package com.zhuorui.iphotoviewlibrary.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zhuorui.iphotoviewlibrary.R
import com.zhuorui.iphotoviewlibrary.ImageLoader

import com.zhuorui.iphotoviewlibrary.adapter.PhotoViewAdapter
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.fragment_photo.view.*
import kotlin.collections.ArrayList

@SuppressLint("ValidFragment")
class AppPhotoView(builder: AppPhotoView.Companion.Builder) : DialogFragment() {

    private var isDel: Boolean = false
    private var currentPosition: Int = 0
    private var imageList = arrayListOf<Any>()
    private var imageLoader: ImageLoader? = null
    private var onClickDeleteListener: OnClickDeleteListener? = null

    init {
        this.isDel = builder.isDel
        this.currentPosition = builder.currentPosition
        this.imageList.addAll(builder.imageList!!)
        this.imageLoader = builder.imageLoader
        this.onClickDeleteListener = builder.onClickDeleteListener
    }


    companion object {

        class Builder {
            internal var isDel: Boolean = false
            internal var currentPosition: Int = 0
            internal var imageList: ArrayList<Any>? = null
            internal var imageLoader: ImageLoader? = null
            internal var onClickDeleteListener: OnClickDeleteListener? = null


            /**
             * Whether to enable deletion
             */
            fun openDel(isDel: Boolean): AppPhotoView.Companion.Builder {
                this.isDel = isDel
                return this
            }

            /**
             * Set the current image subscript
             */
            fun setImagePosition(currentPosition: Int): AppPhotoView.Companion.Builder {
                this.currentPosition = currentPosition
                return this
            }

            /**
             * Set the collection of images for browsing
             */
            fun setImageList(imageList: ArrayList<Any>): AppPhotoView.Companion.Builder {
                this.imageList = imageList
                return this
            }

            /**
             * Set a single image
             */
            fun setImage(img: Any): AppPhotoView.Companion.Builder {
                this.imageList = arrayListOf(img)
                return this
            }

            /**
             * Must be realized
             * Because this framework does not participate in loading images
             * Set the frame to load the image yourself to load the image
             */
            fun setImageLoader(imageLoader: ImageLoader): AppPhotoView.Companion.Builder {
                this.imageLoader = imageLoader
                return this
            }

            /**
             * Delete callback listener
             */
            fun setOnClickDeleteListener(onClickDeleteListener: OnClickDeleteListener): AppPhotoView.Companion.Builder {
                this.onClickDeleteListener = onClickDeleteListener;
                return this
            }


            fun build(): AppPhotoView {
                if (imageList == null) {
                    throw NullPointerException("Image collection cannot be empty")
                }
                if (imageLoader == null) {
                    throw NullPointerException("Must be realized ImageLoader")
                }
                return AppPhotoView(this)
            }
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back.setOnClickListener { dismiss() }
        constraintLayout.setOnClickListener { dismiss() }
        val adapter = PhotoViewAdapter(context!!, imageLoader!!, View.OnClickListener { dismiss() })
        adapter.setData(imageList)
        viewPagerPhoto.adapter = adapter
        viewPagerPhoto.setCurrentItem(currentPosition, false)
        circleIndicator.setViewPager(viewPagerPhoto)
        viewPagerPhoto.setOnClickListener { dismiss() }
        viewPagerPhoto.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }
        })

        del.visibility = when (isDel) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        del.setOnClickListener {
            if (imageList.isEmpty()) {
                dismiss()
                return@setOnClickListener
            }
            onClickDeleteListener?.onCallback(it, adapter.getData()!![currentPosition], viewPagerPhoto.currentItem)
            adapter.removeData(viewPagerPhoto.currentItem)
            if (adapter.getData()!!.isEmpty()) {
                dismiss()
            }
            circleIndicator.setViewPager(viewPagerPhoto)
            viewPagerPhoto.setCurrentItem(currentPosition, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0) //Set style
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window ?: return
        val params = window.attributes
        params.gravity = Gravity.CENTER
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun show(manager: FragmentManager) {
        show(manager, this.tag)
    }


    interface OnClickDeleteListener {
        fun onCallback(view: View, img: Any, position: Int)
    }


    override fun onDestroy() {
        super.onDestroy()
        imageList?.clear()
    }
}
