package me.shkschneider.skeleton;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.shkschneider.skeleton.helper.AndroidHelper;
import me.shkschneider.skeleton.helper.ApplicationHelper;
import me.shkschneider.skeleton.helper.IntentHelper;
import me.shkschneider.skeleton.helper.KeyboardHelper;
import me.shkschneider.skeleton.helper.LogHelper;

public class SkeletonActivity extends ActionBarActivity {

    private int mLoadingCount = 0;
    private SwipeRefreshLayout mSwipeRefreshlayout;
    private boolean mAlive = false;
    private String mSearchHint;
    private SearchCallback mSearchCallback = null;
    private MenuItem mSearchMenuItem;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.swiperefreshlayout);
        mSwipeRefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        // setContentView()
        home(false);
        // logo(false);
        title(true);

        if (AndroidHelper.api() >= AndroidHelper.API_21) {
            init21();
        }
    }

    @SuppressWarnings("NewApi")
    @SuppressLint("NewApi")
    @TargetApi(AndroidHelper.API_21)
    private void init21() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Material Design style
            getSupportActionBar().setElevation(0F);
        }

        final String name = ApplicationHelper.name();
        final Bitmap icon = ApplicationHelper.icon();
        final int color = getResources().getColor(R.color.primaryColor);
        final ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(name, icon, color);
        setTaskDescription(taskDescription);
    }

    // Refresh
    // <https://gist.github.com/antoniolg/9837398>

    @Override
    public void setContentView(final int layoutResID) {
        final View view = getLayoutInflater().inflate(layoutResID, mSwipeRefreshlayout, false);
        setContentView(view);
    }

    @Override
    public void setContentView(final View view) {
        setContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams params) {
        mSwipeRefreshlayout.addView(view, params);
        mSwipeRefreshlayout.setOnRefreshListener(null);
        mSwipeRefreshlayout.setEnabled(false);
        mSwipeRefreshlayout.setColorSchemeResources(R.color.primaryColor);
        mSwipeRefreshlayout.setRefreshing(false);
    }

    public void setRefreshListener(@NonNull final SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        // Resets loading count to avoid side-effects upon re-loading
        mLoadingCount = 0;
        mSwipeRefreshlayout.setOnRefreshListener(onRefreshListener);
        mSwipeRefreshlayout.setEnabled(true);
        mSwipeRefreshlayout.setRefreshing(false);
    }

    public boolean loading() {
        return (mLoadingCount > 0);
    }

    public void loading(final int i) {
        mLoadingCount += i;
        if (mLoadingCount < 0) {
            mLoadingCount = 0;
        }
        loading((mLoadingCount > 0));
    }

    public void loading(final boolean b) {
        if (! b) {
            // Resets loading count to avoid side-effects upon re-loading
            mLoadingCount = 0;
            mSwipeRefreshlayout.setRefreshing(false);
        }
        else if (! mSwipeRefreshlayout.isRefreshing()) {
            mSwipeRefreshlayout.setRefreshing(true);
        }
    }

    // Alive

    @Override
    protected void onResume() {
        super.onResume();

        mAlive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAlive = false;
    }

    public boolean alive() {
        return mAlive;
    }

    // Home

    public void home(final boolean b) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            LogHelper.warning("ActionBar was NULL");
            return ;
        }
        actionBar.setDisplayShowHomeEnabled(b);
        actionBar.setDisplayHomeAsUpEnabled(b);
    }

    // Logo

    @Deprecated
    public void logo(final boolean b) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            LogHelper.warning("ActionBar was NULL");
            return ;
        }
        actionBar.setDisplayUseLogoEnabled(b);
        if (! b) {
            actionBar.setIcon(getResources().getColor(android.R.color.transparent));
        }
        else {
            try {
                final Drawable icon = getPackageManager().getActivityIcon(getComponentName());
                actionBar.setIcon(icon);
            }
            catch (final Exception e) {
                LogHelper.wtf(e);
            }
        }
    }

    // Title

    public void title(final boolean b) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            LogHelper.warning("ActionBar was NULL");
            return ;
        }
        actionBar.setDisplayShowTitleEnabled(b);
    }

    // Search
    // <http://stackoverflow.com/questions/18438890>

    public void searchable(final String hint, final SearchCallback searchCallback) {
        mSearchHint = hint;
        mSearchCallback = searchCallback;
        // AVOID supportInvalidateOptionsMenu()
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (mSearchCallback == null) {
            return super.onCreateOptionsMenu(menu);
        }

        getMenuInflater().inflate(R.menu.search, menu);

        mSearchMenuItem = menu.findItem(R.id.menu_search);
        if (mSearchMenuItem == null) {
            LogHelper.warning("SearchMenuItem was NULL");
            return super.onCreateOptionsMenu(menu);
        }

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        if (searchView == null) {
            LogHelper.warning("SearchView was NULL");
            return super.onCreateOptionsMenu(menu);
        }
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(mSearchHint);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchCallback.onSearchTextChange("");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String q) {
                stopSearch();
                searchView.clearFocus();
                mSearchCallback.onSearchTextSubmit(q);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String q) {
                mSearchCallback.onSearchTextChange(q);
                return true;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            final Intent intent = getPackageManager().getLaunchIntentForPackage(ApplicationHelper.packageName());
            startActivity(intent.setFlags(IntentHelper.HOME_FLAGS));
            return true;
        }

        if (mSearchCallback == null) {
            return super.onOptionsItemSelected(item);
        }

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if (searchView == null) {
            return false;
        }

        if (item.getItemId() == R.id.menu_search) {
            searchView.setIconified(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void stopSearch() {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        if (searchView == null) {
            return ;
        }

        searchView.clearFocus();
        mSearchMenuItem.collapseActionView();
        KeyboardHelper.hide(searchView.getWindowToken());
    }

    public static interface SearchCallback {

        public void onSearchTextChange(final String q);
        public void onSearchTextSubmit(final String q);

    }

    // Navigation

    @Override
    public void onBackPressed() {
        finish();
    }

    public static interface NavigationCallback {

        public void onHomeAsUpPressed();
        public void onBackPressed();

    }

}