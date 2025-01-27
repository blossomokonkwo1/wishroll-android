package co.wishroll.viewmodel;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.wishroll.R;
import co.wishroll.models.repository.UserRepository;
import co.wishroll.models.repository.datamodels.UpdateResponse;
import co.wishroll.models.repository.local.SessionManagement;
import co.wishroll.utilities.AuthListener;
import co.wishroll.utilities.FileUtils;
import co.wishroll.utilities.StateData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static co.wishroll.WishRollApplication.applicationGraph;

public class EditProfileViewModel  extends ViewModel {

    private static final String TAG = "EditProfileViewModel";
    UserRepository userRepository = applicationGraph.userRepository();
    SessionManagement sessionManagement = applicationGraph.sessionManagement();
    public AuthListener authListener = null;
    public Map<String, RequestBody> changedValues = new HashMap<>();
    MediatorLiveData<StateData<UpdateResponse>> editedCurrentUser = new MediatorLiveData<>();
    String profileFileName;
    String backgroundFileName;
    RequestBody profileRequest;
    RequestBody backgroundRequest;
    MultipartBody.Part profileMultipartPart;
    MultipartBody.Part bannerMultipartPart;

    @BindingAdapter("editProfileImage")
    public static void loadProfileImage(CircularImageView view, String imageUrl) {
        Log.d(TAG, "loadProfileImage: binding adapter lolol XDXDXD ");
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop()).placeholder(R.drawable.defaultprofile)
                .into(view);
    }

    @BindingAdapter("editBannerImage")
    public static void loadBannerImage(ImageView view, String bannerImageUrl) {
        Log.d(TAG, "loadProfileImage: binding adapter lolol XDXDXD ");
        Glide.with(view.getContext())
                .load(bannerImageUrl).apply(new RequestOptions().centerCrop()).placeholder(R.color.wishroll_blue)
                .into(view);
    }



    public ObservableField<String> editName = new ObservableField<>(sessionManagement.getName());

    public String getEditName(){
        return editName.get();
    }

    public void setEditName(ObservableField<String> name) {
        this.editName = name;
        this.editName.notifyChange();
    }


    public ObservableField<String> editUsername = new ObservableField<>(sessionManagement.getUsername());

    public String getEditUsername(){
        return editUsername.get();
    }

    public void setEditUsername(ObservableField<String> username) {
        this.editUsername = username;
        this.editUsername.notifyChange();
    }




    public ObservableField<String> editEmail = new ObservableField<>(sessionManagement.getEmail());

    public String getEditEmail(){
        return editEmail.get();
    }

    public void setEditEmail(ObservableField<String> email) {
        this.editEmail = email;
        this.editEmail.notifyChange();
    }


    public ObservableField<String> editBio = new ObservableField<>(sessionManagement.getBio());
    public String getEditBio(){
        return editBio.get();
    }

    public void setEditBio(ObservableField<String> bio) {
        this.editBio = bio;
        this.editBio.notifyChange();
    }




    String editNameNow;
    String editUsernameNow;
    String editEmailNow;
    String editBioNow;
    String editProfileURLNow;
    String editBackgroundURLNow;

    public void setEditProfileURLNow(String url){
        this.editProfileURLNow = url;

    }

    public String getEditProfileURLNow(){
        return editProfileURLNow;
    }

    public void setEditBackgroundURLNow(String url){
        this.editBackgroundURLNow = url;
    }

    public String getEditBackgroundURLNow(){
        return editBackgroundURLNow;
    }




    public void afterNameChange(CharSequence s){
        this.editNameNow =  s.toString();
    }
    public void afterUsernameChange(CharSequence s){
        this.editUsernameNow =  s.toString();



    }
    public void afterEmailChange(CharSequence s){
        this.editEmailNow =  s.toString();
    }
    public void afterBioChange(CharSequence s){
        this.editBioNow =  s.toString();
    }
    public void afterBackgroundURLChange(CharSequence s){
        this.editBackgroundURLNow =  s.toString();
    }
    public void afterProfileURLChange(CharSequence s){
        this.editProfileURLNow =  s.toString();
    }


    public RequestBody createPartFromString(String entryString){

        return RequestBody.create( entryString, MultipartBody.FORM );
    }






    public void onSaveChanges(){
        boolean changedProfile = false;
        boolean changedBackground = false;

        sessionManagement.printEverything("save changes button is pressed.");

        if(editUsername.get() == null ||  editEmail.get() == null ){
            authListener.sendMessage("Please enter a value.");

        }else if(!usernameIsValid(editUsername.get()) && editUsernameNow != null && !editUsernameNow.equals(sessionManagement.getUsername())){
            authListener.sendMessage("Please enter a valid username");

        }else{


            if (editUsernameNow != null && !sessionManagement.getUsername().equals(editUsernameNow)) { //if its changed then do this
                changedValues.put("username", createPartFromString(formatUsername(editUsernameNow)));

            }

            if (editNameNow != null) {  //meaning if it changed
                changedValues.put("name", createPartFromString(editNameNow.trim()));
            }

            if (editEmailNow != null) { //if this changed then do this

                if (!emailIsVerified(editEmailNow)) {
                    authListener.sendMessage("Please enter a valid email");

                } else {
                    changedValues.put("email", createPartFromString(editEmailNow.trim()));
                }

            }

            if (editBioNow != null) {
                changedValues.put("bio", createPartFromString(editBioNow.trim()));

            }

            if (editBackgroundURLNow != null) {
                Log.d(TAG, "onSaveChanges: BACKGROUND PATH GOTTEN FROM ACTIVITY " + editBackgroundURLNow);

                File backgroundPicture = new File(editBackgroundURLNow);

                backgroundFileName = backgroundPicture.getName();

                backgroundRequest = RequestBody.create(backgroundPicture, MediaType.parse(FileUtils.getMimeType(backgroundPicture)));

                bannerMultipartPart = MultipartBody.Part.createFormData("profile_background_media", backgroundFileName, backgroundRequest);

                changedBackground = true;




            }

            if (editProfileURLNow != null) {
                Log.d(TAG, "onSaveChanges: PROFILE PATH GOTTEN FROM ACTIVITY " + editProfileURLNow);
                File profilePicture = new File(editProfileURLNow);
                profileFileName = profilePicture.getName();

                profileRequest = RequestBody.create(profilePicture, MediaType.parse(FileUtils.getMimeType(profilePicture)));

                profileMultipartPart = MultipartBody.Part.createFormData("avatar", profileFileName, profileRequest);

                changedProfile = true;


            }



            if (!changedValues.isEmpty()) {
                sessionManagement.printEverything("user changed some values");
                Log.d(TAG, "onSaveChanges: all values in the changed " + changedValues.toString());

                if(changedValues.get("username") == null ){
                    changedValues.remove("username");
                }

                if( !changedBackground && !changedProfile){
                    //send to server!!!! XD<333
                    updateCurrentUser(changedValues, null, null);

                }else if(!changedBackground && changedProfile){

                    updateCurrentUser(changedValues, profileMultipartPart, null);

                }else if(changedBackground && !changedProfile){

                    updateCurrentUser(changedValues, null, bannerMultipartPart);
                }

            } else {

                if(changedBackground && !changedProfile){

                    updateCurrentUser(null, null, bannerMultipartPart);

                }else if(!changedBackground && changedProfile){

                    updateCurrentUser(null, profileMultipartPart, null);

                }else if(changedBackground && changedProfile){
                    updateCurrentUser(null, profileMultipartPart, bannerMultipartPart);
                }

                sessionManagement.printEverything("user did not change any values");
                Log.d(TAG, "onSaveChanges: the user did not make any changes");
            }


        }

        Log.d(TAG, "DID THE BACKGROUND CHANGE?" + changedBackground);


        Log.d(TAG, "DID THE PROFILE CHANGE?" + changedProfile);
    }





    public MediatorLiveData<StateData<UpdateResponse>> observeEditsMade(){
        return editedCurrentUser;
    }



    public void updateCurrentUser(Map<String, RequestBody> changedAttributes, MultipartBody.Part profilePicture, MultipartBody.Part bannerPicture){
        //for when the save button is pressed
        Log.d(TAG, "updateCurrentUser: in the update current user method of the view model");
        editedCurrentUser.setValue(StateData.loading(null));

        final LiveData<StateData<UpdateResponse>> source = userRepository.updateUser(changedAttributes, profilePicture, bannerPicture);
        editedCurrentUser.addSource(source, new Observer<StateData<UpdateResponse>>() {

            @Override
            public void onChanged(StateData<UpdateResponse> updatedResponseStateData) {
                Log.d(TAG, "onChanged: value has changed so now we set value and remove source");
                editedCurrentUser.setValue(updatedResponseStateData);
                editedCurrentUser.removeSource(source);
            }
        });
    }


    public boolean usernameIsValid(String usernameInput){
        //Username Validation: no triple periods or underscores, no longer than 20 characters, no special characters

        String usernameRegex = "^[A-Z0-9]([._](?![._])|[a-z0-9]){1,20}[a-z0-9]$";

        Pattern usernamePat = Pattern.compile(usernameRegex, Pattern.CASE_INSENSITIVE);


        Matcher matcher = usernamePat.matcher(usernameInput);

        return matcher.find();

    }

    public String formatUsername(String username){

        return username.toLowerCase().trim().replace(' ', '_');
    }

    public static boolean emailIsVerified(String emailInput){
        //checks if email entry is in correct email form

        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = emailPat.matcher(emailInput);

        return matcher.find();
    }


}
