package ru.zavanton.scanner_impl.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import javax.inject.Inject

class ScannerViewModel(
    private val scannerNetworkService: ScannerNetworkService,
) : ViewModel() {

    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                scannerNetworkService.load()
                    .forEach {
                        Log.d("zavanton", "zavanton - repo: ${it.name}")
                    }
            }
        }
    }
}

class ScannerViewModelFactory @Inject constructor(
    private val scannerNetworkService: ScannerNetworkService,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScannerViewModel(scannerNetworkService) as T
    }
}
