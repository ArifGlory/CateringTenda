package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.makeramen.roundedimageview.RoundedImageView
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserPreference
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class UserProfilFragment : BaseFragment(),PermissionHelper.PermissionListener {

    lateinit var rvCatering: RecyclerView
    lateinit var tvLogout : TextView
    lateinit var edEmailAddress : EditText
    lateinit var edUserName : EditText
    lateinit var edMobileNumber : EditText
    lateinit var ivProfile : RoundedImageView
    lateinit var ivGallery : ImageView
    var state = "view"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_profil, container, false)
        mUserPref = UserPreference(this.requireActivity())

        tvLogout = root.findViewById(R.id.tvLogout)
        edEmailAddress = root.findViewById(R.id.edEmailAddress)
        edUserName = root.findViewById(R.id.edUserName)
        edMobileNumber = root.findViewById(R.id.edMobileNumber)
        ivProfile = root.findViewById(R.id.ivProfile)
        ivGallery = root.findViewById(R.id.ivGallery)


        tvLogout.setOnClickListener {
            auth.signOut()
            clearSession()
            val i = Intent(activity, MainActivity::class.java)
            startActivity(i)
        }

        updateUI()
        return root
    }

    companion object {
        fun newInstance(): UserProfilFragment{
            val fragment = UserProfilFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun updateUI(){
        if (state.equals("view")){
            edEmailAddress.setText(mUserPref.getEmail())
            edUserName.setText(mUserPref.getName())
            edMobileNumber.setText(mUserPref.getPhone())

            if (!mUserPref.getFoto().equals("")){
                Glide.with(requireActivity())
                    .load(mUserPref.getFoto())
                    .into(ivProfile)
            }

            edEmailAddress.isEnabled = false
            edUserName.isEnabled = false
            edMobileNumber.isEnabled = false
        }

    }

    fun clearSession(){
        mUserPref.saveName("")
        mUserPref.saveEmail("")
        mUserPref.saveFoto("")
        mUserPref.saveJenisUser("none")
        mUserPref.savePhone("")
    }

    override fun onPermissionCheckDone() {

    }

}
