package lk.supervision.saleapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.supervision.saleapp.R;

public class FragmentManualBillPrint extends Fragment {

    private View fragmentManualBillPrint;

    public FragmentManualBillPrint() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManualBillPrint = inflater.inflate(R.layout.fragment_fragment_manual_bill_print, container, false);
        return fragmentManualBillPrint;
    }
}
