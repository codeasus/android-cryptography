package codeasus.projects.app.features.security.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.app.R
import codeasus.projects.app.databinding.FragmentSecurityBinding
import codeasus.projects.app.features.security.adapter.ContactAdapter
import codeasus.projects.app.features.security.viewmodel.SecurityViewModel
import codeasus.projects.data.features.contact.util.ContactUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SecurityFragment : Fragment() {
    private lateinit var mBinding: FragmentSecurityBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost
    private val mContactAdapter by lazy { ContactAdapter() }

    private val viewModel: SecurityViewModel by viewModels()

    companion object {
        private val TAG = "DBG@${SecurityFragment::class.java.name}"
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val allPermissionGranted = it.keys.all { true }
        if (allPermissionGranted) {
            viewModel.getContacts()
        }
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
                menuInflater.inflate(R.menu.menu_security, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_clear -> {
                        Toast.makeText(requireContext(), "Cleared", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_generate -> {
                        Toast.makeText(requireContext(), "Generated", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        mBinding.rvContact.adapter = mContactAdapter
    }

    private fun setData() {
        val ctx = requireContext()
        if (ContextCompat.checkSelfPermission(ctx, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
             && ContextCompat.checkSelfPermission(ctx, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.getContacts()
//            viewModel.insertContacts(
//                ContactUtility.getDeviceContactsMappedByPhoneNumber(requireContext()).values.toSet()
//            )
        } else {
            requestPermissionLauncher.launch(PERMISSIONS)
        }

        viewLifecycleOwner.lifecycleScope.launch() {
            viewModel.contacts.collectLatest {
                mContactAdapter.contactDiffer.submitList(it)
            }
        }
    }
}