package com.ltud.ltud_app.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.ActivityMainBinding
import com.ltud.ltud_app.fragment.AddNewPlantDetail
import com.ltud.ltud_app.fragment.HomeFragment
import com.ltud.ltud_app.fragment.ProfileFragment
import com.ltud.ltud_app.model.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 2
    private val CAMERA_REQUEST_CODE = 1

    private lateinit var binding : ActivityMainBinding
    private lateinit var addNewPlantDetail: AddNewPlantDetail
    private lateinit var HomeFragment: HomeFragment
    //private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if(!NetworkUtils.isInternetConnected(this)) {
            Toast.makeText(this, "Internet is not connected", Toast.LENGTH_LONG).show()
        }
        HomeFragment = HomeFragment()
        addNewPlantDetail= AddNewPlantDetail()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, HomeFragment)
            .commit()

        replaceFragment(HomeFragment())
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.action_home -> replaceFragment(HomeFragment())
                R.id.action_profile -> replaceFragment(ProfileFragment())
                else ->{

                }
            }
            true
        }
        binding.btnOpenCamera.setOnClickListener{
            cameraCheckPermission()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun cameraCheckPermission() {

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).withListener(

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
        if (resultCode == RESULT_OK && data != null) {
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
                switchToFragmentTwo(imageBitmap)
            }
        } else {

        }
    }
    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", this.packageName, null)
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
    fun switchToFragmentTwo(  imageBitmap: Bitmap) {
        val bundle = Bundle()
        bundle.putParcelable("data", imageBitmap)
        addNewPlantDetail.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, addNewPlantDetail)
            .addToBackStack(null) // Thêm vào Back Stack
            .commit()
    }
    override fun onBackPressed() {
        // Không làm gì khi nút back được nhấn
        // Bằng cách này, nút back sẽ không có hiệu ứng gì trong hoạt động này
    }
}