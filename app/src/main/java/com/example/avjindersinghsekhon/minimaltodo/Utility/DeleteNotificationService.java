package com.example.avjindersinghsekhon.minimaltodo.Utility;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment;

import java.util.ArrayList;
import java.util.UUID;

import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.CHANGE_OCCURED;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.SHARED_PREF_DATA_SET_CHANGED;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.StoreHelper.FILENAME;

public class DeleteNotificationService extends IntentService {

    private StoreRetrieveData storeRetrieveData;
    private ArrayList<ToDoItem> mToDoItems;
    private ToDoItem mItem;

    public DeleteNotificationService() {
        super("DeleteNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        storeRetrieveData = new StoreRetrieveData(this, FILENAME);
        UUID todoID = (UUID) intent.getSerializableExtra(TodoNotificationService.TODOUUID);

        mToDoItems = loadData();
        if (mToDoItems != null) {
            for (ToDoItem item : mToDoItems) {
                if (item.getIdentifier().equals(todoID)) {
                    mItem = item;
                    break;
                }
            }

            if (mItem != null) {
                mToDoItems.remove(mItem);
                dataChanged();
                saveData();
            }

        }

    }

    private void dataChanged() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, true);
        editor.apply();
    }

    private void saveData() {
        try {
            storeRetrieveData.saveToFile(mToDoItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    private ArrayList<ToDoItem> loadData() {
        try {
            return storeRetrieveData.loadFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
