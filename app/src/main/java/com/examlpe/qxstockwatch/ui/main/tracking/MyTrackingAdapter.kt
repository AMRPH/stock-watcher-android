package com.examlpe.qxstockwatch.ui.main.tracking

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.databinding.ItemMyTrackingBinding
import kotlin.math.roundToInt


class MyTrackingAdapter(viewModel: MyTrackingViewModel): RecyclerView.Adapter<MyTrackingAdapter.AddStockViewHolder>() {

    private var listStocks: MutableList<StockDB> = mutableListOf()

    private val viewModel: MyTrackingViewModel

    init {
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStockViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_tracking, parent, false)

        return AddStockViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listStocks.size
    }

    override fun onBindViewHolder(holder: AddStockViewHolder, position: Int) {
        val stock = listStocks[position]
        if (stock.cost == 0F){
            App.netRepository.quote(stock.symbol)
                .subscribe({
                    stock.cost = (it.c!! * 100).roundToInt() / 100.0F
                    stock.day = (it.d!! * 100).roundToInt() / 100.0F
                    stock.dayPerc = (it.dp!! * 100).roundToInt() / 100.0F
                    App.dbRepository.update(stock)
                }, {
                }).let {  }
        }


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

    fun hideKeyboard(view: View){
        val imm: InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    inner class AddStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = ItemMyTrackingBinding.bind(itemView)

        fun onBind(data: StockDB){
            binding.tvName.text = data.name
            binding.tvSymbol.text = data.symbol
            binding.etPrice.setText("")

            if (data.cost != 0F){
                if (data.day >= 0F){
                    binding.tvCost.text = "${data.cost}$"
                    binding.tvDay.text = "+${data.day}$"
                    binding.tvDayPerc.text = "+${data.dayPerc}%"
                    binding.ivTrend.setImageResource(R.drawable.ic_up_green)
                    binding.tvCost.setTextColor(itemView.context.getColor(R.color.green1))
                    binding.tvDay.setTextColor(itemView.context.getColor(R.color.green1))
                    binding.tvDayPerc.setTextColor(itemView.context.getColor(R.color.green2))
                } else {
                    binding.tvCost.text = "${data.cost}$"
                    binding.tvDay.text = "${data.day}$"
                    binding.tvDayPerc.text = "${data.dayPerc}%"
                    binding.ivTrend.setImageResource(R.drawable.ic_down_red)
                    binding.tvCost.setTextColor(itemView.context.getColor(R.color.red1))
                    binding.tvDay.setTextColor(itemView.context.getColor(R.color.red1))
                    binding.tvDayPerc.setTextColor(itemView.context.getColor(R.color.red2))
                }
            }

            if (data.isMark){
                binding.tvMark.text = "${data.mark}$"
                binding.tvLot.text = "1 lot"
            } else {
                binding.tvMark.text = ""
                binding.tvLot.text = ""
            }

            if (data.isMarkReached){
                binding.tvMark.setTextColor(itemView.context.getColor(R.color.blue))
                binding.clMark.visibility = View.VISIBLE
            } else {
                binding.tvMark.setTextColor(itemView.context.getColor(R.color.beige))
                binding.clMark.visibility = View.GONE
            }

            if (data.isNotification){
                binding.btnNotif.setImageResource(R.drawable.ic_notif_grey)
            } else {
                binding.btnNotif.setImageResource(R.drawable.ic_notif_mute)
            }

            binding.btnTrash.setOnClickListener {
                viewModel.deletedLiveData.postValue(data.copy())
                data.isTracking = false
                data.mark = 0F
                data.isMark = false
                data.isMarkReached = false
                data.isNotification = false
                data.trendMark = ""
                App.dbRepository.update(data)
            }

            binding.btnNotif.setOnClickListener {
                data.isNotification = !data.isNotification
                App.dbRepository.update(data)
            }

            binding.btnSetPrice.setOnClickListener {
                val price = binding.etPrice.text.toString().toFloatOrNull()
                if (price != null && price > 0F && data.cost != price){
                    data.mark = price
                    data.isMark = true
                    data.isMarkReached = false
                    if (data.mark > data.cost) {
                        data.trendMark = "up"
                    } else {
                        data.trendMark = "down"
                    }
                    App.dbRepository.update(data)
                    binding.etPrice.setText("")

                    hideKeyboard(itemView)
                }
            }

            binding.btnResetPrice.setOnClickListener {
                if (data.isMark){
                    data.mark = 0F
                    data.isMark = false
                    data.isMarkReached = false
                    data.trendMark = ""
                    App.dbRepository.update(data)

                    hideKeyboard(itemView)
                }
            }
        }
    }
}