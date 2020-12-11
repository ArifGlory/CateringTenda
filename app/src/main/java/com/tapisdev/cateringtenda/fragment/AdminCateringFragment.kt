package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class AdminCateringFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    lateinit var fab : FloatingActionButton
    lateinit var edSearchCatering : EditText
    var TAG_GET_CATERING = "getCatering"
    lateinit var adapter:AdapterCatering

    var listCatering = ArrayList<Catering>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_catering, container, false)
        rvCatering = root.findViewById(R.id.rvCatering)
        fab = root.findViewById(R.id.fab)
        edSearchCatering = root.findViewById(R.id.edSearchCatering)
        mUserPref = UserPreference(requireContext())

        adapter = AdapterCatering(listCatering)

        rvCatering.setHasFixedSize(true)
        rvCatering.layoutManager = LinearLayoutManager(activity)
        rvCatering.adapter = adapter

        fab.setOnClickListener {
            val i = Intent(activity,AddCateringActivity::class.java)
            startActivity(i)
        }
        edSearchCatering.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchCatering = ArrayList<Catering>()

            for (c in 0 until listCatering.size){
                var namaCatering = listCatering.get(c).nama.toString().toLowerCase().trim()
                if (namaCatering.contains(query)){
                    listSearchCatering.add(listCatering.get(c))
                }
            }

            adapter = AdapterCatering(listSearchCatering)
            rvCatering.layoutManager = LinearLayoutManager(activity)
            rvCatering.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        if (mUserPref.getJenisUser().equals("superadmin")){
            fab.visibility = View.GONE
            fab.isEnabled = false
        }

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
            listCatering.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var catering : Catering = document.toObject(Catering::class.java)
                catering.cateringId = document.id
                if (mUserPref.getJenisUser().equals("superadmin")){
                    listCatering.add(catering)
                }else{
                    if (catering.idAdmin.equals(auth.currentUser?.uid)){
                        listCatering.add(catering)
                    }
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
