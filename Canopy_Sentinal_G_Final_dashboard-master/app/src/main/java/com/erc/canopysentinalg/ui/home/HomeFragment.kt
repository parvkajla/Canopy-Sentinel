package com.erc.canopysentinalg.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.erc.canopysentinalg.data.model.ForestStats
import com.erc.canopysentinalg.ui.adapter.AlertsAdapter
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private val viewModel: ForestViewModel by viewModels({ requireActivity() })
    
    private lateinit var totalAreaText: TextView
    private lateinit var deforestedAreaText: TextView
    private lateinit var forestHealthText: TextView
    private lateinit var alertsRecyclerView: RecyclerView
    private lateinit var alertsAdapter: AlertsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        totalAreaText = view.findViewById(R.id.totalAreaValue)
        deforestedAreaText = view.findViewById(R.id.deforestedAreaValue)
        forestHealthText = view.findViewById(R.id.forestHealthValue)
        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView)
        
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        alertsAdapter = AlertsAdapter { alert ->
            // Navigate to map with alert location or show details
            findNavController().navigate(R.id.navigation_map)
        }
        alertsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alertsRecyclerView.adapter = alertsAdapter
    }
    
    private fun observeViewModel() {
        viewModel.forestStats.observe(viewLifecycleOwner, Observer { stats ->
            stats?.let {
                updateStatistics(it)
            }
        })
        
        viewModel.alerts.observe(viewLifecycleOwner, Observer { alerts ->
            alertsAdapter.submitList(alerts.take(5)) // Show only 5 recent alerts
        })
    }
    
    private fun updateStatistics(stats: ForestStats) {
        animateTextChange(totalAreaText, String.format(Locale.getDefault(), "%.1f", stats.totalArea))
        animateTextChange(deforestedAreaText, String.format(Locale.getDefault(), "%.1f", stats.deforestedArea))
        animateTextChange(forestHealthText, String.format(Locale.getDefault(), "%.1f%%", stats.forestHealthPercentage))
    }
    
    private fun animateTextChange(textView: TextView, newValue: String) {
        textView.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                textView.text = newValue
                textView.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }.start()
    }
}
