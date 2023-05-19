
package codeasus.projects.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import codeasus.projects.app.databinding.ActivityMainBinding
import codeasus.projects.data.features.security.SecurityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController
    private lateinit var mNavHostFragment: NavHostFragment

    @Inject
    lateinit var securityManager: SecurityManager

    companion object {
        private const val TAG = "DBG@MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        mNavHostFragment = supportFragmentManager.findFragmentById(mBinding.fragmentContainerView.id) as NavHostFragment
        mNavController = mNavHostFragment.navController
        securityManager.initSecurity()
        setView()
        setContentView(mBinding.root)

    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp() || onSupportNavigateUp()
    }

    private fun setView() {
    }
}