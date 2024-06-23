package is.dkk.snackcheck.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import is.dkk.snackcheck.R;
import is.dkk.snackcheck.fragments.MainFragment;
import is.dkk.snackcheck.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    boolean settingsShown = false;
    SettingsFragment settings;

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

        Button btnSettings = findViewById(R.id.btnSettings);

        FragmentManager manager = getSupportFragmentManager();
        settings = new SettingsFragment(this);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = manager.beginTransaction();

                if (settingsShown) {
                    transaction.remove(settings);
                    btnSettings.setText(R.string.settings_default);
                } else {
                    transaction.replace(R.id.fragment_container, settings);
                    btnSettings.setText(R.string.settings_close);
                }

                settingsShown = !settingsShown;
                transaction.commit();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Log.wtf("Test", String.valueOf(day));


    }
}