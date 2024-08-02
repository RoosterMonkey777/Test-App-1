package com.example.testapp1.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.example.testapp1.R

class SignupFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)

        view.findViewById<Button>(R.id.btn_signup).setOnClickListener {
            if (validateInput()) {
                signup()
            }
        }

        val loginTextView: TextView = view.findViewById(R.id.tv_login)
        loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

    }

    private fun validateInput(): Boolean {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 8) {
            Toast.makeText(context, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun signup() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        Amplify.Auth.signUp(email, password, options,
            { result ->
                activity?.runOnUiThread {
                    if (result.isSignUpComplete) {
                        Toast.makeText(context, "Sign up succeeded", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "A verification code has been sent to your email", Toast.LENGTH_LONG).show()
                        val bundle = Bundle().apply {
                            putString("email", email)
                        }
                        findNavController().navigate(R.id.action_signupFragment_to_confirmationFragment, bundle)
                    }
                }
            },
            { error ->
                activity?.runOnUiThread {
                    val errorMessage = error.message ?: "Sign up failed"
                    if (errorMessage.contains("Username already exists")) {
                        Toast.makeText(context, "Email is already in use, try another", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Sign up failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("SignUpFailed", error.toString())
                }
            }
        )
    }
}
