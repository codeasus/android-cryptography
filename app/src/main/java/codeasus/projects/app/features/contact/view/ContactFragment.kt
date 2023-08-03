package codeasus.projects.app.features.contact.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import codeasus.projects.app.R
import codeasus.projects.app.databinding.FragmentContactBinding
import codeasus.projects.app.features.contact.adapter.ContactAdapter
import codeasus.projects.app.features.contact.viewmodel.ContactViewModel
import codeasus.projects.app.notifications.NotificationService
import codeasus.projects.app.util.Constants
import codeasus.projects.app.worker.enqueueContactSyncWork
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : Fragment() {
    private lateinit var mBinding: FragmentContactBinding
    private lateinit var mNavController: NavController
    private lateinit var mMenuHost: MenuHost

    private lateinit var mNotificationService: NotificationService

    private val mContactAdapter by lazy { ContactAdapter() }
    private val mViewModel: ContactViewModel by viewModels()

    companion object {
        private const val TAG = "DBG@ContactFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentContactBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        mMenuHost = requireActivity()
        mNotificationService = NotificationService(requireContext())
        setup()
        setData(requireContext())
        setView(requireContext())
        return mBinding.root
    }

    private fun setup() {
        mBinding.apply {
            rvContact.adapter = mContactAdapter
        }
    }

    private fun setView(ctx: Context) {
        mMenuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_security, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_clear -> {
                        mViewModel.deleteContacts()
                        true
                    }

                    R.id.menu_generate -> {
                        if(areContactsPermissionsGranted(ctx)) {
                            enqueueContactSyncWork(ctx)
                            return true
                        } else {
                            Toast.makeText(ctx,
                                getString(R.string.permission_contact_required), Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setData(ctx: Context) {
        if (areContactsPermissionsGranted(ctx)) {
            mViewModel.getContacts()
            WorkManager.getInstance(ctx)
                .getWorkInfosForUniqueWorkLiveData(Constants.WorkManager.CONTACT_UNIQUE_WORK)
                .observe(viewLifecycleOwner) {
                    if (it.size == 0) return@observe
                    val uniqueContactSyncWork = it[0]
                    when (uniqueContactSyncWork.state) {
                        WorkInfo.State.RUNNING -> showLoading()
                        else -> hideLoading()
                    }
                }
        } else {
            MaterialAlertDialogBuilder(ctx)
                .setMessage(getString(R.string.contact_permission_description))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .show()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.contacts.collectLatest {
                if (it != null) mContactAdapter.contactDiffer.submitList(it)
            }
        }
    }

    private fun showLoading() {
        mBinding.progressBar.isVisible = true
    }

    private fun hideLoading() {
        mBinding.progressBar.isVisible = false
    }

    private fun areContactsPermissionsGranted(ctx: Context): Boolean {
        return ctx.checkSelfPermission(Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED &&
                ctx.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }
}