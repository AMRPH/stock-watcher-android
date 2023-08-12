package com.examlpe.qxstockwatch.ui.main.notifications

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.examlpe.qxstockwatch.databinding.FragmentNotificationsBinding
import com.examlpe.qxstockwatch.ui.main.tracking.MyTrackingAdapter

class NotificationsFragment : Fragment() {

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        binding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NotificationsAdapter()
        binding.rvNotifications.adapter = adapter

        viewModel.stocksLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
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
                    viewModel.setDefault()
                }
            }

        })
    }
}