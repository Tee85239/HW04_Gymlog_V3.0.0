package com.example.hw04_gymlog_v300;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.example.hw04_gymlog_v300.database.GymLogRepsository;
import com.example.hw04_gymlog_v300.database.entites.GymLog;
import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.hw04_gymlog_v300.MAIN_ACTIVITY_USER_ID";
    private ActivityMainBinding binding;

   private GymLogRepsository repsository;
    public static final String tag = "DAC_GYMLOG";
    String mExercise = "";
    double mWeight;
    int mReps;

    //TODO: add login info
    int loggedInUserID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loginUser();

        if(loggedInUserID == -1){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);

        }



        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());





        repsository = GymLogRepsository.getReposoitory(getApplication());
        updateDisplay();
        binding.logButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getInformationFromDisplay();
            insertGymLogRecord();
            updateDisplay();
        }
    });

binding.exerciseInputText.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        updateDisplay();
    }
});





    }

    private void loginUser() {
        //TODO: create login method
        loggedInUserID = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,-1);

    }
static Intent mainActivityIntentFactory(Context context, int userId) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
    return intent;

}

    private void insertGymLogRecord(){
        if(mExercise.isEmpty()){
            return;
        }

        GymLog log = new GymLog(mExercise,mWeight,mReps, loggedInUserID);
        repsository.insertGymLog(log);

    }
    @SuppressLint("SetTextI18n")
    private void updateDisplay(){
        StringBuilder sb = new StringBuilder();
        ArrayList<GymLog> allLogs = repsository.getAllLogs();
        if(allLogs.isEmpty()) {
            binding.logDisplayTextView.setText("Nothing to show, time to hit the gym.");
        }
            for(GymLog log : allLogs){
                sb.append(log);

            }


        binding.logDisplayTextView.setText(sb.toString());

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