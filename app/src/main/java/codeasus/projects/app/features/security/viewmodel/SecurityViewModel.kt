package codeasus.projects.app.features.security.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import codeasus.projects.data.features.contact.model.Contact
import codeasus.projects.data.features.contact.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    application: Application,
    private val contactRepository: ContactRepository
) : AndroidViewModel(application) {

    val contacts = MutableStateFlow<List<Contact>?>(null)

    fun insertContacts(contacts: Set<Contact>) {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.insertContacts(contacts)
        }
    }

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            contactRepository.getContactsAsFlow().collectLatest {
                contacts.emit(it)
            }
        }
    }
}