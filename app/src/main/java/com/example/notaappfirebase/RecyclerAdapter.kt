package com.example.notaappfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*

class RecyclerAdapter (val activity: MainActivity, val NoteList:ArrayList<List<Any>>): RecyclerView.Adapter<RecyclerAdapter.itemViewHolder>() {
    class itemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        return itemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view,parent,false
            ))
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        var id=NoteList[position][0]
        var note=NoteList[position][1]

        holder.itemView.apply {
            tvNoteNum.text="Note $position: "
            tvNote.text=note.toString()

            EditActionButton.setOnClickListener(){
                activity.updataNote(id.toString())
            }

            DelActionButton.setOnClickListener(){
                activity.delNote(id.toString())
            }

        }//end holder
    }//end onBindViewHolder

    override fun getItemCount(): Int= NoteList.size
}