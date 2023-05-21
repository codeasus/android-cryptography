package codeasus.projects.app.features.security.view

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
import codeasus.projects.app.R
import codeasus.projects.app.databinding.FragmentSecurityBinding
import codeasus.projects.app.features.security.viewmodel.SecurityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecurityFragment : Fragment() {
    private lateinit var mBinding: FragmentSecurityBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost

    private val viewModel: SecurityViewModel by viewModels()

    companion object {
        private const val TAG = "DBG@KeyPairFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSecurityBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        mMenuHost = requireActivity()
        setData()
        setView()
        return mBinding.root
    }

    private fun setView() {
        mMenuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_key_pair, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_key_pair_clean -> {
                        Toast.makeText(requireContext(), "Cleared key pairs", Toast.LENGTH_SHORT).show()
                        viewModel.deleteEllipticCurveKeyPairs()
                        true
                    }
                    R.id.menu_key_pair_generate -> {
                        Toast.makeText(requireContext(), "Generated key pairs", Toast.LENGTH_SHORT).show()
                        viewModel.generateEllipticCurveKeyPairs(5)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setData() {
    }
}