package com.example.testapp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_signup).setOnClickListener {
            // Here you would typically implement the signup logic
            Toast.makeText(context, "Signup button clicked", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btn_back_to_login).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}