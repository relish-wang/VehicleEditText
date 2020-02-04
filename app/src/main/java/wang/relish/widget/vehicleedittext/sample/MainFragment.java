package wang.relish.widget.vehicleedittext.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;

/**
 * @author relish
 * @since 20200203
 */
public class MainFragment extends Fragment {

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_main, container, false);
        EditText vehicleEditView = v.findViewById(R.id.vet);
        VehicleKeyboardHelper.bind(vehicleEditView);
        return v;
    }
}
