package com.example.avjindersinghsekhon.minimaltodo.Main.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.avjindersinghsekhon.minimaltodo.Main.CustomRecyclerScrollViewListener;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.AlarmHelper;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoListener;
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoTheme;
import com.example.avjindersinghsekhon.minimaltodo.Main.viewmodel.ToDoViewModel;
import com.example.avjindersinghsekhon.minimaltodo.Main.viewmodel.ToDoViewModelFactory;
import com.example.avjindersinghsekhon.minimaltodo.database.AppDatabase;
import com.example.avjindersinghsekhon.minimaltodo.database.dao.ToDoDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.avjindersinghsekhon.minimaltodo.About.AboutActivity;
import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity;
import com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoFragment;
import com.example.avjindersinghsekhon.minimaltodo.Analytics.AnalyticsApplication;
import com.example.avjindersinghsekhon.minimaltodo.AppDefault.AppDefaultFragment;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Reminder.ReminderFragment;
import com.example.avjindersinghsekhon.minimaltodo.Settings.SettingsActivity;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ItemTouchHelperClass;
import com.example.avjindersinghsekhon.minimaltodo.Utility.RecyclerViewEmptySupport;
import com.example.avjindersinghsekhon.minimaltodo.Utility.StoreRetrieveData;
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem;
import com.example.avjindersinghsekhon.minimaltodo.Utility.TodoNotificationService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.avjindersinghsekhon.minimaltodo.AddToDo.AddToDoActivity.TODOITEM;

public class MainFragment extends AppDefaultFragment implements ToDoListener {
    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddToDoItemFAB;
    private ArrayList<ToDoItem> mToDoItemsArrayList;
    private CoordinatorLayout mCoordLayout;
    private BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";
    public static final String FILENAME = "todoitems.json";
    private StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
    private ToDoTheme mTheme;
    private List<ToDoItem> mItems;
    private String theme = "name_of_the_theme";
    private AnalyticsApplication app;
    private String[] testStrings = {"Clean my room",
            "Water the plants",
            "Get car washed",
            "Get my dry cleaning"
    };

    private ToDoViewModel toDoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoViewModel = getViewModel();
    }

    private ToDoViewModel getViewModel() {
        ToDoDao dao = AppDatabase.getAppDatabase(getContext()).toDoDao();
        AlarmHelper alarmHelper = new AlarmHelper(getContext());
        PrefsHelper prefsHelper = new PrefsHelper(getContext());

        ToDoViewModelFactory factory = new ToDoViewModelFactory(dao, alarmHelper, prefsHelper);
        ViewModelProvider provider = new ViewModelProvider(getViewModelStore(), factory);
        return provider.get(ToDoViewModel.class);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (AnalyticsApplication) getActivity().getApplication();

        storeRetrieveData = new StoreRetrieveData(getContext(), FILENAME);
        mToDoItemsArrayList = getLocallyStoredData(storeRetrieveData);

        mCoordLayout = view.findViewById(R.id.myCoordinatorLayout);
        mAddToDoItemFAB = view.findViewById(R.id.addToDoItemFAB);

        mAddToDoItemFAB.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                app.send(this, "Action", "FAB pressed");
                AddToDoActivity.startForResult(MainFragment.this, REQUEST_ID_TODO_ITEM);
            }
        });

        toDoViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {
                mItems = toDoItems;
                initRecyclerView(view);
            }
        });

        toDoViewModel.getTheme().observe(getViewLifecycleOwner(), new Observer<ToDoTheme>() {
            @Override
            public void onChanged(ToDoTheme toDoTheme) {
                mTheme = toDoTheme;
                MainFragment.this.getActivity().setTheme(mTheme.getTheme());
            }
        });
    }

    private void initRecyclerView(View view) {
        if (mItems == null || mTheme == null) {
            return;
        }

        adapter = new BasicListAdapter(new ArrayList<>(mItems), MainFragment.this, getContext(), mTheme);
        mRecyclerView = view.findViewById(R.id.toDoRecyclerView);
        if (mTheme.isLightTheme()) {
            mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_lightest));
        }

        if (itemTouchHelper != null) {
            itemTouchHelper.attachToRecyclerView(null);
        }

        mRecyclerView.setEmptyView(view.findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {

                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }

            @Override
            public void hide() {

                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddToDoItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddToDoItemFAB.animate().translationY(mAddToDoItemFAB.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);


        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setAdapter(adapter);
    }

    public static ArrayList<ToDoItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<ToDoItem> items = null;

        try {
            items = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    public void onResume() {
        super.onResume();
        app.send(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutMeMenuItem:
                Intent i = new Intent(getContext(), AboutActivity.class);
                startActivity(i);
                return true;
            case R.id.preferences:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
            ToDoItem item = (ToDoItem) data.getSerializableExtra(TODOITEM);
            if (item.getToDoText().length() <= 0) {
                return;
            }
            toDoViewModel.saveItem(item);
        }
    }

    private void addToDataStore(ToDoItem item) {
        mToDoItemsArrayList.add(item);
        adapter.notifyItemInserted(mToDoItemsArrayList.size() - 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            storeRetrieveData.saveToFile(mToDoItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void onClick(@NotNull ToDoItem item) {
        AddToDoActivity.startForResult(this, item, REQUEST_ID_TODO_ITEM);
    }

    @Override
    public void onRemove(final @NotNull ToDoItem item) {
        app.send(this, "Action", "Swiped Todo Away");

        toDoViewModel.deleteItem(item);

        String toShow = "Todo";
        Snackbar.make(mCoordLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        app.send(this, "Action", "UNDO Pressed");
                        toDoViewModel.saveItem(item);
                    }
                }).show();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
