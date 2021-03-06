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
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringPesanan.adapter.AdapterPesananUser
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Pesanan
import com.tapisdev.cateringtenda.model.UserPreference
import java.util.*
import kotlin.collections.ArrayList

class AdminPesananFragment : BaseFragment() {

    lateinit var rvPesanan: RecyclerView
    var TAG_GET_PESANAN = "getPesanan"
    lateinit var adapter: AdapterPesananUser
    lateinit var edSearchPesanan : EditText
    lateinit var animation_view : LottieAnimationView

    var listPesanan = ArrayList<Pesanan>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_pesanan, container, false)
        rvPesanan = root.findViewById(R.id.rvPesanan)
        animation_view = root.findViewById(R.id.animation_view)
        edSearchPesanan = root.findViewById(R.id.edSearchPesanan)
        mUserPref = UserPreference(requireContext())

        adapter = AdapterPesananUser(listPesanan)

        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(activity)
        rvPesanan.adapter = adapter

        edSearchPesanan.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchPesanan = ArrayList<Pesanan>()

            for (c in 0 until listPesanan.size){
                var tanggal = listPesanan.get(c).tanggalPesan.toString().toLowerCase().trim()
                var alamat = listPesanan.get(c).alamat.toString().toLowerCase().trim()

                tanggal = convertDate(tanggal)

                if (tanggal.contains(query) || alamat.contains(query)){
                    listSearchPesanan.add(listPesanan.get(c))
                }
            }

            adapter = AdapterPesananUser(listSearchPesanan)
            rvPesanan.layoutManager = LinearLayoutManager(activity)
            rvPesanan.adapter = adapter
            adapter.notifyDataSetChanged()

        }


        getDataMyPesanan()
        return root
    }

    fun getDataMyPesanan(){
        pesananRef.get().addOnSuccessListener { result ->
            listPesanan.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var pesanan : Pesanan = document.toObject(Pesanan::class.java)
                if (mUserPref.getJenisUser().equals("superadmin")){
                    listPesanan.add(pesanan)
                }else{
                    if (pesanan.idAdmin.equals(auth.currentUser?.uid)){
                        listPesanan.add(pesanan)
                    }
                }

            }
            if (listPesanan.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            Collections.reverse(listPesanan)
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_PESANAN,"err : "+exception.message)
        }
    }

    companion object {
        fun newInstance(): AdminPesananFragment{
            val fragment = AdminPesananFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMyPesanan()
    }

}
