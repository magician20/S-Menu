package com.example.magician.resturantm.ui.main;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.magician.resturantm.R;
import com.example.magician.resturantm.data.database.ItemEntry;

import java.util.List;

/**
 * Created by Magician on 1/30/2018.
 * adapter for main activity
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {

    private final Context mContext;
    private List<ItemEntry> mMenuItems;


    public MenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MenuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.cart_list_item;  //
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new MenuAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapterViewHolder viewHolder, int position) {
        ItemEntry item = mMenuItems.get(position);
        /* set text name */
        viewHolder.mNameView.setText(item.getName());
        /* set text description */
        viewHolder.mDescriptionView.setText(item.getDescription());
        //set text description
        String s = "$" + String.valueOf(item.getPrice());
        viewHolder.mPriceView.setText(s);

        /* image food */
        //TODO:here get the image by glide library
        Glide.with(mContext).load(item.getThumbnail()).into(viewHolder.mImageView);
        //error here after add volley becuase of glide version wasn't sutable with volley
        

    }

    @Override
    public int getItemCount() {
        if (mMenuItems == null) return 0;
        return mMenuItems.size();
    }


    /**
     * Swaps the list used by the MenuAdapter for its Menu data. This method is called by
     * {@link MainActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newMenuItems the new list of Menu to use as MenuAdapter's data source
     */
    void swapForecast(final List<ItemEntry> newMenuItems) {
        if (mMenuItems == null) {
            mMenuItems = newMenuItems;
            notifyDataSetChanged();
        } else {
            /*
              Otherwise we use DiffUtil to calculate the changes and update accordingly. This
              shows the four methods you need to override to return a DiffUtil callback. The
              old list is the current list stored in mMenuItems, where the new list is the new
               values passed in from the observing the database.
              **/
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mMenuItems.size();
                }

                @Override
                public int getNewListSize() {
                    return newMenuItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mMenuItems.get(oldItemPosition).getId() == newMenuItems.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ItemEntry oldItem = mMenuItems.get(oldItemPosition);
                    ItemEntry newItem = newMenuItems.get(newItemPosition);
                    return oldItem.getId() == newItem.getId()
                            && oldItem.getName().equalsIgnoreCase(newItem.getName())
                            && oldItem.getDescription().equalsIgnoreCase(newItem.getDescription())
                            && oldItem.getPrice() == newItem.getPrice()
                            && oldItem.getThumbnail().equals(newItem.getThumbnail());//error start here no image stored(null)
                }
            });
            mMenuItems = newMenuItems;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    //rebuild this
    /* TODO:1 remove data  */
    public void removeItem(int position) {
        mMenuItems.remove(position);
        notifyItemRemoved(position);
    }

    /*TODO:2 restor data  */
    public void restorItem(int position, ItemEntry item) {
        mMenuItems.add(position, item);
        notifyItemInserted(position);
    }


    class MenuAdapterViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;
        final TextView mNameView;
        final TextView mDescriptionView;
        final TextView mPriceView;
        final ConstraintLayout mBackgroundView;
        final ConstraintLayout mForegroundView;

        public MenuAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageFood);
            mNameView = itemView.findViewById(R.id.textName);
            mDescriptionView = itemView.findViewById(R.id.textDescription);
            mPriceView = itemView.findViewById(R.id.textPrice);
            //this is used with item touch helper
            mBackgroundView = itemView.findViewById(R.id.view_background);
            mForegroundView = itemView.findViewById(R.id.view_foreground);

        }
    }
}
