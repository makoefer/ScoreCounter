package martin.scorecounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.coroutineContext

class HistoryAdapter(private val context: Context, private val mList: List<HistoryItemViewModel>, private val onListItemClick: (position: Int) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)

        return ViewHolder(view, onListItemClick)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        holder.p1Name.text = itemsViewModel.p1Name
        holder.p2Name.text = itemsViewModel.p2Name
        holder.p1Sets.text = itemsViewModel.p1Sets.toString()
        holder.p2Sets.text = itemsViewModel.p2Sets.toString()
        holder.date.text = itemsViewModel.date
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View, private val onListItemClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(ItemView) {
        val p1Name: TextView = itemView.findViewById(R.id.p1_name_history)
        val p2Name: TextView = itemView.findViewById(R.id.p2_name_history)
        val p1Sets: TextView = itemView.findViewById(R.id.p1_sets_history)
        val p2Sets: TextView = itemView.findViewById(R.id.p2_sets_history)
        val date: TextView = itemView.findViewById(R.id.date_history)

        init {
            itemView.setOnClickListener{
                onListItemClick(adapterPosition)
            }
        }
    }
}