package is.dkk.snackcheck.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import is.dkk.snackcheck.R;

public class MainActivity extends AppCompatActivity {

    boolean settingsShown = false;
    Resources res;
    Drawable active, inactive;
    int[] days;
    int today;
    TextView txtAnswer, txtTimeLeft;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        res = getResources();
        calendar = Calendar.getInstance();

        active = ResourcesCompat.getDrawable(res, R.drawable.circle_active, null);
        inactive = ResourcesCompat.getDrawable(res, R.drawable.circle_inactive, null);

        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnSnack = findViewById(R.id.btnSnack);
        ConstraintLayout settings = findViewById(R.id.settings);
        txtAnswer = findViewById(R.id.txtAnswer);
        txtTimeLeft = findViewById(R.id.txtTimeLeft);


        getDaysFromPrefs();
        screenInit();

        btnSettings.setOnClickListener(view -> {
            if (settingsShown) {
                settings.setVisibility(View.INVISIBLE);
                btnSettings.setText(R.string.settings_default);
            } else {
                settings.setVisibility(View.VISIBLE);
                btnSettings.setText(R.string.settings_close);
            }

            settingsShown = !settingsShown;
        });

        btnSnack.setOnClickListener(view -> {
            txtAnswer.setText(getString(R.string.txt_noti_pos));
            txtTimeLeft.setText(R.string.days_0);
        });
    }

    private void screenInit() {
        setupSettings();
        checkToday();
    }

    private void setupSettings() {
        Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);

        Button[] btnArr = {btn1, btn2, btn3, btn4, btn5, btn6, btn7};

        for (int x = 0; x < 7; x++) {
            btnArr[x].setOnClickListener(getListener(btnArr[x], x, days[x]));
        }
    }

    private View.OnClickListener getListener(View view, int idx, int active) {
        // Init the buttons a little
        setButtonData((Button) view, active == 0);

        return v -> setButtonFlip((Button) v, idx);
    }

    private void setButtonFlip(Button button, int idx) {
        boolean setInactive = button.getBackground() == active;

        days[idx] = setInactive ? 0 : 1;
        checkToday();

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

    private void getToday() {
        today = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    private void checkToday() {
        getToday();
        int left = daysToSnack(), str;

        switch (left) {
            case 0:
                str = R.string.days_0;
                break;
            case 1:
                str = R.string.day_left;
                break;
            default:
                str = R.string.days_left;
                break;
        }

        txtAnswer.setText(days[today] == 1 ? getString(R.string.txt_noti_pos) : getString(R.string.txt_noti_neg));

        StringBuilder builder = new StringBuilder();

        if (left != 0) {
            builder.append(left);
            builder.append(" ");
        }
        builder.append(getString(str));

        txtTimeLeft.setText(builder.toString());
    }

    private int daysToSnack() {
        if (days[today] == 1) {
            return 0;
        } else {
            int loop = today, counter = 0;

            while (days[loop] != 1) {
                loop++;
                counter++;

                if (loop >= 7) {
                    loop = 0;
                }

            }

            return counter;
        }
    }

    private void getDaysFromPrefs() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);

        String daylist = prefs.getString(getString(R.string.preference_day_key), getString(R.string.preference_day_default));
        String[] dayArr = daylist.split("");

        days = new int[7];

        for (int x = 0; x < 7; x++) {
            days[x] = Integer.parseInt(dayArr[x]);
        }
    }

    private void setDaysToPrefs() {
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);

        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < 7; x++) {
            builder.append(days[x]);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.preference_day_key), builder.toString());
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();

        setDaysToPrefs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setDaysToPrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDaysFromPrefs();
    }
}