package com.github.muravyova.githubreporeader.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.adapter.ContentAdapter;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.models.UserItem;
import com.github.muravyova.githubreporeader.util.DisplayUtil;
import com.github.muravyova.githubreporeader.viewmodels.SearchUserViewModel;

public class SearchUserActivity extends AppCompatActivity {

    private ContentAdapter mContentAdapter;
    private SearchUserViewModel mSearchUserViewModel;
    private SearchView mSearchView;

    @NonNull
    private final CommonItem.ItemClick mItemClick = item -> {
        switch (item.type){
            case R.id.item_error_id:
                mSearchUserViewModel.makeQuery(mSearchView.getQuery().toString());
                break;
            case R.id.item_user_id:
                startActivity(RepositoryActivity.newIntent(this, (UserItem)item));
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        mSearchUserViewModel = ViewModelProviders.of(this).get(SearchUserViewModel.class);
        mContentAdapter = new ContentAdapter(mItemClick);
        RecyclerView content = findViewById(R.id.search_user_content_list);
        int spanCount = DisplayUtil.calculateSpanCount(this);
        GridLayoutManager manager = new GridLayoutManager(this, spanCount);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mContentAdapter.getItemViewType(position);
                switch (viewType){
                    case R.id.item_user_id:
                        return 1;
                    default:
                        return spanCount;
                }
            }
        });
        content.setLayoutManager(manager);
        content.setAdapter(mContentAdapter);
        observeViewModel();
    }

    private void observeViewModel(){
        mSearchUserViewModel.getContent().observe(this, data ->{
            if (data != null){
                mContentAdapter.show(data);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        search(mSearchView);
        return true;
    }

    private void search(SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2){
                    mSearchUserViewModel.makeQuery(newText);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
