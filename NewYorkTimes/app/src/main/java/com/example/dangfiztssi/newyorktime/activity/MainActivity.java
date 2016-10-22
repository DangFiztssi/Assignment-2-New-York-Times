package com.example.dangfiztssi.newyorktime.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.dangfiztssi.newyorktime.R;
import com.example.dangfiztssi.newyorktime.models.SearchRequest;
import com.example.dangfiztssi.newyorktime.presenter.MainPresenter;
import com.example.dangfiztssi.newyorktime.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;

public class MainActivity extends AppCompatActivity implements FilterSearchDialogFragment.FilterListener {

    @BindView(R.id.refreshMain)
    public SwipeRefreshLayout refreshMain;

    @BindView(R.id.rvMain)
    public RecyclerView rvMain;

    @BindView(R.id.pbLoadMore)
    public ProgressBar loadMore;

    SearchView searchView;

    public Dialog waiting;

    MainPresenter presenter;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        waiting = AppUtils.getWaitingDialog(this);
        waiting.show();

        presenter = new MainPresenter(this);
        setupView();
        presenter.search();

        refreshMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.search();
            }
        });
    }

    private void setupView() {
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager) manager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        rvMain.setLayoutManager(manager);
//        rvMain.setAdapter(new SlideInBottomAnimationAdapter(presenter.getAdapter()));
        rvMain.setAdapter(new SlideInLeftAnimationAdapter(presenter.getAdapter()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                presenter.request.setQuery("");
                presenter.search();
                return true;
            }
        });

        setupSearchIcon(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchIcon(MenuItem menuItem) {
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                presenter.request.setQuery(newText);
                presenter.search();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
            case R.id.action_sort:
                showFilterDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager manager = getSupportFragmentManager();
        FilterSearchDialogFragment dialogFragment = FilterSearchDialogFragment.newInstance(presenter.request);
        dialogFragment.show(manager,"fragment_filter_search");
    }

    @Override
    public void onFinishFilter(SearchRequest result) {
        presenter.search();
    }
}
