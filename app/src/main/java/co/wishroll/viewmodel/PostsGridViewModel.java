package co.wishroll.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import co.wishroll.models.domainmodels.Post;
import co.wishroll.models.repository.PostRepository;
import co.wishroll.models.repository.local.SessionManagement;
import co.wishroll.utilities.StateData;

import static co.wishroll.WishRollApplication.applicationGraph;

public class PostsGridViewModel extends ViewModel {
    MediatorLiveData<StateData<ArrayList<Post>>> postGridPostsLiveData = new MediatorLiveData<>();

    private static final String TAG = "PostsGridViewModel";

    PostRepository postRepository = applicationGraph.postRepository();
    SessionManagement sessionManagement = applicationGraph.sessionManagement();



    public int trendingTagId;
    public boolean isBookmarkQuery = false;
    public int START_OFFSET = 0;
    public static int offset = 0;
    public int dataSetSize = 0;

    public int getDataSetSize() {
        return dataSetSize;
    }

    public void setDataSetSize(int dataSetSize) {
        this.dataSetSize = dataSetSize;
    }

    public static int getOffset() {
        return offset;
    }

    public static void setOffset(int offset) {
        PostsGridViewModel.offset = offset;
    }

    public boolean isBookmarkQuery() {
        return isBookmarkQuery;
    }

    public void setBookmarkQuery(boolean bookmarkQuery) {
        isBookmarkQuery = bookmarkQuery;
    }

    public PostsGridViewModel() {
    }

    public PostsGridViewModel(int trendingTagId) {
        this.trendingTagId = trendingTagId;
    }

    public PostsGridViewModel(int trendingTagId, boolean isBookmarkQuery) {
        this.trendingTagId = trendingTagId;
        this.isBookmarkQuery = isBookmarkQuery;
        getFirstPostGrid();


    }


    public void getFirstPostGrid() {
        postGridPostsLiveData.setValue(StateData.loading((ArrayList<Post>) null));

        if (isBookmarkQuery || trendingTagId == -1) {
            final LiveData<StateData<ArrayList<Post>>> source = postRepository.getBookmarkedPosts(sessionManagement.getCurrentUserId(), START_OFFSET);

            postGridPostsLiveData.addSource(source, new Observer<StateData<ArrayList<Post>>>() {
                @Override
                public void onChanged(StateData<ArrayList<Post>> bookmarkedStateData) {

                    if (bookmarkedStateData.data != null) {
                        setDataSetSize(bookmarkedStateData.data.size());
                    } else {
                        setDataSetSize(15);
                    }

                    postGridPostsLiveData.setValue(bookmarkedStateData);
                    postGridPostsLiveData.removeSource(source);
                }
            });



        } else {


            final LiveData<StateData<ArrayList<Post>>> source = postRepository.getTaggedPosts(trendingTagId, START_OFFSET);

            postGridPostsLiveData.addSource(source, new Observer<StateData<ArrayList<Post>>>() {
                @Override
                public void onChanged(StateData<ArrayList<Post>> trendingTagStateData) {

                    if (trendingTagStateData.data != null) {
                        setDataSetSize(trendingTagStateData.data.size());
                    } else {
                        setDataSetSize(15);
                    }

                    postGridPostsLiveData.setValue(trendingTagStateData);
                    postGridPostsLiveData.removeSource(source);
                }
            });



        }

    }

    public void getMorePostGridPages(){

        postGridPostsLiveData.setValue(StateData.loading((ArrayList<Post>) null));

        if(isBookmarkQuery || trendingTagId == -1) {
            final LiveData<StateData<ArrayList<Post>>> source = postRepository.getBookmarkedPosts(sessionManagement.getCurrentUserId(), offset);
            postGridPostsLiveData.addSource(source, new Observer<StateData<ArrayList<Post>>>() {
                @Override
                public void onChanged(StateData<ArrayList<Post>> trendingTagStateData) {

                    if (trendingTagStateData.data != null) {
                        setDataSetSize(trendingTagStateData.data.size());
                    } else {
                        setDataSetSize(15);
                    }

                    postGridPostsLiveData.setValue(trendingTagStateData);
                    postGridPostsLiveData.removeSource(source);
                }
            });



        }else{

            //FOR OTHER QUERIES MOST LIKELY SEARCH GLOBAL QUERY VARIABLE NEEDED

            final LiveData<StateData<ArrayList<Post>>> source = postRepository.getTaggedPosts(trendingTagId, offset);
            postGridPostsLiveData.addSource(source, new Observer<StateData<ArrayList<Post>>>() {
                @Override
                public void onChanged(StateData<ArrayList<Post>> trendingTagStateData) {

                    if (trendingTagStateData.data != null) {
                        setDataSetSize(trendingTagStateData.data.size());
                    } else {
                        setDataSetSize(15);
                    }

                    postGridPostsLiveData.setValue(trendingTagStateData);
                    postGridPostsLiveData.removeSource(source);
                }
            });


        }



    }







    public LiveData<StateData<ArrayList<Post>>> observePostGrid(){
        return postGridPostsLiveData;
    }


}