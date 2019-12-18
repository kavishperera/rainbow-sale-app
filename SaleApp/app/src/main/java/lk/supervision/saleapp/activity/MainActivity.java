package lk.supervision.saleapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.ServerHostAvailabilityTask;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MSperson;
import lk.supervision.saleapp.receive.RestApiReceiveController;

public class MainActivity extends AppCompatActivity {

    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private TextView networkStatus;
    private Handler handler = new Handler();
    private Button btnSignIn;
    private Button btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(this);
        btnSignIn = (Button) findViewById(R.id.btn_save);
        btnSync = (Button) findViewById(R.id.btn_sync);

        final EditText textUserName = (EditText) findViewById(R.id.text_user_name);
        final EditText textPassowrd = (EditText) findViewById(R.id.text_password);
        networkStatus = (TextView) findViewById(R.id.text_view_network_status);


        networkRefresh();
        userDetailsCheck();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textUserName.getText().toString().isEmpty()) {
                    textUserName.setError("PLASE ENTER USER NAME");
                } else if (textPassowrd.getText().toString().isEmpty()) {
                    textPassowrd.setError("PLASE ENTER PASSWORD");
                } else if (!textUserName.getText().toString().isEmpty() && !textPassowrd.getText().toString().isEmpty()) {

                    List<MSperson> mSpersonsList = saleAppDatabaseHelper.viewMSperson();
                    if (mSpersonsList.isEmpty()) {
                        Toast.makeText(getApplication(), "PLEASE SYNC USERS", Toast.LENGTH_LONG).show();
                    } else {
                        MSperson mSperson = saleAppDatabaseHelper.findByNameAndPasssword(textUserName.getText().toString().toUpperCase(), textPassowrd.getText().toString());

                        if (mSperson.getSpId() == null) {
                            Toast.makeText(getApplication(), "USER NOT FOUND", Toast.LENGTH_LONG).show();
                        } else {

                            //SharedPreferences set user data
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("SALEAPP", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("LOGIN_USER_ID", mSperson.getSpId());
                            editor.putString("LOGIN_USER_NAME", mSperson.getSpName());
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            startActivity(intent);
                        }
                    }

                }
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    if (ServerHostAvailabilityTask.isConnectedToServerLocalHost()) {
                        try {
                            AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_LOCAL;
                            RestApiReceiveController controller1 = new RestApiReceiveController();
                            MSperson[] mSpersonList1 = controller1.getMSperson();
                            Integer syncListSize1 = mSpersonList1.length;
                            boolean clearFunction1 = saleAppDatabaseHelper.clearSperson();
                            if (clearFunction1) {
                                for (MSperson sperson : mSpersonList1) {
                                    int status = saleAppDatabaseHelper.insertMSperson(sperson);
                                    if (status > 0) {
                                        syncListSize1 -= 1;
                                    }
                                }

                                if (syncListSize1 == 0) {
                                    AppEnvironmentValues.snackbarCustome(findViewById(android.R.id.content), "Local Synchronized successfully.", "SUCCESS");
                                    userDetailsCheck();
                                }
                                userDetailsCheck();
                            }
                        } catch (Exception e) {
                            AppEnvironmentValues.snackbarCustome(v, "SERVER NOT CONNECT!", "ERROR");
                        }
                    } else {
                        if (ServerHostAvailabilityTask.isConnectedToServerOnline()) {
                            try {
                                AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_ONLINE;
                                RestApiReceiveController controller = new RestApiReceiveController();
                                MSperson[] mSpersonList = controller.getMSperson();
                                Integer syncListSize = mSpersonList.length;
                                boolean clearFunction = saleAppDatabaseHelper.clearSperson();
                                if (clearFunction) {
                                    for (MSperson sperson : mSpersonList) {
                                        int status = saleAppDatabaseHelper.insertMSperson(sperson);
                                        if (status > 0) {
                                            syncListSize -= 1;
                                        }
                                    }

                                    if (syncListSize == 0) {
                                        AppEnvironmentValues.snackbarCustome(findViewById(android.R.id.content), "Online Synchronized successfully.", "SUCCESS");
                                        userDetailsCheck();
                                    }
                                    userDetailsCheck();
                                }
                            } catch (Exception e) {
                                AppEnvironmentValues.snackbarCustome(v, "SERVER NOT CONNECT!", "ERROR");
                            }
                        } else {
                            AppEnvironmentValues.snackbarCustome(v, "SERVER NOT CONNECT!", "ERROR");
                        }
                    }
                } else {
                    Toast.makeText(getApplication(), "PLEASE CONNECT INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    networkRefresh();
                    networkStatus.setText("NETWORK NOT CONNECTED");
                    networkStatus.setTextColor(getApplication().getResources().getColor(R.color.error));
                }
            }
        });

        networkStatus.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                try {
                    File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "saleapp.db");
                    File currentDB = getApplicationContext().getDatabasePath("saleapp.db");
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        long transferFrom = dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        if (transferFrom > 0) {
                            Toast.makeText(getApplication(), "DATABASE COPED", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplication(), "DATABASE NOT FOUND", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void networkRefresh() {
        //check internet connection
        if (isNetworkConnected()) {
            networkStatus.setText("NETWORK CONNECTED");
            btnSync.setVisibility(View.VISIBLE);
            networkStatus.setTextColor(this.getResources().getColor(R.color.right));
        } else {
            networkStatus.setText("NETWORK NOT CONNECTED");
            btnSync.setVisibility(View.GONE);
            networkStatus.setTextColor(this.getResources().getColor(R.color.error));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void userDetailsCheck() {
        List<MSperson> mSpersonsList = saleAppDatabaseHelper.viewMSperson();
        if (mSpersonsList.isEmpty()) {
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            btnSync.startAnimation(animation);
            btnSignIn.setVisibility(View.INVISIBLE);
        } else {
            btnSync.clearAnimation();
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }
}
