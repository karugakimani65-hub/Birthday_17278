package com.example.birthday;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText etBirthday;
    private TextView tvResult;

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

        etBirthday = findViewById(R.id.et_birthday);
        Button btnCalculate = findViewById(R.id.btn_calculate);
        tvResult = findViewById(R.id.tv_result);

        btnCalculate.setOnClickListener(v -> handleCalculate());
    }

    private void handleCalculate() {
        String input = etBirthday.getText().toString().trim();
        if (input.isEmpty() || !input.contains("/")) {
            Toast.makeText(this, R.string.error_invalid_date, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String[] parts = input.split("/");
            if (parts.length != 2) {
                throw new Exception();
            }
            int month = Integer.parseInt(parts[0]) - 1; // Calendar month is 0-based
            int day = Integer.parseInt(parts[1]);

            // Simple validation
            if (month < 0 || month > 11 || day < 1 || day > 31) {
                throw new Exception();
            }

            calculateDaysToBirthday(month, day);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_invalid_date, Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateDaysToBirthday(int month, int day) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar nextBirthday = Calendar.getInstance();
        nextBirthday.set(Calendar.MONTH, month);
        nextBirthday.set(Calendar.DAY_OF_MONTH, day);
        nextBirthday.set(Calendar.HOUR_OF_DAY, 0);
        nextBirthday.set(Calendar.MINUTE, 0);
        nextBirthday.set(Calendar.SECOND, 0);
        nextBirthday.set(Calendar.MILLISECOND, 0);

        // If birthday has already occurred this year, set it to next year
        if (nextBirthday.before(today)) {
            nextBirthday.add(Calendar.YEAR, 1);
        }

        long diffInMillis = nextBirthday.getTimeInMillis() - today.getTimeInMillis();
        long daysUntil = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (daysUntil == 0) {
            tvResult.setText(getString(R.string.happy_birthday));
        } else {
            tvResult.setText(getString(R.string.days_until_birthday, daysUntil));
        }
    }
}
