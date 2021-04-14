package com.facundojaton.mobilenativefirebasetask.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facundojaton.mobilenativefirebasetask.R
import com.facundojaton.mobilenativefirebasetask.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            loginViewModel.changeEmailContent(text.toString())
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            loginViewModel.changePasswordContent(text.toString())
        }
        binding.btnSignUp.setOnClickListener {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner, {
            when (it) {
                LoginViewModel.LoginResult.SUCCESS -> {
                    goToList()
                }
                LoginViewModel.LoginResult.FAILED -> {
                    hideWaitingMode()
                }
                LoginViewModel.LoginResult.WAITING -> {
                    showWaitingMode()
                }
                else -> {
                    hideWaitingMode()
                }
            }
        })

            loginViewModel.showSnackBarEvent.observe(viewLifecycleOwner, {
                if (it == true) {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_on_login_check_email_and_password),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    loginViewModel.doneShowingSnackBar()
                }
            })
    }

    private fun hideWaitingMode() {
        binding.apply {
            pbLogin.visibility = View.GONE
            btnSignIn.isEnabled = true
            btnSignUp.isEnabled = true
        }
    }

    private fun showWaitingMode() {
        binding.apply {
            pbLogin.visibility = View.VISIBLE
            btnSignIn.isEnabled = false
            btnSignUp.isEnabled = false
        }
    }

    private fun goToList() {
        this.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
    }

}