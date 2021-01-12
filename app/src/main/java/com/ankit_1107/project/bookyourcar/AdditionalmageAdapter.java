package com.ankit_1107.project.bookyourcar;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdditionalmageAdapter extends RecyclerView.Adapter<AdditionalmageAdapter.ViewHolder> {

    ArrayList<Uri> dataList;
    LayoutInflater layoutInflater;
    AdditionalmageAdapter(Context context,ArrayList<Uri> dataList){
        layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.image_item_recycler_view,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView additionalImage = holder.additionalImage;
        Picasso.get().load(dataList.get(position)).into(additionalImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView additionalImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            additionalImage = itemView.findViewById(R.id.image_view_item_recycler_view);
        }
    }
}
