package co.wishroll.utilities;

public interface AuthListener {

    void onStarted();

    void onSuccess();

    void onFailure(String message);


}
