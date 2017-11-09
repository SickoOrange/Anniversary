package com.berber.orange.memories.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berber.orange.memories.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


class AnniversaryTypeRecyclerViewAdapter extends RecyclerView.Adapter<AnniversaryTypeRecyclerViewAdapter.ItemsViewHolder> {
    private int mPageSize;
    private int mIndex;
    private Context mContext;
    private List<ModelAnniversaryTypeDTO> mDatas;
    private final LayoutInflater inflater;
    private ModelAnniversaryTypeDTO currentSelectedAnniversaryType;

    AnniversaryTypeRecyclerViewAdapter(Context context, ArrayList<ModelAnniversaryTypeDTO> modelAnniversaryTypes, int i, int homeItemSize) {

        this.mContext = context;
        this.mDatas = modelAnniversaryTypes;
        this.mIndex = i;
        this.mPageSize = homeItemSize;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemsViewHolder(inflater.inflate(R.layout.item_anniversary_type, null));
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        final int pos = position + mIndex * mPageSize;
        System.out.println(mDatas.get(pos).toString());
        holder.anniversaryTypesImageResource.setImageResource(mDatas.get(pos).getImageResource());
        holder.anniversaryTypesText.setText(mDatas.get(pos).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedAnniversaryType = mDatas.get(pos);
                CircleImageView anniversaryTypeImageView = ((AddItemActivity) mContext).getAnniversaryTypeImageView();
                anniversaryTypeImageView.setImageResource(currentSelectedAnniversaryType.getImageResource());
                TextView anniversaryTypeTextView = ((AddItemActivity) mContext).getAnniversaryTypeTextView();
                anniversaryTypeTextView.setText(currentSelectedAnniversaryType.getName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    public ModelAnniversaryTypeDTO getCurrentSelectedAnniversaryType() {
        return currentSelectedAnniversaryType;
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {

        TextView anniversaryTypesText;
        ImageView anniversaryTypesImageResource;

        ItemsViewHolder(View itemView) {
            super(itemView);
            anniversaryTypesText = itemView.findViewById(R.id.anniversary_title);
            anniversaryTypesImageResource = itemView.findViewById(R.id.anniversary_image);
        }
    }
}
