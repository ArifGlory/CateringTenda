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
import com.tapisdev.cateringtenda.adapter.AdapterPengguna
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class SuperadminDataUserFragment : BaseFragment() {

    lateinit var rvPengguna: RecyclerView
    lateinit var fab : FloatingActionButton
    lateinit var edSearchPengguna : EditText
    var TAG_GET_PENGGUNA = "getPengguna"
    lateinit var adapter:AdapterPengguna

    var listPengguna = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_superadmin_data_user, container, false)
        rvPengguna = root.findViewById(R.id.rvPengguna)
        edSearchPengguna = root.findViewById(R.id.edSearchPengguna)

        adapter = AdapterPengguna(listPengguna)

        rvPengguna.setHasFixedSize(true)
        rvPengguna.layoutManager = LinearLayoutManager(activity)
        rvPengguna.adapter = adapter

        edSearchPengguna.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchCatering = ArrayList<UserModel>()

            for (c in 0 until listPengguna.size){
                var namaCatering = listPengguna.get(c).name.toString().toLowerCase().trim()
                if (namaCatering.contains(query)){
                    listSearchCatering.add(listPengguna.get(c))
                }
            }

            adapter = AdapterPengguna(listSearchCatering)
            rvPengguna.layoutManager = LinearLayoutManager(activity)
            rvPengguna.adapter = adapter
            adapter.notifyDataSetChanged()
        }



        getDataPengguna()
        return root
    }

    companion object {
        fun newInstance(): SuperadminDataUserFragment{
            val fragment = SuperadminDataUserFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataPengguna(){
        userRef.get().addOnSuccessListener { result ->
            listPengguna.clear()
            //Log.d(TAG_GET_PENGGUNA," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_PENGGUNA, "Datanya : "+document.data)
                var pengguna : UserModel = document.toObject(UserModel::class.java)
                pengguna.uId = document.id
                if (!pengguna.jenis.equals("superadmin")){
                    listPengguna.add(pengguna)
                }
            }
            if (listPengguna.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_PENGGUNA,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataPengguna()
    }

}
