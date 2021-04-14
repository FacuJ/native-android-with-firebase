package com.facundojaton.mobilenativefirebasetask.ui.register

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
import com.facundojaton.mobilenativefirebasetask.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = registerViewModel

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            registerViewModel.changeEmailContent(text.toString())
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            registerViewModel.changePasswordContent(text.toString())
        }

        binding.btnSignIn.setOnClickListener {
            this.findNavController().popBackStack()
        }

        registerViewModel.registerResult.observe(viewLifecycleOwner, {
            when (it) {
                RegisterViewModel.RegisterResult.SUCCESS -> {
                    registerViewModel.doneNavigatingToWelcomeScreen()
                    this.findNavController()
                        .navigate(RegisterFragmentDirections.actionRegisterFragmentToWelcomeFragment())
                }
                RegisterViewModel.RegisterResult.FAILED -> {
                    hideWaitingMode()
                }
                RegisterViewModel.RegisterResult.WAITING -> {
                    showWaitingMode()
                }
                else -> {
                    hideWaitingMode()
                }
            }
        })
    }

    private fun hideWaitingMode() {
        binding.apply {
            pbRegister.visibility = View.GONE
            btnSignIn.isEnabled = true
            btnSignUp.isEnabled = true
        }
    }

    private fun showWaitingMode() {
        binding.apply {
            pbRegister.visibility = View.VISIBLE
            btnSignIn.isEnabled = false
            btnSignUp.isEnabled = false
        }
    }
}