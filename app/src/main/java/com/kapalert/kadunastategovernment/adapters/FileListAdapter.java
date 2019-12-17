package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.BeanUploadImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.MyViewHolder>
{
    Context mContext;
    List<BeanUploadImage> beanUploadImages;
    public FileListAdapter(Context mContext,     List<BeanUploadImage> beanUploadImages)
    {
        this.mContext = mContext;
        this.beanUploadImages = beanUploadImages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_row_file, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String imgUrl = String.valueOf(beanUploadImages.get(position).getUri());
       String filename = imgUrl.substring(imgUrl.lastIndexOf("/")+1);

        holder.tv_fileName.setText(filename);
            if(imgUrl.contains(".jpeg"))
            {
                Picasso.with(mContext)
                        .load(R.drawable.ic_jpg)
                        .into(holder.img_FileImage);
            }

            else if(imgUrl.contains(".pdf"))
            {
                Picasso.with(mContext)
                        .load(R.drawable.ic_pdf)
                        .into(holder.img_FileImage);
            }
    }

    @Override
    public int getItemCount() {
        return beanUploadImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fileName;
        ImageView img_FileImage;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_fileName = itemView.findViewById(R.id.tv_fileName);
            img_FileImage = itemView.findViewById(R.id.img_pdf);

        }
    }
}
