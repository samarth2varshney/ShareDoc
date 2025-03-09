package com.example.sharedoc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedoc.api.ApiClient
import com.example.sharedoc.databinding.ActivitySignUpBinding
import com.example.sharedoc.model.ApiResponse
import com.example.sharedoc.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup.setOnClickListener{
            username = binding.username.text.toString()
            email = binding.email.text.toString()
            password = binding.password.text.toString()
            confirmPassword = binding.confirmPassword.text.toString()

            if(confirmPassword == password){
                userSignUp(username, email, password)
            }
            else{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun userSignUp(username: String, email: String, password: String) {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        val user = User(username, email, password)
        val apiService = ApiClient.getApiService(this@SignUpActivity)

        apiService.userSignUp(user).enqueue(object : Callback<ApiResponse<User>>{
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
               if (response.isSuccessful && response.body()?.status.equals("success", true)){
                   Toast.makeText(this@SignUpActivity, "User created successfully", Toast.LENGTH_SHORT).show()
               }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

        })
    }
}