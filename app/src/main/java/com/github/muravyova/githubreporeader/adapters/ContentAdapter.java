package com.github.muravyova.githubreporeader.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.muravyova.githubreporeader.App;
import com.github.muravyova.githubreporeader.R;
import com.github.muravyova.githubreporeader.models.CommonItem;
import com.github.muravyova.githubreporeader.models.EmptyItem;
import com.github.muravyova.githubreporeader.models.ErrorItem;
import com.github.muravyova.githubreporeader.models.DocumentItem;
import com.github.muravyova.githubreporeader.models.RepositoryItem;
import com.github.muravyova.githubreporeader.models.UserItem;
import com.github.muravyova.githubreporeader.network.DocumentType;
import com.github.muravyova.githubreporeader.utils.CircularTransformation;
import com.github.muravyova.githubreporeader.utils.StringUtil;
import com.github.muravyova.githubreporeader.widgets.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.CommonViewHolder>{

    @NonNull
    private final CommonItem.ItemClick mItemClick;
    @NonNull
    private List<CommonItem> mItems = new ArrayList<>();

    public ContentAdapter(@NonNull CommonItem.ItemClick itemClick) {
        mItemClick = itemClick;
    }

    @NonNull
    @Override
    public ContentAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case R.id.item_document_id:
                view = inflater.inflate(R.layout.item_document, parent, false);
                return new DocumentViewHolder(view);
            case R.id.item_user_id:
                view = inflater.inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(view);
            case R.id.item_error_id:
                view = inflater.inflate(R.layout.item_error, parent, false);
                return new ErrorViewHolder(view);
            case R.id.item_repository_id:
                view = inflater.inflate(R.layout.item_repository, parent, false);
                return new RepositoryViewHolder(view);
            case R.id.item_empty_id:
                view = inflater.inflate(R.layout.item_empty, parent, false);
                return new EmptyViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_loading, parent, false);
                return new LoadViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        holder.bind(mItems.get(position), mItemClick);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    public void show(List<CommonItem> data){
        notifyItemRangeRemoved(0 , mItems.size());
        mItems = data;
        notifyItemRangeInserted(0 , mItems.size());
    }

    abstract static class CommonViewHolder extends RecyclerView.ViewHolder{

        CommonViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(CommonItem item, CommonItem.ItemClick click);

    }

    public static class LoadViewHolder extends CommonViewHolder{

        LoadViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) { }

    }

    public static class EmptyViewHolder extends CommonViewHolder{

        private final TextView mMessage;

        EmptyViewHolder(View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.message);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) {
            mMessage.setText(((EmptyItem)item).getMessage());
        }

    }

    public static class ErrorViewHolder extends CommonViewHolder{

        private final TextView mTryAgainButton;
        private final TextView mErrorMessageText;

        ErrorViewHolder(View itemView) {
            super(itemView);
            mTryAgainButton = itemView.findViewById(R.id.try_again);
            mErrorMessageText = itemView.findViewById(R.id.error_message);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) {
            ErrorItem errorItem = (ErrorItem) item;
            mErrorMessageText.setText(errorItem.getMessage());
            mTryAgainButton.setOnClickListener(v -> click.onClick(item));
        }

    }

    public static class UserViewHolder extends CommonViewHolder{

        private final SquareImageView mImageView;
        private final TextView mUserLogin;
        private final CardView mContent;

        UserViewHolder(View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.user_content);
            mImageView = itemView.findViewById(R.id.user_image);
            mUserLogin = itemView.findViewById(R.id.user_login);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) {
            UserItem userItem = (UserItem) item;
            Context context = itemView.getContext();
            mUserLogin.setText(userItem.getUser().login);
            Picasso.with(context)
                    .load(userItem.getUser().avatarUrl)
                    .transform(new CircularTransformation())
                    .into(mImageView);
            mContent.setOnClickListener(v -> click.onClick(item));
        }

    }

    public static class RepositoryViewHolder extends CommonViewHolder{

        private final TextView mName;
        private final TextView mLanguage;
        private final ImageView mImage;
        private final TextView mCreated;

        RepositoryViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.repository_name);
            mLanguage = itemView.findViewById(R.id.language_name);
            mImage = itemView.findViewById(R.id.language);
            mCreated = itemView.findViewById(R.id.created_date);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) {
            RepositoryItem repositoryItem = (RepositoryItem) item;
            mName.setText(repositoryItem.getRepository().name);
            mCreated.setText(StringUtil.getDateText(repositoryItem.getRepository().date()));
            mLanguage.setText(repositoryItem.getRepository().language);
            if (StringUtil.isNullOrEmpty(repositoryItem.getRepository().language)){
                mImage.setVisibility(View.GONE);
            } else {
                int color = App.INJECTOR.colorUtil.getColor(repositoryItem.getRepository().language);
                mImage.setImageTintList(ColorStateList.valueOf(color));
                mImage.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(v -> click.onClick(item));
        }

    }

    public static class DocumentViewHolder extends CommonViewHolder{

        private final ImageView mIcon;
        private final TextView mDocName;
        private final TextView mDocSize;

        DocumentViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.document_icon);
            mDocName = itemView.findViewById(R.id.document_name);
            mDocSize = itemView.findViewById(R.id.document_size);
        }

        @Override
        void bind(CommonItem item, CommonItem.ItemClick click) {
            DocumentItem documentItem = (DocumentItem) item;
            if (documentItem.getDocument().type == DocumentType.FILE){
                mIcon.setImageResource(R.drawable.ic_file);
                mDocSize.setText(StringUtil.getSizeString(documentItem.getDocument().size));
            } else {
                mIcon.setImageResource(R.drawable.ic_folder);
                mDocSize.setText("");
            }
            mDocName.setText(documentItem.getDocument().name);
            itemView.setOnClickListener(v -> click.onClick(item));
        }

    }
}
