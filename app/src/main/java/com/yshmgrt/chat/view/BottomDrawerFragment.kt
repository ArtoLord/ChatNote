package com.yshmgrt.chat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yshmgrt.chat.R

class BottomDrawerFragment(val onCreated:(View)->Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_drawer_fragment, container, false)
        onCreated(view)
        return view
    }
}