package com.example.sharedoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedoc.api.ApiClient
import com.example.sharedoc.databinding.ActivityLogInBinding
import com.example.sharedoc.model.ApiResponse
import com.example.sharedoc.model.User
import com.example.sharedoc.utils.SharedPrefsHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            userLogIn(email, password)
        }

        binding.signup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userLogIn(email: String, password: String) {
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        val user = User(email = email, password = password)
        val apiService = ApiClient.getApiService(this@LogInActivity)

        apiService.userLogIn(user).enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                Log.d("Response", response.toString())
                if(response.isSuccessful && response.body()?.status.equals("success", true)){
                    val token = response.body()?.data
                    SharedPrefsHelper.saveToken(this@LogInActivity, token.toString())
                    val intent = Intent(this@LogInActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }
}