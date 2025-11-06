package com.example.hw04_gymlog_v300;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static final String tag = "DAC_GYMLOG";
    String mExercise = "";
    double mWeight;
    int mReps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

    binding.logButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getInformationFromDisplay();
            updateDisplay();
        }
    });

    }

    private void updateDisplay(){
        String currentInfo = binding.logDisplayTextView.getText().toString();
        Log.d(tag, "current info: " + currentInfo);
        String newDisplay = String.format(Locale.US,"Exercise:%s%nWeight:%.2f%nReps:%d%n=-=-=-=%n%s",mExercise,mWeight,mReps,currentInfo);



        binding.logDisplayTextView.setText(newDisplay);
    }
    private void getInformationFromDisplay()
    {
        mExercise = binding.exerciseInputText.getText().toString();

        try {
            mWeight = Double.parseDouble(binding.weightInputText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(tag, "Error reading value from weight edit text");

        }

        try {
            mReps = Integer.parseInt(binding.weightInputText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(tag, "Error reading value from rep edit text");

        }

    }
}