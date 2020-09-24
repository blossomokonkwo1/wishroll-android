package co.wishroll.models.networking;

import java.util.List;
import java.util.Map;

import co.wishroll.models.domainmodels.Notification;
import co.wishroll.models.domainmodels.Post;
import co.wishroll.models.domainmodels.User;
import co.wishroll.models.repository.datamodels.AuthResponse;
import co.wishroll.models.repository.datamodels.EValidationRequest;
import co.wishroll.models.repository.datamodels.LoginRequest;
import co.wishroll.models.repository.datamodels.SignupRequestMany;
import co.wishroll.models.repository.datamodels.UValidationRequest;
import co.wishroll.models.repository.datamodels.UpdateResponse;
import co.wishroll.models.repository.datamodels.UploadPostResponse;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WishRollApi {
    //all wishroll api methods and calls



    //Authentication and Signup
    @POST("login")
    Flowable<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<AuthResponse> signupUser(@Body SignupRequestMany signupRequestMany);

    @POST("signup/email")
    Call<AuthResponse> validateEmail(@Body EValidationRequest eValidationRequest);

    @POST("signup/username")
    Call<AuthResponse> validateUsername(@Body UValidationRequest uValidationRequest);





    //User Profiles & Current User Profile

    @GET("users/{id}")
    Flowable<User> getUserById(@Path ("id") int id);


    @GET("users/{username}")
    Flowable<User> getUserByUsername(@Path ("username") String username);


    @Multipart
    @PUT("user/update")
    Flowable<UpdateResponse> updateUserDetails(@PartMap Map<String, RequestBody> parameters,
                                               @Part MultipartBody.Part profile,
                                               @Part MultipartBody.Part banner);

    @Multipart
    @PUT("user/update")
    Flowable<UpdateResponse> updateUserDetails(@PartMap Map<String, RequestBody> parameters,
                                               @Part MultipartBody.Part profile);

    @Multipart
    @PUT("user/update")
    Flowable<UpdateResponse> updateUserDetails(@PartMap Map<String, RequestBody> parameters);

    @Multipart
    @PUT("user/update")
    Flowable<UpdateResponse> updateUserDetails( @Part MultipartBody.Part profile,
                                                @Part MultipartBody.Part banner);

    @Multipart
    @PUT("user/update")
    Flowable<UpdateResponse> updateUserDetails( @Part MultipartBody.Part profile);




    //Uploading Posts

    @Multipart
    @POST("posts")
    Single<UploadPostResponse> uploadPost(@Part MultipartBody.Part post, @Part("caption") RequestBody caption);

    @Multipart
    @POST("posts")
    Single<UploadPostResponse> uploadPost(@Part MultipartBody.Part post); //does api accept nulls or separate calls for each case?

    @Multipart
    @POST("posts")
    Single<UploadPostResponse> uploadVideo(@Part MultipartBody.Part post, @Part MultipartBody.Part videoThumbnail);

    @Multipart
    @POST("posts")
    Single<UploadPostResponse> uploadVideo(@Part MultipartBody.Part post, @Part MultipartBody.Part videoThumbnail, @Part("caption") RequestBody caption );


    @FormUrlEncoded
    @POST("posts/{post_id}/tags")
    Completable sendTags(@Path("post_id") int postID,  @Field("tags[]") String[] tags);




    @GET("trending/posts")
    Flowable<List<Post>> getDiscoverPosts(@Query("offset") int offset);
    //loads data straight to local database with room...^ - ^



    @GET("trending/posts")
    Call<List<Post>> getDiscoverPostss(@Query("offset") int offset);









    @GET("")
    Flowable<List<User>> getListFollowers();
    //gets list of user's followers

    @GET("")
    Flowable<List<User>> getListFollowing();
    //gets list of users who user followed

    @GET("")
    Flowable<List<Post>> getUploadedPosts();
    //gets list of uploads by user

    @GET("")
    Flowable<List<Post>> getLikedPosts();
    //gets list of posts liked by user

    @GET("")
    Flowable<List<Post>> getBookmarkedPost();
    //gets list of posts bookmarked by user

    @GET("")
    Flowable<List<Notification>> getNotifications();

    @POST("")
    Completable likePost();
    //toggle mechanism

    @POST("")
    Completable bookmarkPost();
    //toggle mechanism




}
