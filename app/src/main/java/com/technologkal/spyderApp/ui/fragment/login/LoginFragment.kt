package com.technologkal.spyderApp.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

//import com.facebook.CallbackManager
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.login.LoginResult

import com.technologkal.spyderApp.ui.activity.host.HostActivity
import com.technologkal.ui.fragment.onBoarding.walkthroughactivity.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val rCSignIn = 1
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

        setupListeners()
        setUpObservers()

        return binding.root
    }

    private fun setupListeners() {
        binding.etEmail.doAfterTextChanged { email -> viewModel.username = email.toString() }
        binding.etPassword.doAfterTextChanged { pass -> viewModel.password = pass.toString() }
        binding.signInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setUpObservers() {
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                startActivity(Intent(context, HostActivity::class.java))
                (activity as HostActivity).finish()
                viewModel.doneHomeNavigation()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rCSignIn) {
            viewModel.handleGoogleSignInResult(data)
        }

    }

    private fun signInWithGoogle() {
        val signInIntent = (activity as HostActivity).googleSignInClient.signInIntent
        startActivityForResult(signInIntent, rCSignIn)
    }
}