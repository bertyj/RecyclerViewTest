
package com.conti.jing.recyclerviewtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private final List<CardItem> mCardList;
    private final Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(List<CardItem> cardList, Context context) {
        super();
        this.mCardList = cardList;
        this.mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public ImageView mThumbnailImageView;

        public RecyclerViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.text_title);
            mThumbnailImageView = (ImageView) view.findViewById(R.id.image_thumbnail);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mCardList != null) {
            return mCardList.size();
        } else {
            return 0;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        // get element from your dataset at this position
        CardItem cardItem = mCardList.get(position);

        // replace the contents of the view with that element
        recyclerViewHolder.mTitleTextView.setText(Html.fromHtml(cardItem.getTitle()));
        Picasso.with(mContext).load(cardItem.getThumbnail()).error(R.drawable.image_placeholder)
                .placeholder(R.drawable.image_placeholder).into(recyclerViewHolder.mThumbnailImageView);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // create a new view
        View view = layoutInflater.inflate(R.layout.layout_card_item, null);

        // set the view's size, margins, paddings and layout parameters
        // ...

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }
}
