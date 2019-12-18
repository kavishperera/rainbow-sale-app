package lk.supervision.saleapp.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.DatePickerFragment;
import lk.supervision.saleapp.constant.IDGenarator;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MTransactionData;
import lk.supervision.saleapp.print.UnicodeFormatter;

public class FragmentCardEdit extends Fragment implements Runnable {

    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private FragmentActivity fragmentActivityContext;
    private View view;


    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothAdapter mBluetoothAdapter;

    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;

    private Button btnSave;

    public FragmentCardEdit() {
    }

    @Override
    public void onAttach(Activity activity) {
        fragmentActivityContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_card_edit, container, false);
        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(view.getContext());

        final MCardDetails mCardDetails = (MCardDetails) getArguments().getSerializable("SELECT_CARD_DETAILS");

        final TextView textCustomerName = (TextView) view.findViewById(R.id.text_customer_name);
        final TextView textConNo = (TextView) view.findViewById(R.id.text_con_no);
        final TextView textCardNo = (TextView) view.findViewById(R.id.text_card_no);
        final TextView textConDate = (TextView) view.findViewById(R.id.text_con_date);

        final TextView textTotalAmount = (TextView) view.findViewById(R.id.text_total_amount);
        final TextView textDpAmount = (TextView) view.findViewById(R.id.text_dp_amount);
        final TextView textBalAmount = (TextView) view.findViewById(R.id.text_bal_amount);
        final TextView textPaidAmount = (TextView) view.findViewById(R.id.text_paid_amount);
        final TextView textDueAmount = (TextView) view.findViewById(R.id.text_due_amount);
        final TextView textNewPaymentDate = (TextView) view.findViewById(R.id.text_new_payment_date);

        final EditText textRemark = (EditText) view.findViewById(R.id.text_remark);
        final EditText textTotalPayAmount = (EditText) view.findViewById(R.id.text_total_pay_amount);

        btnSave = (Button) view.findViewById(R.id.btn_save);
        final Button btnDateSelect = (Button) view.findViewById(R.id.btn_date_select);
        final Button btnCardSelect = (Button) view.findViewById(R.id.btn_back_card_select);


        final RadioGroup radioGroupPaymenType = (RadioGroup) view.findViewById(R.id.radio_group_payment_type);
        final RadioButton radioButtonIns = (RadioButton) view.findViewById(R.id.radio_button_ins);
        final RadioButton radioButtonDp = (RadioButton) view.findViewById(R.id.radio_button_dp);
        final RadioButton radioButtonVisit = (RadioButton) view.findViewById(R.id.radio_button_visit);

        final TableRow tableRowRemark = (TableRow) view.findViewById(R.id.table_row_remark);
        final TableRow tableRowdateSelect = (TableRow) view.findViewById(R.id.table_row_date_select);
        final TableRow tableRowPaymentInput = (TableRow) view.findViewById(R.id.table_row_payment_input);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("SALEAPP", 0);
        final String loginUserId = sharedPreferences.getString("LOGIN_USER_ID", "NULL");
        final String loginUseName = sharedPreferences.getString("LOGIN_USER_NAME", "NULL");
        final String uiFrame = sharedPreferences.getString("UI_FRAME", "NULL");

        tableRowRemark.setVisibility(View.GONE);
        tableRowdateSelect.setVisibility(View.GONE);

        textCustomerName.setText(mCardDetails.getCusName());
        textConNo.setText(mCardDetails.getConNo());
        textCardNo.setText(mCardDetails.getmInvNo());

        textConDate.setText(mCardDetails.getConDate());

        textTotalAmount.setText(mCardDetails.getTotVal().toString());
        textDpAmount.setText(mCardDetails.getDpmt().toString());
        textBalAmount.setText(mCardDetails.getBalVal().toString());
        textPaidAmount.setText(String.valueOf(mCardDetails.getBalVal().subtract(mCardDetails.getDueAmt())));
        textDueAmount.setText(mCardDetails.getDueAmt().toString());

        btnCardSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("MANUAL_CARD_SERACH".equals(uiFrame)) {
                    FragmentManualCard fragmentCardEdit = new FragmentManualCard();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragmentCardEdit);
                    fragmentTransaction.commit();
                } else {
                    FragmentCardSelect fragmentCardEdit = new FragmentCardSelect();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragmentCardEdit);
                    fragmentTransaction.commit();
                }
            }
        });

        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(fragmentActivityContext.getFragmentManager(), "datePicker");
            }
        });

        radioGroupPaymenType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonIns.isChecked()) {
                    tableRowRemark.setVisibility(View.GONE);
                    tableRowdateSelect.setVisibility(View.GONE);
                    tableRowPaymentInput.setVisibility(View.VISIBLE);
                } else if (radioButtonDp.isChecked()) {
                    tableRowRemark.setVisibility(View.GONE);
                    tableRowdateSelect.setVisibility(View.GONE);
                    tableRowPaymentInput.setVisibility(View.VISIBLE);
                } else {
                    tableRowRemark.setVisibility(View.VISIBLE);
                    tableRowdateSelect.setVisibility(View.VISIBLE);
                    tableRowPaymentInput.setVisibility(View.GONE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("If you want to print bill");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final MTransactionData mTransactionData = new MTransactionData();
                                mTransactionData.setSpId(loginUserId);

                                final Integer lastSerialNo = saleAppDatabaseHelper.findByMSpersonLastSerialNo(loginUserId);
                                String genarateNewPaymentId = IDGenarator.newGenaratedId(lastSerialNo);

                                mTransactionData.setPmtNo(genarateNewPaymentId);
                                mTransactionData.setConNo(mCardDetails.getConNo());
                                mTransactionData.setPhoneId(123445);
                                mTransactionData.setSysUpdate(0);

                                if (radioButtonIns.isChecked()) {
                                    mTransactionData.setPmtType("IN");
                                    mTransactionData.setPmtRemark(null);
                                    mTransactionData.setNpmtDate(null);

                                    if (TextUtils.isEmpty(textTotalPayAmount.getText())) {
                                        textTotalPayAmount.setError("PLEASE. ENTER PAY AMOUNT");
                                    } else {
                                        mTransactionData.setPmtAmt(new java.math.BigDecimal(textTotalPayAmount.getText().toString()));
                                        if (Double.parseDouble(textTotalPayAmount.getText().toString()) <= Double.parseDouble(mCardDetails.getBalVal().toString())) {
                                            mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                            int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                            if (status > 0) {
                                                // print bill
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
                                                                BILL = BILL + AppEnvironmentValues.getSystemDateTimeFormat(new Date()) + "\n";
                                                                BILL = BILL + "PAYMENT NO  -  " + mTransactionData.getPmtNo() + "\n";
                                                                BILL = BILL + "CARD NO     -  " + mCardDetails.getmInvNo() + "\n";
                                                                BILL = BILL + "CON NO      -  " + mTransactionData.getConNo() + "\n";
                                                                BILL = BILL + "CUSTOMER    -  " + "\n";
                                                                BILL = BILL + mCardDetails.getCusName() + "\n";
                                                                BILL = BILL + "PAID AMOUNT -  " + mTransactionData.getPmtAmt() + ".00 \n";
                                                                BILL = BILL + "-------------------------------\n";
                                                                BILL = BILL + "COLLECTOR   - " + loginUseName + "/" + loginUserId + "\n";
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

                                                                //update payment serial no
                                                                int updateLastPaymentSerialNo = saleAppDatabaseHelper.updateMSpersonLastSerialNo(lastSerialNo + 1, loginUserId);
                                                                if (updateLastPaymentSerialNo > 0) {
                                                                    Log.d("Payment Serial", "Update");
                                                                } else {
                                                                    Log.d("Payment Serial Update", "Fail");
                                                                }

                                                                updateMCardDetails(mCardDetails.getmInvNo());
                                                                backToCardSelectFragment();

                                                            } catch (Exception e) {
                                                                Log.d("PRINT ACTIVITY", e.toString());
                                                            }
                                                            closeSocket(mBluetoothSocket);
                                                        }
                                                    };
                                                    t.start();
                                                    AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE SUCCESS", "SUCCESS");
                                                }
                                            } else {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE ERROR", "ERROR");
                                            }
                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "PLEASE ENTER VALIED AMOUNT", "ERROR");
                                        }
                                    }

                                } else if (radioButtonDp.isChecked()) {
                                    mTransactionData.setPmtType("DP");
                                    mTransactionData.setPmtRemark(null);
                                    mTransactionData.setNpmtDate(null);

                                    if (TextUtils.isEmpty(textTotalPayAmount.getText())) {
                                        textTotalPayAmount.setError("PLEASE. ENTER PAY AMOUNT");
                                    } else {
                                        mTransactionData.setPmtAmt(new java.math.BigDecimal(textTotalPayAmount.getText().toString()));
                                        if (Double.parseDouble(textTotalPayAmount.getText().toString()) <= Double.parseDouble(mCardDetails.getBalVal().toString())) {
                                            mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                            int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                            if (status > 0) {
                                                // print bill
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
                                                                BILL = BILL + AppEnvironmentValues.getSystemDateTimeFormat(new Date()) + "\n";
                                                                BILL = BILL + "PAYMENT NO  -  " + mTransactionData.getPmtNo() + "\n";
                                                                BILL = BILL + "CARD NO     -  " + mCardDetails.getmInvNo() + "\n";
                                                                BILL = BILL + "CON NO      -  " + mTransactionData.getConNo() + "\n";
                                                                BILL = BILL + "CUSTOMER    -  " + "\n";
                                                                BILL = BILL + mCardDetails.getCusName() + "\n";
                                                                BILL = BILL + "PAID AMOUNT -  " + mTransactionData.getPmtAmt() + ".00 \n";
                                                                BILL = BILL + "-------------------------------\n";
                                                                BILL = BILL + "COLLECTOR   - " + loginUseName + "/" + loginUserId + "\n";
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

                                                                //update payment serial no
                                                                int updateLastPaymentSerialNo = saleAppDatabaseHelper.updateMSpersonLastSerialNo(lastSerialNo + 1, loginUserId);
                                                                if (updateLastPaymentSerialNo > 0) {
                                                                    Log.d("Payment Serial", "Update");
                                                                } else {
                                                                    Log.d("Payment Serial Update", "Fail");
                                                                }

                                                                updateMCardDetails(mCardDetails.getmInvNo());
                                                                backToCardSelectFragment();

                                                            } catch (Exception e) {
                                                                Log.d("PRINT ACTIVITY", e.toString());
                                                            }
                                                            closeSocket(mBluetoothSocket);
                                                        }
                                                    };
                                                    t.start();
                                                }
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE SUCCESS", "SUCCESS");
                                            } else {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE ERROR", "ERROR");
                                            }
                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "PLEASE ENTER VALIED AMOUNT", "ERROR");
                                        }
                                    }

                                } else {
                                    mTransactionData.setPmtType("VISIT");
                                    mTransactionData.setPmtRemark(textRemark.getText().toString());
                                    mTransactionData.setNpmtDate(textNewPaymentDate.getText().toString());
                                    mTransactionData.setPmtAmt(BigDecimal.ZERO);
                                    mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                    int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                    if (status > 0) {

                                        updateMCardDetails(mCardDetails.getmInvNo());
                                        backToCardSelectFragment();

                                        AppEnvironmentValues.snackbarCustome(view, "JOB SAVE SUCCESS", "SUCCESS");
                                    } else {
                                        AppEnvironmentValues.snackbarCustome(view, "JOB SAVE ERROR", "ERROR");
                                    }
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final MTransactionData mTransactionData = new MTransactionData();
                                mTransactionData.setSpId(loginUserId);

                                final Integer lastSerialNo = saleAppDatabaseHelper.findByMSpersonLastSerialNo(loginUserId);
                                String genarateNewPaymentId = IDGenarator.newGenaratedId(lastSerialNo);

                                mTransactionData.setPmtNo(genarateNewPaymentId);
                                mTransactionData.setConNo(mCardDetails.getConNo());
                                mTransactionData.setPhoneId(123445);
                                mTransactionData.setSysUpdate(0);
                                Log.d("TEST DATA1", mCardDetails.getConNo());

                                if (radioButtonIns.isChecked()) {
                                    mTransactionData.setPmtType("IN");
                                    mTransactionData.setPmtRemark(null);
                                    mTransactionData.setNpmtDate(null);

                                    if (TextUtils.isEmpty(textTotalPayAmount.getText())) {
                                        textTotalPayAmount.setError("PLEASE. ENTER PAY AMOUNT");
                                    } else {
                                        mTransactionData.setPmtAmt(new java.math.BigDecimal(textTotalPayAmount.getText().toString()));
                                        if (Double.parseDouble(textTotalPayAmount.getText().toString()) <= Double.parseDouble(mCardDetails.getBalVal().toString())) {
                                            mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                            int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                            if (status > 0) {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE SUCCESS", "SUCCESS");
                                                dialog.cancel();
                                                backToCardSelectFragment();
                                            } else {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE ERROR", "ERROR");
                                            }

                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "PLEASE ENTER VALIED AMOUNT", "ERROR");
                                        }
                                    }

                                } else if (radioButtonDp.isChecked()) {
                                    mTransactionData.setPmtType("DP");
                                    mTransactionData.setPmtRemark(null);
                                    mTransactionData.setNpmtDate(null);

                                    if (TextUtils.isEmpty(textTotalPayAmount.getText())) {
                                        textTotalPayAmount.setError("PLEASE. ENTER PAY AMOUNT");
                                    } else {
                                        mTransactionData.setPmtAmt(new java.math.BigDecimal(textTotalPayAmount.getText().toString()));
                                        if (Double.parseDouble(textTotalPayAmount.getText().toString()) <= Double.parseDouble(mCardDetails.getBalVal().toString())) {
                                            mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                            int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                            if (status > 0) {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE SUCCESS", "SUCCESS");
                                                dialog.cancel();
                                                backToCardSelectFragment();
                                            } else {
                                                AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE ERROR", "ERROR");
                                            }

                                        } else {
                                            AppEnvironmentValues.snackbarCustome(view, "PLEASE ENTER VALIED AMOUNT", "ERROR");
                                        }
                                    }

                                } else {
                                    mTransactionData.setPmtType("VISIT");
                                    mTransactionData.setPmtRemark(textRemark.getText().toString());
                                    mTransactionData.setNpmtDate(textNewPaymentDate.getText().toString());
                                    mTransactionData.setPmtAmt(BigDecimal.ZERO);
                                    mTransactionData.setmInvNo(mCardDetails.getmInvNo());
                                    int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                                    if (status > 0) {

                                        updateMCardDetails(mCardDetails.getmInvNo());
                                        backToCardSelectFragment();

                                        AppEnvironmentValues.snackbarCustome(view, "JOB SAVE SUCCESS", "SUCCESS");
                                        dialog.cancel();
                                    } else {
                                        AppEnvironmentValues.snackbarCustome(view, "JOB SAVE ERROR", "ERROR");
                                    }
                                }

                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        return view;
    }

    private void updateMCardDetails(String invNo) {
        //update mcard details from remark use m card details
        int updateMCardDetailsRegFrag = saleAppDatabaseHelper.updateMCardDetails(invNo);
        if (updateMCardDetailsRegFrag > 0) {
            Log.d("Reg Flag", "Update Done");
        } else {
            Log.d("Reg Flag", "Update Fail");
        }
    }

    private void backToCardSelectFragment() {
        FragmentCardSelect fragmentCardSelect = new FragmentCardSelect();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragmentCardSelect);
        fragmentTransaction.commit();
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
            AppEnvironmentValues.snackbarCustome(view, "PRINT ERROR PLEASE RE CONFIG PRINTER", "ERROR");
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
            Toast.makeText(view.getContext(), "BlueTooth Printer Device Connected", Toast.LENGTH_SHORT).show();
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
