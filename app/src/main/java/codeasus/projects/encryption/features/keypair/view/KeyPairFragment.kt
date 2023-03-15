package codeasus.projects.encryption.features.keypair.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.encryption.R
import codeasus.projects.encryption.databinding.FragmentKeyPairBinding
import codeasus.projects.encryption.features.keypair.adapter.KeyPairAdapter
import codeasus.projects.encryption.features.keypair.viewmodel.KeyPairViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeyPairFragment : Fragment() {

    private lateinit var mBinding: FragmentKeyPairBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost
    private lateinit var mKeyPairAdapter: KeyPairAdapter

    private val viewModel: KeyPairViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentKeyPairBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        mMenuHost = requireActivity()
        setData()
        setView()
        return mBinding.root
    }

    private fun setView() {
        mBinding.apply {
            rvKeypair.adapter = mKeyPairAdapter
        }

        mMenuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_key_pair, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_key_pair_clean -> {
                        Toast.makeText(requireContext(), "Cleared key pairs", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setData() {
        mKeyPairAdapter = KeyPairAdapter() {
        }
    }
}