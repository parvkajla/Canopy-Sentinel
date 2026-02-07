package com.erc.canopysentinalg.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.erc.canopysentinalg.data.model.Country
import com.erc.canopysentinalg.databinding.FragmentMapBinding
import com.erc.canopysentinalg.ui.viewmodel.ForestViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForestViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 1. Initialize osmdroid configuration
        val context = requireContext().applicationContext
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        Configuration.getInstance().load(context, prefs)

        // 2. Set User Agent (Prevents "Blank Map" issues)
        Configuration.getInstance().userAgentValue = context.packageName

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()
        setupUIListeners()
        observeViewModel()
    }

    private fun setupMap() {
        binding.map.apply {
            setMultiTouchControls(true)
            controller.setZoom(4.0)
            // Center on Amazon (Brazil) coordinates
            controller.setCenter(GeoPoint(-14.2350, -51.9253))
        }
    }

    private fun setupUIListeners() {
        binding.timeSlider.addOnChangeListener { _, _, _ ->
            updateMapPolygons()
        }
    }

    private fun observeViewModel() {
        viewModel.countries.observe(viewLifecycleOwner) {
            updateMapPolygons()
        }

        viewModel.selectedCountry.observe(viewLifecycleOwner) { country ->
            country?.let {
                binding.map.controller.animateTo(GeoPoint(it.latitude, it.longitude))
                binding.map.controller.setZoom(6.0)
            }
        }
    }

    private fun updateMapPolygons() {
        binding.map.overlays.clear()
        val countries = viewModel.countries.value ?: return

        countries.forEach { country ->
            drawCountryHealthBox(country)
        }

        binding.map.invalidate()
    }

    private fun drawCountryHealthBox(country: Country) {
        val health = country.forestHealthPercentage

        // AI Color Logic: Green (Healthy), Yellow (Warning), Red (Critical)
        val color = when {
            health > 80 -> Color.argb(120, 0, 255, 0)
            health > 60 -> Color.argb(120, 255, 255, 0)
            else -> Color.argb(120, 255, 0, 0)
        }

        val polygon = Polygon(binding.map)
        val rectPoints = listOf(
            GeoPoint(country.latitude + 2.0, country.longitude - 2.0),
            GeoPoint(country.latitude + 2.0, country.longitude + 2.0),
            GeoPoint(country.latitude - 2.0, country.longitude + 2.0),
            GeoPoint(country.latitude - 2.0, country.longitude - 2.0)
        )

        polygon.apply {
            points = rectPoints
            fillPaint.color = color
            outlinePaint.color = Color.BLACK
            outlinePaint.strokeWidth = 2f
            title = "${country.name}: ${health.toInt()}% Health"
        }

        binding.map.overlays.add(polygon)
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}