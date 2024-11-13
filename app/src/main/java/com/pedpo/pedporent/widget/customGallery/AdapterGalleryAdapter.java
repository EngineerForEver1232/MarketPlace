package com.pedpo.pedporent.widget.customGallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.pedpo.pedporent.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

public class AdapterGalleryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<CustomGalleryTO> data = new ArrayList<CustomGalleryTO>();
    private ImageLoader imageLoader;
    private int index = 0;
    private boolean isActionMultiplePick;
    private ArrayList<CustomGalleryTO> arrayListNumberPhoto = new ArrayList<>();
//    private ArrayList<ImageView> arrayListPhotoSelectd = new ArrayList<>();
    private OnItemClickListenerGallery onItemClickListenerGallery ;


    public AdapterGalleryAdapter(Context c, ImageLoader imageLoader, int countImage) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
        this.imageLoader = imageLoader;
        index = countImage;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CustomGalleryTO getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void selectAll(boolean selection) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSeleted(selection);
        }

        notifyDataSetChanged();
    }

    public ArrayList<CustomGalleryTO> getSelected() {
        ArrayList<CustomGalleryTO> dataList = new ArrayList<CustomGalleryTO>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isSeleted()) {
                dataList.add(data.get(i));
            }
        }

        return dataList;
    }

    public int getSize() {
        return getSelected().size();
    }


    public void addAll(ArrayList<CustomGalleryTO> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }


    public void changeSelection(View v, int position, int countImage) {

        if (!data.get(position).isSeleted() && (getSize() + countImage) == 5) {
//            Toast.makeText(mContext, "only 5", Toast.LENGTH_SHORT).show();
            return;
        }
        //dar inja isSelected meghdar midahim va dar payin az an dar imgMarkMultiSelected estefade mikonim
        if (data.get(position).isSeleted()) {
            --index;
            data.get(position).setSeleted(false);
            arrayListNumberPhoto.remove(data.get(position));
//            data.get(position).setIndex((index));
        } else {
            ++index;
            data.get(position).setSeleted(true);
            arrayListNumberPhoto.add(data.get(position));
//            data.get(position).setIndex((index));
        }

//        getSelectedPhoto();

        ((ViewHolder) v.getTag()).imgMarkMultiSelected.setSelected(data
                .get(position).isSeleted());
    }

    private ImageView imgMarkMultiSelected_OLD;
    private CustomGalleryTO customGalleryTO ;

    public void singleChoose(View v, int position){

        for (int i = 0 ; i < data.size() ; i++){
            data.get(i).setSeleted(false);
        }
//        for (int i = 0 ; i < arrayListPhotoSelectd.size() ; i++){
//            arrayListPhotoSelectd.get(i).setSelected(false);
//        }

        if (imgMarkMultiSelected_OLD !=null)
            imgMarkMultiSelected_OLD.setSelected(false);
        imgMarkMultiSelected_OLD = ((ViewHolder) v.getTag()).imgMarkMultiSelected;

        data.get(position).setSeleted(true);
        customGalleryTO =data.get(position);

        ((ViewHolder) v.getTag()).imgMarkMultiSelected.setSelected(data
                .get(position).isSeleted());
    }


    public CustomGalleryTO getSinglePhoto(){
        return customGalleryTO;
    }

    public ArrayList<CustomGalleryTO> getSelectedPhoto() {
        int number = 0;
        for (int i = 0; i < arrayListNumberPhoto.size(); i++) {
            arrayListNumberPhoto.get(i).setIndex(++number);
        }
        return arrayListNumberPhoto;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = infalter.inflate(R.layout.item_custom_gallery, null);
            viewHolder.imgs = (ImageView) convertView
                    .findViewById(R.id.imgQueue);
            viewHolder.imgMarkMultiSelected = (ImageView) convertView
                    .findViewById(R.id.imgQueueMultiSelected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        onBindViewHolder(convertView,viewHolder,position);

//        arrayListPhotoSelectd.add(viewHolder.imgMarkMultiSelected);

        return convertView;
    }
    public class ViewHolder {
        ImageView imgs;
        ImageView imgMarkMultiSelected;
    }

    public void onBindViewHolder(View view ,final ViewHolder viewHolder,final int position)
    {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListenerGallery!=null)
                    onItemClickListenerGallery.onItemClickListener(view,position);
            }
        });
        viewHolder.imgs.setTag(position);
        try {

            imageLoader.displayImage("file://" + data.get(position).getSdcardPath() ,
                    viewHolder.imgs, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            viewHolder.imgs
                                    .setImageResource(R.drawable.no_media);
                            super.onLoadingStarted(imageUri, view);
                        }
                    });

            viewHolder.imgMarkMultiSelected
                    .setSelected(data.get(position).isSeleted());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListenerGallery(OnItemClickListenerGallery onItemClickListenerGallery)
    {
        this.onItemClickListenerGallery = onItemClickListenerGallery;
    }

    public void clearCache() {
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
