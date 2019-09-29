package wang.relish.widget.vehicleedittext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import relish.wang.vehicleedittext.R;


/**
 * 输入框键盘绑定帮助类
 * <p>
 * 直接在布局中使用该控件:
 * <pre>
 * <code>
 *
 * &lt;wang.relish.vehicle.VehicleEditText
 *      android:id="@+id/vet"
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content" /&gt;
 * </code>
 * </pre>
 * <p>
 * 或者使用普通的EditText:
 *
 * <pre>
 * <code>
 *
 *      EditText et = findViewById(R.id.et);
 *      VehicleKeyboardHelper.bind(et);
 * </code>
 * </pre>
 *
 * @author Relish Wang
 * @since 2017/09/18
 */
public class VehicleKeyboardHelper {

    private static final String PROVINCES =
            "京津渝沪冀晋辽吉黑苏浙皖闽赣鲁豫鄂湘粤琼川贵云陕甘青蒙桂宁新藏使领警学港澳";

    /**
     * 为EditText绑定车牌号输入键盘
     *
     * @param et 绑定的输入框控件(EditText)
     */
    public static void bind(Activity activity, EditText et) {
        bind(activity, et, VehicleKeyboardView.newInstance(et.getContext()));
    }

    /**
     * 为EditText绑定车牌号输入键盘
     *
     * @param et       绑定的输入框控件(EditText)
     * @param keyboard 自定义键盘(KeyboardView)
     */
    public static void bind(Activity activity, EditText et, VehicleKeyboardView keyboard) {
        bind(activity, et, keyboard, null);
    }


    static Activity mCurrentActivity;

    /**
     * 为EditText绑定自定义键盘，并设置自定义键盘的特殊键的功能
     *
     * @param et       绑定的输入框控件(EditText)
     * @param keyboard 自定义键盘(KeyboardView)
     * @param l        键盘事件监听器
     */
    @SuppressWarnings("SameParameterValue")
    @SuppressLint("ClickableViewAccessibility")
    private static void bind(
            Activity activity,
            final EditText et,
            final VehicleKeyboardView keyboard,
            @Nullable VehicleKeyboardView.OnKeyboardActionListener l) {
        if (et == null) return;//throw new NullPointerException("EditText should not be NULL!");
        if (keyboard == null) return; // 表示使用系统键盘

        mCurrentActivity = activity;

        keyboard.setOnKeyboardActionListener(l != null
                ? l : new OnKeyboardActionAdapter(et) {
            @Override
            public void close() {
                hideCustomInput(et);
            }

            @Override
            public boolean onKeyEvent(int primaryCode, int[] keyCodes) {
                String s = et.getText().toString();
                if ("ABC".hashCode() == primaryCode) {
                    keyboard.switchToLetters();
                    return true;
                } else if ("中文".hashCode() == primaryCode) {
                    keyboard.switchToProvinces();
                    return true;
                } else {
                    // 除功能键以外的键
                    if (primaryCode != Keyboard.KEYCODE_DELETE && s.length() >= 8) {
                        return true;// 车牌号到最长长度了
                    }
                    if (PROVINCES.contains(String.valueOf((char) primaryCode))) {
                        if (s.length() == 0) {
                            keyboard.switchToLetters();
                        }
                        return false;
                    } else {
                        return super.onKeyEvent(primaryCode, keyCodes);
                    }
                }
            }
        });
        // 设置触摸的点击事件。为了显示光标。（直接设置InputType.TYPE_NULL的话光标会消失）
        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = et.getInputType();
                et.setInputType(InputType.TYPE_NULL);
                et.onTouchEvent(event);
                et.setInputType(inType);
                // 光标始终在内容的最后面
                et.setSelection(et.getText().length());
                if (et instanceof VehicleEditText) {
                    View.OnTouchListener toucheListener = ((VehicleEditText) et).mToucheListener;
                    if (toucheListener != null) {
                        return toucheListener.onTouch(v, event);
                    }
                    // 不拦截
                }
                return true;
            }
        });
        // 监听输入框的焦点事件：获得焦点显示自定义键盘；失去焦点收起自定义键盘
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideSysInput(et);
                    showCustomInput(et, keyboard);
                } else {
                    hideCustomInput(et);
                }
                if (et instanceof VehicleEditText) {
                    View.OnFocusChangeListener changeListener =
                            ((VehicleEditText) et).mFocusChangeListener;
                    if (changeListener != null) {
                        changeListener.onFocusChange(v, hasFocus);
                    }
                }
            }
        });
        // 监听物理返回键，收起自定义键盘
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideCustomInput(et);
                    return true;
                }
                if (et instanceof VehicleEditText) {
                    View.OnKeyListener keyListener = ((VehicleEditText) et).mKeyListener;
                    if (keyListener != null) {
                        return keyListener.onKey(v, keyCode, event);
                    }
                }
                return false;
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    keyboard.switchToProvinces();// 当没有文字的时候键盘切换回省份
                }
            }
        });
    }

    /**
     * 展示自定义键盘
     *
     * @param et       输入框控件
     * @param keyboard 自定义键盘
     */
    private static void showCustomInput(final EditText et, final VehicleKeyboardView keyboard) {
        Object obj = et.getTag(R.id.keyboard);
        final PopupWindow keyboardWindow;
        if (obj == null) {
            keyboardWindow = new PopupWindow(
                    keyboard,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            keyboardWindow.setAnimationStyle(R.style.WindowAnimation);
            et.setTag(R.id.keyboard, keyboardWindow);
        } else {
            keyboardWindow = ((PopupWindow) obj);
        }
        if (keyboardWindow.isShowing()) return;
        keyboardWindow.setOutsideTouchable(false);
        keyboard.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (keyboardWindow != null && keyboardWindow.isShowing()) {
                        hideCustomInput(et);
                        return true;
                    }
                }
                return false;
            }
        });
        Window window = getWindow(mCurrentActivity,et);
        View decorView = null;
        if (window != null) {
            // 解决底部被导航栏遮挡问题
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            decorView = window.getDecorView();
            if (!isActivityRunning(et.getContext())) return;
        }
        keyboardWindow.showAtLocation(decorView == null ? et : decorView, Gravity.BOTTOM, 0, 0);
        keyboardWindow.update();

    }

    /**
     * 隐藏自定义键盘
     *
     * @param et 输入框控件
     */
    private static void hideCustomInput(EditText et) {
        if (et == null) return;
        if (!isActivityRunning(et.getContext())) return;
        et.clearFocus();
        Object tag = et.getTag(R.id.keyboard);
        if (tag == null) return;
        if (tag instanceof PopupWindow) {
            PopupWindow window = (PopupWindow) tag;
            if (window.isShowing()) {
                window.dismiss();
            }
        }
    }

    /**
     * 隐藏系统输入法
     *
     * @param et 输入框控件
     */
    private static void hideSysInput(EditText et) {
        IBinder windowToken = et.getWindowToken();
        if (windowToken != null) {
            InputMethodManager imm = (InputMethodManager)
                    et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static Window getWindow(Activity activity, EditText et) {
        return activity.getWindow();
    }

    private static boolean isActivityRunning(Context context) {
        if (context == null) return false;
        if (context instanceof Activity) {
            return !((Activity) context).isFinishing();
        } else {
            return true;
        }
    }
}
