package lk.supervision.saleapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.activity.DeviceListActivity;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MSettings;
import lk.supervision.saleapp.print.UnicodeFormatter;

public class FragmentSettings extends Fragment implements Runnable {

    private View settingsFragment;
    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private MSettings mSettings;

    //bluetooth
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothAdapter mBluetoothAdapter;

    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;

    private Button btnBluetoothScan;
    private Button btnBluetoothTest;

    private Button btnSaveSettings;
    private Button btnBackup;
    private Button btnSentBackupEmail;

    private TextView textSettingsBluetoothPrinter;
    private TextView textSettingsBluetoothPrinterMac;

    public FragmentSettings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);

        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(settingsFragment.getContext());
        textSettingsBluetoothPrinter = (TextView) settingsFragment.findViewById(R.id.text_settings_bluetooth_printer);
        textSettingsBluetoothPrinterMac = (TextView) settingsFragment.findViewById(R.id.text_settings_bluetooth_printer_mac);

        btnSaveSettings = (Button) settingsFragment.findViewById(R.id.btn_save_settings);
        btnBluetoothScan = (Button) settingsFragment.findViewById(R.id.btn_bluetooth_scan);
        btnBluetoothTest = (Button) settingsFragment.findViewById(R.id.btn_bluetooth_test);
        btnBackup = (Button) settingsFragment.findViewById(R.id.btn_backup);
        btnSentBackupEmail = (Button) settingsFragment.findViewById(R.id.btn_send_backup_send_email);

        mSettings = new MSettings();
        long mSettingsCount = saleAppDatabaseHelper.mSettingsCount();
        if (mSettingsCount > 0) {
            mSettings = saleAppDatabaseHelper.viewMSettings();
            textSettingsBluetoothPrinter.setText(mSettings.getBluetoothPrinter());
            textSettingsBluetoothPrinterMac.setText(mSettings.getBluetoothPrinterMac());
        }

        btnBluetoothScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(settingsFragment.getContext(), "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(getContext(), DeviceListActivity.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSettingsBluetoothPrinter.getText().toString().isEmpty()) {
                    textSettingsBluetoothPrinter.setError("PLEASE SELECT BLUETOOTH PRINTER");
                } else if (textSettingsBluetoothPrinterMac.getText().toString().isEmpty()) {
                    textSettingsBluetoothPrinterMac.setError("PLEASE SELECT BLUETOOTH PRINTER");
                } else {
                    mSettings.setBluetoothPrinter(textSettingsBluetoothPrinter.getText().toString());
                    mSettings.setBluetoothPrinterMac(textSettingsBluetoothPrinterMac.getText().toString());
                    int updateMSettings = saleAppDatabaseHelper.updateMSettings(mSettings);
                    if (updateMSettings > 0) {
                        AppEnvironmentValues.snackbarCustome(settingsFragment, "PRINT SUCCESS", "SUCCESS");
                    } else {
                        AppEnvironmentValues.snackbarCustome(settingsFragment, "PRINT ERROR", "ERROR");
                    }
                }
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SV-SALE APP-" + AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(new Date()) + ".db");
                    File currentDB = settingsFragment.getContext().getDatabasePath("saleapp.db");
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        long transferFrom = dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        if (transferFrom > 0) {
                            Toast.makeText(settingsFragment.getContext(), "DATABASE COPED", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(settingsFragment.getContext(), "DATABASE NOT FOUND", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        btnSentBackupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = settingsFragment.getContext().getSharedPreferences("SALEAPP", 0);
                final String loginUserId = sharedPreferences.getString("LOGIN_USER_ID", "NULL");
                final String loginUserName = sharedPreferences.getString("LOGIN_USER_NAME", "NULL");

                String filename = "SV-SALE APP-" + AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(new Date()) + ".db";
                try {
                //database backup function
                File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
                File currentDB = settingsFragment.getContext().getDatabasePath("saleapp.db");
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    long transferFrom = dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    if (transferFrom > 0) {
                        Toast.makeText(settingsFragment.getContext(), "DATABASE COPED", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(settingsFragment.getContext(), "DATABASE NOT FOUND", Toast.LENGTH_SHORT).show();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //email function
                File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
                Uri path = Uri.fromFile(filelocation);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // set the type to 'email'
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {"kavishmanjitha@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                // the attachment
                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                // the mail subject
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,  "SV-SALE APP-" + AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(new Date()));

                String body = "";
                body = body + "---------------------------------------------------------------------\n";
                body = body + "DATABASE NAME    - " + SaleAppDatabaseHelper.DATABASE_NAME + "\n";
                body = body + "DATABASE VERSION - " + SaleAppDatabaseHelper.DATABASE_VERSION + "\n";
                body = body + "LOGIN USER NAME  - " + loginUserName + "\n";
                body = body + "LOGIN USER ID    - " + loginUserId + "\n";
                body = body + "DATE             - " + AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(new Date()) + "\n";
                body = body + "TIME              - " + AppEnvironmentValues.SIMPLE_TIME_FORMAT.format(new Date()) + "\n";
                body = body + "SERVER IP        - " + AppEnvironmentValues.SERVER_ADDRESS + "\n";
                body = body + "---------------------------------------------------------------------\n";

                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(emailIntent, "Send Email..."));
            }
        });

        btnBluetoothTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deafaulBluetoothPrinter = saleAppDatabaseHelper.getDeafaulBluetoothPrinter();
                mBluetoothSocket = getBlueToothPrinter(deafaulBluetoothPrinter);
                if (mBluetoothSocket != null) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                OutputStream os = mBluetoothSocket.getOutputStream();
                                String BILL = "";
                                BILL = BILL + "-------------------------------\n";
                                BILL = BILL + "       ROYAL MARKETING \n";
                                BILL = BILL + "No: 60/40, Polgasowita, Halpita.\n";
                                BILL = BILL + "Hot Line : 0703500556 \n";
                                BILL = BILL + "           0703500550 \n";
                                BILL = BILL + "-------------------------------\n";
                                BILL = BILL + "     www.supervision.lk";
                                BILL = BILL + "\n";
                                BILL = BILL + "\n";
                                BILL = BILL + "\n";
                                os.write(BILL.getBytes());
                                //This is printer specific code you can comment ==== > Start

                                // Setting height
                                int gs = 29;
                                os.write(intToByteArray(gs));
                                int h = 104;
                                os.write(intToByteArray(h));
                                int n = 162;
                                os.write(intToByteArray(n));

                                // Setting Width
                                int gs_width = 29;
                                os.write(intToByteArray(gs_width));
                                int w = 119;
                                os.write(intToByteArray(w));
                                int n_width = 2;
                                os.write(intToByteArray(n_width));
                            } catch (Exception e) {
                                Log.d("PRINT ACTIVITY", e.toString());
                            }
                            AppEnvironmentValues.snackbarCustome(settingsFragment, "PRINT BILL", "SUCCESS");
                            closeSocket(mBluetoothSocket);
                        }
                    };
                    t.start();
                } else {
                    AppEnvironmentValues.snackbarCustome(settingsFragment, "PRINT ERROR", "ERROR");
                }
            }
        });
        return settingsFragment;
    }

    private boolean checklueToothDevice() {
        try {
            if (mBluetoothDevice.getName() != null) {
                textSettingsBluetoothPrinter.setText(mBluetoothDevice.getName());
                textSettingsBluetoothPrinterMac.setText(mBluetoothDevice.getAddress());
                return true;
            }
        } catch (NullPointerException ex) {
            textSettingsBluetoothPrinter.setText("PLEASE SELECT BLUETOOTH PRINTER");
            textSettingsBluetoothPrinterMac.setText("PLEASE SELECT BLUETOOTH PRINTER");
        }
        return false;
    }

    private BluetoothSocket getBlueToothPrinter(String blutoothDeviceAddress) {
        BluetoothDevice tempBluetoothDevice = null;
        BluetoothSocket tempBluetoothSocket = null;
        BluetoothAdapter tempBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = tempBluetoothAdapter.getBondedDevices();
        if (!mPairedDevices.isEmpty()) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                if (blutoothDeviceAddress.equals(mDevice.getAddress())) {
                    tempBluetoothDevice = mDevice;
                    break;
                }
            }
        }
        try {
            tempBluetoothSocket = tempBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            tempBluetoothAdapter.cancelDiscovery();
            tempBluetoothSocket.connect();
        } catch (IOException eConnectException) {
            closeSocket(mBluetoothSocket);
        } catch (NullPointerException ex) {
            AppEnvironmentValues.snackbarCustome(settingsFragment, "PRINT ERROR PLEASE RE CONFIG PRINTER", "ERROR");
        }
        Thread mBlutoothConnectThread = new Thread(this);
        mBlutoothConnectThread.start();
        return tempBluetoothSocket;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.d("PRINT ACTIVITY", e.toString());
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v("PRINT ACTIVITY", "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(settingsFragment.getContext(), "Connecting...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(settingsFragment.getContext(), DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(settingsFragment.getContext(), "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v("PRINT ACTIVITY", "PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d("PRINT ACTIVITY", "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        } catch (NullPointerException ex) {
            Log.d("PRINT ACTIVITY", "NULL");
            //closeSocket(mBluetoothSocket);
            //return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d("PRINT ACTIVITY", "SocketClosed");
        } catch (IOException ex) {
            Log.d("PRINT ACTIVITY", "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            mBluetoothConnectProgressDialog.cancel();
            checklueToothDevice();
            Toast.makeText(settingsFragment.getContext(), "BlueTooth Printer Device Connected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x" + UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }
}
