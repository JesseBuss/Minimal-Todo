package com.example.avjindersinghsekhon.minimaltodo.AddToDo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.avjindersinghsekhon.minimaltodo.AppDefault.AppDefaultActivity;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem;

public class AddToDoActivity extends AppDefaultActivity {
    public static final String TODOITEM = "com.avjindersinghsekhon.com.avjindersinghsekhon.minimaltodo.MainActivity";

    public static void startForResult(Fragment fragment, int requestCode) {
        Intent newTodo = new Intent(fragment.getContext(), AddToDoActivity.class);
        ToDoItem item = new ToDoItem("","", false, null);
        int color = ColorGenerator.MATERIAL.getRandomColor();
        item.setTodoColor(color);
        newTodo.putExtra(TODOITEM, item);

        fragment.startActivityForResult(newTodo, requestCode);
    }

    public static void startForResult(Fragment fragment, ToDoItem item, int requestCode) {
        Intent newTodo = new Intent(fragment.getContext(), AddToDoActivity.class);
        newTodo.putExtra(TODOITEM, item);

        fragment.startActivityForResult(newTodo, requestCode);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_add_to_do;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return AddToDoFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

