package lk.supervision.saleapp.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.DailySummryDatePicker;
import lk.supervision.saleapp.constant.DatePickerFragment;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MTransactionData;
import lk.supervision.saleapp.print.UnicodeFormatter;
import lk.supervision.saleapp.receive.RestApiReceiveController;

public class FragmentDailySummary extends Fragment implements Runnable {

    private View fragmentDailySummary;
    private FragmentActivity fragmentActivityContext;
    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private String BILL = "";

    //bluetooth
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothAdapter mBluetoothAdapter;

    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;

    public FragmentDailySummary() {
    }

    @Override
    public void onAttach(Activity activity) {
        fragmentActivityContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDailySummary = inflater.inflate(R.layout.fragment_fragment_daily_summary, container, false);

        final Button btnPrintDailySummary = (Button) fragmentDailySummary.findViewById(R.id.btn_print_daily_summary);
        final Button btnGeDailySummary = (Button) fragmentDailySummary.findViewById(R.id.btn_get_daily_summary);
        final Button btnDateSelect = (Button) fragmentDailySummary.findViewById(R.id.btn_date_select);
        final TextView textPrintView = (TextView) fragmentDailySummary.findViewById(R.id.text_print_view);
        final TextView textSelectDate = (TextView) fragmentDailySummary.findViewById(R.id.text_select_date);

        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(fragmentDailySummary.getContext());

        //select current date
        String date = AppEnvironmentValues.SIMPLE_DATE_FORMAT.format(new Date());
        textSelectDate.setText(date);

        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailySummryDatePicker dailySummryDatePicker = new DailySummryDatePicker();
                dailySummryDatePicker.show(fragmentActivityContext.getFragmentManager(), "datePicker");
            }
        });

        btnGeDailySummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BILL = "";

                List<MTransactionData> mTransactionDataList = saleAppDatabaseHelper.viewMTransactionDataByDate(textSelectDate.getText().toString());
                SharedPreferences sharedPreferences = fragmentDailySummary.getContext().getSharedPreferences("SALEAPP", 0);
                final String loginUseName = sharedPreferences.getString("LOGIN_USER_NAME", "NULL");


                BILL = BILL + "-------------------------------\n";
                BILL = BILL + "       ROYAL MARKETING \n";
                BILL = BILL + "No: 60/40, Polgasowita, Halpita.\n";
                BILL = BILL + "Hot Line : 0703500556 \n";
                BILL = BILL + "           0703500550 \n";
                BILL = BILL + "-------------------------------\n";
                BILL = BILL + "USER :" + loginUseName + "\n";
                BILL = BILL + "DATE :" + AppEnvironmentValues.getSystemDateTimeFormat() + "\n";
                BILL = BILL + "PRINT DATE :" + AppEnvironmentValues.getSystemDateTimeFormat() + "\n";
                BILL = BILL + "-------------------------------\n";
                BILL = BILL + String.format("%1$-7s %2$5s %3$7s %4$3s", "CARD NO", "CONN NO", "AMNT", "TYPE");
                BILL = BILL + "\n-------------------------------\n";
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (MTransactionData mTransactionData : mTransactionDataList) {
                    BILL = BILL + String.format("%1$-7s %2$5s %3$5s %4$5s", mTransactionData.getmInvNo(), mTransactionData.getConNo(), mTransactionData.getPmtAmt(), mTransactionData.getPmtType()) + "\n";
                    totalAmount = totalAmount.add(mTransactionData.getPmtAmt());
                }

                BILL = BILL + "-------------------------------\n";
                BILL = BILL + String.format("%1$-17s %2$5s", "TOTAL", totalAmount);
                BILL = BILL + "\n===============================\n";
                BILL = BILL + String.format("%1$-17s %2$5s", "NO OF CARDS", String.valueOf(mTransactionDataList.size()));
                BILL = BILL + "\n";
                BILL = BILL + "\n";
                textPrintView.setText(BILL);
                Log.d("PRINT BILL", "GET DETAILS");
                Log.d("PRINT BILL", BILL);
            }
        });

        btnPrintDailySummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PRINT BILL", "PRINT DETAILS");
                String deafaulBluetoothPrinter = saleAppDatabaseHelper.getDeafaulBluetoothPrinter();
                mBluetoothSocket = getBlueToothPrinter(deafaulBluetoothPrinter);
                if (mBluetoothSocket != null) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                OutputStream os = mBluetoothSocket.getOutputStream();
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
                            closeSocket(mBluetoothSocket);
                        }
                    };
                    t.start();
                    AppEnvironmentValues.snackbarCustome(fragmentDailySummary, "PAYMENT SAVE SUCCESS", "SUCCESS");
                }
            }
        });

        return fragmentDailySummary;
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
            AppEnvironmentValues.snackbarCustome(fragmentDailySummary, "PRINT ERROR PLEASE RE CONFIG PRINTER", "ERROR");
        }
        Thread mBlutoothConnectThread = new Thread(this);
        mBlutoothConnectThread.start();
        return tempBluetoothSocket;
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
            Toast.makeText(fragmentDailySummary.getContext(), "BlueTooth Printer Device Connected", Toast.LENGTH_SHORT).show();
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
