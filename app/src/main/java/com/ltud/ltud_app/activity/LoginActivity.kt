package com.ltud.ltud_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ltud.ltud_app.R
import com.ltud.ltud_app.databinding.ActivityLoginBinding
import com.ltud.ltud_app.model.NetworkUtils
import com.ltud.ltud_app.viewmodel.AuthViewModel
import com.ltud.ltud_app.viewmodel.Status

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding


    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.tvSignup.setOnClickListener(this@LoginActivity)
        binding.tvForgotPassword.setOnClickListener(this@LoginActivity)
        binding.btnLogin.setOnClickListener(this@LoginActivity)

        binding.edtEmailLogin.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.edtEmailLogin.error = null
            } else if (binding.edtEmailLogin.editText?.text.isNullOrEmpty())
                binding.edtEmailLogin.error = "Full name is required!"
        }
        binding.edtPasswordLogin.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.edtPasswordLogin.error = null
            } else if (binding.edtPasswordLogin.editText?.text.isNullOrEmpty())
                binding.edtPasswordLogin.error = "Full name is required!"
        }
        viewModel.loadSavedLoginInfo()
        notification()
    }

    private fun notification() {
        viewModel.logInStatus.observe(this) { status ->
            when (status) {
                is Status.Loading -> {
                    binding.progressLogin.visibility = View.VISIBLE
                }
                is Status.Success -> {
                    binding.progressLogin.visibility = View.GONE
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Status.Error -> {
                    binding.progressLogin.visibility = View.GONE
                    Toast.makeText(this, status.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {

                if (NetworkUtils.isInternetConnected(this)) {
                    val email = binding.edtEmailLogin.editText?.text.toString().trim()
                    val password = binding.edtPasswordLogin.editText?.text.toString().trim()
                    val rememberMe = binding.checkbox.isChecked
                    if (email.isEmpty()) {
                        binding.edtEmailLogin.error = "Email is required!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        binding.edtEmailLogin.error = "Pleas provide valid email!"
                        //binding.edtEmailSignup.requestFocus()
                        return
                    }
                    if (password.isEmpty()) {
                        binding.edtPasswordLogin.error = "Password is required!"
                        //binding.edtPasswordSignup.requestFocus()
                        return
                    }
                    if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&./])(?=\\S+\$).{11,}".toRegex())) {
                        binding.edtPasswordLogin.error = "Invalid password"
                        //binding.edtPasswordSignup.requestFocus()
                        return
                    }
                    viewModel.logIn(email, password, rememberMe)
                } else {
                    Toast.makeText(this, "Internet is not connected", Toast.LENGTH_LONG).show()
                }

            }
            R.id.tv_signup -> startActivity(Intent(this, SignUpActivity::class.java))
            R.id.tv_forgot_password -> startActivity(Intent(this, ForgotPasswordActivity::class.java)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy theo dõi LiveData
        viewModel.logInStatus.removeObservers(this)
    }
}