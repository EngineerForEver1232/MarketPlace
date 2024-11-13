package com.pedpo.pedporent.model.market.category


open class CategoryTO {

    var categoryMarketID:String?="";
    var appIconeAddress:String?=null;
    var categoryMarketName:String?=null;
    var englishCategoryMarketName:String?=null;

    var parentID:String?=null;
    var type:String?=null;
    var isHome:Boolean?=false;
    var isCar:Boolean?=false;
    var isAll:Boolean?=false;
//    @BindingAdapter("android:showImage")
//    fun setImage(imageView: ImageView, url: String) {
//        Picasso.get().load(url).into(imageView);
//    }



}