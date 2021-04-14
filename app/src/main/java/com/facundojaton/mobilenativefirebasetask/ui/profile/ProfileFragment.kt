package com.facundojaton.mobilenativefirebasetask.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facundojaton.mobilenativefirebasetask.R
import com.facundojaton.mobilenativefirebasetask.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = profileViewModel

        profileViewModel.logOutResult.observe(viewLifecycleOwner, {
            when(it){
                ProfileViewModel.LogOutResult.SUCCESS -> {
                    goToLogin()
                }
                ProfileViewModel.LogOutResult.WAITING -> {
                }
            }
        })

    }

    private fun goToLogin() {
        this.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
    }

}