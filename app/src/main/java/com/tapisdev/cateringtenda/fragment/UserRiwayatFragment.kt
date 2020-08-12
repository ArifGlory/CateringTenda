package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringPesanan.adapter.AdapterPesananUser
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.activity.pengguna.KeranjangActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterCateringUser
import com.tapisdev.cateringtenda.adapter.AdapterTendaUser
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Pesanan
import com.tapisdev.cateringtenda.model.Tenda
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.fragment_admin_tenda.*
import java.util.*
import kotlin.collections.ArrayList

class UserRiwayatFragment : BaseFragment() {

    lateinit var rvPesanan: RecyclerView
    lateinit var animation_view : LottieAnimationView
    var TAG_GET_PENYEDIA = "getPenyedia"
    lateinit var adapter: AdapterPesananUser
    lateinit var ivCart : ImageView

    var listPesanan = ArrayList<Pesanan>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_riwayat, container, false)
        rvPesanan = root.findViewById(R.id.rvPesanan)
        animation_view = root.findViewById(R.id.animation_view)

        adapter = AdapterPesananUser(listPesanan)
        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(activity)
        rvPesanan.adapter = adapter


        getDataPesanan()
        return root
    }

    companion object {
        fun newInstance(): UserRiwayatFragment{
            val fragment = UserRiwayatFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getDataPesanan(){
        pesananRef.get().addOnSuccessListener { result ->
            listPesanan.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var pesanan : Pesanan = document.toObject(Pesanan::class.java)
                if (pesanan.idUser.equals(auth.currentUser?.uid)){
                    listPesanan.add(pesanan)
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
            Log.d(TAG_GET_PENYEDIA,"err : "+exception.message)
        }
    }


    override fun onResume() {
        super.onResume()
        getDataPesanan()
    }

}
