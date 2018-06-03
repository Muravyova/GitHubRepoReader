package com.github.muravyova.githubreporeader.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.activities.ReaderActivity;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {

    private String mUrl;
    private PhotoView mPhotoView;

    public static ImageFragment newInstance(String url){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ReaderActivity.URL, url);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ReaderActivity.URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        mPhotoView = rootView.findViewById(R.id.photo_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Picasso.with(getContext())
                .load(mUrl)
                .into(mPhotoView);
    }
}
