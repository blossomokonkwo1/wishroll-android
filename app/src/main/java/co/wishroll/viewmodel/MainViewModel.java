package co.wishroll.viewmodel;

import androidx.lifecycle.ViewModel;

import co.wishroll.models.repository.UserRepository;
import co.wishroll.models.repository.local.SessionManagement;

import static co.wishroll.WishRollApplication.applicationGraph;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";
    SessionManagement sessionManagement = applicationGraph.sessionManagement();
    UserRepository userRepository = applicationGraph.userRepository();
    boolean isDeleted = false;



    public MainViewModel() {
    }



}