package wang.relish.widget.vehicleedittext.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;

/**
 * @author wangxin
 * @since 20190929
 */
public class TestDialog extends Dialog {

    public TestDialog(@NonNull Context context) {
        super(context);
    }

    public TestDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TestDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_test);

        EditText et = findViewById(R.id.et_dialog_input);
        VehicleKeyboardHelper.bind(getOwnerActivity(),et);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            window.setAttributes(params);
        }
    }
}
