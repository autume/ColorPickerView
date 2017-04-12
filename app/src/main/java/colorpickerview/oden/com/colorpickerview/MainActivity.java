package colorpickerview.oden.com.colorpickerview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import colorpickerview.oden.com.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    ImageView img_color;
    ImageView img_picker;
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRgbPicker();
    }

    private void initView() {
        img_color = (ImageView) findViewById(R.id.img_color);
        img_picker = (ImageView) findViewById(R.id.img_picker);
    }

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
//        colorPickerView.setImgResource(R.mipmap.colors_picture);
    }

}
