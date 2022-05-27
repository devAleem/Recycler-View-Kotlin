/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

/**
 * [ListAdapter] must be imported from [androidx.recyclerview.widget.ListAdapter]
 * we use [ListAdapter] rather than [RecyclerView.Adapter] because we want to implement Recycler View
 * backed by a list that can be used by diff util to check whether a there is a change or not
 * Now list will be tracked and Recycler View will be notified when there will be change
 */
class SleepNightAdapter : ListAdapter<SleepNight,SleepNightAdapter.ViewHolder>(SleepNightCallback()){
    /**
     * We can implement Recycler View like below using [notifyDataSetChanged] but when there is a change
     * all the list is updated and views are redrawn that gives poor performance
     * Therefore we are implementing [SleepNightCallback] so that we can improve performance and using [ListAdapter] rather
     * than [RecyclerView.Adapter]
     */


    /**
     * commented code below because now we are using [ListAdapter]
     * [ListAdapter] will take care of list
     */
//    var data = listOf<SleepNight>()
//        @SuppressLint("NotifyDataSetChanged")
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /**
         * Now no need to to data[position] because data is commented out and now list is managed by
         * [ListAdapter] use getItem(position) instead to get item
         */
//        val item: SleepNight = data[position]

        val item =  getItem(position)
        holder.bind(item)
    }


    /**
     * There is no need to override [getItemCount]
     * [ListAdapter] will take care of it
     */
//    override fun getItemCount(): Int {
//       return data.size
//    }

    class ViewHolder private constructor(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        private val quality: TextView = itemView.findViewById(R.id.quality_string)
        private val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
        fun bind(
            item: SleepNight
        ) {

            val res = itemView.context.resources
            sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)

            qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_active
                }
            )

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }
    }

}

/**
 * Using [SleepNightCallback] to improve performance of Recycler view
 * [SleepNightCallback] inherits from [DiffUtil.ItemCallback]
 * [DiffUtil.ItemCallback] has two methods that must be overridden
 */

class SleepNightCallback:DiffUtil.ItemCallback<SleepNight>(){
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
    return oldItem == newItem
    }
}
