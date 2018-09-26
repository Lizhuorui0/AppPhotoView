package com.zhuorui.appphotoview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhuorui.iphotoviewlibrary.ImageLoader
import com.zhuorui.iphotoviewlibrary.dialog.AppPhotoView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val images = ArrayList<Any>()
        images.add("https://tpc.googlesyndication.com/pagead/imgad?id=CICAgKCb99vdMhCsAhj6ATII-_3MLsYiRnw")
        images.add("http://demo.dededei.com/zentao/www/index.php?m=file&f=read&t=png&fileID=1123")
        images.add("https://www.baidu.com/s?tn=90073801_hao_pg&wd=%E4%BB%A3%E7%A0%81%E9%87%8D%E6%9E%84&ie=utf-8&rsv_cq=android+scrollview+%E6%BB%9A%E5%8A%A8%E5%88%B0%E5%BA%95%E9%83%A8&rsv_dl=0_right_recommends_merge_21102&euri=86680c1ccc3846ed9651ff0e3f1799e8")
        images.add("https://github.com/square/leakcanary/wiki/assets/screenshot.png")
        images.add("https://blog.csdn.net/whr0227")
        images.add(R.mipmap.ic_launcher)
        images.add(R.mipmap.ic_launcher)
        images.add(R.mipmap.ic_launcher)
        images.add(R.mipmap.ic_launcher)

        button.setOnClickListener {
            AppPhotoView.Companion.Builder()
                    .setImage(R.mipmap.ic_launcher)
                    .setImageLoader(GlideImageLoader())
                    .build().show(supportFragmentManager)
        }

        button1.setOnClickListener {
            AppPhotoView.Companion.Builder()
                    .setImageList(images)
                    .setImagePosition(3)
                    .setImageLoader(GlideImageLoader())
                    .build().show(supportFragmentManager)
        }

        button2.setOnClickListener {
            AppPhotoView.Companion.Builder()
                    .openDel(true)
                    .setImageList(images)
                    .setImagePosition(3)
                    .setImageLoader(GlideImageLoader())
                    .setOnClickDeleteListener(object : AppPhotoView.OnClickDeleteListener {
                        override fun onCallback(view: View, img: Any, position: Int) {
                            images.removeAt(position)
                        }
                    })
                    .build().show(supportFragmentManager)
        }

    }

    class GlideImageLoader : ImageLoader() {
        override fun loadImage(context: Context, img: Any, view: ImageView) {
            Glide.with(context).load(img).into(view)
        }
    }
}
