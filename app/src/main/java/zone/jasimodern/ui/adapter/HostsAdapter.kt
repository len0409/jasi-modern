package zone.jasimodern.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import zone.jasimodern.R
import zone.jasimodern.service.HostsManager

class HostsAdapter(
    private val rules: List<HostsManager.HostsRule>,
    private val onItemClick: (HostsManager.HostsRule) -> Unit
) : RecyclerView.Adapter<HostsAdapter.HostsViewHolder>() {
    
    class HostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDomain: TextView = view.findViewById(R.id.tv_hosts_domain)
        val tvIP: TextView = view.findViewById(R.id.tv_hosts_ip)
        val tvCategory: TextView = view.findViewById(R.id.tv_hosts_category)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hosts, parent, false)
        return HostsViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: HostsViewHolder, position: Int) {
        val rule = rules[position]
        holder.tvDomain.text = rule.domain
        holder.tvIP.text = rule.ip
        holder.tvCategory.text = rule.category
        holder.itemView.setOnClickListener { onItemClick(rule) }
    }
    
    override fun getItemCount() = rules.size
}