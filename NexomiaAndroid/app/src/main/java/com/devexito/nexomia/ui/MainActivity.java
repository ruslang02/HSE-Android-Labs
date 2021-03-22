package com.devexito.nexomia.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.devexito.nexomia.R;
import com.devexito.nexomia.api.NVWSClient;
import com.devexito.nexomia.api.RequestListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    NavigationView navigationView;

    NavController navController;

    Toolbar toolbar;

    SharedPreferences sharedPref;

    SubMenu serverMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_channels_list, R.id.nav_welcome, R.id.nav_friends_list)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu menu = navigationView.getMenu();
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toolbar.getMenu().clear();
                navController.navigate(R.id.nav_welcome);
                item.setChecked(true);
                toolbar.getMenu().clear();
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NVWSClient.getInstance().setServerId("@me");
                navController.navigate(R.id.nav_channels_list);
                createOptionsMenu(toolbar.getMenu());
                toolbar.setTitle("Direct Messages");
                item.setChecked(true);
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navController.navigate(R.id.nav_friends_list);
                item.setChecked(true);
                toolbar.getMenu().clear();
                return true;
            }
        });
        NavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NVWSClient.getInstance().logout();
                Intent intent = new Intent(MainActivity.this,
                        StatusChangeActivity.class);
                startActivity(intent);
                return false;
            }
        });
        bottom_nav.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });
        bottom_nav.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NVWSClient.getInstance().logout();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("token", null);
                editor.apply();
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });
        serverMenu = menu.addSubMenu("Servers");
        initClient();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initClient();
    }

    private void initClient() {
        if (checkToken()) return;
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        updateUserInfo();
                    }
                },
                1000);
        updateServers();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", NVWSClient.getInstance().getToken());
        editor.apply();
    }

    private boolean checkToken() {
        if (NVWSClient.getInstance().getToken() == null) {
            String token = sharedPref.getString("token", null);
            if (token == null) {
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                return true;
            } else {
                NVWSClient.getInstance().setContext(getApplicationContext());
                NVWSClient.getInstance().setToken(token);
                NVWSClient.getInstance().createWS();
            }
        }
        return false;
    }

    private void updateUserInfo() {
        NVWSClient.getInstance().fetchCurrentUser(new RequestListener() {
            @Override
            public void onFinish(JSONObject object) {
                ImageView userAvatar = (ImageView) findViewById(R.id.userAvatar);
                TextView userName = (TextView) findViewById(R.id.userName);
                TextView userTag = (TextView) findViewById(R.id.userTag);
                try {
                    String avatar = object.getString("avatar");
                    if (avatar.equals("")) {
                        Picasso.get().load("http://nexo.fun:8084/img/defaultAvatar.3304bd4b.png").into(userAvatar);
                    } else {
                        Picasso.get().load(avatar).into(userAvatar);
                    }
                    userName.setText(object.getString("name"));
                    userTag.setText("#" + object.getString("tag"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateServers() {
        NVWSClient.getInstance().sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/servers/list", new RequestListener() {
            @Override
            public void onFinish(JSONObject object) {
                serverMenu.clear();
                try {
                    JSONArray servers = object.getJSONArray("servers");
                    for(int i = 0; i < servers.length(); i++) {
                        final JSONObject server = servers.getJSONObject(i);
                        final MenuItem item = serverMenu.add(server.getString("title"));
                        item.setIcon(getResources().getDrawable(R.drawable.ic_channel));
                        if (!server.getString("avatar").equals("")) {
                            Picasso.get().load(server.getString("avatar")).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    item.setIcon(new BitmapDrawable(getResources(), bitmap));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                        }
                        item.setCheckable(true);
                        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem i) {
                                try {
                                    NVWSClient.getInstance().setServerId(server.getString("id"));
                                    navController.navigate(R.id.nav_channels_list);
                                    toolbar.setTitle(server.getString("title"));
                                    item.setChecked(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.i("sss", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void createOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server, menu);
        menu.findItem(R.id.leave_server).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NVWSClient.getInstance().setStatus();
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}