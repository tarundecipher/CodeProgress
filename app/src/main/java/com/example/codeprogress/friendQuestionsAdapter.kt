package com.example.codeprogress

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class friendQuestionsAdapter(val items:MutableList<FriendQuestions.Submissions> ): RecyclerView.Adapter<questionViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): questionViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemfriendquestions,
            parent,false)
        return questionViewholder(view)
    }

    override fun onBindViewHolder(holder: questionViewholder, position: Int) {
         val currentItem = items[position]
         holder.platform.text = currentItem.platform
         holder.question.text = currentItem.name
         holder.verdict.text = currentItem.verdict
         if(currentItem.verdict=="Accepted"){
             holder.verdict.setTextColor(Color.parseColor("#00FF00"))
         }
        else{
            holder.verdict.setTextColor((Color.parseColor("#FF0000")))
         }
         holder.time.text = currentItem.time
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class questionViewholder(itemView: View):RecyclerView.ViewHolder(itemView){
     val platform:TextView = itemView.findViewById(R.id.platform)
     val question:TextView = itemView.findViewById(R.id.question)
     val verdict:TextView = itemView.findViewById(R.id.verdict)
     val time:TextView = itemView.findViewById(R.id.time)

}