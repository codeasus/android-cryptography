package codeasus.projects.encryption.features.publickey.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.encryption.databinding.FragmentPublicKeyBinding

class PublicKeyFragment : Fragment() {

    private lateinit var mBinding: FragmentPublicKeyBinding
    private lateinit var mNavController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPublicKeyBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        return mBinding.root
    }
}