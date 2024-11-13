package com.pedpo.pedporent.model.bookmark

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.myItems.MyMarkets

data class Bookmarks (
    @SerializedName("bookmarksmarkets")
    var myMarkets:List<BookmarkTO>,

    @SerializedName("bookmarksservcies")
    var myService:List<BookmarkTO>
        )