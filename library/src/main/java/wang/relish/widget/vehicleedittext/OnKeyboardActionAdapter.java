package wang.relish.widget.vehicleedittext;

import android.annotation.SuppressLint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.widget.EditText;

/**
 * 实现了键盘通用功能的OnKeyboardActionListener
 *
 * @author Relish Wang
 * @since 2017/09/18
 */
/* package */ abstract class OnKeyboardActionAdapter implements KeyboardView.OnKeyboardActionListener {

    private EditText mEditText;

    OnKeyboardActionAdapter(EditText mEditText) {
        this.mEditText = mEditText;
    }

    /**
     * （用于拓展）处理特殊的键盘按键响应
     *
     * @param primaryCode 主键值
     * @param keyCodes    所有键值
     */
    @SuppressLint("SetTextI18n")
    public boolean onKeyEvent(int primaryCode, int[] keyCodes) {
        return false;
    }

    /**
     * 关闭键盘
     */
    public abstract void close();

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (onKeyEvent(primaryCode, keyCodes)) {
            return;
        }
        if (mEditText == null) return;
        if (primaryCode == Keyboard.KEYCODE_DELETE) { // 回退(backspace)
            int start = Math.max(mEditText.getSelectionStart(), 0);
            int end = Math.max(mEditText.getSelectionEnd(), 0);
            if (start != end) {
                mEditText.getText().delete(start, end);
            } else if (start > 0) {
                mEditText.getText().delete(start - 1, end);
            }
        } else if (primaryCode == Keyboard.KEYCODE_CANCEL) { // 完成（complete）
            close();
        } else if (primaryCode == Keyboard.EDGE_LEFT) {
            int start = mEditText.getSelectionStart();
            if (start > 0) mEditText.setSelection(start - 1);
        } else if (primaryCode == Keyboard.EDGE_RIGHT) {
            int start = mEditText.getSelectionStart();
            if (start < mEditText.length()) mEditText.setSelection(start + 1);
        } else {
            String text = Character.toString((char) primaryCode);
            int start = Math.max(mEditText.getSelectionStart(), 0);
            int end = Math.max(mEditText.getSelectionEnd(), 0);
            mEditText.getText().replace(Math.min(start, end), Math.max(start, end),
                    text, 0, text.length());
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
