package lk.supervision.saleapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.constant.AppEnvironmentValues;
import lk.supervision.saleapp.constant.MCaredDetailsListAdapter;
import lk.supervision.saleapp.database.SaleAppDatabaseHelper;
import lk.supervision.saleapp.model.MCardDetails;

public class FragmentCardSelect extends Fragment {

    private SaleAppDatabaseHelper saleAppDatabaseHelper;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public FragmentCardSelect() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_card_select, container, false);
        final EditText textCardDetailFilter = view.findViewById(R.id.text_card_filter);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        this.saleAppDatabaseHelper = new SaleAppDatabaseHelper(getActivity());
        List<MCardDetails> mCardDetailsesList = saleAppDatabaseHelper.viewMCardDetails();
        mCardDetailListUpdate(mCardDetailsesList, getContext());

        textCardDetailFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filterValue = textCardDetailFilter.getText().toString();
                List<MCardDetails> filterMCardDetailList = saleAppDatabaseHelper.findMCardDetails(filterValue);
                if (!filterMCardDetailList.isEmpty()) {
                    mCardDetailListUpdate(filterMCardDetailList, getContext());
                }
            }
        });


        return view;
    }

    public void mCardDetailListUpdate(List<MCardDetails> data, Context context) {
        adapter = new MCaredDetailsListAdapter(data, context);
        recyclerView.setAdapter(adapter);
    }
}
