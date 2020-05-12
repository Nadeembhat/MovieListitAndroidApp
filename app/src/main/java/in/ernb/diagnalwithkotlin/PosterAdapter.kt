package `in`.ernb.diagnalwithkotlin

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Tuesday, May, 2020.
 * Copy Right (c) 11:36 AM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * Diagnal WIth Kotlin
 */


class PosterAdapter(val posterlist:ArrayList<DataModel>,val screenHeight:Int):RecyclerView.Adapter<PosterAdapter.ViewHolder>() ,
    Filterable{

    var modelList:ArrayList<DataModel> = posterlist
    var searchList:ArrayList<DataModel> = posterlist
    lateinit var onListner:OnBottomReachedListener


    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        onListner = onBottomReachedListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.postercard, parent, false)
        val params = itemView.layoutParams
        params.height = screenHeight
        itemView.layoutParams = params
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return modelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == modelList.size - 1) {
            onListner.onBottomReached(position)
        }
        val model: DataModel = modelList.get(position)
        holder.bindItems(model)
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                //if Search Bard is empty means no Character to search
                if (charString.isEmpty()) {
                    modelList = searchList
                } else if (charString.length <= 2) {
                    modelList = searchList
                } else {
                    val filteredList: ArrayList<DataModel> = ArrayList()
                    for (row in searchList) { // name match condition. this might differ depending on your requirement
                    // here we are looking for name  match
                        //val name = row.name !!.toLowerCase().contains(charString.toLowerCase())
                        if (row.name !!.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT))) {
                            filteredList.add(row)
                        } else {
                            Log.e("Filter", "\tData not Found\t")
                        }
                    }
                    modelList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = modelList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                /* modellist = (ArrayList<DataModel>) filterResults.values;*/
                modelList = filterResults.values as ArrayList<DataModel>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: DataModel) {
            val textViewName = itemView.findViewById(R.id.postername) as TextView
            val image  = itemView.findViewById(R.id.posterimage) as ImageView
            textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            textViewName.text = data.name
            when (data.poster_image) {
                1 -> image.setImageResource(R.drawable.poster1)
                2 -> image.setImageResource(R.drawable.poster2)
                3 -> image.setImageResource(R.drawable.poster3)
                4 -> image.setImageResource(R.drawable.poster4)
                5 -> image.setImageResource(R.drawable.poster5)
                6 -> image.setImageResource(R.drawable.poster6)
                7 -> image.setImageResource(R.drawable.poster7)
                8 -> image.setImageResource(R.drawable.poster8)
                9 -> image.setImageResource(R.drawable.poster9)
                else -> image.setImageResource(R.drawable.poster_not_avail)
            }
        }
    }

    interface OnBottomReachedListener{
        fun onBottomReached(position: Int)
    }

}