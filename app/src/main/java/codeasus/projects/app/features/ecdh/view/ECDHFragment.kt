package codeasus.projects.app.features.ecdh.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.app.R
import codeasus.projects.app.databinding.FragmentEcKeyPairBinding
import codeasus.projects.app.features.ecdh.adapter.ECKeyPairAdapter
import codeasus.projects.app.features.ecdh.viewmodel.ECDHViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ECDHFragment : Fragment() {

    private lateinit var mBinding: FragmentEcKeyPairBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost
    private lateinit var mECKeyPairAdapter: ECKeyPairAdapter

    private val viewModel: ECDHViewModel by viewModels()

    companion object {
        private const val TAG = "DBG@KeyPairFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEcKeyPairBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        mMenuHost = requireActivity()
        setData()
        setView()
        return mBinding.root
    }

    private fun setView() {
        mECKeyPairAdapter = ECKeyPairAdapter {

        }

        mBinding.apply {
            rvKeypair.adapter = mECKeyPairAdapter
        }

        mMenuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_key_pair, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_key_pair_clean -> {
                        Toast.makeText(requireContext(), "Cleared key pairs", Toast.LENGTH_SHORT)
                            .show()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ecKeyPairs.collectLatest {
                it?.let {
                    mECKeyPairAdapter.setData(it)
                }
            }
        }
    }
}