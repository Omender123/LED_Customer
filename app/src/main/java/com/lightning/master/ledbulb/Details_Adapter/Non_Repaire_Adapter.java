package com.lightning.master.ledbulb.Details_Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.Details_Model.Non_Repairable_Model;
import com.lightning.master.ledbulb.R;

public class Non_Repaire_Adapter extends RecyclerView.Adapter<Non_Repaire_Adapter.MyViewHolder> {

    Context mContext;

    public Non_Repaire_Adapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_non_repairable, viewGroup, false);
//        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        final Non_Repairable_Model non_repairable_model = Complaint_Detail.non_repairable_modelArrayList.get(position);

        myViewHolder.tv_quanity_non.setText(non_repairable_model.getQuantity_non());
        myViewHolder.non_replacement_cost.setText(non_repairable_model.getNon_replacement_cost());
        myViewHolder.non_sale_cost.setText(non_repairable_model.getNon_sale_cost());
        myViewHolder.non_return_cost.setText(non_repairable_model.getNon_return_cost());
        myViewHolder.non_repairCommnt.setText(non_repairable_model.getComment());

        if (non_repairable_model.getResponse_type().equals("REPLACE")) {
            myViewHolder.radio_Replacement.setChecked(true);

            myViewHolder.radio_Return.setEnabled(false);
            myViewHolder.radio_saleCost.setEnabled(false);


        } else if (non_repairable_model.getResponse_type().equals("SALE")) {
            myViewHolder.radio_saleCost.setChecked(true);

            myViewHolder.radio_Return.setEnabled(false);
            myViewHolder.radio_Replacement.setEnabled(false);


        } else if (non_repairable_model.getResponse_type().equals("RETURN")) {
            myViewHolder.radio_Return.setChecked(true);

            myViewHolder.radio_Replacement.setEnabled(false);
            myViewHolder.radio_saleCost.setEnabled(false);


        } else {
        }

        myViewHolder.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String text = radioButton.getText().toString();

                final Non_Repairable_Model non_repairable_model = Complaint_Detail.non_repairable_modelArrayList.get(position);

                Complaint_Detail.smry_category_non.set(position,"Non-Repairable");
                Complaint_Detail.smry_qty_non.set(position,non_repairable_model.getQuantity_non()+"");


                if (checkedId == R.id.radio_nonReplacement) {
                    Log.i("checkedId non rep:", "REPLACE");

                    Complaint_Detail.non_idcomplain.set(position, Integer.parseInt(non_repairable_model.getId()));
                    Complaint_Detail.non_responsearray.set(position, "REPLACE");

                    Complaint_Detail.smry_type_non.set(position,"REPLACE");
                    Complaint_Detail.smry_price_non.set(position,non_repairable_model.getNon_replacement_cost()+"");
                } else if (checkedId == R.id.radio_nonsaleCost) {
                    Log.i("checkedId non rep:", "SALE");

                    Complaint_Detail.non_idcomplain.set(position, Integer.parseInt(non_repairable_model.getId()));
                    Complaint_Detail.non_responsearray.set(position, "SALE");

                    Complaint_Detail.smry_type_non.set(position,"SALE");
                    Complaint_Detail.smry_price_non.set(position,non_repairable_model.getNon_sale_cost()+"");
                } else if (checkedId == R.id.radio_nonReturn) {
                    Log.i("checkedId non rep:", "RETURN");

                    Complaint_Detail.non_idcomplain.set(position, Integer.parseInt(non_repairable_model.getId()));
                    Complaint_Detail.non_responsearray.set(position, "RETURN");

                    Complaint_Detail.smry_type_non.set(position,"RETURN");
                    Complaint_Detail.smry_price_non.set(position,non_repairable_model.getNon_return_cost()+"");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Complaint_Detail.non_repairable_modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView non_replacement_cost, non_sale_cost, non_return_cost, tv_quanity_non,non_repairCommnt;
        RadioButton radio_Replacement, radio_saleCost, radio_Return;

        RadioGroup radio_group;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_quanity_non = (TextView) itemView.findViewById(R.id.tv_quanity_non);
            non_replacement_cost = (TextView) itemView.findViewById(R.id.non_replacement_cost);
            non_sale_cost = (TextView) itemView.findViewById(R.id.non_sale_cost);
            non_return_cost = (TextView) itemView.findViewById(R.id.non_return_cost);
            non_repairCommnt = (TextView) itemView.findViewById(R.id.non_repairCommnt);

            radio_Replacement = (RadioButton) itemView.findViewById(R.id.radio_nonReplacement);
            radio_saleCost = (RadioButton) itemView.findViewById(R.id.radio_nonsaleCost);
            radio_Return = (RadioButton) itemView.findViewById(R.id.radio_nonReturn);

            radio_group = (RadioGroup) itemView.findViewById(R.id.radio_non);

        }
    }
}
