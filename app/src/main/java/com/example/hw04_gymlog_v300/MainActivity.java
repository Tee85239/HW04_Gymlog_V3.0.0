package com.example.hw04_gymlog_v300;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw04_gymlog_v300.database.GymLogRepsository;
import com.example.hw04_gymlog_v300.database.entites.GymLog;
import com.example.hw04_gymlog_v300.database.entites.User;
import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;
import com.example.hw04_gymlog_v300.viewHolder.GymLogAdator;
import com.example.hw04_gymlog_v300.viewHolder.GymLogViewHolder;
import com.example.hw04_gymlog_v300.viewHolder.GymLogViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final String SHARED_PREFRENCE_USERID_KEY = "com.example.hw04_gymlog_v300.PREFRENCE_USERID_KEY";
  //  static final String MAIN_ACTIVITY_USERID_KEY_VALUE= "com.example.hw04_gymlog_v300.PREFRENCE_USERID_KEY_VALUE";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.hw04_gymlog_v300.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.hw04_gymlog_v300.MAIN_ACTIVITY_USER_ID";
    private static final int LOGGED_OUT = -1;

    private ActivityMainBinding binding;

   private GymLogRepsository repsository;
   private GymLogViewModel gymLogViewModel;

    public static final String tag = "DAC_GYMLOG";
    String mExercise = "";
    double mWeight;
    int mReps;


    private int loggedInUserID = -1;
    private User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this,"LOGOUT TO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();

                showLogoutDialog();

                return false;
            }
        });

        item.setTitle(user != null ? user.getUsername() : "Account");
        return true;
    }


    private void showLogoutDialog(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.create().show();

    }
    private void logout() {
        //TODO FINISH LOGOUT

        loggedInUserID = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedprefernceEditor = sharedPreferences.edit();
        sharedprefernceEditor.putInt(getString(R.string.preference_user_ID_key),loggedInUserID);
        sharedprefernceEditor.apply();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gymLogViewModel = new ViewModelProvider(this).get(GymLogViewModel.class);

        RecyclerView recyclerView = binding.logDisplayRecylerView;
        final GymLogAdator adator= new GymLogAdator(new GymLogAdator.GymlogDiff());
        recyclerView.setAdapter(adator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        repsository = GymLogRepsository.getReposoitory(getApplication());
        loginUser(savedInstanceState);
        gymLogViewModel.getAllLogsByID(loggedInUserID).observe(this, gymLogs -> {adator.submitList(gymLogs);});






        //User not logged in at this point
        if(loggedInUserID == LOGGED_OUT){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);

        }
        updateSharedPreference();


        //TODO:REMOVE two lines below

       // binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());





    //TODO: remove line below
       // updateDisplay();
        binding.logButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getInformationFromDisplay();
            insertGymLogRecord();
           // updateDisplay();
        }
    });



    }

    private void loginUser(Bundle savedInstanceState) {
        //TODO: create login method
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

          loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_user_ID_key),LOGGED_OUT);



      if(loggedInUserID == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
        loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);

      }

          if(loggedInUserID == LOGGED_OUT){
              loggedInUserID = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);

          }
        //Checked preference for loggedUser

        if(loggedInUserID == LOGGED_OUT){
           return;
        }

        //Checked intent for loggedUser

        LiveData<User> userObserver = repsository.getUserByUserID(loggedInUserID);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();

            }


        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)

    {
        super.onSaveInstanceState(outState);
       outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY,loggedInUserID);
       updateSharedPreference();

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

    @Deprecated
    @SuppressLint("SetTextI18n")
    private void updateDisplay(){
        StringBuilder sb = new StringBuilder();
        ArrayList<GymLog> allLogs = repsository.getAllLogsByUserID(loggedInUserID);
        if(allLogs.isEmpty()) {
          //  binding.logDisplayTextView.setText("Nothing to show, time to hit the gym.");
        }
            for(GymLog log : allLogs){
                sb.append(log);

            }


        // binding.logDisplayTextView.setText(sb.toString());

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