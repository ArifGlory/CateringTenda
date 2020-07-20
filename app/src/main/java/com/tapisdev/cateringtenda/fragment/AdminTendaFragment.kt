package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.activity.admin.AddTendaActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterTenda
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Tenda

class AdminTendaFragment : BaseFragment() {

    lateinit var rvTenda: RecyclerView
    lateinit var fab : FloatingActionButton
    var TAG_GET_TENDA = "getTenda"
    lateinit var adapter: AdapterTenda

    var listTenda = ArrayList<Tenda>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_tenda, container, false)
        rvTenda = root.findViewById(R.id.rvTenda)
        fab = root.findViewById(R.id.fab)

        adapter = AdapterTenda(listTenda)

        rvTenda.setHasFixedSize(true)
        rvTenda.layoutManager = LinearLayoutManager(activity)
        rvTenda.adapter = adapter

        fab.setOnClickListener {
            val i = Intent(activity, AddTendaActivity::class.java)
            startActivity(i)
        }


        getDataMyTenda()
        return root
    }

    companion object {
        fun newInstance(): AdminTendaFragment{
            val fragment = AdminTendaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataMyTenda(){
        //activity?.applicationContext?.let { showLoading(it) }
        showInfoMessage("sedang mengambil data..")

        cateringRef.get().addOnSuccessListener { result ->
            listTenda.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            showSuccessMessage("Data berhasil diambil")
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)

                var tenda : Tenda = document.toObject(Tenda::class.java)
                tenda.tendaId = document.id
                if (tenda.idAdmin.equals(auth.currentUser?.uid)){
                    listTenda.add(tenda)
                }
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_TENDA,"err : "+exception.message)
        }
    }

}
