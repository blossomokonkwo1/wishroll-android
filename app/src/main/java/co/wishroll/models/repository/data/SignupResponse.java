package co.wishroll.models.repository.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupResponse {
        //maps the data given by a signup POST request

        @SerializedName("user")
        @Expose
        private UserModel userModel;

        @SerializedName("access_token")
        @Expose
        private AccessToken accessToken;


        public SignupResponse() {
        }

        public SignupResponse(UserModel userModel, AccessToken accessToken) {
            super();
            this.userModel = userModel;
            this.accessToken = accessToken;
        }

        public UserModel getUser() {
            return userModel;
        }

        public void setUser(UserModel userModel) {
            this.userModel = userModel;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }




















}
