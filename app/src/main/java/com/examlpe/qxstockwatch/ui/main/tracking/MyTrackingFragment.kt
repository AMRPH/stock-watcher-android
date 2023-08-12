package com.examlpe.qxstockwatch.ui.main.tracking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.databinding.FragmentMyTrackingBinding
import com.google.android.material.snackbar.Snackbar

class MyTrackingFragment : Fragment() {

    private lateinit var viewModel: MyTrackingViewModel
    private lateinit var binding: FragmentMyTrackingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[MyTrackingViewModel::class.java]
        binding = FragmentMyTrackingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MyTrackingAdapter(viewModel)
        binding.rvTracking.adapter = adapter

        viewModel.stocksLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        viewModel.deletedLiveData.observe(viewLifecycleOwner){
            if (it != null){
                showDialogCancel(it)
            }
        }

        binding.etSeacrh.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    viewModel.getStocksByString(s.toString())
                } else{
                    viewModel.setDefault()
                }
            }

        })
    }

    private fun showDialogCancel(stock: StockDB) {
        val snack: Snackbar = Snackbar.make(binding.cl, "${stock.symbol} deleted", Snackbar.LENGTH_LONG)
        snack.setAction("Cancel", View.OnClickListener {
            App.dbRepository.update(stock)
        })
        snack.show()
        viewModel.deletedLiveData.postValue(null)
    }
}