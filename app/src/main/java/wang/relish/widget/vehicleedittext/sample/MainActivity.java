package wang.relish.widget.vehicleedittext.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;

public class MainActivity extends AppCompatActivity {

    EditText mVehicleEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVehicleEditView = findViewById(R.id.vet);
        VehicleKeyboardHelper.bind(mVehicleEditView);

        View btn = findViewById(R.id.seeFragment);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(v -> WithFragmentActivity.start(v.getContext()));
    }
}
