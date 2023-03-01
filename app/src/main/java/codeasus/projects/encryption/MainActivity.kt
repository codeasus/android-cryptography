package codeasus.projects.encryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import codeasus.projects.encryption.multiplatform.MultiplatformSampleTests

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DBG@MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MultiplatformSampleTests.setupRSA()
        MultiplatformSampleTests.encryptDataForIOS()
    }
}