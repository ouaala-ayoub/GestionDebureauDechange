package com.example.gestiondebureaudechangededevises.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.User
import com.example.gestiondebureaudechangededevises.data.repositories.BureauRepository
import com.example.gestiondebureaudechangededevises.data.repositories.UserRepository
import com.example.gestiondebureaudechangededevises.databinding.FragmentUserEditBinding
import com.example.gestiondebureaudechangededevises.ui.activities.UsersActivity
import com.example.gestiondebureaudechangededevises.ui.viewmodels.UserEditViewModel
import com.example.gestiondebureaudechangededevises.utils.setWithList
import com.example.gestiondebureaudechangededevises.utils.shortToast
import com.example.gestiondebureaudechangededevises.utils.updateLiveData

class UserEditFragment : Fragment() {

    companion object {
        private const val TAG = "UserEditFragment"
    }

    private var _binding: FragmentUserEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserEditViewModel =
        UserEditViewModel(UserRepository.getInstance(), BureauRepository.getInstance()).also {
            it.getBureaux()
        }
    private val args: UserEditFragmentArgs by navArgs()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = args.user
        Log.i(TAG, "user: $user")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserEditBinding.inflate(inflater, container, false)

        setUpViews()
        initialiseViews(user)

        binding.updateUser.setOnClickListener {
            viewModel.apply {
                val phone = binding.phoneEditText.text
                val email = binding.emailEditText.text
                val user = User(
                    user.id,
                    name.value.toString(),
                    desk = bureau.value.toString(),
                    userName = userName.value.toString()
                )
                if (!phone.isNullOrBlank())
                    user.phone = phone.toString()
                if (!email.isNullOrBlank())
                    user.email = email.toString()

                viewModel.putUser(user.id!!, user)
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            binding.apply {

                loading.observe(viewLifecycleOwner) { loading ->
                    binding.progressBar.isVisible = loading
                    blockUi(loading)
                }
                updated.observe(viewLifecycleOwner) { updated ->
                    if (updated != null) {
                        requireContext().shortToast(updated)
                        (requireActivity() as UsersActivity).dataUpdateListener?.onDataUpdated()
                    } else {
                        requireContext().shortToast(getString(R.string.error))
                    }
                }
                bureaux.observe(viewLifecycleOwner) { bureaux ->
                    Log.d(TAG, "bureaux: $bureaux")
                    if (bureaux != null) {
                        bureauEditText.apply {
                            val desk = bureaux.find { bureau -> bureau.id == user.desk }
                            desk?.apply {
                                setText(this.name)
                                setBureau(desk.id!!)
                            }

                            setWithList(bureaux.map { bureau -> bureau.name })
                            setOnItemClickListener { _, _, i, _ ->
                                setBureau(bureaux[i].id!!)
                            }
                        }
                    } else {
                        requireContext().shortToast(getString(R.string.error))
                    }
                }
                validData.observe(viewLifecycleOwner) { valid ->
                    Log.d(TAG, "valid: $valid")
                    updateUser.isEnabled = valid
                }
            }

        }
    }

    private fun blockUi(loading: Boolean) {
        for (v in binding.linearLayout.children) {
            v.isEnabled = !loading
        }
        binding.updateUser.isEnabled = !loading
    }

    private fun initialiseViews(user: User) {
        binding.apply {
            user.apply {
                nameEditText.setText(name)
                userNameEditText.setText(userName)
                phone?.apply {
                    phoneEditText.setText(this)
                }
                email?.apply {
                    emailEditText.setText(this)
                }
            }
        }
    }

    private fun setUpViews() {
        binding.apply {
            viewModel.apply {
                nameEditText.updateLiveData {
                    setName(it)
                }
                userNameEditText.updateLiveData {
                    setUserName(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}