package codeasus.projects.data.features.security

import codeasus.projects.security.crypto.keyprotector.KeyProtector
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityManager @Inject constructor(
    private val keyProtector: KeyProtector
)  {

    fun initSecurity() {
        keyProtector.initialize()
    }
}