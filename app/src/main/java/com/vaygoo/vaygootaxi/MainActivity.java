package com.vaygoo.vaygootaxi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.vaygoo.vaygootaxi.fragment.MapFragment;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private BottomSheetBehavior mBottomSheetBehavior;
    private MapFragment mapFragment;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences mSettings;

    public static final String APP_PREFERENCES_SAVE_COORDINATE = "saveCoordinate";
   // private String saveCoordinate;
    public static String saveCoordinate_s;

    public static final String APP_PREFERENCES_SAVE_ZOOM = "saveZoom";
    public static int saveZoom = 13;

    private LinearLayout bottomBar;
    private int imI =-1;

    private static final int PERMISSION_REQUEST_CODE_MAP = 111;
    private static final int PERMISSION_REQUEST_CODE_PHOTO = 222;
    private static final int PERMISSION_REQUEST_CODE_PHONE = 333;

    private String[] permissionsMap = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private String[] permissionsPhone = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE};

    private String[] permissionsPhoto = new String[]{Manifest.permission.CAMERA};

    private Drawer drawerD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.app_bar_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  appBarLayout = (AppBarLayout)findViewById(R.id.appbar);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_SAVE_COORDINATE)) {

            saveCoordinate_s = mSettings.getString(APP_PREFERENCES_SAVE_COORDINATE, null);
            // saveCoordinate_s = saveCoordinate;
        }
        if (mSettings.contains(APP_PREFERENCES_SAVE_ZOOM)) {

            saveZoom = mSettings.getInt(APP_PREFERENCES_SAVE_ZOOM, 13);
        }
       setSupportActionBar(toolbar);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.man))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_account).withSelectable(false)
                .withIcon(R.drawable.man);

//create the drawer and remember the `Drawer` drawerD object
         drawerD = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar) //.withAccountHeader(headerResult)
                .addDrawerItems(
                       // item1,
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.man)),
                       new DividerDrawerItem(),

                        new PrimaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(R.drawable.ic_action_settings),

                        new PrimaryDrawerItem().withName(R.string.drawer_item_help).withIcon(R.drawable.ic_action_help_outline),
                        new SwitchDrawerItem().withName(R.string.drawer_item_driver).withIcon(R.drawable.ic_action_directions_car),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(R.drawable.ic_action_drafts)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position)
                        {
                            case 0:
                              Log.v("Excel", "CASE 0");
                                break;
                            case 1:
                              Log.v("Excel", "CASE 1");
                                break;
                            case 2:
                              Log.v("Excel", "CASE 2");
                                break;
                            case 3:
                              Log.v("Excel", "CASE 3");
                                break;
                            case 5:
                              Log.v("Excel", "CASE 4");
                                break;

                        }
                        return true;
                    }
                })
                .build();

        ImageButton menuLeftDraw = (ImageButton) findViewById(R.id.menuLeftDraw);
        menuLeftDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
               // drawer.openDrawer(GravityCompat.START);
                drawerD.openDrawer();
            }
        });

        ImageButton gpsIB = (ImageButton) findViewById(R.id.gpsButton);
        gpsIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permGet(0, true);
            }
        });

      /*   bottomBar = (LinearLayout) findViewById(R.id.tabs);

       for (imI= 0; imI < 5; imI++) {
            ImageView imageView = (ImageView) bottomBar.getChildAt(imI);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    switch (view.getId())
                    {
                        case R.id.menu1:
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.openDrawer(GravityCompat.START);
                            break;
                        case R.id.minus:
                            mapFragment.zoomMap(false);
                            break;
                        case R.id.expand:

                            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                             //   bottomBar.setVisibility(View.GONE);
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                               // expand.setImageResource(R.drawable.ic_action_expand_less);
                            } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                               // bottomBar.setVisibility(View.VISIBLE);
                               // expand.setImageResource(R.drawable.ic_action_expand_more);
                            }
                            break;
                        case R.id.plus:
                            mapFragment.zoomMap(true);
                            break;
                        case R.id.gps:
                           permGet(0, true);
                            break;
                    }

                }
            });
        }*/
        View bottomSheet = findViewById( R.id.bottom_sheet1 );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED); //AUTO
       // mBottomSheetBehavior.setPeekHeight(110); //250
       // mBottomSheetBehavior.setHideable(false);


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
              // ImageView expand = (ImageView) findViewById(R.id.expand);
                Log.v("Excel", "CHANGED "+ mBottomSheetBehavior.getState());
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                //   expand.setImageResource(R.drawable.ic_action_expand_less);
                  //  mBottomSheetBehavior.setPeekHeight(50);
                   // appBarLayout.setExpanded(true);
                  //  bottomBar.setVisibility(View.VISIBLE);

                }
                else if(newState == BottomSheetBehavior.STATE_EXPANDED)
                {
                   // appBarLayout.setExpanded(false);
                   // bottomBar.setVisibility(View.GONE);
                 //   expand.setImageResource(R.drawable.ic_action_expand_more);
                  //  mBottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }

        });

      /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.getLocationReload();
            }
        });  */


       /* mapFragment = new MapFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

        ft1.add(R.id.container, mapFragment);
        ft1.commit();*/
        permGet(0, false);
    }

   /* public void closeBottomPanel()
    {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }*/


   final public void collapseOrExpandBottomSheetWhenTouchMap(boolean collapse)
   {
       if(collapse)
       mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
       else
           mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
   }

    @Override
    public void onBackPressed() {


      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
        {
            super.onBackPressed();
        }  */

        if (drawerD.isDrawerOpen()) {
            drawerD.closeDrawer();
        } else if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_SAVE_COORDINATE)) {

            saveCoordinate_s = mSettings.getString(APP_PREFERENCES_SAVE_COORDINATE, null);
           // saveCoordinate_s = saveCoordinate;
        }
        if (mSettings.contains(APP_PREFERENCES_SAVE_ZOOM)) {

            saveZoom = mSettings.getInt(APP_PREFERENCES_SAVE_ZOOM, 13);
        }
    }

    ////////////
  public final void permGet(int numOfPermissions, boolean isGPS)
    {
        Log.v("Excel", "isGPS "+ isGPS + " numOfperm "+ numOfPermissions);
        if (hasPermissions(numOfPermissions)){
            // our app has permissions.
            if(numOfPermissions == 0 && isGPS)
            {
                mapFragment.getLocationReload();
                Log.v("Excel", "isGPS0  numOfperm "+ numOfPermissions);
            }
            else if(numOfPermissions == 0)
            {
                Log.v("Excel", "isGPS1  numOfperm "+ numOfPermissions);
                mapFragment = new MapFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                ft1.add(R.id.container, mapFragment);
                ft1.commit();
            }
            else if(numOfPermissions == 1)
            {

            }
            else if(numOfPermissions == 2)
            {

            }

        }
        else {
            //our app doesn't have permissions, So i m requesting permissions.
            requestPermissionWithRationale(numOfPermissions);
        }
    }
    private boolean hasPermissions(int numOfPermission){ // 0 map and storage, 1 camera, 2 phone
        int res = 0;
        //string array of permissions,

        if(numOfPermission == 0)
        {
            for (String perms : permissionsMap){
                res = checkCallingOrSelfPermission(perms);
                if (!(res == PackageManager.PERMISSION_GRANTED)){
                    return false;
                }
            }
        }
        else if(numOfPermission == 1)
        {
            for (String perms : permissionsPhoto){
                res = checkCallingOrSelfPermission(perms);
                if (!(res == PackageManager.PERMISSION_GRANTED)){
                    return false;
                }
            }
        }
        else if(numOfPermission == 2)
        {
            for (String perms : permissionsPhone){
                res = checkCallingOrSelfPermission(perms);
                if (!(res == PackageManager.PERMISSION_GRANTED)){
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPerms(int numOfPerm){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(numOfPerm ==0)
            requestPermissions(permissionsMap, PERMISSION_REQUEST_CODE_MAP);
            else if(numOfPerm == 1)
                requestPermissions(permissionsPhoto, PERMISSION_REQUEST_CODE_PHOTO);
            else if(numOfPerm == 2)
                requestPermissions(permissionsPhone, PERMISSION_REQUEST_CODE_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        int numOfPermis = -1;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE_MAP:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                numOfPermis =0;
                break;
            case PERMISSION_REQUEST_CODE_PHOTO:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                numOfPermis =1;
                break;
            case PERMISSION_REQUEST_CODE_PHONE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                numOfPermis =2;
                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            if(numOfPermis == 0)
            {
                mapFragment = new MapFragment();

            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

            ft1.add(R.id.container, mapFragment);
            ft1.commit();
            }
            else if(numOfPermis == 1)
            {

            }
            else if(numOfPermis == 2)
            {

            }
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) && numOfPermis == 0){
                    Toast.makeText(this, R.string.denied_map, Toast.LENGTH_SHORT).show();

                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && numOfPermis == 0){
                    Toast.makeText(this, R.string.denied_storage, Toast.LENGTH_SHORT).show();

                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && numOfPermis == 1){
                    Toast.makeText(this, R.string.denied_camera, Toast.LENGTH_SHORT).show();

                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) && numOfPermis == 2){
                    Toast.makeText(this, R.string.denied_phone, Toast.LENGTH_SHORT).show();

                } else {
                    showNoPermissionSnackbar(numOfPermis);
                }
            }
        }

    }



    public final void showNoPermissionSnackbar(final int numOfPerm) {
        Snackbar.make(findViewById(R.id.coordinator), R.string.isnt_granted , Snackbar.LENGTH_LONG)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings(numOfPerm);

                        Toast.makeText(getApplicationContext(),
                                R.string.grant_permiss,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    public final void openApplicationSettings(int numOfPerm) {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        if(numOfPerm == 0)
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE_MAP);
        else if(numOfPerm == 1)
            startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE_PHOTO);
        else if(numOfPerm == 2)
            startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE_PHONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE_MAP) {
            mapFragment = new MapFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

            ft1.add(R.id.container, mapFragment);
            ft1.commit();
            return;
        }
        else if (requestCode == PERMISSION_REQUEST_CODE_PHOTO) {
           ///////////////////////////
            return;
        }
        else if (requestCode == PERMISSION_REQUEST_CODE_PHONE) {
           ////////////////////////////
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public final void requestPermissionWithRationale(final int numOfPerm) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)) {
            final String message = getString(R.string.show_perm);
            Snackbar.make(findViewById(R.id.coordinator), message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE)
                    .setAction(R.string.grant, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPerms(numOfPerm);
                        }
                    })
                    .show();
        } else {
            requestPerms(numOfPerm);
        }
    }

}
