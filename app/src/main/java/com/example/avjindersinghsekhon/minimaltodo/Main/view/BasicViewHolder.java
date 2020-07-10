package com.example.avjindersinghsekhon.minimaltodo.Main.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoListener;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem;

import static com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity.TODOITEM;

@SuppressWarnings("deprecation")
public class BasicViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView mToDoTextview;
    ImageView mColorImageView;
    TextView mTimeTextView;
    ToDoItem item;

    public BasicViewHolder(View v, final ToDoListener listener) {
        super(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(item);
            }
        });
        mToDoTextview = v.findViewById(R.id.toDoListItemTextview);
        mTimeTextView = v.findViewById(R.id.todoListItemTimeTextView);
        mColorImageView = v.findViewById(R.id.toDoListItemColorImageView);
        linearLayout = v.findViewById(R.id.listItemLinearLayout);
    }


}