package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
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
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class AdminTendaFragment : BaseFragment() {

    lateinit var rvTenda: RecyclerView
    lateinit var fab : FloatingActionButton
    var TAG_GET_TENDA = "getTenda"
    lateinit var adapter: AdapterTenda
    lateinit var edSearchTenda : EditText

    var listTenda = ArrayList<Tenda>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_tenda, container, false)
        rvTenda = root.findViewById(R.id.rvTenda)
        fab = root.findViewById(R.id.fab)
        edSearchTenda = root.findViewById(R.id.edSearchTenda)
        mUserPref = UserPreference(requireContext())

        adapter = AdapterTenda(listTenda)

        rvTenda.setHasFixedSize(true)
        rvTenda.layoutManager = LinearLayoutManager(activity)
        rvTenda.adapter = adapter

        fab.setOnClickListener {
            val i = Intent(activity, AddTendaActivity::class.java)
            startActivity(i)
        }
        edSearchTenda.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchTenda = ArrayList<Tenda>()

            for (c in 0 until listTenda.size){
                var namaTenda = listTenda.get(c).nama.toString().toLowerCase().trim()
                if (namaTenda.contains(query)){
                    listSearchTenda.add(listTenda.get(c))
                }
            }

            adapter = AdapterTenda(listSearchTenda)
            rvTenda.layoutManager = LinearLayoutManager(activity)
            rvTenda.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        if (mUserPref.getJenisUser().equals("superadmin")){
            fab.visibility = View.GONE
            fab.isEnabled = false
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
        tendaRef.get().addOnSuccessListener { result ->
            listTenda.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                Log.d(TAG_GET_TENDA, "Datanya : "+document.data)

                var tenda : Tenda = document.toObject(Tenda::class.java)
                tenda.tendaId = document.id
                if (mUserPref.getJenisUser().equals("superadmin")){
                    listTenda.add(tenda)
                }else{
                    if (tenda.idAdmin.equals(auth.currentUser?.uid)){
                        listTenda.add(tenda)
                    }
                }
            }
            if (listTenda.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_TENDA,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMyTenda()
    }

}
