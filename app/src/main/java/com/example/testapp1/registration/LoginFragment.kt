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
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.Amplify
import com.example.testapp1.R



class LoginFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            login()

        }

        val signupTextView: TextView = view.findViewById(R.id.tv_signup)
        signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }


    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        Amplify.Auth.signIn(email, password,
            { result ->
                activity?.runOnUiThread {
                    if (result.isSignedIn) {
                        Toast.makeText(context, "Login succeeded", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { error ->
                activity?.runOnUiThread {
                    Toast.makeText(context, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginFailed", error.toString())
                }
            }
        )
    }
}
