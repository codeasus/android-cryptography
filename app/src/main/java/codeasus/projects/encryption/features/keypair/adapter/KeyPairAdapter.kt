package codeasus.projects.encryption.features.keypair.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import codeasus.projects.encryption.data.model.features.keypair.KeyPair
import codeasus.projects.encryption.databinding.RvListItemKeypairBinding

class KeyPairAdapter(val onKeyPairClicked: (KeyPair) -> Unit) :
    RecyclerView.Adapter<KeyPairAdapter.ViewHolder>() {

    private var oldKeyPairs = emptyList<KeyPair>()

    private class KeyPairDiffUtil(
        private val oldList: List<KeyPair>,
        private val newList: List<KeyPair>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ViewHolder(private val binding: RvListItemKeypairBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(keyPair: KeyPair) = binding.apply {
            tvId.text = keyPair.id.toString()
            tvPublicKey.text = keyPair.publicKey
            tvPrivateKey.text = keyPair.privateKey
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvListItemKeypairBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldKeyPairs[position])
    }

    override fun getItemCount(): Int {
        return oldKeyPairs.size
    }

    fun setData(newKeyPairs: List<KeyPair>) {
        val diffUtil = KeyPairDiffUtil(oldKeyPairs, newKeyPairs)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldKeyPairs = newKeyPairs
        diffResult.dispatchUpdatesTo(this)
    }
}