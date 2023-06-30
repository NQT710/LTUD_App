package com.ltud.ltud_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.ActivitySignUpBinding
import com.ltud.ltud_app.model.NetworkUtils
import com.ltud.ltud_app.model.UserSignUp
import com.ltud.ltud_app.viewmodel.AuthViewModel
import com.ltud.ltud_app.viewmodel.Status

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        binding.tvLogin.setOnClickListener(this@SignUpActivity)
        binding.btnSignup.setOnClickListener(this@SignUpActivity)
        binding.edtFullName.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.edtFullName.error = null
            }else if(binding.edtFullName.editText?.text.isNullOrEmpty())
                binding.edtFullName.error = "Full name is required!"
        }
        binding.edtLocation.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.edtLocation.error = null
            }else if(binding.edtLocation.editText?.text.isNullOrEmpty())
                binding.edtLocation.error = "Location is required!"
        }
        binding.edtEmailSignup.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.edtEmailSignup.error = null
            }else if(binding.edtEmailSignup.editText?.text.isNullOrEmpty())
                binding.edtEmailSignup.error = "Email is required!"
        }
        binding.edtPasswordSignup.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.edtPasswordSignup.error = null
            }else if(binding.edtPasswordSignup.editText?.text.isNullOrEmpty())
                binding.edtPasswordSignup.error = "Password is required!"
        }
        notification()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.btn_signup -> {

                if(NetworkUtils.isInternetConnected(this)){
                    val fullName = binding.edtFullName.editText?.text.toString().trim()
                    val location = binding.edtLocation.editText?.text.toString().trim()
                    val email = binding.edtEmailSignup.editText?.text.toString().trim()
                    val password = binding.edtPasswordSignup.editText?.text.toString().trim()
                    val avatar = ""
                    val user = UserSignUp(fullName, email, avatar)
                    if(fullName.isEmpty()){
                        binding.edtFullName.error = "Full name is required!"
                        //binding.edtFullName.editText?.requestFocus()
                        return
                    }
                    if(location.isEmpty()){
                        binding.edtFullName.error = "Location is required!"
                        //binding.edtFullName.editText?.requestFocus()
                        return
                    }
                    if (email.isEmpty()){
                        binding.edtEmailSignup.error = "Email is required!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        binding.edtEmailSignup.error = "Pleas provide valid email!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    if (password.isEmpty()){
                        binding.edtPasswordSignup.error = "Password is required!"
                        //binding.edtPasswordSignup.requestFocus()
                        return
                    }
                    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&./])(?=\\S+\$).{11,}".toRegex())){
                        binding.edtPasswordSignup.error = "Invalid password"
                        //binding.edtPasswordSignup.requestFocus()
                        return
                    }
                    viewModel.signUp(user,password)
                }else{
                    Toast.makeText(this, "Internet is not connected", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    private fun notification(){
        viewModel.signUpStatus.observe(this) { status ->
            when (status) {
                is Status.Loading -> {
                    binding.progressSignup.visibility = View.VISIBLE
                }
                is Status.Success -> {
                    binding.progressSignup.visibility = View.GONE
                    Toast.makeText(this, "Please check your email to complete the registration!", Toast.LENGTH_LONG).show()
                }
                is Status.Error -> {
                    binding.progressSignup.visibility = View.GONE
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}