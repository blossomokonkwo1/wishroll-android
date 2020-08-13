package co.wishroll.models.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.wishroll.utilities.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  ApiService {
    //houses Retrofit Instance

    private static Retrofit retrofitInstance = null;



    Gson gson = new GsonBuilder().serializeNulls().create();
    //gson class that lets you be able to change values into nulls in a PUT/PATCH method




    public static Retrofit getRetrofitInstance() {

        if(retrofitInstance == null){

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            return new Retrofit.Builder().baseUrl(Constants.getAPI_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


        }
        return retrofitInstance;
    }

    public static WishRollApi wishRollApi = ApiService.getRetrofitInstance().create(WishRollApi.class);

    public WishRollApi getWishRollApi() {
        return wishRollApi;
    }
}
