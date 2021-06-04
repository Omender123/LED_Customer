package com.lightning.master.ledbulb.summary_customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lightning.master.ledbulb.Complaint.fragfortabs.Complaint_Detail;
import com.lightning.master.ledbulb.R;


public class NonMyOrdersSummary_Adapter extends RecyclerView.Adapter<NonMyOrdersSummary_Adapter.MyViewHolder> {

    Context mContext;

    SharedPreferences sharedPref;

    ProgressDialog pd;
    String user_loggedin_id;
    private RecyclerView.ViewHolder holder;

    public NonMyOrdersSummary_Adapter(Context context) {
        this.mContext = context;
    }

    int count = 0;

    Dialog dialog_progress;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);


        holder.tv_category.setText(Complaint_Detail.smry_type_non.get(position));
        holder.tv_quantity.setText("Qty: "+Complaint_Detail.smry_qty_non.get(position));
        holder.tv_price.setText(Complaint_Detail.smry_price_non.get(position));
        holder.tv_reprairable.setVisibility(View.GONE);
        holder.tv_nonreprairable.setVisibility(View.VISIBLE);

    }






    @Override
    public int getItemCount() {
        return Complaint_Detail.smry_category_non.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_category, tv_quantity, tv_price,tv_reprairable,tv_nonreprairable;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            tv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_reprairable = (TextView) itemView.findViewById(R.id.tv_reprairable);
            tv_nonreprairable = (TextView) itemView.findViewById(R.id.tv_nonreprairable);


        }
    }
}
