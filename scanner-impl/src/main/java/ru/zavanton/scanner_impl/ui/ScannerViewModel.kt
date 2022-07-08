package ru.zavanton.scanner_impl.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zavanton.db_api.GithubRepoEntity
import ru.zavanton.db_api.IAppDatabaseDao
import ru.zavanton.scanner_impl.data.ScannerNetworkService
import javax.inject.Inject

class ScannerViewModel(
    private val scannerNetworkService: ScannerNetworkService,
    private val dao: IAppDatabaseDao,
) : ViewModel() {

    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                dao.delete()

                val repos = scannerNetworkService.load()
                    .onEach {
                        Log.d("zavanton", "zavanton - repo: ${it.name}")
                    }
                    .map { githubRepo ->
                        GithubRepoEntity(
                            id = githubRepo.id,
                            name = githubRepo.name
                        )
                    }

                dao.insert(repos)
            }
        }
    }
}

class ScannerViewModelFactory @Inject constructor(
    private val scannerNetworkService: ScannerNetworkService,
    private val dao: IAppDatabaseDao,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScannerViewModel(scannerNetworkService, dao) as T
    }
}
