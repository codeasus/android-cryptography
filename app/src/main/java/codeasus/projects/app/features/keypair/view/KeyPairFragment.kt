package codeasus.projects.app.features.keypair.view

import android.os.Bundle
import android.util.Log
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
import codeasus.projects.app.databinding.FragmentKeyPairBinding
import codeasus.projects.app.features.keypair.adapter.KeyPairAdapter
import codeasus.projects.app.features.keypair.viewmodel.KeyPairViewModel
import codeasus.projects.security.multiplatform.MultiplatformCryptography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KeyPairFragment : Fragment() {

    private lateinit var mBinding: FragmentKeyPairBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost
    private lateinit var mKeyPairAdapter: KeyPairAdapter

    private val viewModel: KeyPairViewModel by viewModels()

    companion object {
        private const val TAG = "DBG@KeyPairFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentKeyPairBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        mMenuHost = requireActivity()
        MultiplatformCryptography.testMessageCryptographyWithArgon2("Message 🍺🍺🍺🍺🍺")
//        MultiplatformSampleTests.testMessageCryptographyWithHKDF()
//        MultiplatformSampleTests.testMessageCryptographyWithArgon2()
//        val threadForHKDF = Thread {
//            val numberOfCycles = 500
//            val res = MultiplatformCryptography.benchmarkHKDF(numberOfCycles)
//            Log.i(TAG, "Based on $numberOfCycles cycles, average single HKDF hash generation time: ${res}Ns")
//        }
//        threadForHKDF.start()
//        val threadForArgon2 = Thread {
//            val numberOfCycles = 10
//            val res = MultiplatformCryptography.benchmarkArgon2(numberOfCycles)
//            Log.i(TAG, "Result of generating Argon2 hashes in $numberOfCycles cycles.")
//            val sortedRes = res.entries.sortedBy { it.value }
//            sortedRes.forEach { entry ->
//                Log.w(TAG, "${entry.key}: ${entry.value}")
//            }
//        }
//        threadForArgon2.start()
        setData()
        setView()
        return mBinding.root
    }

    private fun setView() {
        mKeyPairAdapter = KeyPairAdapter {

        }

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
                        viewModel.deleteAllECKeyPairs()
                        Toast.makeText(requireContext(), "Cleared key pairs", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }
                    R.id.menu_key_pair_generate -> {
                        viewModel.generateECKeyPairs(10)
                        Toast.makeText(requireContext(), "Generated key pairs", Toast.LENGTH_SHORT)
                            .show()
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
                    mKeyPairAdapter.setData(it)
                }
            }
        }
    }
}