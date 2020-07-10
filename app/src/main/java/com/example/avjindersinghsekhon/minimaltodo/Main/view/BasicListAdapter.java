package com.example.avjindersinghsekhon.minimaltodo.Main.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity;
import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoFragment;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoListener;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ItemTouchHelperClass;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem;
import com.example.avjindersinghsekhon.minimaltodo.Utility.TodoNotificationService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;
import static com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity.TODOITEM;
import static com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment.LIGHTTHEME;
import static com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment.THEME_PREFERENCES;
import static com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment.THEME_SAVED;

public class BasicListAdapter extends RecyclerView.Adapter<BasicViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
    private ArrayList<ToDoItem> items;
    private ToDoListener listener;
    private Context context;

    public BasicListAdapter(ArrayList<ToDoItem> items, ToDoListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
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
        //Remove this line if not using Google Analytics
//        app.send(this, "Action", "Swiped Todo Away");

        listener.onRemove(items.get(position));
//        toDoViewModel.deleteItem(mJustDeletedToDoItem);
//
//        Intent i = new Intent(getContext(), TodoNotificationService.class);
//        deleteAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode());
//        notifyItemRemoved(position);
//
////            String toShow = (mJustDeletedToDoItem.getToDoText().length()>20)?mJustDeletedToDoItem.getToDoText().substring(0, 20)+"...":mJustDeletedToDoItem.getToDoText();
//        String toShow = "Todo";
//        Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
//                .setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //Comment the line below if not using Google Analytics
//                        app.send(this, "Action", "UNDO Pressed");
//                        toDoViewModel.saveItem(mJustDeletedToDoItem);
//                        if (mJustDeletedToDoItem.getToDoDate() != null && mJustDeletedToDoItem.hasReminder()) {
//                            Intent i = new Intent(getContext(), TodoNotificationService.class);
//                            i.putExtra(TodoNotificationService.TODOTEXT, mJustDeletedToDoItem.getToDoText());
//                            i.putExtra(TodoNotificationService.TODOUUID, mJustDeletedToDoItem.getIdentifier());
//                            createAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode(), mJustDeletedToDoItem.getToDoDate().getTime());
//                        }
//                    }
//                }).show();
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
//            if(item.getToDoDate()!=null && item.getToDoDate().before(new Date())){
//                item.setToDoDate(null);
//            }
        SharedPreferences sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
        //Background color for each to-do item. Necessary for night/day mode
        int bgColor;
        //color of title text in our to-do item. White for night mode, dark gray for day mode
        int todoTextColor;
        if (sharedPreferences.getString(THEME_SAVED, LIGHTTHEME).equals(LIGHTTHEME)) {
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
//                holder.mToDoTextview.setVisibility(View.GONE);
        } else {
            holder.mTimeTextView.setVisibility(View.GONE);
            holder.mToDoTextview.setMaxLines(2);
        }
        holder.mToDoTextview.setText(item.getToDoText());
        holder.mToDoTextview.setTextColor(todoTextColor);
//            holder.mColorTextView.setBackgroundColor(Color.parseColor(item.getTodoColor()));

//            TextDrawable myDrawable = TextDrawable.builder().buildRoundRect(item.getToDoText().substring(0,1),Color.RED, 10);
        //We check if holder.color is set or not
//            if(item.getTodoColor() == null){
//                ColorGenerator generator = ColorGenerator.MATERIAL;
//                int color = generator.getRandomColor();
//                item.setTodoColor(color+"");
//            }
//            Log.d("OskarSchindler", "Color: "+item.getTodoColor());
        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());

//            TextDrawable myDrawable = TextDrawable.builder().buildRound(item.getToDoText().substring(0,1),holder.color);
        holder.mColorImageView.setImageDrawable(myDrawable);
        if (item.getToDoDate() != null) {
            String timeToShow;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                timeToShow = AddToDoFragment.formatDate(MainFragment.DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
            } else {
                timeToShow = AddToDoFragment.formatDate(MainFragment.DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
            }
            holder.mTimeTextView.setText(timeToShow);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
