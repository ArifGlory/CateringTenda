package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterCateringUser
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.SharedVariable
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class TabCateringFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    lateinit var fab : FloatingActionButton
    var TAG_GET_CATERING = "getCatering"
    lateinit var adapter:AdapterCateringUser

    var listCatering = ArrayList<Catering>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_tab_catering, container, false)
        rvCatering = root.findViewById(R.id.rvCatering)

        adapter = AdapterCateringUser(listCatering)

        rvCatering.setHasFixedSize(true)
        rvCatering.layoutManager = GridLayoutManager(activity , 2) as RecyclerView.LayoutManager?
        rvCatering.adapter = adapter


        getDataMyCatering()
        return root
    }

    companion object {
        fun newInstance(): TabCateringFragment{
            val fragment = TabCateringFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataMyCatering(){
        cateringRef.get().addOnSuccessListener { result ->
            listCatering.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var catering : Catering = document.toObject(Catering::class.java)
                catering.cateringId = document.id
                if (catering.idAdmin.equals(SharedVariable.selectedIdPenyedia)){
                    listCatering.add(catering)
                }
            }
            if (listCatering.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_CATERING,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMyCatering()
    }

}
