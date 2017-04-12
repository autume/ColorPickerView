# Android自定义view-图片选色器
## 简介
主要实现以下几个功能：
- 选取圆盘选色图片上的颜色，实时监听
- 可设置选色指示图片，跟随触摸位置、指示所选颜色，示例中为白色圆环
- 可自己设置选色图片（目前只支持圆形图片）

## 使用效果
首先看下使用效果：
![](http://i.imgur.com/oIM1je2.gif)

## 使用示例
### 在项目中导入该库
在工程的 build.gradle中加入：
```java
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
module的build.gradle中加入依赖：
```java
dependencies {
	       compile 'com.github.autume:ColorPickerView:1.0'
	}
```
### xml
```java
<RelativeLayout
        android:id="@+id/rl_picker"
        android:layout_below="@+id/img_color"
        android:layout_marginTop="30dp"
        android:layout_centerHorizontal="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <colorpickerview.oden.com.colorpicker.ColorPickerView
            android:id="@+id/color_picker"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>


        <ImageView
            android:id="@+id/img_picker"
            android:layout_centerInParent="true"
            android:src="@mipmap/color_picker"
            android:layout_width="25dp"
            android:layout_height="25dp" />

    </RelativeLayout>
```
### 选色代码
```java
   private void initRgbPicker() {
        colorPickerView = (ColorPickerView) findViewById(R.id.color_picker);
        colorPickerView.setImgPicker(MainActivity.this, img_picker, 25); //最后一个参数是该颜色指示圈的大小(dp)
        colorPickerView.setColorChangedListener(new ColorPickerView.onColorChangedListener() {
            @Override
            public void colorChanged(int red, int blue, int green) {
                img_color.setColorFilter(Color.argb(255, red, green, blue));
            }

            @Override
            public void stopColorChanged(int red, int blue, int green) {

            }
        });
    }
```
#### 对外公开的API
```java
 public void setImgPicker(final Context context, final ImageView imgPicker, final int pickerViewWidth)

 public void setImgResource(final int imgResource)

 public void setColorChangedListener(onColorChangedListener colorChangedListener)
```
 
[详细介绍见博客](http://blog.csdn.net/yaodong379/article/details/70147486)
