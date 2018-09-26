# AppPhotoView

#### openDel() 是否使用删除功能
#### setImagePosition() 设置下标
#### setImageLoader(ImageLoader()) 设置图片加载方式
#### setOnClickDeleteListener() 删除回调

### 设置图片

```kotlin
  setImageList()   多张
or
  setImage()       单张
```

### 姿势1 

```kotlin
AppPhotoView.Companion.Builder()
                    .setImageList(images)
                    .setImagePosition(3)
                    .setImageLoader(GlideImageLoader())
                    .build().show(supportFragmentManager)  
```                 
                    
### 姿势2----带删除
```kotlin
AppPhotoView.Companion.Builder()
                    .openDel(true)
                    .setImageList(imageList)
                    .setImagePosition(3)
                    .setImageLoader(GlideImageLoader())
                    .setOnClickDeleteListener(object : AppPhotoView.OnClickDeleteListener {
                        override fun onCallback(view: View, img: Any, position: Int) {
                            imageList.removeAt(position)
                        }
                    })
                    .build().show(supportFragmentManager)
```                    
  
### ImageLoader--举个栗子-Glide
#### 不参与图片的加载,可自行根据自己的喜好使用图片加载框架

```kotlin
class GlideImageLoader : ImageLoader() {
        override fun loadImage(context: Context, img: Any, view: ImageView) {
            Glide.with(context).load(img).into(view)
        }
    }
```  
# 版本更新

 
 
