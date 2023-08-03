package codeasus.projects.app.features.security.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import codeasus.projects.app.databinding.RvContactBinding
import codeasus.projects.data.features.contact.model.Contact
import java.util.Objects

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private val contactDiffUtil = object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return Objects.hashCode(oldItem) == Objects.hashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return Objects.equals(oldItem, newItem)
        }
    }

    val contactDiffer = AsyncListDiffer(this, contactDiffUtil)

    companion object {
        private fun getInitial(str: String): String {
            return if (str.isNotEmpty()) str[0].toString() else ""
        }
    }

    class ContactViewHolder(private val binding: RvContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.apply {
                contact.displayName?.split(" ")?.let {
                    if (it.size == 1) {
                        tvInitials.text = getInitial(it[0])
                    } else if (it.size >= 2) {
                        val initials = getInitial(it[0]) + getInitial(it[1])
                        tvInitials.text = initials
                    }
                }
                tvDisplayName.text = contact.displayName
                tvPhoneNumber.text = contact.phoneNumber
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = RvContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactDiffer.currentList[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contactDiffer.currentList.size
    }
}