package com.example.gestiondebureaudechangededevises.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.databinding.FragmentPasswordChangeBinding
import com.example.gestiondebureaudechangededevises.utils.shortToast

class PasswordChangeFragment : Fragment() {
    companion object {
        private const val TAG = "PasswordChangeFragment"
    }

    private var _binding: FragmentPasswordChangeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PasswordChangeViewModel
    private val args: PasswordChangeFragmentArgs by navArgs()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = args.userId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PasswordChangeViewModel::class.java]

        binding.changePasswordButton.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString()
            val retypeNewPassword = binding.retypeNewPasswordEditText.text.toString()

            // Check if the new password and retype password match
            if (newPassword == retypeNewPassword) {
                viewModel.changePassword(userId, newPassword)
            } else {
                // Passwords don't match, show an error message
                // You can display an error message to the user
                // For example:
                binding.retypeNewPasswordEditText.error = "Passwords do not match"
            }
        }

        // Observe the LiveData to handle UI changes
        viewModel.apply {
            passwordChanged.observe(viewLifecycleOwner) { passwordChanged ->
                Log.d(TAG, "passwordChanged: $passwordChanged")
                if (passwordChanged != null) {
                    // Password changed successfully, navigate to another screen
                    requireContext().shortToast(getString(R.string.update_password_success))
                    findNavController().popBackStack()
                } else {
                    // Handle password change failure
                    // You can show an error message to the user
                    requireContext().shortToast(getString(R.string.error_changing_password))
                }

            }
            loading.observe(viewLifecycleOwner) { loading ->
                binding.progressBar2.isVisible = loading
                blockUi(loading!!)
            }
        }
    }

    private fun blockUi(loading: Boolean) {
        binding.apply {
            changePasswordButton.isEnabled = !loading
            newPasswordEditText.isEnabled = !loading
            retypeNewPasswordEditText.isEnabled = !loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}