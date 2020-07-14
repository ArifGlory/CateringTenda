package com.tapisdev.cateringtenda.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseFragment

class AdminCateringFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    lateinit var fab : FloatingActionButton
    var TAG_GET_CATERING = "getCatering"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_catering, container, false)
        rvCatering = root.findViewById(R.id.rvCatering)
        fab = root.findViewById(R.id.fab)
        getDataMyCatering()


        return root
    }

    companion object {
        fun newInstance(): AdminCateringFragment{
            val fragment = AdminCateringFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataMyCatering(){
        cateringRef.get().addOnSuccessListener { result ->
            for (document in result){
                Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG_GET_CATERING,"err : "+exception.message)
        }
    }

}
