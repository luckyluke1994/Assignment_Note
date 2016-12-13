package com.example.jax.Note.custom.view.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jax.Note.activity.MainActivity;
import com.example.jax.Note.activity.NewNoteActivity;
import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.ItemClickListener;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.assignment_note.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jax on 30-Nov-16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<NoteInfo> noteInfo = new ArrayList<>();
    private Context mContext;
    ItemClickListener listener;
    DBHelper dbHelper;

    public RecyclerAdapter(List<NoteInfo> noteInfo, Context context) {
        this.noteInfo = noteInfo;
        this.mContext = context;
        this.listener = (ItemClickListener) context;
    }

    //giao dien 1 phan tu
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent,false);
        return  new ViewHolder(view);
    }

    //dat cac gia tri trong phan tu
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String title = noteInfo.get(position).title;
        String note = noteInfo.get(position).note;

        final NoteInfo noteInfo1= new NoteInfo();
        noteInfo1.title = title;
        noteInfo1.note = note;
        noteInfo1.id = position;

        String currentDateTime = new SimpleDateFormat("dd/MM HH:mm").format(new Date());
        holder.mTime.setText(currentDateTime);

        //Bind
        holder.mTitle.setText(title);
        holder.mNote.setText(note);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  //xoa
                  listener.onItemClick(noteInfo.get(position).id);
                  //edit
                  listener.editNote(dbHelper.getSingleNote(noteInfo1.id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteInfo.size();
    }



}
