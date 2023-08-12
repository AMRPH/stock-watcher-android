package com.examlpe.qxstockwatch.ui.main.add

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
import com.examlpe.qxstockwatch.databinding.FragmentAddTrackingBinding
import com.examlpe.qxstockwatch.databinding.FragmentNotificationsBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddTrackingFragment : Fragment() {

    private lateinit var viewModel: AddTrackingViewModel
    private lateinit var binding: FragmentAddTrackingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[AddTrackingViewModel::class.java]
        binding = FragmentAddTrackingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AddStockAdapter(viewModel)
        binding.rvAdd.adapter = adapter

        viewModel.stocksLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        viewModel.addedLiveData.observe(viewLifecycleOwner){
            if (it != null){
                showDialogCancel(it)
            }
        }

        binding.etSeacrh.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    viewModel.getStocksByString(s.toString())
                } else{
                    viewModel.setEmpty()
                }
            }
        })
    }

    private fun showDialogCancel(stock: StockDB) {
        val snack: Snackbar = Snackbar.make(binding.cl, "${stock.symbol} added", Snackbar.LENGTH_LONG)
        snack.setAction("Cancel", View.OnClickListener {
            App.dbRepository.update(stock)
        })
        snack.show()
        viewModel.addedLiveData.postValue(null)
    }
}