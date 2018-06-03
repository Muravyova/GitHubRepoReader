package com.github.muravyova.githubreporeader.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.adapter.ContentAdapter;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.models.FileItem;
import com.github.muravyova.githubreporeader.viewmodels.RepositoryFileViewModel;

public class RepositoryFileActivity extends AppCompatActivity {

    private static final String USER_NAME = "userName";
    private static final String REPO_NAME = "repoName";

    private ContentAdapter mContentAdapter;
    private RepositoryFileViewModel mRepositoryFileViewModel;
    private String mUserName;
    private String mRepoName;
    private String mCurrentPath;

    private final CommonItem.ItemClick mClick = new CommonItem.ItemClick() {
        @Override
        public void onClick(CommonItem item) {
            switch (item.type){
                case R.id.item_error_id:
                    mRepositoryFileViewModel.loadFiles(mUserName, mRepoName, mCurrentPath);
                    break;
                case R.id.item_file_id:
                    FileItem fileItem = (FileItem) item;
                    if (fileItem.getFile().type.equals("file")){
                        startActivity(ReaderActivity.newIntent(RepositoryFileActivity.this, fileItem));
                    } else {
                        mRepositoryFileViewModel.loadFiles(mUserName, mRepoName, fileItem.getFile().path);
                    }
                    break;
            }
        }
    };

    public static Intent newIntent(Context context, String userName, String repoName){
        Intent intent = new Intent(context, RepositoryFileActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(REPO_NAME, repoName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_file);
        mUserName = getIntent().getStringExtra(USER_NAME);
        mRepoName = getIntent().getStringExtra(REPO_NAME);
        mRepositoryFileViewModel = ViewModelProviders.of(this).get(RepositoryFileViewModel.class);
        mRepositoryFileViewModel.check(mUserName, mRepoName);
        findViews();
        observeViewModel();
    }

    private void findViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentAdapter = new ContentAdapter(mClick);
        RecyclerView recyclerView = findViewById(R.id.repo_files);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mContentAdapter);
    }

    private void observeViewModel(){
        mRepositoryFileViewModel.getStackList().observe(this, data ->{
            if(data != null){
                mContentAdapter.show(data.items);
                mCurrentPath = mRepositoryFileViewModel.addToStack(data.items, data.path);
                getSupportActionBar().setTitle("./" + mCurrentPath);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mRepositoryFileViewModel.canGoBack()){
            mRepositoryFileViewModel.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
