package codeasus.projects.app.features.security.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import codeasus.projects.data.features.security.model.EllipticCurveKeyPair
import codeasus.projects.app.databinding.RvListItemKeypairBinding

class ECKeyPairAdapter(val onECKeyPairClicked: (EllipticCurveKeyPair) -> Unit) :
    RecyclerView.Adapter<ECKeyPairAdapter.ViewHolder>() {

    private var oldEllipticCurveKeyPairs = emptyList<EllipticCurveKeyPair>()

    private class KeyPairDiffUtil(
        private val oldList: List<EllipticCurveKeyPair>,
        private val newList: List<EllipticCurveKeyPair>
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

        fun bind(EllipticCurveKeyPair: EllipticCurveKeyPair) = binding.apply {
            tvId.text = EllipticCurveKeyPair.id.toString()
            tvPublicKey.text = EllipticCurveKeyPair.publicKey
            tvPrivateKey.text = EllipticCurveKeyPair.privateKey
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
        holder.bind(oldEllipticCurveKeyPairs[position])
    }

    override fun getItemCount(): Int {
        return oldEllipticCurveKeyPairs.size
    }

    fun setData(newEllipticCurveKeyPairs: List<EllipticCurveKeyPair>) {
        val diffUtil = KeyPairDiffUtil(oldEllipticCurveKeyPairs, newEllipticCurveKeyPairs)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldEllipticCurveKeyPairs = newEllipticCurveKeyPairs
        diffResult.dispatchUpdatesTo(this)
    }
}