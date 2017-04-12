package colorpickerview.oden.com.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 项目名称：ColorPickerView
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/2/22
 */
public class ColorPickerView extends FrameLayout {
    private final String TAG = "ColorPickerView";
    private ImageView imgColorRang;//颜色选择盘
    private ImageView imgPicker;//颜色选择器
    private RelativeLayout rl_root;

    private int range_radius;//圆盘半径
    private int select_radius = 0;//可选取的半径
    private int centreX;//圆盘中心X坐标
    private int centreY;//圆盘中心Y坐标
    private Bitmap bitmap;//颜色选择盘图片
    private int pickerViewPadding = 0;//用来确定选色指示圆环可以超出选色图片的距离
    private int imgResource;
    private onColorChangedListener colorChangedListener;//颜色变换监听

    public ColorPickerView(Context context) {
        super(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView(context);
        initTouchListener();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
            imgResource = typedArray.getResourceId(R.styleable.ColorPickerView_picture_resource, 0);
            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_picker, this);
        imgColorRang = (ImageView) view.findViewById(R.id.img_color_rang);
        rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);

        if (imgResource != 0)
            imgColorRang.setImageResource(imgResource);

        bitmap = ((BitmapDrawable) imgColorRang.getDrawable()).getBitmap();//获取圆盘图片
        Log.d(TAG, "initView bitmap.getWidth(): " + bitmap.getWidth() + ",imgColorRang.getWidth(): " + imgColorRang.getWidth());
    }

    private void initTouchListener() {
        rl_root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (range_radius == 0) {
                    range_radius = imgColorRang.getWidth() / 2;  //圆盘半径
                    centreX = imgColorRang.getRight() - range_radius;
                    centreY = imgColorRang.getBottom() - imgColorRang.getHeight() / 2;
                    select_radius = range_radius - pickerViewPadding/5;
                }

                float xInView = event.getX();
                float yInView = event.getY();
                Log.d(TAG, "xInView: " + xInView + ",yInView: " + yInView + ",left: " + imgColorRang.getLeft() + ",top: " + imgColorRang.getTop() + ",right: " +imgColorRang.getRight() + ",bottom: " + imgColorRang.getBottom());

                //触摸点与圆盘圆心距离
                float diff = (float) Math.sqrt((centreY - yInView) * (centreY - yInView) + (centreX - xInView) *
                        (centreX - xInView));

                //在选色图片内则进行读取颜色等操作
                if (diff <= select_radius) {

                    //选色位置指示，若设置了则移动到点取的位置
                    if (imgPicker != null ) {
                        int xInWindow = (int) event.getX();
                        int yInWindow = (int) event.getY();
                        int left = xInWindow + v.getLeft() - imgPicker.getWidth() / 2;
                        int top = yInWindow + v.getTop() - imgPicker.getWidth() / 2;
                        int right = left + imgPicker.getWidth();
                        int bottom = top + imgPicker.getHeight();

                        imgPicker.layout(left, top, right, bottom);
                    }


                    if ((event.getY() - imgColorRang.getTop()) < 0)
                        return true;
                    //读取颜色
                    int pixel = bitmap.getPixel((int) (event.getX() - imgColorRang.getLeft()), (int) (event.getY() - imgColorRang.getTop()));   //获取选择像素
                    if (colorChangedListener != null) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            colorChangedListener.stopColorChanged(Color.red(pixel), Color.blue(pixel), Color.green(pixel));
                        }else {
                            colorChangedListener.colorChanged(Color.red(pixel), Color.blue(pixel), Color.green(pixel));
                        }
                    }
                    Log.d(TAG, "radValue=" + Color.red(pixel) + "  blueValue=" + Color.blue(pixel) + "  greenValue" + Color.green(pixel));
                }
                return true;
            }
        });
    }

    public void setColorChangedListener(onColorChangedListener colorChangedListener) {
        this.colorChangedListener = colorChangedListener;
    }

    /**
     * 颜色变换监听接口
     */
    public interface onColorChangedListener {
        void colorChanged(int red, int blue, int green);
        void stopColorChanged(int red, int blue, int green);
    }

    public void setImgPicker(final Context context, final ImageView imgPicker, final int pickerViewWidth) {
        this.imgPicker = imgPicker;
        pickerViewPadding = dip2px(context, pickerViewWidth/2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_root.setPadding(pickerViewPadding, pickerViewPadding, pickerViewPadding, pickerViewPadding);
                bitmap = ((BitmapDrawable) imgColorRang.getDrawable()).getBitmap();//获取圆盘图片
            }
        },10);
    }

    public void setImgResource(final int imgResource) {
        this.imgResource = imgResource;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgColorRang.setImageResource(imgResource);
                bitmap = ((BitmapDrawable) imgColorRang.getDrawable()).getBitmap();//获取圆盘图片
            }
        },10);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
