package com.lightning.master.ledbulb.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.lightning.master.ledbulb.Complaint.fragfortabs.TabFragment1;
import com.lightning.master.ledbulb.Interface.ItemClickListner;
import com.lightning.master.ledbulb.Model.ModelPriceList;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.databinding.ItemComaplinlistBinding;
import com.lightning.master.ledbulb.databinding.ItemsComplaintBinding;

public class Adapter_Complaint_items extends RecyclerView.Adapter<Adapter_Complaint_items.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ModelPriceList modelPriceList;
    ItemClickListner itemClickListner;
    public Adapter_Complaint_items(Context mContext,ItemClickListner itemClickListner) {
        this.mContext = mContext;
        this.itemClickListner = itemClickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ItemsComplaintBinding binding = ItemsComplaintBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        modelPriceList = TabFragment1.itemDataList.get(position);

        holder.binding.tvName.setText(modelPriceList.getTITLE());
        holder.binding.tvWatt.setText(modelPriceList.getWATT()+"W");
        holder.binding.tvPiece.setText(modelPriceList.getPIECES());
        holder.binding.tvRuppee.setText("₹"+modelPriceList.getCHARGES());


        //  TabFragment1.itemName = "" + Add_Invoice.attributevalue.toString().replace("[", "").replace("]", "");


        holder.binding.remove.setOnClickListener(v -> new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove  item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    TabFragment1.itemDataList.remove(position);
                    TabFragment1.Id.remove(position);
                    TabFragment1.Qty.remove(position);
                    TabFragment1.Watt.remove(position);
                    itemClickListner.onClick("");
                    notifyDataSetChanged();
                })
                .setNegativeButton("No", null)
                .show());


    }


    @Override
    public int getItemCount() {
        return TabFragment1.itemDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final ItemsComplaintBinding binding;

        public MyViewHolder(ItemsComplaintBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
