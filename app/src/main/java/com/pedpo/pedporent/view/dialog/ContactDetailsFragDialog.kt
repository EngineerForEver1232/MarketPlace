package com.pedpo.pedporent.view.dialog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.MessageFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogDetailsContactFragBinding
import com.pedpo.pedporent.model.ResponseTORenter
import com.pedpo.pedporent.utills.ContextApp
import com.squareup.picasso.Picasso
import androidx.core.view.isVisible


class ContactDetailsFragDialog : DialogFragment() {

    var binding: DialogDetailsContactFragBinding? = null;


    fun newInstance(renterTO: ResponseTORenter?, isUser: Boolean?): ContactDetailsFragDialog {
        var contactDetailsFragDialog = ContactDetailsFragDialog();
        var bundle = Bundle();
        bundle.putString(ContextApp.PHONE_NUMBER, renterTO?.phoneNumber);
        bundle.putString(ContextApp.URI_IMAGE, renterTO?.image);
        bundle.putString(ContextApp.NAME, renterTO?.name);
        bundle.putString(ContextApp.MAIL, renterTO?.email);
        bundle.putBoolean(ContextApp.IS_USER, isUser ?: false);
        contactDetailsFragDialog.arguments = bundle;

        return contactDetailsFragDialog;
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
        binding = DialogDetailsContactFragBinding.inflate(inflater)
        binding?.listener = this;
        return binding?.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_transparent);
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
//        dialog?.window?.setBackgroundDrawable( ColorDrawable(Color.RED));



        if (arguments?.getBoolean(ContextApp.IS_USER) == false) {
            binding?.layoutData?.visibility = View.INVISIBLE;
            binding?.tResult?.visibility = View.VISIBLE;
        } else {
            binding?.tName?.text = arguments?.getString(ContextApp.NAME);
            binding?.tMail?.text = arguments?.getString(ContextApp.MAIL);
            binding?.tCall?.text = arguments?.getString(ContextApp.PHONE_NUMBER);
            binding?.tMessage?.text = arguments?.getString(ContextApp.PHONE_NUMBER);
//            binding?.tMail?.text = arguments?.getCharSequence(dialog.toString());

            binding?.lauoutCall?.isVisible = !arguments?.getString(ContextApp.PHONE_NUMBER).isNullOrEmpty() ;
            binding?.lauoutMessage?.isVisible = !arguments?.getString(ContextApp.PHONE_NUMBER).isNullOrEmpty() ;
            binding?.lauoutMail?.isVisible = !arguments?.getString(ContextApp.MAIL).isNullOrEmpty() ;

            binding?.line1?.isVisible = !arguments?.getString(ContextApp.PHONE_NUMBER).isNullOrEmpty() ;
//            binding?.line2?.isVisible = !arguments?.getString(ContextApp.PHONE_NUMBER).isNullOrEmpty() ;
            binding?.line2?.isVisible = !arguments?.getString(ContextApp.MAIL).isNullOrEmpty() ;


            Picasso.get().load(arguments?.getString(ContextApp.URI_IMAGE))
                .placeholder(R.drawable.ic_profile).into(binding?.icProfile)

        }


        binding?.lauoutCall?.setOnClickListener {
            val number = Uri.parse("tel:${arguments?.getString(ContextApp.PHONE_NUMBER) ?: ""}")
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(callIntent)
        }
        binding?.lauoutMessage?.setOnClickListener {

            val uri: Uri = Uri.parse("smsto:${arguments?.getString(ContextApp.PHONE_NUMBER) ?: ""}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "THE SMS BODY")
            startActivity(intent)
        }
        binding?.lauoutMail?.setOnClickListener {

            val i = Intent(Intent.ACTION_SENDTO)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
            i.putExtra(Intent.EXTRA_TEXT, "body of email")
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    /* OnClick Close */
    fun onClickClose(view: View) {
        dismiss()
    }

}