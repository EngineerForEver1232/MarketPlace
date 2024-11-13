package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogFragAssignmentBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.view.adapter.AssignmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AssignmentFragDialog : DialogFragment() , ReturnContent{

    private lateinit var binding: DialogFragAssignmentBinding;
    @Inject
    lateinit var assigmentAdapter: AssignmentAdapter;
     var returnContent:ReturnContent?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragAssignmentBinding.inflate(layoutInflater)
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val recycler = binding.recycler;
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recycler.adapter = assigmentAdapter
        val dividerItemDecoration = DividerItemDecoration(context , LinearLayoutManager.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)
        assigmentAdapter.updateAdapter(resources.getStringArray(R.array.assignment))
        assigmentAdapter.returnContent = this;


    }


    override fun returnContent(content: String?) {
        returnContent?.returnContent(content = content)
        dismiss()
    }


}