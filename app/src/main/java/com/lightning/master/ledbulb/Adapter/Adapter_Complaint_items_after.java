package com.lightning.master.ledbulb.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.Interface.ItemClickListner;
import com.lightning.master.ledbulb.Model.ModelPriceList;
import com.lightning.master.ledbulb.databinding.ItemsComplaintAfterBinding;
import com.lightning.master.ledbulb.databinding.ItemsComplaintAfterBinding;

public class Adapter_Complaint_items_after extends RecyclerView.Adapter<Adapter_Complaint_items_after.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ModelPriceList modelPriceList;
    ItemClickListner itemClickListner;
    public Adapter_Complaint_items_after(Context mContext) {
        this.mContext = mContext;
        this.itemClickListner = itemClickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ItemsComplaintAfterBinding binding = ItemsComplaintAfterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        modelPriceList = Complaint_Detail.itemDataList.get(position);

        holder.binding.tvName.setText(modelPriceList.getTITLE());
        holder.binding.tvWatt.setText(modelPriceList.getWATT()+"W");
        holder.binding.tvPiece.setText(modelPriceList.getPIECES());
        holder.binding.tvRuppee.setText("₹"+modelPriceList.getCHARGES());


        //  Complaint_Detail.itemName = "" + Add_Invoice.attributevalue.toString().replace("[", "").replace("]", "");




    }


    @Override
    public int getItemCount() {
        return Complaint_Detail.itemDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final ItemsComplaintAfterBinding binding;

        public MyViewHolder(ItemsComplaintAfterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
