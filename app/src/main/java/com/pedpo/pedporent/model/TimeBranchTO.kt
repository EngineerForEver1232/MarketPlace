package com.pedpo.pedporent.model

import com.google.gson.annotations.SerializedName

 class TimeBranchTO {

     var day: String? = null;
     @SerializedName("on")
     var enable: Boolean? = false;

     @SerializedName("startTime1")
     var startTime1: String? = "";

     @SerializedName("endTime1")
     var endTime1: String? = "";

     @SerializedName("startTime2")
     var startTime2: String? = null;

     @SerializedName("endTime2")
     var endTime2: String? = null;

       private var onClickShowShift1: Boolean? = false;
      private var onClickShowShift2: Boolean? = false;


     constructor()
     constructor(day: String?) {
         this.day = day
     }


     fun getShowShift1():Boolean{
         return  !(startTime1.isNullOrEmpty() && endTime1.isNullOrEmpty())
     }

     fun setOnClickShowShift1(onClickShowShift1:Boolean?){
         this.onClickShowShift1 = onClickShowShift1 ;
     }

     fun getOnClickShowShift1():Boolean?{
//         onClickShowShift1 = !(startTime1.isNullOrEmpty() && endTime1.isNullOrEmpty())
         return onClickShowShift1 ;
     }

     fun setOnClickShowShift2(onClickShowShift2:Boolean){
         this.onClickShowShift2 = onClickShowShift2;
     }

     fun getOnClickShowShift2():Boolean?{
         onClickShowShift2 = !(startTime2.isNullOrEmpty() && endTime2.isNullOrEmpty())
         return onClickShowShift2;
     }

 }