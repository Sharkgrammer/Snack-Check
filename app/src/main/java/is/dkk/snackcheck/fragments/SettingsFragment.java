package is.dkk.snackcheck.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import is.dkk.snackcheck.R;

public class SettingsFragment extends Fragment {

    Context c;
    Resources res;
    Drawable active, inactive;

    public SettingsFragment(Context c) {
        this.c = c;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings, container, false);

        res = getResources();

        active = ResourcesCompat.getDrawable(res, R.drawable.circle_active, null);
        inactive = ResourcesCompat.getDrawable(res, R.drawable.circle_inactive, null);

        //TODO write code for the view
        Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        btn7 = view.findViewById(R.id.btn7);

        Button[] btnArr = {btn1, btn2, btn3, btn4, btn5, btn6, btn7};

        SharedPreferences prefs = c.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);

        String days = prefs.getString("daylist", "1110011");
        String[] dayArr = days.split("");

        for (int x = 0; x < 7; x++) {
            btnArr[x].setOnClickListener(getListener(btnArr[x], Integer.parseInt(dayArr[0])));
        }


        return view;
    }

    private View.OnClickListener getListener(View view, int num) {
        // Init the buttons a little
        Log.wtf("test", String.valueOf(num));
        setButtonData((Button) view, num == 0);

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonFlip((Button) view);
            }
        };

    }

    private void setButtonFlip(Button button) {
        boolean setInactive = button.getBackground() == active;

        setButtonData(button, setInactive);
    }

    private void setButtonData(Button button, boolean setInactive) {
        if (setInactive) {
            button.setBackground(inactive);
            button.setTextColor(res.getColor(R.color.black, null));
        } else {
            button.setBackground(active);
            button.setTextColor(res.getColor(R.color.white, null));
        }
    }
}

