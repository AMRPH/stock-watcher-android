package com.examlpe.qxstockwatch.ui.main.add

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.databinding.ItemAddTrackingBinding
import com.examlpe.qxstockwatch.ui.main.tracking.MyTrackingViewModel
import io.finnhub.api.models.CompanyProfile2

class AddStockAdapter(viewModel: AddTrackingViewModel): RecyclerView.Adapter<AddStockAdapter.AddStockViewHolder>() {

    private var listStocks: MutableList<StockDB> = mutableListOf()

    private val viewModel: AddTrackingViewModel

    init {
        this.viewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStockViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add_tracking, parent, false)

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
        private val binding = ItemAddTrackingBinding.bind(itemView)

        fun onBind(data: StockDB){
            binding.tvName.text = data.name
            binding.tvSymbol.text = data.symbol

            binding.btnAdd.setOnClickListener {
                viewModel.addedLiveData.postValue(data.copy())
                data.isTracking = true
                App.dbRepository.update(data)
            }
        }
    }
}