package com.pedpo.pedporent.widget.customGallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.pedpo.pedporent.R;
import com.pedpo.pedporent.utills.ContextApp;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class ActivityCustomGallery extends AppCompatActivity implements View.OnClickListener, OnItemClickListenerGallery {

    private GridView gridGallery;
    private Handler handler;
    private AdapterGalleryAdapter adapter;
    private ImageView imgNoMedia;
    private ImageLoader imageLoader;
    private MaterialButton btnApply;
    private Toolbar toolbar;
    private MaterialTextView tCountImage;
    private int countImage;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gallery);
        setToolbar();
        countImage = getIntent().getIntExtra(ContextApp.COUNT_IMAGE, 0);
        type = getIntent().getStringExtra(ContextApp.TYPE);
        initImageLoader();
        init();



        tCountImage.setText(String.valueOf(countImage));


    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void init() {

        tCountImage = findViewById(R.id.tCountImage);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);
        imgNoMedia =  findViewById(R.id.imgNoMedia);
        handler = new Handler();
        gridGallery =  findViewById(R.id.gridGallery);
//        gridGallery.setFastScrollEnabled(true);
        PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
                true, true);
        gridGallery.setOnScrollListener(listener);
        adapter = new AdapterGalleryAdapter(getApplicationContext(), imageLoader, countImage);
        adapter.setOnItemClickListenerGallery(this);


        gridGallery.setAdapter(adapter);


        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        adapter.addAll(getGalleryPhotos());
                        checkImageStatus();
                    }
                });
                Looper.loop();
            }

            ;

        }.start();

        if (type!=null && type.equals(ContextApp.SINGLE)){
            findViewById(R.id.linearCount).setVisibility(View.GONE);
        }

    }

    //aya axi vase namayesh vojod darad
    private void checkImageStatus() {
        if (adapter.isEmpty()) {
            imgNoMedia.setVisibility(View.VISIBLE);
        } else {
            imgNoMedia.setVisibility(View.GONE);
        }
    }

    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
                    CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getBaseContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {

        }
    }

//    AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//            adapter.changeSelection(v, position,countImage);
//
//            Animation animation = AnimationUtils.loadAnimation(ActivityCustomGallery.this,R.anim.scale_click);
//            tCountImage.startAnimation(animation);
//            tCountImage.setText((adapter.getSelected().size()+countImage)+"");
//
//        }
//    };

    private ArrayList<CustomGalleryTO> getGalleryPhotos() {
        ArrayList<CustomGalleryTO> galleryList = new ArrayList<CustomGalleryTO>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imagecursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    CustomGalleryTO item = new CustomGalleryTO();

                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);

                    item.setSdcardPath(imagecursor.getString(dataColumnIndex));

                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
        return galleryList;
    }

    @Override
    public void onClick(View view) {
        if (type!=null){
        if (type.equals(ContextApp.SINGLE) || type.equals(ContextApp.PERMISSION_PHOTO))
            returnSingleImage();
        else {
            returnImage();
        }}
    }

    public void returnImage() {
        ArrayList<CustomGalleryTO> selected = adapter.getSelectedPhoto();
        Intent data = new Intent().putExtra(ContextApp.MULTI, selected);
        setResult(RESULT_OK, data);
        finish();
    }
    public void returnSingleImage() {
        CustomGalleryTO selected = adapter.getSinglePhoto();
        Intent data = new Intent().putExtra(ContextApp.SINGLE, selected);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onItemClickListener(View view, int position) {

        if (type !=null ){
         if (type.equals(ContextApp.SINGLE) || type.equals(ContextApp.PERMISSION_PHOTO))
            adapter.singleChoose(view, position);
         else {
            adapter.changeSelection(view, position, countImage);

            tCountImage.startAnimation(AnimationUtils.loadAnimation(ActivityCustomGallery.this, R.anim.scale_click));
            tCountImage.setText((adapter.getSelected().size() + countImage) + "");

        }}
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

}
