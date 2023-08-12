package com.examlpe.qxstockwatch.ui.main.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.databinding.ItemNotificationsBinding

class NotificationsAdapter(): RecyclerView.Adapter<NotificationsAdapter.AddStockViewHolder>() {

    private var listStocks: MutableList<StockDB> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStockViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)

        return AddStockViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listStocks.size
    }

    override fun onBindViewHolder(holder: AddStockViewHolder, position: Int) {
        val stock = listStocks[position]
        getInfo(stock, holder)
    }

    private fun getInfo(data: StockDB, holder: AddStockViewHolder){
        holder.onBind(data)
    }

    fun setItems(it: List<StockDB>) {
        listStocks.clear()
        listStocks.addAll(it)
        notifyDataSetChanged()
    }


    inner class AddStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ItemNotificationsBinding.bind(itemView)

        fun onBind(data: StockDB){
            binding.tvName.text = data.name

            if (data.isNotification){
                binding.btnNotif.setImageResource(R.drawable.ic_notif_grey)
            } else {
                binding.btnNotif.setImageResource(R.drawable.ic_notif_mute)
            }

            if (data.isMarkReached){
                binding.btnNotif.setImageResource(R.drawable.ic_notif_blue)
                binding.clMark.visibility = View.VISIBLE
            } else {
                binding.clMark.visibility = View.GONE
            }

            binding.btnNotif.setOnClickListener {
                data.isNotification = !data.isNotification
                App.dbRepository.update(data)
            }
        }
    }
}