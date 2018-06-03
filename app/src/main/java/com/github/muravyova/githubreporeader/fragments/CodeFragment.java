package com.github.muravyova.githubreporeader.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.activities.ReaderActivity;
import com.github.muravyova.githubreporeader.viewmodels.RepositoryFileViewModel;
import com.protectsoft.webviewcode.Codeview;
import com.protectsoft.webviewcode.Settings;

public class CodeFragment extends Fragment {

    private WebView mWebView;
    private String mUrl;
    private ProgressBar mLoadingProgress;
    private TextView mErrorMessage;
    private TextView mTryAgain;
    private ImageView mErrorIcon;
    private RepositoryFileViewModel mRepositoryFileViewModel;

    public static CodeFragment newInstance(String url){
        CodeFragment codeFragment = new CodeFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ReaderActivity.URL, url);
        codeFragment.setArguments(bundle);
        return codeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ReaderActivity.URL);
        mRepositoryFileViewModel = ViewModelProviders.of(this).get(RepositoryFileViewModel.class);
        mRepositoryFileViewModel.check(mUrl);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_code, container, false);
        findViews(rootView);
        setClickListeners();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel();
    }

    private void findViews(View rootView){
        mWebView = rootView.findViewById(R.id.code);
        mErrorIcon = rootView.findViewById(R.id.error_icon);
        mLoadingProgress = rootView.findViewById(R.id.load_progress);
        mErrorMessage = rootView.findViewById(R.id.error_message);
        mTryAgain = rootView.findViewById(R.id.try_again);
    }

    private void setClickListeners(){
        mTryAgain.setOnClickListener(v -> mRepositoryFileViewModel.tryAgain(mUrl));
    }

    private void observeViewModel(){
        mRepositoryFileViewModel.getCodeContent().observe(this, data ->{
            if (data != null){
                switch (data.status){
                    case ERROR:
                        showError(data.message);
                        break;
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        showCode(data.data);
                        break;
                }
            }
        });
    }

    private void showError(String message){
        mTryAgain.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorIcon.setVisibility(View.VISIBLE);
        mErrorMessage.setText(message);
        mLoadingProgress.setVisibility(View.GONE);
    }

    private void showLoading(){
        mTryAgain.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mErrorIcon.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    private void showCode(String code){
        mTryAgain.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mErrorIcon.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.GONE);
        Codeview.with(getContext())
                .withCode(code)
                .setStyle(Settings.WithStyle.GITHUB)
                .into(mWebView);
    }
}
