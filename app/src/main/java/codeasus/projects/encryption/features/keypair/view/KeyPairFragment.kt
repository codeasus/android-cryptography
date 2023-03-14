package codeasus.projects.encryption.features.keypair.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.encryption.databinding.FragmentKeyPairBinding
import codeasus.projects.encryption.features.keypair.viewmodel.KeyPairViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KeyPairFragment : Fragment() {

    private lateinit var mBinding: FragmentKeyPairBinding
    private lateinit var mNavController: NavController

    private val viewModel: KeyPairViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentKeyPairBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        return mBinding.root
    }
}