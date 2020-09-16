package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.activity.pengguna.KeranjangActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterCateringUser
import com.tapisdev.cateringtenda.adapter.AdapterPenyedia
import com.tapisdev.cateringtenda.adapter.AdapterTendaUser
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Tenda
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.fragment_admin_tenda.*
import java.util.*
import kotlin.collections.ArrayList

class UserHomeFragment : BaseFragment() {

    lateinit var rvPenyedia: RecyclerView
    lateinit var animation_view : LottieAnimationView
    var TAG_GET_PENYEDIA = "getPenyedia"
    lateinit var  edSearchPenyedia : EditText
    lateinit var adapter:AdapterPenyedia
    lateinit var ivCart : ImageView

    var listPenyedia = ArrayList<UserModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_home, container, false)
        rvPenyedia = root.findViewById(R.id.rvPenyedia)
        ivCart = root.findViewById(R.id.ivCart)
        animation_view = root.findViewById(R.id.animation_view)
        edSearchPenyedia = root.findViewById(R.id.edSearchPenyedia)

        adapter = AdapterPenyedia(listPenyedia)
        rvPenyedia.setHasFixedSize(true)
        rvPenyedia.layoutManager = GridLayoutManager(requireContext(), 2)
        rvPenyedia.adapter = adapter

        ivCart.setOnClickListener {
            val i = Intent(activity,KeranjangActivity::class.java)
            startActivity(i)
        }
        edSearchPenyedia.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchPenyedia = ArrayList<UserModel>()

            for (c in 0 until listPenyedia.size){
                var namaPenyedia = listPenyedia.get(c).name.toString().toLowerCase().trim()

                if (namaPenyedia.contains(query)){
                    listSearchPenyedia.add(listPenyedia.get(c))
                }
            }

            adapter = AdapterPenyedia(listSearchPenyedia)
            rvPenyedia.layoutManager = GridLayoutManager(requireContext(), 2)
            rvPenyedia.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        getDataPenyedia()
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

    fun getDataPenyedia(){
        userRef.get().addOnSuccessListener { result ->
            listPenyedia.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var userModel : UserModel = document.toObject(UserModel::class.java)
                userModel.uId = document.id
                if(userModel.jenis.equals("admin")){
                    listPenyedia.add(userModel)
                }
            }
            if (listPenyedia.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            Collections.reverse(listPenyedia)
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_PENYEDIA,"err : "+exception.message)
        }
    }


    override fun onResume() {
        super.onResume()
        getDataPenyedia()
    }

}
