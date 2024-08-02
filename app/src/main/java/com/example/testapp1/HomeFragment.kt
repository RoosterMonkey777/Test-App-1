package com.example.testapp1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.Amplify

class HomeFragment : Fragment() {

    private lateinit var tvEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEmail = view.findViewById(R.id.tv_email)

        fetchUserEmail()

        view.findViewById<Button>(R.id.btn_signout).setOnClickListener {
            signOut()

        }

    }

    private fun signOut() {
        Amplify.Auth.signOut { signOutResult ->
            when (signOutResult) {
                is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                    // Sign out completed fully and without errors.
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

                    }
                    Log.i("AuthQuickStart", "Signed out successfully")
                }
                is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                    // Sign out completed with some errors. User is signed out of the device.
                    signOutResult.hostedUIError?.let {
                        Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                        // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.
                    }
                    signOutResult.globalSignOutError?.let {
                        Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.accessToken.
                    }
                    signOutResult.revokeTokenError?.let {
                        Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                        // Optional: Use escape hatch to retry revocation of it.refreshToken.
                    }
                }
                is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                    // Sign out failed with an exception, leaving the user signed in.
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Sign out failed: ${signOutResult.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
                }
            }
        }
    }

    private fun fetchUserEmail() {
        // Call the Amplify Auth method to fetch user attributes
        Amplify.Auth.fetchUserAttributes(
            // Lambda function to handle successful response
            { attributes ->
                // Find the email attribute from the list of attributes
                val emailAttribute = attributes.firstOrNull { it.key.keyString == "email" }
                // Update the UI on the main thread
                activity?.runOnUiThread {
                    // Set the email TextView to the email attribute value or a default message if not found
                    tvEmail.text = emailAttribute?.value ?: "Email not found"
                }
            },
            // Lambda function to handle error response
            { error ->
                // Update the UI on the main thread
                activity?.runOnUiThread {
                    // Show a toast message indicating the failure
                    Toast.makeText(context, "Failed to fetch user attributes", Toast.LENGTH_SHORT).show()
                    // Log the error for debugging
                    Log.e("FetchUserAttributesFailed", error.toString())
                }
            }
        )
    }
}
