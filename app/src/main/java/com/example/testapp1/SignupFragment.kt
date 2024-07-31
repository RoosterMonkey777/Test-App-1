package com.example.testapp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

class SignupFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etUsername = view.findViewById(R.id.et_username)
        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)

        view.findViewById<Button>(R.id.btn_signup).setOnClickListener {
            signup()
        }

        view.findViewById<Button>(R.id.btn_back_to_login).setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun signup() {
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        Amplify.Auth.signUp(username, password, options,
            { result ->
                if (result.isSignUpComplete) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Sign up succeeded", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }
                } else {
                    // Handle the next step if sign up is not complete (e.g., confirmation step)
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Please confirm your email", Toast.LENGTH_SHORT).show()
                        // Navigate to a confirmation fragment if you have one
                    }
                }
            },
            { error ->
                activity?.runOnUiThread {
                    Toast.makeText(context, "Sign up failed: ${error.toString()}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}