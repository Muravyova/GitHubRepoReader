package com.github.muravyova.githubreporeader.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.adapters.ContentAdapter;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.models.RepositoryItem;
import com.github.muravyova.githubreporeader.models.UserItem;
import com.github.muravyova.githubreporeader.network.User;
import com.github.muravyova.githubreporeader.utils.CircularTransformation;
import com.github.muravyova.githubreporeader.viewmodels.RepositoryViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepositoryActivity extends AppCompatActivity {

    private static final String USER = "user";
    private RepositoryViewModel mRepositoryViewModel;
    private ImageView mUserImage;
    private TextView mUserLogin;
    private ContentAdapter mContentAdapter;
    private TextView mRepositoryCount;
    private User mUser;

    public static Intent newIntent(Context context, UserItem item){
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(USER, item.getUser());
        return intent;
    }

    private final CommonItem.ItemClick mClick = new CommonItem.ItemClick() {
        @Override
        public void onClick(CommonItem item) {
            switch (item.type){
                case R.id.item_error_id:
                    mRepositoryViewModel.tryAgain(mUser.login);
                    break;
                case R.id.item_repository_id:
                    RepositoryItem repositoryItem = (RepositoryItem) item;
                    startActivity(RepositoryFileActivity.newIntent(RepositoryActivity.this,
                            mUser.login, repositoryItem.getRepository().name));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        mRepositoryViewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);
        findViews();
        mUser = getIntent().getParcelableExtra(USER);
        putInViews(mUser);
        mRepositoryViewModel.check(mUser.login);
        observeViewModel();
    }

    private void findViews(){
        ImageView backIcon = findViewById(R.id.icon_back);
        backIcon.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        backIcon.setOnClickListener(v -> finish());
        mContentAdapter = new ContentAdapter(mClick);
        RecyclerView recyclerView = findViewById(R.id.repositories_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mContentAdapter);
        mUserImage = findViewById(R.id.user_image);
        mUserLogin = findViewById(R.id.user_login);
        mRepositoryCount = findViewById(R.id.user_repositories_count);
    }

    private void putInViews(User user){
        mUserLogin.setText(user.login);
        Picasso.with(this)
                .load(user.avatarUrl)
                .transform(new CircularTransformation())
                .into(mUserImage);
    }

    private void observeViewModel(){
        mRepositoryViewModel.getContent().observe(this, data ->{
            if (data != null){
                mContentAdapter.show(data);
                applyData(data);
            }
        });
    }

    private void applyData(List<CommonItem> items){
        int size;
        if (items.size() == 1){
            if (!(items.get(0) instanceof RepositoryItem)){
                size = 0;
            } else {
                size = 1;
            }
        } else {
            size = items.size();
        }
        String s = getString(R.string.repositories) + " " + size;
        mRepositoryCount.setText(s);
    }
}
