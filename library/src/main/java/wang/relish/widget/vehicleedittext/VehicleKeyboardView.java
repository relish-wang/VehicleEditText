package wang.relish.widget.vehicleedittext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

import relish.wang.vehicleedittext.R;

/**
 * 为KeyboardView编辑自定义样式
 *
 * @author Relish Wang
 * @since 2019/03/15
 */
class VehicleKeyboardView extends KeyboardView {

    public static VehicleKeyboardView newInstance(Context context) {
        return new VehicleKeyboardView(context);
    }

    public VehicleKeyboardView(Context context) {
        super(context, null);
        init(context);
    }

    public VehicleKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public VehicleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 含省份的简称的输入键盘
     */
    private Keyboard mProvincesKeyBoard;
    /**
     * 含数字/字母的输入键盘
     */
    private Keyboard mLettersKeyBoard;

    private void init(Context context) {
        mProvincesKeyBoard = new Keyboard(context, R.xml.keyboard_car_number_provinces);
        mLettersKeyBoard = new Keyboard(context, R.xml.keyboard_car_number_letters);
        setKeyboard(mProvincesKeyBoard);
        setPreviewEnabled(false);// 无法再popupwindow上展示popupwindow，而预览恰恰就是一个popupwindow
        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
    }

    void switchToProvinces() {
        if (getKeyboard() == mProvincesKeyBoard) return;
        setKeyboard(mProvincesKeyBoard);
    }

    void switchToLetters() {
        if (getKeyboard() == mLettersKeyBoard) return;
        setKeyboard(mLettersKeyBoard);
    }

    private Paint paint;
    private DisplayMetrics metrics;

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) paint = new Paint();
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        @SuppressLint("DrawAllocation") Rect bgRect = new Rect();
        getDrawingRect(bgRect);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.keyboard_bg_color));
        canvas.drawRect(bgRect, paint);

        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {
            paint.setTextAlign(Paint.Align.CENTER);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
            paint.setTypeface(font);
            paint.setAntiAlias(true);
            for (Keyboard.Key key : keys) {
                // 1 绘制背景（左右-3dp 上下-6dp）
                int _3dpToPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
                int _6dpToPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 6, metrics);
                Drawable dr;
                if (Arrays.asList(
                        Keyboard.KEYCODE_CANCEL,
                        "ABC".hashCode(),
                        "中文".hashCode(),
                        Keyboard.KEYCODE_DELETE).contains(key.codes[0])) {
                    dr = getContext().getResources().getDrawable(R.drawable.btn_gray);
                } else {
                    dr = getContext().getResources().getDrawable(R.drawable.btn_white);
                }
                @SuppressLint("DrawAllocation")
                Rect bounds = new Rect(
                        key.x + _3dpToPx,
                        key.y + _6dpToPx,
                        key.x + key.width - _3dpToPx,
                        key.y + key.height - _6dpToPx);
                dr.setBounds(bounds);
                dr.draw(canvas);
                // 2 绘制文字
                if (key.label != null) {
                    if (Arrays.asList("ABC".hashCode(), "中文".hashCode()).contains(key.codes[0])) {
                        paint.setTextSize(bounds.height() - 5 * _6dpToPx);
                    } else {
                        paint.setTextSize(bounds.height() - 4 * _6dpToPx);
                    }
                    if (key.codes[0] == Keyboard.KEYCODE_DONE) {
                        paint.setColor(getContext().getResources().getColor(R.color.black));
                    } else {
                        paint.setColor(getContext().getResources().getColor(R.color.black));
                    }
                    @SuppressLint("DrawAllocation")
                    Rect rect = new Rect(
                            key.x,
                            key.y,
                            key.x + key.width,
                            key.y + key.height);
                    Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                    int baseline =
                            (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                    // 实现水平居中，drawText对应改为传入targetRect.centerX()
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
                }
                // 3 绘制图标
                if (key.icon != null) {
                    int _15dpToPx = 15 * _6dpToPx / 6;
                    @SuppressLint("DrawAllocation") Rect iconRect = new Rect(
                            bounds.left + _15dpToPx,
                            bounds.top + _15dpToPx,
                            bounds.right - _15dpToPx,
                            bounds.bottom - _15dpToPx);
                    key.icon.setBounds(iconRect);
                    key.icon.draw(canvas);
                }
            }
        }
    }


}
