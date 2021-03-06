package com.ss.moviedb.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ss.moviedb.R;

public class MainMenuActivity extends AppCompatActivity {

    private AppBarLayout main_appBarLayout;
    private Toolbar main_toolbar;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView main_bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initialize();
        setListener();
    }

    private void setListener() {
        // * Expand app bar on fragment change
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                main_appBarLayout.setExpanded(true, true);
            }
        });
    }

    private void initialize() {
        main_appBarLayout = findViewById(R.id.main_appBarLayout);
        main_toolbar = findViewById(R.id.main_toolbar);
        main_bottomNavView = findViewById(R.id.main_bottomNavView);

        setSupportActionBar(main_toolbar);

        //==Start of navigation based on documentation & stackoverflow
        // * NavHostFragment - without this will cause app crashed (solution by Google)
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navHostFragment);

        // * NavController
        navController = navHostFragment.getNavController();

        // * Default navigation - this activity as top-level destination
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        // * Custom navigation - can set top-level destination
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.nowPlayingFragment, R.id.upcomingFragment).build();

        // * Action bar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // * Bottom navigation
        NavigationUI.setupWithNavController(main_bottomNavView, navController);
        //==End of navigation based on documentation
    }

    // * Action bar up button pressed
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}