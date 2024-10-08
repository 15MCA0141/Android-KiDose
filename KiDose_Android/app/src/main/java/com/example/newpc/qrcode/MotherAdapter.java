package com.example.newpc.qrcode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ku$haL on 17-06-2017.
 */
public class MotherAdapter extends RecyclerView.Adapter<MotherAdapter.MotherViewHolder> {
    private Context context;
    private List<MotherDTO> motherDTOList;
   // private LayoutInflater inflater;

    public MotherAdapter(Context context, List<MotherDTO> motherDTOList) {
        super();
        this.context = context;
        this.motherDTOList = motherDTOList;
       // inflater = LayoutInflater.from(context);
    }

    @Override
    public MotherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // private LayoutInflater inflater;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mother_custom_row,parent,false);

        MotherViewHolder holder = new MotherViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MotherViewHolder holder, int position) {
        MotherDTO adapter1=motherDTOList.get(position);
        holder.tvMother.setText(adapter1.getMotherName());
        holder.tvFather.setText(adapter1.getFatherName());
        holder.tvMobile.setText(adapter1.getMobile());
        holder.tvAadhar.setText(adapter1.getAadhar());
        holder.tvChild.setText(adapter1.getChildren());
    }

    @Override
    public int getItemCount() {
        return motherDTOList.size();
    }

    class MotherViewHolder extends RecyclerView.ViewHolder {
        TextView tvMother, tvFather, tvAadhar, tvMobile, tvChild;

        public MotherViewHolder(View itemView) {
            super(itemView);
            tvMother = (TextView) itemView.findViewById(R.id.tvMother);
            tvFather = (TextView) itemView.findViewById(R.id.tvFather);
            tvMobile = (TextView) itemView.findViewById(R.id.tvMobile);
            tvAadhar = (TextView) itemView.findViewById(R.id.tvAadhar);
            tvChild = (TextView) itemView.findViewById(R.id.tvChild);
        }

    }
}
