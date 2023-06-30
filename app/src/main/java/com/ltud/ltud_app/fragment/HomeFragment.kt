package com.ltud.ltud_app.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ltud.ltud_app.R
import com.ltud.ltud_app.activity.MainActivity
import com.ltud.ltud_app.adapter.RvAdapterPhotography
import com.ltud.ltud_app.adapter.RvAdapterPlantsTypes
import com.ltud.ltud_app.databinding.FragmentHomeBinding
import com.ltud.ltud_app.model.OutData
import com.ltud.ltud_app.model.OutDataPhotography
import com.ltud.ltud_app.viewmodel.AuthViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private val GALLERY_REQUEST_CODE = 2
    private val CAMERA_REQUEST_CODE = 1
    private lateinit var mainActivity: MainActivity

    private lateinit var fragmentSpecies: SpeciesFragment
    private lateinit var fragmentArticles: ArticlesFragment

    private lateinit var binding : FragmentHomeBinding

    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        photography()
        plant()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
        viewModel.loadStatus.observe(viewLifecycleOwner, Observer { data ->
            binding.tvName.text = data?.name + ","
            context?.let { Glide.with(it).load(data?.avatar).error(R.drawable.avatar_default).into(binding.imgAvatarHome) }

        })
        binding.layoutIdentify.setOnClickListener(this@HomeFragment)
        binding.layoutSpecies.setOnClickListener(this@HomeFragment)
        binding.layoutArticle.setOnClickListener(this@HomeFragment)
    }

    private fun plant(){
        val ds= mutableListOf<OutData>()

        ds.add(OutData(R.drawable.plant_typle1,"Home Plants","68 types of plants"))
        ds.add(OutData(R.drawable.plant_typle2,"Bonsai Plants","224 types of plants"))
        ds.add(OutData(R.drawable.plant_typle3,"Decor Plants","123 types of plants"))
        ds.add(OutData(R.drawable.plant_typle4,"Garden Plants","38 types of plants"))

        val recyclerView: RecyclerView = binding.revPlantType
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        val itemAdapter = RvAdapterPlantsTypes(ds)
        recyclerView.adapter = itemAdapter
    }

    private fun photography(){
        val dsphotofraphy= mutableListOf<OutDataPhotography>()
        dsphotofraphy.add(OutDataPhotography(R.drawable.photography1,"#Mini"))
        dsphotofraphy.add(OutDataPhotography(R.drawable.photography2,"#Cute"))
        dsphotofraphy.add(OutDataPhotography(R.drawable.photography3,"#Mini"))
        dsphotofraphy.add(OutDataPhotography(R.drawable.photography4,"#Cute"))
        dsphotofraphy.add(OutDataPhotography(R.drawable.photography5,"#Mini"))

        val recyclerView2: RecyclerView = binding.revPhotography
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        val itemAdapter2 = RvAdapterPhotography(dsphotofraphy)
        recyclerView2.adapter = itemAdapter2
    }

    private fun cameraCheckPermission() {

        Dexter.withContext(requireActivity())
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }
                }
            ).onSameThread().check()
    }
    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        contract.launch(intent)
    }


    @SuppressLint("ResourceType")
    private val contract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK && data != null) {
            // Xử lý kết quả thành công và dữ liệu trả về từ Camera

            val imageBitmap: Bitmap? = data.extras?.get("data") as Bitmap?

//            // Tạo Bundle và truyền dữ liệu
            val bundle = Bundle()
            bundle.putParcelable("imageBitmap", imageBitmap)
//
//            // Chuyển đổi sang màn hình khác với Bundle dữ liệu
//            val intent = Intent(requireContext(), fragment1::class.java)
//            intent.putExtras(bundle)
//            startActivity(intent)
            //  Toast.makeText(requireContext(), "hello"+imageBitmap, Toast.LENGTH_SHORT).show()

            if (imageBitmap != null) {
                mainActivity.switchToFragmentTwo(imageBitmap)
            }
        } else {

        }
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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.layout_identify -> {
                cameraCheckPermission()
            }
            R.id.layout_species -> {
                fragmentSpecies = SpeciesFragment()
                replaceFragment(fragmentSpecies)
            }
            R.id.layout_article -> {
                fragmentArticles = ArticlesFragment()
                replaceFragment(fragmentArticles)
            }
        }
    }
}