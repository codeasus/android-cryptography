package codeasus.projects.app.features.main.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import codeasus.projects.app.R
import codeasus.projects.app.databinding.FragmentMainBinding
import codeasus.projects.app.util.Constants
import codeasus.projects.app.worker.ContactSyncWorker

class MainFragment : Fragment() {
    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mNavController: NavController

    companion object {
        private const val TAG = "DBG@MainFragment"
    }

    private val permissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS
            )
        else
            arrayOf(
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS
            )

    private val permissionsRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.READ_CONTACTS] == true && it[Manifest.permission.WRITE_CONTACTS] == true) {
                Log.d(TAG, "[ALL]: Contact permissions granted.")
                enqueueContactSyncWork(requireContext())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(requireContext())
        setView()
    }

    private fun setup(ctx: Context) {
        registerPermissions(ctx)
    }

    private fun setView() {
        mBinding.apply {
            btnToContactScreen.setOnClickListener {
                mNavController.navigate(R.id.mainFragToContactFrag)
            }
        }
    }

    private fun registerPermissions(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isPostNotificationPermissionGranted(ctx) || !areContactsPermissionGranted(ctx)) {
                permissionsRequestLauncher.launch(permissions)
            }
        } else {
            if (!areContactsPermissionGranted(ctx)) {
                permissionsRequestLauncher.launch(permissions)
            }
        }
    }

    private fun areContactsPermissionGranted(ctx: Context): Boolean {
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isPostNotificationPermissionGranted(ctx: Context): Boolean {
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun getContactSyncWorkRequest(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<ContactSyncWorker>()
            .addTag(Constants.WorkManager.CONTACT_SYNC)
            .build()
    }

    private fun enqueueContactSyncWork(ctx: Context) {
        WorkManager.getInstance(ctx).enqueueUniqueWork(
            Constants.WorkManager.CONTACT_UNIQUE_WORK,
            ExistingWorkPolicy.KEEP,
            getContactSyncWorkRequest()
        )
    }
}