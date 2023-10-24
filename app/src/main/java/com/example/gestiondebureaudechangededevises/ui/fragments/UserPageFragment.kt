package com.example.gestiondebureaudechangededevises.ui.fragments

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentUserPageBinding
import com.example.gestiondebureaudechangededevises.ui.viewmodels.UserPageViewModel
import com.example.gestiondebureaudechangededevises.utils.shortToast

class UserPageFragment : Fragment() {

    companion object {
        private const val TAG = "UserPageFragment"
    }

    private var _binding: FragmentUserPageBinding?=null
    private val binding get() = _binding!!
    private val viewModel: UserPageViewModel = UserPageViewModel(UserRepository.getInstance())
    private val args: UserPageFragmentArgs by navArgs()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = args.userId
        viewModel.getUserById(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            user.observe(viewLifecycleOwner) { user ->
                Log.d(TAG, "user: $user")
                if (user != null) {
                    val phone = user.phone
                    binding.apply {
                        usernameTextView.text =
                            getString(R.string.user_name_placeholder, user.userName)
                        nameTextView.text = getString(R.string.name_placeholder, user.name)
                        if (phone != null){
                            phoneTextView.text =
                                getString(R.string.phone_placeholder, phone)
                        } else{
                            phoneTextView.text =
                                getString(R.string.phone_placeholder, "-")
                        }
                        emailTextView.text =
                            getString(R.string.email_placeholder, user.email.nullToEmptyString())

                    }
                } else {
                    requireContext().shortToast(getString(R.string.error))
                    findNavController().popBackStack()
                }
            }
            loading.observe(viewLifecycleOwner) { loading ->
                binding.userPageProgressBar.isVisible = loading
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

fun Any?.nullToEmptyString(): String {
    return this?.toString() ?: "-"
}
