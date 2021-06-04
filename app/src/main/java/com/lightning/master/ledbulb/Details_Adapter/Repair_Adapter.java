package com.lightning.master.ledbulb.Details_Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.lightning.master.ledbulb.Details_Model.Repairable_Model;
import com.lightning.master.ledbulb.R;

public class  Repair_Adapter extends RecyclerView.Adapter<Repair_Adapter.MyViewHolder>
{
    Context mContext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Repair_Adapter(Context mContext)
    {
        this.mContext= mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repairable,viewGroup,false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        final Repairable_Model repairable_model = Complaint_Detail.repair_modelArrayList.get(position);

        myViewHolder.tv_quanity.setText(repairable_model.getQuantity());
        myViewHolder.repair_cost.setText(repairable_model.getRepair_cost());
        myViewHolder.replacement_cost.setText(repairable_model.getReplacement_cost());
        myViewHolder.sale_cost.setText(repairable_model.getSale_cost());
        myViewHolder.return_cost.setText(repairable_model.getReturn_cost());
        myViewHolder.repairCommnt.setText(repairable_model.getComments());

        if(repairable_model.getResponse_type().equals("REPAIR")) {
            myViewHolder.radio_Repair.setChecked(true);

            myViewHolder.radio_Replacement.setEnabled(false);
            myViewHolder.radio_saleCost.setEnabled(false);
            myViewHolder.radio_Return.setEnabled(false);

        }else if (repairable_model.getResponse_type().equals("REPLACE")) {
            myViewHolder.radio_Replacement.setChecked(true);

            myViewHolder.radio_Repair.setEnabled(false);
            myViewHolder.radio_saleCost.setEnabled(false);
            myViewHolder.radio_Return.setEnabled(false);

        } else if (repairable_model.getResponse_type().equals("SALE")) {
            myViewHolder.radio_saleCost.setChecked(true);

            myViewHolder.radio_Repair.setEnabled(false);
            myViewHolder.radio_Replacement.setEnabled(false);
            myViewHolder.radio_Return.setEnabled(false);

        } else if (repairable_model.getResponse_type().equals("RETURN")) {
            myViewHolder.radio_Return.setChecked(true);

            myViewHolder.radio_Repair.setEnabled(false);
            myViewHolder.radio_Replacement.setEnabled(false);
            myViewHolder.radio_saleCost.setEnabled(false);

        } else {
        }



        myViewHolder.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String text = radioButton.getText().toString();

                Log.i("checkedId rep:",text+"");


                final Repairable_Model repairable_model = Complaint_Detail.repair_modelArrayList.get(position);
                Complaint_Detail.smry_category.set(position,"Repairable");
                Complaint_Detail.smry_qty.set(position,repairable_model.getQuantity()+"");

                if (checkedId==R.id.radio_Repair) {

                    Complaint_Detail.isEmpty=true;
                    Complaint_Detail.idcomplain.set(position, Integer.parseInt(repairable_model.getId()));
                    Complaint_Detail.responsearray.set(position,"REPAIR");

                    Complaint_Detail.smry_type.set(position,"REPAIR");
                    Complaint_Detail.smry_price.set(position,repairable_model.getRepair_cost()+"");

                }else if (checkedId==R.id.radio_Replacement) {

                    Complaint_Detail.isEmpty=true;
                    Complaint_Detail.idcomplain.set(position, Integer.parseInt(repairable_model.getId()));
                    Complaint_Detail.responsearray.set(position,"REPLACE");

                    Complaint_Detail.smry_type.set(position,"REPLACE");
                    Complaint_Detail.smry_price.set(position,repairable_model.getReplacement_cost()+"");

                }else if (checkedId==R.id.radio_saleCost) {

                    Complaint_Detail.isEmpty=true;
                    Complaint_Detail.idcomplain.set(position, Integer.parseInt(repairable_model.getId()));
                    Complaint_Detail.responsearray.set(position,"SALE");

                    Complaint_Detail.smry_type.set(position,"SALE");
                    Complaint_Detail.smry_price.set(position,repairable_model.getSale_cost()+"");

                }else if (checkedId==R.id.radio_Return) {

                    Complaint_Detail.isEmpty=true;
                    Complaint_Detail.idcomplain.set(position, Integer.parseInt(repairable_model.getId()));
                    Complaint_Detail.responsearray.set(position,"RETURN");

                    Complaint_Detail.smry_type.set(position,"RETURN");
                    Complaint_Detail.smry_price.set(position,repairable_model.getReturn_cost()+"");

                }else{
                    Complaint_Detail.isEmpty=false;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Complaint_Detail.repair_modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView repair_cost,replacement_cost,sale_cost,return_cost,tv_quanity,repairCommnt;
        RadioButton radio_Repair,radio_Replacement,radio_saleCost,radio_Return;
        RadioGroup radio_group;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_quanity=(TextView)itemView.findViewById(R.id.tv_quanity);
            repair_cost=(TextView)itemView.findViewById(R.id.repair_cost);
            replacement_cost=(TextView)itemView.findViewById(R.id.replacement_cost);
            sale_cost=(TextView)itemView.findViewById(R.id.sale_cost);
            return_cost=(TextView)itemView.findViewById(R.id.return_cost);
            repairCommnt=(TextView)itemView.findViewById(R.id.repairCommnt);

            radio_Repair=(RadioButton)itemView.findViewById(R.id.radio_Repair);
            radio_Replacement=(RadioButton)itemView.findViewById(R.id.radio_Replacement);
            radio_saleCost=(RadioButton)itemView.findViewById(R.id.radio_saleCost);
            radio_Return=(RadioButton)itemView.findViewById(R.id.radio_Return);
            radio_group=(RadioGroup)itemView.findViewById(R.id.radio_group);
        }
    }
}
