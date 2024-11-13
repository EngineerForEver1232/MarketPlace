package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.pedpo.pedporent.databinding.DialogShowSingleImageBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.ConvertImage
import com.pedpo.pedporent.utills.RotationPhotoUtills
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors

@AndroidEntryPoint
class DialogFragmentShowImage : DialogFragment() {

    private var returnContent: ReturnContent? = null;
    val executor = Executors.newSingleThreadExecutor();
    val handler = Handler(Looper.getMainLooper());

    fun setReturn(returnContent: ReturnContent) {
        this.returnContent = returnContent;
    }

    private lateinit var binding: DialogShowSingleImageBinding;


    fun newInstance(uri: Uri?, url: String?): DialogFragmentShowImage {
        val fragment = DialogFragmentShowImage()
        val bundle = Bundle()

        bundle.putParcelable(ContextApp.URI, uri)
        bundle.putString(ContextApp.URL, url)
        fragment.arguments = bundle;

        return fragment;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogShowSingleImageBinding.inflate(inflater, container, false)
        binding.listener = this;
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        if (arguments?.getParcelable<Uri>(ContextApp.URI) == null)
            if (!arguments?.getString(ContextApp.URL).isNullOrEmpty())
                Picasso.get().load(arguments?.getString(ContextApp.URL)).into(binding.imgPermission)
            else
                convertPhotoPermission(arguments?.getParcelable<Uri>(ContextApp.URI))

    }

    fun icClose(view: View) {
        dismiss()
    }


    private fun convertPhotoPermission(uri: Uri?) {
        if (uri != null)
            executor.execute {
                val bitmaptest = RotationPhotoUtills.handleSamplingAndRotationBitmap(context, uri);
//            val base64 = ConvertImage.encodeTobase64(bitmaptest);
//            photosBase64.add(PhotoMarketsTO(counter.toString(), base64))

                handler.post {
                    binding.imgPermission.setImageBitmap(bitmaptest)
                    //                binding.imgMadrak.setImageBitmap(bitmaptest)
                }
            }
    }

}