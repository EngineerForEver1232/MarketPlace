package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.market.category.Categories
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.pedpo.pedporent.room.dao.category.market.ChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.market.ParentCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceParentCategoryDAO
import com.pedpo.pedporent.room.entity.category.ChildCategory
import com.pedpo.pedporent.room.entity.category.ParentCategory
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryChild
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryParent
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.NetConnection
import kotlinx.coroutines.*
import javax.inject.Inject

class CategoryRepository {

    private var serviceAPI: ServiceAPI? = null;
    private var parentCategoryDAO: ParentCategoryDAO? = null;
    private var childCategoryDAO: ChildCategoryDAO? = null;
    private var serviceParentCategoryDAO: ServiceParentCategoryDAO? = null
    private var serviceChildCategoryDAO: ServiceChildCategoryDAO? = null

    @Inject
    constructor(
        serviceAPI: ServiceAPI,
        parentCategoryDAO: ParentCategoryDAO,
        childCategoryDAO: ChildCategoryDAO,
        serviceParentCategoryDAO: ServiceParentCategoryDAO,
        serviceChildCategoryDAO: ServiceChildCategoryDAO
    ) {
        this.serviceAPI = serviceAPI;
        this.parentCategoryDAO = parentCategoryDAO;
        this.childCategoryDAO = childCategoryDAO;
        this.serviceParentCategoryDAO = serviceParentCategoryDAO;
        this.serviceChildCategoryDAO = serviceChildCategoryDAO;
    }

    fun refreshCategory(mutable: MutableLiveData<DataWrapper<CategoryData>>): MutableLiveData<DataWrapper<CategoryData>> {

        CoroutineScope(Dispatchers.IO).launch {
            parentCategoryDAO?.deleteAll();
            childCategoryDAO?.deleteAll();
        }
        categoryMarket( mutable = mutable)

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(
                "categoryTestSql",
                "changeData exception: ${throwable.message}"
            )
        }
        if (!NetConnection.newInstance().isDisconnect(null))
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = serviceAPI?.changeData();

                withContext(Dispatchers.Main) {
                    Log.i(
                        "categoryTestSql",
                        "changeData: ${response?.isSuccessful}"
                    )
                    if (response?.isSuccessful == true) {
                        if (response.body()?.data?.isChange_MarketCategory == true ) {
                            Log.i(
                                "categoryTestSql",
                                "changeData: ${response?.body()?.data?.isChange_MarketCategory}"
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                parentCategoryDAO?.deleteAll();
                                childCategoryDAO?.deleteAll();
                            }

                            categoryMarket( mutable = mutable)
                        } else if (response.body()?.data?.isChange_ServiceCategory == true){
                            Log.i(
                                "categoryTestSql",
                                "changeData: ${response?.body()?.data?.isChange_ServiceCategory}"
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                serviceParentCategoryDAO?.deleteAll()
                                serviceChildCategoryDAO?.deleteAll()
                            }
                            categoryMarket(mutable = mutable)
                        }  else if (response.body()?.data?.isChange_ServiceCategory == true && response.body()?.data?.isChange_ServiceCategory == true){
                            Log.i(
                                "categoryTestSql",
                                "changeData: ${response?.body()?.data?.isChange_ServiceCategory}"
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                parentCategoryDAO?.deleteAll();
                                childCategoryDAO?.deleteAll();
                                serviceParentCategoryDAO?.deleteAll()
                                serviceChildCategoryDAO?.deleteAll()
                            }
                            categoryMarket(mutable = mutable)
                        } else {
                            Log.i(
                                "categoryTestSql",
                                "changeData else : false"
                            )

                        }
                    } else {
//                        categoryMarket(  mutable = mutable)
                    }
                }
            }
        else{

        }
        return mutable;
    }


    fun selectListCategory(
        isAll: Boolean
    ): MutableLiveData<DataWrapper<CategoryData>>? {
        val mutable = MutableLiveData<DataWrapper<CategoryData>>();
        refreshCategory(mutable = mutable)

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryTestSql", "selectParent error : " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = parentCategoryDAO?.select();

            Log.i(
                "categoryTestSql",
                " selectParentCategory in sql size : ${response?.size}"
            )

            withContext(Dispatchers.Main) {
                if (response?.isEmpty() == true) {
                    Log.i(
                        "categoryTestSql",
                        " select in sql : isEmpty"
                    )
//                    refreshCategory(isAll, mutable = mutable)
                    categoryMarket(mutable = mutable)
                } else {
                    val listParent = ArrayList<CategoryTO>();
                    val categoryData = CategoryData();
                    val categories = Categories();

                    for (parent in response ?: arrayListOf()) {
                        Log.i(
                            "categoryTestSql",
                            " selectParent in sql : ${parent.englishCategoryMarketName} " + parent?.isHome
                        )
                        if (isAll && parent.isAll)
                            continue

                        val categoryTO = CategoryTO();
                        categoryTO.categoryMarketID = parent.categoryMarketID;
                        categoryTO.appIconeAddress = parent.appIconeAddress;
                        categoryTO.categoryMarketName = parent.categoryMarketName;
                        categoryTO.englishCategoryMarketName = parent.englishCategoryMarketName;
                        categoryTO.isAll = parent.isAll;
                        categoryTO.isHome = parent.isHome;
                        categoryTO.isCar = parent.isCar;
                        listParent.add(categoryTO)
                    }
                    categories.categoryMarket = listParent;
                    categories.type = ContextApp.CATEGORY;
                    categoryData.data = categories;
                    categoryData.isSuccess = true;

                    Log.i(
                        "categoryTestSql",
                        " selectParent in sql : ${listParent?.size} ${categoryData.data?.categoryMarket?.size}"
                    )

                    mutable.value = DataWrapper(null, categoryData);
                }
            }

        }

        return mutable;
    }


    fun categoryMarket(mutable: MutableLiveData<DataWrapper<CategoryData>>) {
        //for serviceCategory
//        getAllCategoriesService();


        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryTestSql", "exception server : " + throwable.localizedMessage)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.categorys();

            withContext(Dispatchers.Main) {
                Log.i("categoryTestSql", " response : " + response?.isSuccessful)
//                parentCategoryDAO?.insert()

                if (response?.isSuccessful == true) {

                    if (response.body()?.isSuccess == true) {
                        ///// vase intionlize category market
                        val listCategoryTO = ArrayList<CategoryTO>();
                        val listParent = ArrayList<ParentCategory>();
                        val listChild = ArrayList<ChildCategory>();
                        Log.i(
                            "categoryTestSql",
                            " category market in sql size : ${response.body()?.data?.categoryMarket?.size}"
                        )
                        for (category in response.body()?.data?.categoryMarket ?: arrayListOf()) {
                            Log.i(
                                "categoryType", " response market :" +
                                        " ${category.englishCategoryMarketName} ${category.isHome} ${category.type}"
                            )

                            if (category.type != null && category.type.equals("Parent")) {



                                val parent = ParentCategory(
                                    category.categoryMarketID?:"",
                                    category.categoryMarketName ?: "",
                                    category.englishCategoryMarketName ?: "",
                                    category.appIconeAddress ?: "",
                                    category.isHome ?: false,
                                    category.isCar ?: false,
                                    category.isAll ?: false

                                );
                                listParent.add(parent)
                                listCategoryTO.add(category)
                            } else {
                                val child = ChildCategory(
                                    category.categoryMarketID ?: "",
                                    category.categoryMarketName ?: "",
                                    category.englishCategoryMarketName ?: "",
                                    category.parentID ?: "",
                                )
                                listChild.add(child)
                            }
                        }
                        insertParent(listParent)
                        insertChild(listChild)

                        response.body()?.data?.type = ContextApp.CATEGORY;
                        response.body()?.data?.categoryMarket = listCategoryTO

                        ///////// vase initialize category service

                        val listServiceParentG = ArrayList<ServiceCategoryParent>();
                        val listServiceChildG = ArrayList<ServiceCategoryChild>();

                        Log.i(
                            "categoryTestSql",
                            " category service in sql size : ${response.body()?.data?.categoryService?.size}"
                        )

                        for (category in response.body()?.data?.categoryService ?: arrayListOf()) {
                            Log.i(
                                "categoryService", " response service:" +
                                        " ${category.englishCategoryMarketName} ${category.type}"
                            )

                            if (category.type != null && category.type.equals("Parent")) {
                                val parent = ServiceCategoryParent(
                                    category.categoryMarketID!!,
                                    category.categoryMarketName ?: "",
                                    category.englishCategoryMarketName ?: "",
                                    category.appIconeAddress ?: "",
                                    category?.isAll ?: false
                                );
                                listServiceParentG.add(parent)
                            } else {
                                val child = ServiceCategoryChild(
                                    category.categoryMarketID ?: "",
                                    category.categoryMarketName ?: "",
                                    category.englishCategoryMarketName ?: "",
                                    category.parentID ?: "",
                                )
                                listServiceChildG.add(child)
                            }
                        }

                        insertServiceParentCategory(listServiceParentG)
                        insertServiceChildCategory(listServiceChildG)
                        ///////////


//                        val res = response.body()
//                        val catMarketList = arrayListOf<CategoryTO>()
//                        for (category in response.body()?.data?.categoryMarket ?: arrayListOf()) {
//                            if (category.isAll == true)
//                                continue
//                                catMarketList.add(category)
//                        }
//
//                        res?.data?.categoryMarket = catMarketList
//
//                        mutable.value = DataWrapper(null, res)
                        mutable.value = DataWrapper(null, response.body());
                    }
                } else {
                    mutable.value = DataWrapper(Exception("error"), null);
                }
            }
        }
    }

    fun insertParent(list: List<ParentCategory>) {

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryTestSql", "exception category market insert : " + throwable.message)
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = parentCategoryDAO?.insert(list)

            withContext(Dispatchers.Main) {
                Log.i("categoryTestSql", "repository category market insert : " + response)
            }
        }

    }

    fun insertChild(list: List<ChildCategory>) {

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryTestSql", "exception child insert : " + throwable.message)
        }

        CoroutineScope(Dispatchers.IO).launch {
            var response = childCategoryDAO?.insert(list)

            withContext(Dispatchers.Main + exceptionHandler) {
                Log.i("categoryTestSql", "repository child category sql insert : $response" )
            }
        }
    }

    fun selectChild(parentID: String?): MutableLiveData<DataWrapper<CategoryData>>? {

        var mutable = MutableLiveData<DataWrapper<CategoryData>>();


        var exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("categoryTestSql", "onSuccess: " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = childCategoryDAO?.selectChild(parentID = parentID)

            withContext(Dispatchers.Main) {
                var listParent = ArrayList<CategoryTO>();
                var categoryData = CategoryData();
                var categories = Categories();
//                Log.i("categoryTestSql", "onSuccess: " + response)
                for (child in response ?: arrayListOf()) {
                    var categoryTO = CategoryTO();
                    categoryTO.categoryMarketID = child.categoryMarketID;
                    categoryTO.englishCategoryMarketName = child.englishCategoryMarketName;
                    categoryTO.categoryMarketName = child.categoryMarketName;
                    categoryTO.parentID = child.parentID;
                    listParent.add(categoryTO)
                }
                categories?.categoryMarket = listParent
                categories?.type = ContextApp.SUB_CATEGORY;

                categoryData?.data = categories;

                mutable.value = DataWrapper(null, categoryData);
            }
        }
        return mutable;
    }


    /// Category Service

    fun insertServiceParentCategory(list: List<ServiceCategoryParent>?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("categoryTestSql", "insertCategorySerice: ${throwable.message}")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceParentCategoryDAO?.insert(list ?: arrayListOf())

            withContext(Dispatchers.Main) {
                Log.i("categoryTestSql", "insert category service sql : $response")
            }

        }
    }

    fun insertServiceChildCategory(list: List<ServiceCategoryChild>?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("categoryTestSql", "insertCategorySerice: ${throwable.message}")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var response = serviceChildCategoryDAO?.insert(list ?: arrayListOf())

            withContext(Dispatchers.Main) {
                Log.i("categoryTestSql", "insert child category sql : $response")
            }

        }
    }

    fun selectServiceParent(isAll: Boolean): MutableLiveData<DataWrapper<CategoryData>>? {

        val mutable = MutableLiveData<DataWrapper<CategoryData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("categoryService", "selectParent error : " + throwable.message)
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceParentCategoryDAO?.select()

            withContext(Dispatchers.Main) {
                if (response?.isEmpty() == true) {
//                    categoryMarket(mutable = mutable)
                } else {
                    val listParent = ArrayList<CategoryTO>();
                    val categoryData = CategoryData();
                    val categories = Categories();

                    for (parent in response ?: arrayListOf()) {
                        Log.i(
                            "categoryService",
                            "parent categoryService in sql : ${parent.englishCategoryMarketName} "
                        )
                        if (isAll && parent.isAll)
                            continue

                        val categoryTO = CategoryTO();
                        categoryTO.categoryMarketID = parent.categoryMarketID;
                        categoryTO.appIconeAddress = parent.appIconeAddress;
                        categoryTO.categoryMarketName = parent.categoryMarketName;
                        categoryTO.englishCategoryMarketName = parent.englishCategoryMarketName;
                        categoryTO.isAll = parent.isAll;

                        listParent.add(categoryTO)
                    }
                    categories.categoryService = listParent;
                    categories?.type = ContextApp.SERVICE;
                    categoryData.data = categories;
                    mutable.value = DataWrapper(null, categoryData);
                }
            }

        }
        return mutable;
    }


    fun selectServiceChild(parentID: String?): MutableLiveData<DataWrapper<CategoryData>> {

        var mutable = MutableLiveData<DataWrapper<CategoryData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceChildCategoryDAO?.selectChild(parentID = parentID)

            withContext(Dispatchers.Main) {
                if (response?.isEmpty() == true) {
                    mutable.value = DataWrapper(null, null);

                } else {
                    var listChild = ArrayList<CategoryTO>();
                    var categoryData = CategoryData();
                    var categories = Categories();

                    for (child in response ?: arrayListOf()) {
                        Log.i(
                            "categoryService",
                            "parent categoryService in sql : ${child.englishCategoryMarketName} "
                        )

                        var categoryTO = CategoryTO();
                        categoryTO.categoryMarketID = child.categoryMarketID;
                        categoryTO.categoryMarketName = child.categoryMarketName;
                        categoryTO.englishCategoryMarketName = child.englishCategoryMarketName;

                        listChild.add(categoryTO)
                    }
                    categories.categoryService = listChild;
                    categories?.type = ContextApp.SERVICE_CHILD_CATEGORY;
                    categoryData.data = categories;
                    mutable.value = DataWrapper(null, categoryData);
                }
            }


        }


        return mutable
    }


}