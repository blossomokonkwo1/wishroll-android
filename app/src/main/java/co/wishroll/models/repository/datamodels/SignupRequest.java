package co.wishroll.models.repository.datamodels;

import com.google.gson.annotations.SerializedName;


public class SignupRequest {

    static SignupRequest signupRequest;


    @SerializedName("name")
    static String name;

    @SerializedName("username")
    static String username;

    @SerializedName("password")
    static String password;

    @SerializedName("email")
    static String email;

    @SerializedName("birth_date")
    static String birthday;

    @SerializedName("gender")
    static int gender;



    public static SignupRequest getInstance()
    {
        if (signupRequest == null)
            signupRequest = new SignupRequest();

        return signupRequest;
    }

    public SignupRequest() {
    }






    public static String getName() {
        return name;
    }

    public static void setName(String names) {
        name = names;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String passwords) {
        password = passwords;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String emails) {
        email = emails;
    }




}