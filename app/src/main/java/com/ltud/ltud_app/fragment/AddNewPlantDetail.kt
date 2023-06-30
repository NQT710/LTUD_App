package com.ltud.ltud_app.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.FragmentAddNewPlantDetailBinding
import com.ltud.ltud_app.model.Species
import com.ltud.ltud_app.viewmodel.AuthViewModel
import java.io.ByteArrayOutputStream

class AddNewPlantDetail : Fragment() {

    private lateinit var binding : FragmentAddNewPlantDetailBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var image : Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_plant_detail, container, false)
        binding = FragmentAddNewPlantDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        // Inflate the layout for this fragment

        val imgView = view.findViewById<ImageView>(R.id.imageProfile)
        val imageBitmap = arguments?.getParcelable<Bitmap>("data")
        if (imageBitmap != null) {
            imgView.setImageBitmap(imageBitmap)
            image = imageBitmap
        }



        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.tvNameAdd.setOnClickListener {
            binding.tvNameAdd.requestFocus()
            imm.showSoftInput(binding.tvNameAdd, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.tvKingdomAdd.setOnClickListener {
            binding.tvKingdomAdd.requestFocus()
            imm.showSoftInput(binding.tvKingdomAdd, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.tvFamilyAdd.setOnClickListener {
            binding.tvFamilyAdd.requestFocus()
            imm.showSoftInput(binding.tvFamilyAdd, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.tvDescriptionAdd.setOnClickListener {
            binding.tvDescriptionAdd.requestFocus()
            imm.showSoftInput(binding.tvDescriptionAdd, InputMethodManager.SHOW_IMPLICIT)
        }

        val button = view.findViewById<AppCompatButton>(R.id.btn_add_plant)
        button.setOnClickListener{
            Log.i("TAG_L","btn click")
            addNewSpecies()
        }
        return view
    }

    private fun addNewSpecies(){
        val species = Species()

//        val baos = ByteArrayOutputStream()
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//        FirebaseApp.initializeApp(this)
//        val storage : FirebaseFirestore = FirebaseFirestore.getInstance()
//        val storageRef = storage.reference
//        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val imageFileName = "IMG_$timestamp.jpg"
//        val imageRef = storageRef.child("images/$imageFileName")
//
//        species.image_species = imageRef


        val etName = view?.findViewById<EditText>(R.id.tv_name_add)
        if (etName != null) {
            species.name = etName.text.toString()
        }
        val etKingdom = view?.findViewById<EditText>(R.id.tv_kingdom_add)
        if (etKingdom != null) {
            species.kingdom = etKingdom.text.toString()
        }
        val etFamily = view?.findViewById<EditText>(R.id.tv_family_add)
        if (etFamily != null) {
            species.family = etFamily.text.toString()
        }
        val etDescription = view?.findViewById<EditText>(R.id.tv_description_add)
        if (etDescription != null) {
            species.description = etDescription.text.toString()
        }

        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference.child("species")
        val bitmap = image
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()
        val fileName = etName?.text.toString()+".jpg"
        val imageRef: StorageReference = storageRef.child(fileName)
        val uploadTask = imageRef.putBytes(data)
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            species.image_species = imageUrl
            viewModel.saveNewSpecies(species)
            // Sử dụng imageUrl trong ứng dụng của bạn
        }.addOnFailureListener {
            // Xảy ra lỗi trong quá trình lấy URL của ảnh
        }

    }
}