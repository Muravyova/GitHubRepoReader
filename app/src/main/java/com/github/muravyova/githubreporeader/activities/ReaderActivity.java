package com.github.muravyova.githubreporeader.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.fragments.CodeFragment;
import com.github.muravyova.githubreporeader.fragments.ImageFragment;
import com.github.muravyova.githubreporeader.models.FileItem;
import com.github.muravyova.githubreporeader.util.StringUtil;

import java.util.List;

public class ReaderActivity extends AppCompatActivity {

    public final static String URL = "url";
    private final static String TITLE = "title";

    private final static String TYPE = "type";

    public static Intent newIntent(Context context, FileItem item){
        Intent intent = new Intent(context, ReaderActivity.class);
        if (StringUtil.canShowLikeImage(item.getFile().name)){
            intent.putExtra(TYPE, 0);
        } else {
            intent.putExtra(TYPE, 1);
        }

        intent.putExtra(URL, item.getFile().downloadUrl);
        intent.putExtra(TITLE, item.getFile().path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        String url = getIntent().getStringExtra(URL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("./" + getIntent().getStringExtra(TITLE));
        int type = getIntent().getIntExtra(TYPE, 0);
        if (savedInstanceState == null || notHaveFragments()){
            if (type == 0){
                setFragment(ImageFragment.newInstance(url), ImageFragment.class.getSimpleName());
            } else {
                setFragment(CodeFragment.newInstance(url), CodeFragment.class.getSimpleName());
            }
        }
    }

    private boolean notHaveFragments(){
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        return fragmentList == null || fragmentList.size() == 0;
    }

    private void setFragment(Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
