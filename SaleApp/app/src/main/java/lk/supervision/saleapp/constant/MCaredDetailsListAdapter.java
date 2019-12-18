package lk.supervision.saleapp.constant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lk.supervision.saleapp.R;
import lk.supervision.saleapp.fragment.FragmentCardEdit;
import lk.supervision.saleapp.model.MCardDetails;

/**
 * Created by kavish manjitha on 10/13/2017.
 */

public class MCaredDetailsListAdapter extends RecyclerView.Adapter<MCaredDetailsListAdapter.ViewHolder> {
    private List<MCardDetails> mCardDetailsList;
    private Context context;

    public MCaredDetailsListAdapter(List<MCardDetails> mCardDetailsList, Context context) {
        this.mCardDetailsList = mCardDetailsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_m_card_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MCardDetails mCardDetails = mCardDetailsList.get(position);
        holder.textMInvoiveNo.setText(mCardDetails.getmInvNo());
        holder.textCustomerName.setText(mCardDetails.getCusName());
        holder.textMobile.setText(mCardDetails.getMobNo());
        holder.textNicNo.setText(mCardDetails.getIdNo());
        holder.textAddress1.setText(mCardDetails.getAdd1() + mCardDetails.getAdd2());
        holder.textAddress2.setText("");

//        Activity activity = (Activity) context;
//        if ("1".equals(mCardDetails.getRecFlag())) {
//            Drawable myIcon = context.getResources().getDrawable(R.drawable.border);
//            holder.textMInvoiveNo.setBackgroundDrawable(myIcon);
//        }

        holder.linearLayoutMcardDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("SELECT_CARD_DETAILS", mCardDetails);
                bundle.putSerializable("UI_FRAME", "CARD_SERACH");

                FragmentCardEdit fragmentCardEdit = new FragmentCardEdit();
                fragmentCardEdit.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentCardEdit);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textMInvoiveNo;
        public TextView textCustomerName;
        public TextView textMobile;
        public TextView textNicNo;
        public TextView textAddress1;
        public TextView textAddress2;
        public LinearLayout linearLayoutMcardDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            textMInvoiveNo = (TextView) itemView.findViewById(R.id.text_m_invoice_number);
            textCustomerName = (TextView) itemView.findViewById(R.id.text_customer_name);
            textMobile = (TextView) itemView.findViewById(R.id.text_customer_mobile_no);
            textNicNo = (TextView) itemView.findViewById(R.id.text_customer_nic_no);
            textAddress1 = (TextView) itemView.findViewById(R.id.text_customer_address1);
            textAddress2 = (TextView) itemView.findViewById(R.id.text_customer_address2);
            textAddress2 = (TextView) itemView.findViewById(R.id.text_customer_address2);
            linearLayoutMcardDetail = (LinearLayout) itemView.findViewById(R.id.linear_layout_mcard_detail);
        }
    }
}
