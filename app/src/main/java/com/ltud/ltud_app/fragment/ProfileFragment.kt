package com.ltud.ltud_app.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.ltud.ltud_app.R
import com.ltud.ltud_app.activity.LoginActivity
import com.ltud.ltud_app.adapter.ViewPagerAdapter
import com.ltud.ltud_app.databinding.FragmentProfileBinding
import com.ltud.ltud_app.model.NetworkUtils
import com.ltud.ltud_app.model.UserSignUp
import com.ltud.ltud_app.viewmodel.AuthViewModel
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random


class ProfileFragment : Fragment(), View.OnClickListener {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var pagerTest: ViewPager2
    private lateinit var tabDemo: TabLayout

    private lateinit var binding : FragmentProfileBinding

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.imageProfile.setOnClickListener{
            galleryCheckPermission()
        }
        loadData()
        binding.tvNameProfile.setOnClickListener{
            binding.tvNameProfile.requestFocus()
            showEditTextDialog(binding.tvNameProfile)
        }

        binding.tvNameProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                upDateUi()
            }
        })

        binding.tvLocation.setOnClickListener{
            binding.tvLocation.requestFocus()
            showEditTextDialog(binding.tvLocation)
        }

        binding.tvLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                upDateUi()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        likeData()
        binding.btnSignout.setOnClickListener(this@ProfileFragment)
    }

    private fun likeData(){
        pagerTest = binding.pagerTest
        tabDemo = binding.tabDemo
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        pagerTest.adapter = adapter
        TabLayoutMediator(tabDemo,pagerTest){tab,position ->
            when(position){
                0->{
                    tab.text="SPECIES"
                }
                1->{
                    tab.text="ARTICLES"
                }
            }
        }.attach()
    }
    private fun loadData(){
        viewModel.loadData()
        viewModel.loadStatus.observe(viewLifecycleOwner, Observer { data ->
            binding.tvNameProfile.text = data?.name
            binding.tvLocation.text = data?.location
            context?.let { Glide.with(it).load(data?.avatar).error(R.drawable.avatar_default).into(binding.imageProfile) }
        })
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_signout -> {
                if(NetworkUtils.isInternetConnected(requireContext())){
                    viewModel.logout()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }else{
                    Toast.makeText(requireContext(), "Internet is not connected", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showEditTextDialog(textView : TextView) {
        val editText = EditText(requireContext())

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Enter new")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val content = editText.text.toString()
                textView.text = content

                // Hide the keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        // Show the keyboard
        editText.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun upDateUi() {
        var data = UserSignUp()
        data.name = binding.tvNameProfile.text.toString()
        data.location = binding.tvLocation.text.toString()
        viewModel.saveData(data)
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        contract.launch(intent)
    }

    @SuppressLint("ResourceType")
    private val contract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            val imageBitmap: Bitmap? = imageUri?.let {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                BitmapFactory.decodeStream(inputStream)
            }
            if (imageBitmap != null) {

                val storage = FirebaseStorage.getInstance()
                val storageRef: StorageReference = storage.reference.child("avatar")
                val bitmap = imageBitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val data: ByteArray = baos.toByteArray()
                val id = FirebaseAuth.getInstance().currentUser?.uid
                val fileName = id + ".jpg"
                val imageRef: StorageReference = storageRef.child(fileName)
                val uploadTask = imageRef.putBytes(data)

                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    id?.let {
                        database.collection("user").document(it).update("avatar", imageUrl)
                    }
                }.addOnFailureListener {
                    // Xảy ra lỗi trong quá trình lấy URL của ảnh
                }
            }
        }
    }


    private fun galleryCheckPermission() {

        Dexter.withContext(requireContext()).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }
    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(requireActivity())
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}