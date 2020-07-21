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
import com.tapisdev.cateringtenda.adapter.AdapterTendaUser
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Tenda
import kotlinx.android.synthetic.main.fragment_admin_tenda.*
import java.util.*
import kotlin.collections.ArrayList

class UserHomeFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    lateinit var rvTenda : RecyclerView
    var TAG_GET_CATERING = "getCatering"
    var TAG_GET_TENDA = "getTenda"
    lateinit var adapter:AdapterCateringUser
    lateinit var adapterTenda:AdapterTendaUser

    var listCatering = ArrayList<Catering>()
    var listTenda = ArrayList<Tenda>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_home, container, false)
        rvCatering = root.findViewById(R.id.rvCatering)
        rvTenda = root.findViewById(R.id.rvTenda)

        adapter = AdapterCateringUser(listCatering)
        adapterTenda = AdapterTendaUser(listTenda)

        rvCatering.setHasFixedSize(true)
        rvTenda.setHasFixedSize(true)
        //rvCatering.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL ,false)
        rvCatering.layoutManager = GridLayoutManager(requireContext(), 2)
        rvCatering.adapter = adapter

        rvTenda.layoutManager = GridLayoutManager(requireContext(), 2)
        rvTenda.adapter = adapterTenda


        getDataCatering()
        getDataTenda()
        return root
    }

    companion object {
        fun newInstance(): UserHomeFragment{
            val fragment = UserHomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataCatering(){
        cateringRef.get().addOnSuccessListener { result ->
            listCatering.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var catering : Catering = document.toObject(Catering::class.java)
                catering.cateringId = document.id
                listCatering.add(catering)
            }
           /* if (listCatering.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }*/
            Collections.reverse(listCatering)
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_CATERING,"err : "+exception.message)
        }
    }

    fun getDataTenda(){
        tendaRef.get().addOnSuccessListener { result ->
            listTenda.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var tenda : Tenda = document.toObject(Tenda::class.java)
                tenda.tendaId = document.id
                listTenda.add(tenda)
            }
            /* if (listCatering.size == 0){
                 animation_view.setAnimation(R.raw.empty_box)
                 animation_view.playAnimation()
                 animation_view.loop(false)
             }else{
                 animation_view.visibility = View.INVISIBLE
             }*/
            Collections.reverse(listTenda)
            adapterTenda.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_TENDA,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataCatering()
        getDataTenda()
    }

}
