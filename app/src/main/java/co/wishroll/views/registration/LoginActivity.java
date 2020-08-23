package co.wishroll.views.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import co.wishroll.R;
import co.wishroll.databinding.ActivityLoginBinding;
import co.wishroll.utilities.AuthListener;
import co.wishroll.viewmodel.AuthViewModel;
import co.wishroll.views.home.MainActivity;


public class LoginActivity extends AppCompatActivity implements AuthListener {


    private static final String TAG = "LOGIN ACTIVITY";
    ActivityLoginBinding activityLoginBinding;


    AuthViewModel authViewModel;

    TextView signupInstead;
    ProgressBar progressBar;
    EditText emailUser, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        activityLoginBinding.setAuthviewmodel(authViewModel);
        authViewModel.authListener = this;
        progressBar = findViewById(R.id.progressBarLogin);
        signupInstead = findViewById(R.id.newSignUp);

        emailUser = findViewById(R.id.etEmailUsername);
        password = findViewById(R.id.etPasswordEntry);


        signupInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });




    }




   @Override
   public void onStarted() {
        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "Login Started", Toast.LENGTH_SHORT).show();


}

    @Override
    public void onSuccess() {

        progressBar.setVisibility(View.INVISIBLE);
        //Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailure(String message) {
        progressBar.setVisibility(View.INVISIBLE);
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void sendMessage(String message){
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
   }

   @Override
    public void errorMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
   }

    @Override
    public void statusGetter(int statusCode) {
        progressBar.setVisibility(View.INVISIBLE);
        switch(statusCode){
            
            case 100:
            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
            break;

            case 200:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            break;

            case 404:
                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
                break;

            case 401:
                Toast.makeText(this, "You entered the wrong credentials, please try again", Toast.LENGTH_SHORT).show();
                break;

            case 400:
                Toast.makeText(this, "Could not be fulfilled, please try again", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "There was an error, please try again", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
