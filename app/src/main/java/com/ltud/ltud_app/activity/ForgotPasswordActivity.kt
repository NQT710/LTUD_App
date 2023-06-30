package com.ltud.ltud_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.ActivityForgotPasswordBinding
import com.ltud.ltud_app.model.NetworkUtils
import com.ltud.ltud_app.viewmodel.AuthViewModel
import com.ltud.ltud_app.viewmodel.Status

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener  {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        binding.tvBack.setOnClickListener(this@ForgotPasswordActivity)
        binding.btnSendEmail.setOnClickListener(this@ForgotPasswordActivity)
        binding.edtEmailForgot.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.edtEmailForgot.error = null
            }else if(binding.edtEmailForgot.editText?.text.isNullOrEmpty())
                binding.edtEmailForgot.error = "Email is required!"
        }
        notification()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_back -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.btn_send_email -> {
                if(NetworkUtils.isInternetConnected(this)){
                    val email = binding.edtEmailForgot.editText?.text.toString().trim()

                    if (email.isEmpty()){
                        binding.edtEmailForgot.error = "Email is required!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        binding.edtEmailForgot.error = "Pleas provide valid email!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    viewModel.forgotPassword(email)
                }else{
                    Toast.makeText(this, "Internet is not connected", Toast.LENGTH_LONG).show()
                }


            }
        }
    }
    private fun notification(){
        viewModel.forgotStatus.observe(this) { status ->
            when (status) {
                is Status.Loading -> {
                    binding.progressForgot.visibility = View.VISIBLE
                }
                is Status.Success -> {
                    binding.progressForgot.visibility = View.GONE
                    Toast.makeText(this, "Please check your email to reset your password!", Toast.LENGTH_LONG).show()
                }
                is Status.Error -> {
                    binding.progressForgot.visibility = View.GONE
                    Toast.makeText(this, "Email is incorrect! Please re-enter your email!", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

}