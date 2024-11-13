package com.pedpo.pedporent.view.dialog

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogShiftTimeBinding
import com.pedpo.pedporent.utills.ContextApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DialogAddTimeShiftFragment : DialogFragment() , OnClickListener {

    private lateinit var binding : DialogShiftTimeBinding ;

    private var typeClick:Int?=null;

    fun newInstance(day: Int,shift:String):DialogAddTimeShiftFragment{
        val dialog = DialogAddTimeShiftFragment();
        val bundle = Bundle()
        bundle.putInt(ContextApp.DAY , day)
        bundle.putString(ContextApp.SHIFT , shift)
        dialog.arguments = bundle;
        return dialog;
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
        binding = DialogShiftTimeBinding.inflate(inflater, container, false)
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        binding.tStartTime1.setOnClickListener(this)
        binding.tEndTime1.setOnClickListener(this)

        binding.tSave.setOnClickListener {
            onTimeStore?.onTimeStore(
                arguments?.getInt(ContextApp.DAY),
                arguments?.getString(ContextApp.SHIFT),
                binding.tStartTime1.text.toString(),
                binding.tEndTime1.text.toString()
            )
            dismiss()
        }

    }

    // listener which is triggered when the
    // time is picked from the time picker dialog
    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            // logic to properly handle
            // the picked timings by user
            val formattedTime: String = when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "${hourOfDay + 12}:0${minute} am"
                    } else {
                        "${hourOfDay + 12}:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            }

            when(typeClick){
                R.id.tStartTime1 ->
                    binding.tStartTime1.text = formattedTime
                R.id.tEndTime1 ->
                    binding.tEndTime1.text = formattedTime
            }

            //                previewSelectedTimeTextView.text = formattedTime
        }

    override fun onClick(view: View?) {
        typeClick= view?.id
        val timePicker = TimePickerDialog(
            context,
            timePickerDialogListener,
            8,
            10,
            false
        )
        timePicker.show()
    }


    interface OnTimeStore{
        fun onTimeStore(day:Int?,shift: String?,startTime1 : String? , endTime1 :String? )
    }

    private var onTimeStore:OnTimeStore?=null

    fun setOnTimeStore(onTimeStore: OnTimeStore){
        this.onTimeStore = onTimeStore;
    }

}