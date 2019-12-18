package lk.supervision.saleapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.IDGenarator;
import lk.supervision.saleapp.constant.MCaredDetailsListAdapter;
import lk.supervision.saleapp.constant.ServerHostAvailabilityTask;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MCardDetails;
import lk.supervision.saleapp.model.MSperson;
import lk.supervision.saleapp.model.MTransactionData;
import lk.supervision.saleapp.receive.RestApiReceiveController;

public class FragmentManualCard extends Fragment {

    private SaleAppDatabaseHelper saleAppDatabaseHelper;
    private TextView networkStatus;
    private View view;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public FragmentManualCard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment_manual_card, container, false);

        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(view.getContext());

        //card search
        final TextView textManualCardSearch = (TextView) view.findViewById(R.id.text_manual_card_search);
        final Button btnManualCardSearch = (Button) view.findViewById(R.id.btn_manual_card_search);
        final Button btnManualCardSearchCustomerName = (Button) view.findViewById(R.id.btn_manual_card_search_customer_name);
        final EditText textCustomerName = (EditText) view.findViewById(R.id.text_customer_name);
        networkStatus = (TextView) view.findViewById(R.id.text_view_network_status);

        //payment save
        final TextView textManualCardPayAmount = (TextView) view.findViewById(R.id.text_manual_card_pay_amount);
        final TextView textManualCardRemark = (TextView) view.findViewById(R.id.text_manual_card_remark);
        final Button btnSavePayment = (Button) view.findViewById(R.id.btn_payment_save);

        //payment insert visibal table rows
        final TableRow textPayAmountRow = (TableRow) view.findViewById(R.id.text_pay_amount_row);
        final TableRow textRemarkRow = (TableRow) view.findViewById(R.id.text_remark_row);
        final TableRow manualCardSearchRow = (TableRow) view.findViewById(R.id.manual_card_serach_row);

        // Spinner element
        final Spinner spinner = (Spinner) view.findViewById(R.id.sppiner_filter_type);

        recyclerView = (RecyclerView) view.findViewById(R.id.manual_filter_customer_name);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("CARD NO");
        categories.add("CON NO");
        categories.add("TELEPHONE NO");
        categories.add("MOBILE NO");
        categories.add("NIC NO");
        categories.add("CUSTOMER NAME");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        networkStatus = (TextView) view.findViewById(R.id.text_view_network_status);

        textPayAmountRow.setVisibility(View.GONE);
        textRemarkRow.setVisibility(View.GONE);
        btnSavePayment.setVisibility(View.GONE);
        manualCardSearchRow.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("SALEAPP", 0);
        final String loginUserId = sharedPreferences.getString("LOGIN_USER_ID", "NULL");

        networkRefresh();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if ("CUSTOMER NAME".equals(spinner.getSelectedItem().toString())) {
                    textManualCardSearch.setVisibility(View.GONE);
                    btnManualCardSearch.setVisibility(View.GONE);
                    textCustomerName.setVisibility(View.VISIBLE);
                    btnManualCardSearchCustomerName.setVisibility(View.VISIBLE);
                    manualCardSearchRow.setVisibility(View.VISIBLE);
                    textCustomerName.setText("");
                    List<MCardDetails> data = new ArrayList<>();
                    mCardDetailListUpdate(data, getContext());
                } else if ("TELEPHONE NO".equals(spinner.getSelectedItem().toString())) {
                    textManualCardSearch.setVisibility(View.GONE);
                    btnManualCardSearch.setVisibility(View.GONE);
                    textCustomerName.setVisibility(View.VISIBLE);
                    btnManualCardSearchCustomerName.setVisibility(View.VISIBLE);
                    manualCardSearchRow.setVisibility(View.VISIBLE);
                    textCustomerName.setText("");
                    List<MCardDetails> data = new ArrayList<>();
                    mCardDetailListUpdate(data, getContext());
                } else if ("MOBILE NO".equals(spinner.getSelectedItem().toString())) {
                    textManualCardSearch.setVisibility(View.GONE);
                    btnManualCardSearch.setVisibility(View.GONE);
                    textCustomerName.setVisibility(View.VISIBLE);
                    btnManualCardSearchCustomerName.setVisibility(View.VISIBLE);
                    manualCardSearchRow.setVisibility(View.VISIBLE);
                    textCustomerName.setText("");
                    List<MCardDetails> data = new ArrayList<>();
                    mCardDetailListUpdate(data, getContext());
                } else if ("NIC NO".equals(spinner.getSelectedItem().toString())) {
                    textManualCardSearch.setVisibility(View.GONE);
                    btnManualCardSearch.setVisibility(View.GONE);
                    textCustomerName.setVisibility(View.VISIBLE);
                    btnManualCardSearchCustomerName.setVisibility(View.VISIBLE);
                    manualCardSearchRow.setVisibility(View.VISIBLE);
                    textCustomerName.setText("");
                    List<MCardDetails> data = new ArrayList<>();
                    mCardDetailListUpdate(data, getContext());
                } else {
                    textManualCardSearch.setVisibility(View.VISIBLE);
                    btnManualCardSearch.setVisibility(View.VISIBLE);
                    textCustomerName.setVisibility(View.GONE);
                    textManualCardSearch.setText("");
                    btnManualCardSearchCustomerName.setVisibility(View.GONE);
                    manualCardSearchRow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        btnManualCardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textManualCardSearch.getText().toString().isEmpty()) {
                    textManualCardSearch.setError("Please enter card no");
                } else {
                    if (isNetworkConnected()) {
                        if (ServerHostAvailabilityTask.isConnectedToServerOnline()) {
                            AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_ONLINE;
                            try {
                                MCardDetails manualCardSearchData;
                                RestApiReceiveController controller = new RestApiReceiveController();
                                if ("CON NO".equals(spinner.getSelectedItem().toString())) {
                                    manualCardSearchData = controller.getManualCardSearch(textManualCardSearch.getText().toString().replace('/', '$'), spinner.getSelectedItem().toString());
                                } else {
                                    manualCardSearchData = controller.getManualCardSearch(textManualCardSearch.getText().toString(), spinner.getSelectedItem().toString());
                                }

                                if (manualCardSearchData.getmInvNo() == null) {
                                    Toast.makeText(view.getContext(), "CARD NOT FOUND", Toast.LENGTH_LONG).show();
                                    textPayAmountRow.setVisibility(View.VISIBLE);
                                    textRemarkRow.setVisibility(View.VISIBLE);
                                    btnSavePayment.setVisibility(View.VISIBLE);
                                    manualCardSearchRow.setVisibility(View.GONE);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("SELECT_CARD_DETAILS", manualCardSearchData);
                                    bundle.putSerializable("UI_FRAME", "MANUAL_CARD_SERACH");

                                    FragmentCardEdit fragmentCardEdit = new FragmentCardEdit();
                                    fragmentCardEdit.setArguments(bundle);
                                    FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragmentCardEdit);
                                    fragmentTransaction.commit();
                                    Log.d("SEARCH DATA", manualCardSearchData.toString());
                                }
                            } catch (Exception e) {
                                AppEnvironmentValues.snackbarCustome(v, "SERVER NOT CONNECT!", "ERROR");
                            }
                        } else {
                            Toast.makeText(view.getContext(), "CARD NOT FOUND", Toast.LENGTH_LONG).show();
                            textPayAmountRow.setVisibility(View.VISIBLE);
                            textRemarkRow.setVisibility(View.VISIBLE);
                            btnSavePayment.setVisibility(View.VISIBLE);
                            manualCardSearchRow.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        btnManualCardSearchCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    if (ServerHostAvailabilityTask.isConnectedToServerOnline()) {
                        AppEnvironmentValues.SERVER_ADDRESS = AppEnvironmentValues.SERVER_ADDRESS_ONLINE;
                        try {
                            if (textCustomerName.getText().length() > 4) {
                                String filterValue = textCustomerName.getText().toString();
                                RestApiReceiveController restApiReceiveController = new RestApiReceiveController();
                                MCardDetails[] manualCardDetailsList = restApiReceiveController.getManualCardDetailsBy(filterValue, spinner.getSelectedItem().toString());
                                ArrayList<MCardDetails> mCardDetails = new ArrayList<>(Arrays.asList(manualCardDetailsList));
                                if (!mCardDetails.isEmpty()) {
                                    mCardDetailListUpdate(mCardDetails, getContext());
                                } else {
                                    Toast.makeText(view.getContext(), "CARD NOT FOUND", Toast.LENGTH_LONG).show();
                                    textPayAmountRow.setVisibility(View.VISIBLE);
                                    textRemarkRow.setVisibility(View.VISIBLE);
                                    btnSavePayment.setVisibility(View.VISIBLE);
                                    manualCardSearchRow.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            AppEnvironmentValues.snackbarCustome(v, "SERVER NOT CONNECT!", "ERROR");
                        }
                    } else {
                        Toast.makeText(view.getContext(), "CARD NOT FOUND", Toast.LENGTH_LONG).show();
                        textPayAmountRow.setVisibility(View.VISIBLE);
                        textRemarkRow.setVisibility(View.VISIBLE);
                        btnSavePayment.setVisibility(View.VISIBLE);
                        manualCardSearchRow.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MTransactionData mTransactionData = new MTransactionData();
                mTransactionData.setSpId(loginUserId);

                if (
                        "CUSTOMER NAME".equals(spinner.getSelectedItem().toString())
                                || "TELEPHONE NO".equals(spinner.getSelectedItem().toString())
                                || "MOBILE NO".equals(spinner.getSelectedItem().toString())
                                || "NIC NO".equals(spinner.getSelectedItem().toString())
                        ) {
                    mTransactionData.setConNo(textCustomerName.getText().toString());
                    mTransactionData.setmInvNo(textCustomerName.getText().toString());
                } else {
                    mTransactionData.setConNo(textManualCardSearch.getText().toString());
                    mTransactionData.setmInvNo(textManualCardSearch.getText().toString());
                }

                final Integer lastSerialNo = saleAppDatabaseHelper.findByMSpersonLastSerialNo(loginUserId);
                if (lastSerialNo > 0) {
                    String genarateNewPaymentId = IDGenarator.newGenaratedId(lastSerialNo);
                    mTransactionData.setPmtNo(genarateNewPaymentId);
                    Log.d("Payment Serial", "Create");
                } else {
                    Log.d("Payment Serial Create", "Fail");
                }
                mTransactionData.setPhoneId(123445);
                mTransactionData.setSysUpdate(0);

                if (TextUtils.isEmpty(textManualCardPayAmount.getText())) {
                    textManualCardPayAmount.setError("PLEASE ENTER PAY AMOUNT");
                } else if (textManualCardRemark.getText().toString().isEmpty()) {
                    textManualCardRemark.setError("PLEASE ENTER PAY AMOUNT");
                } else if (!TextUtils.isEmpty(textManualCardPayAmount.getText()) && !textManualCardRemark.getText().toString().isEmpty()) {
                    mTransactionData.setPmtType("MANUAL_CARD_INPUT");
                    mTransactionData.setPmtAmt(new java.math.BigDecimal(textManualCardPayAmount.getText().toString()));
                    mTransactionData.setPmtRemark(textManualCardRemark.getText().toString());
                    Log.d("Payment Serial Create", mTransactionData.toString());
                    int status = saleAppDatabaseHelper.insertMTransactionData(mTransactionData);
                    if (status > 0) {
                        int updateLastPaymentSerialNo = saleAppDatabaseHelper.updateMSpersonLastSerialNo(lastSerialNo + 1, loginUserId);
                        if (updateLastPaymentSerialNo > 0) {
                            Log.d("Payment Serial", "Update");
                        } else {
                            Log.d("Payment Serial Update", "Fail");
                        }
                        FragmentCardSelect fragmentCardSelect = new FragmentCardSelect();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragmentCardSelect);
                        fragmentTransaction.commit();

                        AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE SUCCESS", "SUCCESS");
                    } else {
                        AppEnvironmentValues.snackbarCustome(view, "PAYMENT SAVE ERROR", "ERROR");
                    }
                }
            }
        });

        return view;
    }

    private void networkRefresh() {
        //check internet connection
        if (isNetworkConnected()) {
            networkStatus.setText("NETWORK CONNECTED");
            networkStatus.setTextColor(this.getResources().getColor(R.color.right));
        } else {
            networkStatus.setText("NETWORK NOT CONNECTED");
            networkStatus.setTextColor(this.getResources().getColor(R.color.error));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void mCardDetailListUpdate(List<MCardDetails> data, Context context) {
        adapter = new MCaredDetailsListAdapter(data, context);
        recyclerView.setAdapter(adapter);
    }
}
