package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.BeanUploadImage;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;
import com.kapalert.kadunastategovernment.templates.NoteModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CaseNote_Adapter extends RecyclerView.Adapter<CaseNote_Adapter.MyViewHolder>
{
    Context mContext;
    List<CaseDetailPOJO.CaseNotes> modelList;
    public CaseNote_Adapter(Context mContext, List<CaseDetailPOJO.CaseNotes> modelList)
    {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note_case, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.notes_data.setText(modelList.get(position).caseNote);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notes_data;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            notes_data =itemView.findViewById(R.id.notes_data);

        }
    }
}
