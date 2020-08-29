package co.wishroll.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.wishroll.models.repository.AuthRepository;
import co.wishroll.models.repository.datamodels.AuthResponse;
import co.wishroll.models.repository.datamodels.EValidationRequest;
import co.wishroll.models.repository.datamodels.SignupRequest;
import co.wishroll.models.repository.datamodels.UValidationRequest;
import co.wishroll.models.repository.datamodels.ValidationResponse;
import co.wishroll.utilities.AuthListener;
import co.wishroll.utilities.AuthResource;

import static co.wishroll.WishRollApplication.applicationGraph;


public class SignupViewModel extends ViewModel {
    private static final String TAG = "Signup View Model";


    AuthRepository authRepository = applicationGraph.authRepository();
    public AuthListener authListenerSign = null;
    MediatorLiveData<AuthResource<AuthResponse>> authResponseMediatorLiveData = new MediatorLiveData<>();






    //Signup Procees
    public String signupEmail = "";
    public String signupName = "";
    public String signupUsername = "";
    public String signupPassword = "";
    public String signupPasswordTwo = "";

    SignupRequest signupRequest = SignupRequest.getInstance();
    AuthResponse signupResponse;

    EValidationRequest eValidationRequest;
    UValidationRequest uValidationRequest;
    ValidationResponse validationResponse;

    public String year = "";
    public String month = "";
    public String day = "";

    public String signupBirthdate;






    public void onNextEmail(){

        Log.d(TAG, "onNextEmail: in the on next email entering for the first time");
        if (TextUtils.isEmpty(signupEmail) || !emailIsVerified(signupEmail)){
            authListenerSign.onFailure("Please enter a valid email");
            Log.d(TAG, "onNextEmail: this person did not enter value");

        } else{
            authRepository.checkEmail(signupEmail.toLowerCase());


            switch(authRepository.getStatusCode()){
                case 400:
                    Log.d(TAG, "onNextEmail: this person entered an email that is taken");
                    authListenerSign.statusGetter(400);
                    break;
                case 200:
                    Log.d(TAG, "onNextEmail: at the 200 status code swithc case");
                    SignupRequest.setEmail(signupEmail.toLowerCase());
                    authListenerSign.statusGetter(200);
                    Log.d(TAG, "onNextEmail: asc values: " + SignupRequest.getEmail());
                    break;

            }
        }
    }

    public void onNextName(){

        if(TextUtils.isEmpty(signupName)){
            authListenerSign.onFailure("Please enter your name");

        }else{
            SignupRequest.setName(signupName);
            authListenerSign.statusGetter(200);
            Log.d(TAG, "onNextEmail: asc values: " + SignupRequest.getEmail() + " " + SignupRequest.getName()) ;
        }

    }

    public void onNextAge(){

        if((TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) || !ageClean(month, day, year)){
            authListenerSign.onFailure("Please enter a correct birthday");

        }else if(!ofAge(month, day, year)){
            authListenerSign.onFailure("You need to be 12 or older to use WishRoll");

        }else{

            SignupRequest.setBirthday(formatBirthdate(month, day, year));
            authListenerSign.statusGetter(200);
            Log.d(TAG, "onNextEmail: asc values: " + SignupRequest.getEmail() + " " + SignupRequest.getName() + " " + SignupRequest.getBirthday()) ;
        }


    }



    public void onSignupPressed(){

       authListenerSign.onStarted();

        if(TextUtils.isEmpty(signupUsername) || TextUtils.isEmpty(signupPassword) ||
                TextUtils.isEmpty(signupPasswordTwo)) {
            authListenerSign.onFailure("Please enter a username and password");

        }else if (!usernameIsValid(signupUsername)) {
            authListenerSign.onFailure("Please enter a valid username");

        }else if(!signupPassword.equals(signupPasswordTwo)) {
            authListenerSign.onFailure("Please enter the correct password");
            
        }else if(authRepository.checkUsername(signupUsername) == 400){
            authListenerSign.onFailure("This username is taken");


        }else if(authRepository.checkUsername(signupUsername) == 200){
            SignupRequest.setUsername(signupUsername);
            SignupRequest.setPassword(signupPassword);

            Log.d(TAG, "onNextEmail: asc values: " + SignupRequest.getEmail() + " " + SignupRequest.getName() + " " + SignupRequest.getBirthday() + " " + SignupRequest.getUsername() + " " + SignupRequest.getPassword());
            authenticateUserSignup(SignupRequest.getInstance());

        }

    }


    public void authenticateUserSignup(SignupRequest signupRequest){

        authResponseMediatorLiveData.setValue(AuthResource.loading((AuthResponse)null));
        final LiveData<AuthResource<AuthResponse>> source = authRepository.signupUser(signupRequest);
        authResponseMediatorLiveData.addSource(source, new Observer<AuthResource<AuthResponse>>() {
            @Override
            public void onChanged(AuthResource<AuthResponse> authResponse) {
                authResponseMediatorLiveData.setValue(authResponse);

                authResponseMediatorLiveData.removeSource(source);

            }
        });
    }

    public LiveData<AuthResource<AuthResponse>> observeSignupUser(){
        return authResponseMediatorLiveData;
    }


    private int usernameAvailable(String signupUsername) {
        int statusCode;
        uValidationRequest = new UValidationRequest(signupUsername);
        //validationResponse =  authRepository.checkUsername(uValidationRequest);
        statusCode = authRepository.getStatusCode();

        return statusCode;

    }






    /*private int emailAvailable(String signupEmail) {
        Log.d(TAG, "emailAvailable: about to check if the email is taken");
        int statusCode;

        eValidationRequest = new EValidationRequest(signupEmail);
        Log.d(TAG, "emailAvailable: just made a validation request object");
        //validationResponse =  ;
        //statusCode = validationResponse
        Log.d(TAG, "emailAvailable: this is the status code I got " + statusCode);

        *//*if(statusCode == 400) {
            authListenerSign.sendMessage("This email is linked with another account");
        }*//*


        return statusCode;


    }*/









    public boolean emailIsVerified(String emailInput) {
        //checks if email entry is in correct email form  easy regex: ^(.+)@(.+)$
        String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(emailInput);
        return matcher.find();
    }

    public boolean usernameIsValid(String usernameInput) {
        //Username Validation: no ... or ___ , < 20 char
        String usernameRegex = "^[a-z0-9]([._](?![._])|[a-z0-9]){1,20}[a-z0-9]$";
        Pattern usernamePat = Pattern.compile(usernameRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = usernamePat.matcher(usernameInput);
        return matcher.find();

    }

    public boolean ofAge(String month, String day, String year){
        int monthNum = Integer.parseInt(month);
        int dayNum = Integer.parseInt(day);
        int yearNum = Integer.parseInt(year);

        final Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mYear = c.get(Calendar.YEAR);

       int birth = (monthNum * 10) + (dayNum) + (yearNum * 100);
       int rightNow = (mMonth * 10) + (mDay) + (mYear * 100);
       int twelve = 1200;
       int age = rightNow - birth;


        return (age >= twelve);




    }

    public String formatBirthdate(String month, String day, String year){
        int monthNum = Integer.parseInt(month);
        int dayNum = Integer.parseInt(day);


        String dbMonth;
        String dbDay;

        if(monthNum <= 9) {
            dbMonth = "0" + monthNum;
        }else{
            dbMonth = month;
            }


         if(dayNum <= 9){
            dbDay = "0" + dayNum;
        }else{
             dbDay = day;
         }


        return year + "-" + dbMonth + "-" + dbDay;
    }

    public boolean ageClean(String month, String day, String year){
        int monthNum = Integer.parseInt(month);
        int dayNum = Integer.parseInt(day);
        int yearNum = Integer.parseInt(year);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);

        if(monthNum > 12 || dayNum > 31 || yearNum >= mYear || monthNum == 0 || dayNum == 0 || yearNum == 0){
            return false;

        }else{
            return true;

        }


    }






}
