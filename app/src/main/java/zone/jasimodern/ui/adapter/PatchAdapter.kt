package zone.jasimodern.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import zone.jasimodern.R
import zone.jasimodern.service.PatchManager

class PatchAdapter(
    private val patches: List<PatchManager.Patch>,
    private val onClick: (PatchManager.Patch) -> Unit
) : RecyclerView.Adapter<PatchAdapter.PatchViewHolder>() {
    
    class PatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_patch_name)
        val tvVersion: TextView = view.findViewById(R.id.tv_patch_version)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patch, parent, false)
        return PatchViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PatchViewHolder, position: Int) {
        val patch = patches[position]
        holder.tvName.text = patch.name
        holder.tvVersion.text = "v${patch.version}"
        holder.itemView.setOnClickListener { onClick(patch) }
    }
    
    override fun getItemCount() = patches.size
}
