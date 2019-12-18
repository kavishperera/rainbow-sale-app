package lk.supervision.saleapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.ServerHostAvailabilityTask;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MSettings;
import lk.supervision.saleapp.model.MTransactionData;
import lk.supervision.saleapp.receive.RestApiReceiveController;


public class FragmentSync extends Fragment {

    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private View view;
    private TextView networkStatus;
    private Button btnSyncBefore;
    private Button btnCardDetailClear;
    private Button btnAfterSync;
    private MSettings mSettings;

    public FragmentSync() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_sync, container, false);
        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(view.getContext());
        btnAfterSync = (Button) view.findViewById(R.id.btn_after_sync);
        btnSyncBefore = (Button) view.findViewById(R.id.btn_sync_before);
        btnCardDetailClear = (Button) view.findViewById(R.id.btn_card_detail_clear);
        networkStatus = (TextView) view.findViewById(R.id.text_view_network_status);

        long mSettingsCount = saleAppDatabaseHelper.mSettingsCount();
        if (mSettingsCount > 0) {
            mSettings = saleAppDatabaseHelper.viewMSettings();
            btnCardDetailClear.setText("CARD CLEAR \n" + mSettings.getClearCardLastDateTime());
            btnSyncBefore.setText("CARD DETAIL SYNC \n" + mSettings.getBeforSyncLastDateTime());
            btnAfterSync.setText("DAY END SYNC \n" + mSettings.getAfterSyncLastDateTime());
        }

        networkRefresh();

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("SALEAPP", 0);
        final String loginUser = sharedPreferences.getString("LOGIN_USER_ID", "NULL");

        btnAfterSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    if (ServerHostAvailabilityTask.isConnectedToServerLocalHost()) {
                        try {
                            AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_LOCAL;
                            List<MTransactionData> mTransactionDatas = saleAppDatabaseHelper.viewMTransactionData();
                            if (mTransactionDatas.isEmpty()) {
                                AppEnvironmentValues.snackbarCustome(view, "Synchronized Already.", "SUCCESS");
                            } else {
                                Integer syncListSize = mTransactionDatas.size();
                                RestApiReceiveController restApiReceiveController = new RestApiReceiveController();
                                for (MTransactionData mTransactionData : mTransactionDatas) {
                                    mTransactionData.setSysUpdateDate(new Date());
                                    Integer status = restApiReceiveController.saveTransactionData(mTransactionData);
                                    if (status > 0) {
                                        mTransactionData.setSysUpdate(1);
                                        int updateSatatus = saleAppDatabaseHelper.updateMTransactionData(mTransactionData);
                                        if (updateSatatus > 0) {
                                            syncListSize -= 1;
                                        }
                                    } else {
                                        AppEnvironmentValues.snackbarCustome(view, "Synchronized fail.", "ERROR");
                                    }
                                }

                                if (syncListSize == 0) {
                                    if (!loginUser.isEmpty()) {
                                        final Integer lastSerialNo = saleAppDatabaseHelper.findByMSpersonLastSerialNo(loginUser);
                                        String respondStatus = restApiReceiveController.saveSpersondata(loginUser, lastSerialNo);
                                        if (loginUser.equals(respondStatus)) {
                                            AppEnvironmentValues.snackbarCustome(view, "Local Synchronized successfully.", "SUCCESS");
                                            mSettings.setAfterSyncLastDateTime(AppEnvironmentValues.getSystemDateTimeFormat());
                                            int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                                            if (updateMSettings > 0) {
                                                btnAfterSync.setText("DAY END SYNC \n" + AppEnvironmentValues.getSystemDateTimeFormat());
                                            }
                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "Local Synchronized fail.", "ERROR");
                                        }
                                    } else {
                                        AppEnvironmentValues.snackbarCustome(view, "PLEASE LOGOUT AND RELOGIN", "ERROR");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            AppEnvironmentValues.snackbarCustome(view, "SERVER NOT CONNECT!", "ERROR");
                        }
                    } else {
                        if (ServerHostAvailabilityTask.isConnectedToServerOnline()) {
                            try {
                                AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_ONLINE;
                                List<MTransactionData> mTransactionDatas = saleAppDatabaseHelper.viewMTransactionData();
                                if (mTransactionDatas.isEmpty()) {
                                    AppEnvironmentValues.snackbarCustome(view, "Synchronized Already.", "SUCCESS");
                                } else {
                                    Integer syncListSize = mTransactionDatas.size();
                                    RestApiReceiveController restApiReceiveController = new RestApiReceiveController();
                                    for (MTransactionData mTransactionData : mTransactionDatas) {
                                        mTransactionData.setSysUpdateDate(new Date());
                                        Integer status = restApiReceiveController.saveTransactionData(mTransactionData);
                                        if (status > 0) {
                                            mTransactionData.setSysUpdate(1);
                                            int updateSatatus = saleAppDatabaseHelper.updateMTransactionData(mTransactionData);
                                            if (updateSatatus > 0) {
                                                syncListSize -= 1;
                                            }
                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "Synchronized fail.", "ERROR");
                                        }
                                    }

                                    if (syncListSize == 0) {
                                        if (!loginUser.isEmpty()) {
                                            final Integer lastSerialNo = saleAppDatabaseHelper.findByMSpersonLastSerialNo(loginUser);
                                            String respondStatus = restApiReceiveController.saveSpersondata(loginUser, lastSerialNo);
                                            if (loginUser.equals(respondStatus)) {
                                                AppEnvironmentValues.snackbarCustome(view, "Online Synchronized successfully.", "SUCCESS");
                                                mSettings.setAfterSyncLastDateTime(AppEnvironmentValues.getSystemDateTimeFormat());
                                                int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                                                if (updateMSettings > 0) {
                                                    btnAfterSync.setText("DAY END SYNC \n" + AppEnvironmentValues.getSystemDateTimeFormat());
                                                }
                                            } else {
                                                AppEnvironmentValues.snackbarCustome(view, "Online Synchronized fail.", "ERROR");
                                            }
                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "PLEASE LOGOUT AND RELOGIN", "ERROR");
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                AppEnvironmentValues.snackbarCustome(view, "SERVER NOT CONNECT!", "ERROR");
                            }
                        }
                    }
                } else {
                    AppEnvironmentValues.snackbarCustome(view, "PLEASE CONNECT INTERNET!", "ERROR");
                }
            }
        });

        btnSyncBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    if (ServerHostAvailabilityTask.isConnectedToServerLocalHost()) {
                        AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_LOCAL;
                        try {
                            if (!loginUser.isEmpty()) {
                                RestApiReceiveController controller = new RestApiReceiveController();
                                MCardDetails[] cardDetailsList = controller.getCardDetails(loginUser);
                                Integer syncListSize = cardDetailsList.length;
                                saleAppDatabaseHelper.clearMCardDetails();
                                for (MCardDetails mCardDetails : cardDetailsList) {
                                    int status = saleAppDatabaseHelper.insertMCardDetails(mCardDetails);
                                    if (status > 0) {
                                        syncListSize -= 1;
                                    }
                                }

                                if (syncListSize == 0) {
                                    AppEnvironmentValues.snackbarCustome(view, "Local Synchronized successfully.", "SUCCESS");
                                    mSettings.setBeforSyncLastDateTime(AppEnvironmentValues.getSystemDateTimeFormat());
                                    int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                                    if (updateMSettings > 0) {
                                        btnSyncBefore.setText("CARD DETAIL SYNC \n" + AppEnvironmentValues.getSystemDateTimeFormat());
                                    }
                                }
                            } else {
                                AppEnvironmentValues.snackbarCustome(view, "PLEASE LOGOUT AND RELOGIN", "ERROR");
                            }
                        } catch (Exception e) {
                            AppEnvironmentValues.snackbarCustome(view, "SERVER NOT CONNECT!", "ERROR");
                        }
                    } else {
                        if (ServerHostAvailabilityTask.isConnectedToServerOnline()) {
                            AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_ONLINE;
                            try {
                                if (!loginUser.isEmpty()) {
                                    RestApiReceiveController controller = new RestApiReceiveController();
                                    MCardDetails[] cardDetailsList = controller.getCardDetails(loginUser);
                                    Integer syncListSize = cardDetailsList.length;
                                    saleAppDatabaseHelper.clearMCardDetails();
                                    for (MCardDetails mCardDetails : cardDetailsList) {
                                        int status = saleAppDatabaseHelper.insertMCardDetails(mCardDetails);
                                        Log.d("TEST", mCardDetails.getConNo());
                                        if (status > 0) {
                                            syncListSize -= 1;
                                        }
                                    }

                                    if (syncListSize == 0) {
                                        AppEnvironmentValues.snackbarCustome(view, "Online Synchronized successfully.", "SUCCESS");
                                        mSettings.setBeforSyncLastDateTime(AppEnvironmentValues.getSystemDateTimeFormat());
                                        int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                                        if (updateMSettings > 0) {
                                            btnSyncBefore.setText("CARD DETAIL SYNC \n" + AppEnvironmentValues.getSystemDateTimeFormat());
                                        }
                                    }
                                } else {
                                    AppEnvironmentValues.snackbarCustome(view, "PLEASE LOGOUT AND RELOGIN", "ERROR");
                                }

                            } catch (Exception e) {
                                AppEnvironmentValues.snackbarCustome(view, "SERVER NOT CONNECT!", "ERROR");
                            }
                        }
                    }
                } else {
                    AppEnvironmentValues.snackbarCustome(view, "PLEASE CONNECT INTERNET!", "ERROR");
                }
            }
        });

        btnCardDetailClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saleAppDatabaseHelper.clearMCardDetails();
                mSettings.setClearCardLastDateTime(AppEnvironmentValues.getSystemDateTimeFormat());
                int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                if (updateMSettings > 0) {
                    btnCardDetailClear.setText("CARD CLEAR \n" + AppEnvironmentValues.getSystemDateTimeFormat());
                    AppEnvironmentValues.snackbarCustome(view, "Synchronized successfully.", "SUCCESS");
                }
            }
        });

        return view;
    }

    private void networkRefresh() {
        //check internet connection
        if (isNetworkConnected()) {
            networkStatus.setText("NETWORK CONNECTED");
            btnSyncBefore.setVisibility(View.VISIBLE);
            btnAfterSync.setVisibility(View.VISIBLE);
            networkStatus.setTextColor(this.getResources().getColor(R.color.right));
        } else {
            networkStatus.setText("NETWORK NOT CONNECTED");
            btnSyncBefore.setVisibility(View.INVISIBLE);
            btnAfterSync.setVisibility(View.INVISIBLE);
            networkStatus.setTextColor(this.getResources().getColor(R.color.error));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
