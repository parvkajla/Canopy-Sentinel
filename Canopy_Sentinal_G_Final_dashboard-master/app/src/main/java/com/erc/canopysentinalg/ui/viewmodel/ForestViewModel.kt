package com.erc.canopysentinalg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erc.canopysentinalg.data.model.Country
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.erc.canopysentinalg.data.model.ForestStats
import com.erc.canopysentinalg.data.model.TimeRange
import com.erc.canopysentinalg.data.repository.ForestDataRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ForestViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ForestDataRepository()
    
    private val _forestStats = MutableLiveData<ForestStats?>()
    val forestStats: LiveData<ForestStats?> = _forestStats
    
    private val _alerts = MutableLiveData<List<DeforestationAlert>>()
    val alerts: LiveData<List<DeforestationAlert>> = _alerts
    
    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries
    
    private val _selectedCountry = MutableLiveData<Country?>()
    val selectedCountry: LiveData<Country?> = _selectedCountry
    
    private val _selectedTimeRange = MutableLiveData<TimeRange>(TimeRange.YEAR_1)
    val selectedTimeRange: LiveData<TimeRange> = _selectedTimeRange
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    init {
        loadCountries()
        loadForestStats()
        loadAlerts()
    }
    
    fun loadForestStats(countryCode: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getForestStats(countryCode)
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { stats ->
                    _forestStats.value = stats
                    _isLoading.value = false
                }
        }
    }
    
    fun loadAlerts(countryCode: String? = null, limit: Int = 50) {
        viewModelScope.launch {
            repository.getAlerts(countryCode, limit)
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { alertsList ->
                    _alerts.value = alertsList
                }
        }
    }
    
    fun loadCountries() {
        viewModelScope.launch {
            repository.getCountries()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { countriesList ->
                    _countries.value = countriesList
                }
        }
    }
    
    fun selectCountry(countryCode: String?) {
        viewModelScope.launch {
            if (countryCode == null) {
                _selectedCountry.value = null
                loadForestStats(null)
                loadAlerts(null)
            } else {
                repository.getCountryByCode(countryCode)
                    .catch { e ->
                        _errorMessage.value = e.message
                    }
                    .collect { country ->
                        _selectedCountry.value = country
                        loadForestStats(countryCode)
                        loadAlerts(countryCode)
                    }
            }
        }
    }
    
    fun setTimeRange(timeRange: TimeRange) {
        _selectedTimeRange.value = timeRange
        loadForestStats(_selectedCountry.value?.code)
    }
    
    fun refreshData() {
        loadForestStats(_selectedCountry.value?.code)
        loadAlerts(_selectedCountry.value?.code)
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
