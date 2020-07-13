package com.tapisdev.cateringtenda.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseFragment

class AdminPesananFragment : BaseFragment() {

    lateinit var tvSubJudul: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_pesanan, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)




        return root
    }

    companion object {
        fun newInstance(): AdminPesananFragment{
            val fragment = AdminPesananFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
