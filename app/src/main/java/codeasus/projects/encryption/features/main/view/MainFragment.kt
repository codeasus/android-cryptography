package codeasus.projects.encryption.features.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import codeasus.projects.encryption.R
import codeasus.projects.security.crypto.keystore.AndroidKeystore
import codeasus.projects.encryption.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mNavController: NavController

    companion object {
        const val TAG = "DBG@MainFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        mNavController = findNavController()
        setData()
        setView()
        return mBinding.root
    }

    private fun setView() {
        mBinding.apply {
            btnToKeyPairFrag.setOnClickListener {
                mNavController.navigate(R.id.mainFragToPublicKeyFrag)
            }
        }
    }

    private fun setData() {
        if (!AndroidKeystore.isAESKeyGenerated()) {
            Log.d(TAG, "Could not find AES key, generating...")
            AndroidKeystore.generateSecretAESKey()
        } else {
            Log.d(TAG, "AES key exists.")
        }
    }
}