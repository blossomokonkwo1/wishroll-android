package co.wishroll.views.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import co.wishroll.R;
import co.wishroll.databinding.ActivitySignupBinding;
import co.wishroll.utilities.AuthListener;
import co.wishroll.viewmodel.SignupViewModel;


public class SignupActivity extends AppCompatActivity implements AuthListener {

    private static final String TAG = "SIGNUP ACTIVITY";
    ActivitySignupBinding activitySignupBinding;
    SignupViewModel signupViewModel;




    ProgressBar progressBarEmail;
    Button bSignup;
    ImageButton backEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_signup);

        activitySignupBinding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        activitySignupBinding.setSignupviewmodel(signupViewModel);
        signupViewModel.authListenerSign = this;

        bSignup = findViewById(R.id.bCreateAccount);

        progressBarEmail = findViewById(R.id.progressBarEmail);


        backEmail = findViewById(R.id.backEmail);

        backEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });















    }








    private void showEmailProgressBar(boolean isVisible){
        if(isVisible){
            progressBarEmail.setVisibility(View.VISIBLE);
        }else{
            progressBarEmail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void statusGetter(int statusCode) {
            if(statusCode == 400) {
                Log.d(TAG, "statusGetter: showing the taken message");
                Toast.makeText(this, "This email is linked with another account", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, "statusGetter: on our way to the next activity");
                startActivity(new Intent(SignupActivity.this, NameSignupActivity.class));
            }
    }
}