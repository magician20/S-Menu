package com.example.magician.resturantm.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.magician.resturantm.R;
import com.example.magician.resturantm.data.database.ItemEntry;
import com.example.magician.resturantm.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MenuAdapter mMenuAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;

    private  MainActivityViewModel mainViewModel;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //change name of bar
        getSupportActionBar().setTitle(getString(R.string.my_cart));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);

        mRecyclerView = findViewById(R.id.recycler_menu);
        /* recycler view atttributes */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        /* set adapter */
        mMenuAdapter = new MenuAdapter(this);
        mRecyclerView.setAdapter(mMenuAdapter);

        /* attach ItemTouchHelper for swipe */
        RecyclerItemTouchHelper itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        /* get repository object and create ViewModel by using Factory */
        MainViewModelFactory mainFactory = InjectorUtils.provideMainActivityViewModelFactory(this);
        // get ViewModel object
         mainViewModel = ViewModelProviders.of(this, mainFactory).get(MainActivityViewModel.class);
       /* observe the data */
        Log.d(LOG_TAG, "Start observer");// no data is comming lol
        mainViewModel.getMenuItems().observe(this, itemEntries -> {
            mMenuAdapter.swapForecast(itemEntries);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (itemEntries != null && itemEntries.size() != 0) {
                showMenuDataView();
            } else {
                showLoading();
            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        // backup of removed item for undo purpose
        final int deletedItemIndex = viewHolder.getAdapterPosition();
        final ItemEntry deletedItem = mainViewModel.getMenuItems().getValue().get(deletedItemIndex);

        // get the removed item name to display it in snack bar
        String name = deletedItem.getName();

        // remove the item from recycler view
        mMenuAdapter.removeItem(viewHolder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, name + " remove from mrnu!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // undo is selected, restore the deleted item
                mMenuAdapter.restorItem(deletedItemIndex, deletedItem);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMenuDataView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the weather data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        // Then, hide the weather data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


}
