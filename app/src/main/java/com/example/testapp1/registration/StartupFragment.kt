package com.example.testapp1.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapp1.R

class StartupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonLogin: View = view.findViewById(R.id.buttonLogin)
        val buttonSignup: View = view.findViewById(R.id.buttonSignup)

        buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_startupFragment_to_loginFragment)
        }

        buttonSignup.setOnClickListener {
            findNavController().navigate(R.id.action_startupFragment_to_signupFragment)
        }
    }
}
