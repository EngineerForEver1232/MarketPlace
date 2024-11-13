package com.pedpo.pedporent.listener

import com.pedpo.pedporent.databinding.AdapterBookmarkBinding
import com.pedpo.pedporent.model.bookmark.BookmarkTO

interface ClickAdapterBookmark {

    fun onClickAdapterPaging(bookmark: BookmarkTO?,action:String?,binding: AdapterBookmarkBinding?,position:Int?)

//    fun onClickTest();
}