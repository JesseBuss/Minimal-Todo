package com.example.avjindersinghsekhon.minimaltodo.Main.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoFragment;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoListener;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoTheme;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ItemTouchHelperClass;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem;

import java.util.ArrayList;
import java.util.Collections;

public class BasicListAdapter extends RecyclerView.Adapter<BasicViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
    private static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    private static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";

    private ArrayList<ToDoItem> items;
    private ToDoListener listener;
    private Context context;
    private ToDoTheme theme;

    public BasicListAdapter(ArrayList<ToDoItem> items, ToDoListener listener, Context context, ToDoTheme theme) {
        this.items = items;
        this.listener = listener;
        this.context = context;
        this.theme = theme;
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(final int position) {
        listener.onRemove(items.get(position));
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_circle_try, parent, false);
        return new BasicViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final BasicViewHolder holder, final int position) {
        ToDoItem item = items.get(position);
        holder.item = item;
        int bgColor;
        int todoTextColor;
        if (theme.isLightTheme()) {
            bgColor = Color.WHITE;
            todoTextColor = context.getResources().getColor(R.color.secondary_text);
        } else {
            bgColor = Color.DKGRAY;
            todoTextColor = Color.WHITE;
        }
        holder.linearLayout.setBackgroundColor(bgColor);

        if (item.hasReminder() && item.getToDoDate() != null) {
            holder.mToDoTextview.setMaxLines(1);
            holder.mTimeTextView.setVisibility(View.VISIBLE);
        } else {
            holder.mTimeTextView.setVisibility(View.GONE);
            holder.mToDoTextview.setMaxLines(2);
        }
        holder.mToDoTextview.setText(item.getToDoText());
        holder.mToDoTextview.setTextColor(todoTextColor);
        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());
        holder.mColorImageView.setImageDrawable(myDrawable);
        if (item.getToDoDate() != null) {
            String timeToShow;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                timeToShow = AddToDoFragment.formatDate(DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
            } else {
                timeToShow = AddToDoFragment.formatDate(DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
            }
            holder.mTimeTextView.setText(timeToShow);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
