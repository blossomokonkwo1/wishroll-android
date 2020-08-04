package co.wishroll.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import co.wishroll.R;
import co.wishroll.utilities.SearchViewPagerAdapter;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchBar = findViewById(R.id.etSearchBarView);



        TextView cancelButton = findViewById(R.id.cancelButton);



        ViewPager2 viewPager2 = findViewById(R.id.viewPagerSearchView);
        viewPager2.setAdapter(new SearchViewPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayoutSearchView);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {


                switch (position){

                    case 0:{
                        tab.setText("Posts");

                        break;}

                    default: {
                        tab.setText("Profiles");
                        break;
                    }
                }

            }
        });

        tabLayoutMediator.attach();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });
    }

}