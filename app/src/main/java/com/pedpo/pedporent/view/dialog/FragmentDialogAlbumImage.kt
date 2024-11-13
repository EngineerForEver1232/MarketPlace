package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.databinding.DialogShowImageBinding
import com.pedpo.pedporent.model.details.PhotoDetailTO
import com.pedpo.pedporent.view.adapter.ShowImageAdapter

class FragmentDialogAlbumImage : DialogFragment() {

    lateinit var binding: DialogShowImageBinding;
    private var albumAdapter: ShowImageAdapter? = null;
    private var list: ArrayList<PhotoDetailTO>?=null;
    var positionViewPager = 0;


    fun newInstance(position: Int,list: ArrayList<PhotoDetailTO>?): FragmentDialogAlbumImage {
        var fragmentDialogAlbumImage = FragmentDialogAlbumImage()
        var bundle = Bundle();
        bundle.putInt("position", position)
        bundle.putParcelableArrayList("list",list)
        fragmentDialogAlbumImage.arguments = bundle

        return fragmentDialogAlbumImage;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = arguments?.getParcelableArrayList("list");
        positionViewPager = arguments?.getInt("position")?:0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogShowImageBinding.inflate(inflater, container, false)
        binding.component = this;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumAdapter = ShowImageAdapter(requireContext());
        binding.viewPager.adapter = albumAdapter
        binding.dotIndicator.setViewPager2(binding.viewPager)

        albumAdapter?.updateAdapter(list!!)
        binding.viewPager.setCurrentItem(positionViewPager, false)

    }

    /*OnClick*/
    fun onClickIcClose(view: View) {
        dismiss()
    }

}