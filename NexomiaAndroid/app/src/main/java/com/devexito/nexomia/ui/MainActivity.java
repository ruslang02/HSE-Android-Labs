package com.devexito.nexomia.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    SubMenu serversMenu;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkToken()) return;

        updateUserInfo();
        updateServers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_channels_list, R.id.nav_welcome, R.id.nav_messages)
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
                item.setChecked(true);
                return true;
            }
        });
        serversMenu = menu.addSubMenu("Servers");

        if (checkToken()) return;

        updateUserInfo();
        updateServers();
    }

    private boolean checkToken() {
        if (NVWSClient.getInstance().getToken() == null) {
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void updateUserInfo() {
        NVWSClient.getInstance().sendRequest(Request.Method.POST, "http://nexo.fun:8081/api/users/me", new RequestListener() {
            @Override
            public void onFinish(JSONObject object) {
                ImageView userAvatar = (ImageView) findViewById(R.id.userAvatar);
                TextView userName = (TextView) findViewById(R.id.userName);
                TextView userTag = (TextView) findViewById(R.id.userTag);
                try {
                    String avatar = object.getJSONObject("user").getString("avatar");
                    if (avatar.equals("")) {
                        Picasso.get().load("http://nexo.fun:8084/img/defaultAvatar.3304bd4b.png").into(userAvatar);
                    } else {
                        Picasso.get().load(avatar).into(userAvatar);
                    }
                    userName.setText(object.getJSONObject("user").getString("name"));
                    userTag.setText("#" + object.getJSONObject("user").getString("tag"));
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
                try {
                    JSONArray servers = object.getJSONArray("servers");
                    serversMenu.clear();
                    for(int i = 0; i < servers.length(); i++) {
                        final JSONObject server = servers.getJSONObject(i);
                        final MenuItem item = serversMenu.add(server.getString("title"));
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
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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