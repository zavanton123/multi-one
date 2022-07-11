package ru.zavanton.accounts_impl.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zavanton.accounts_api.domain.IAccountInteractor
import ru.zavanton.accounts_impl.di.AccountComponentHolder
import javax.inject.Inject

class AccountViewModel constructor(
    private val accountInteractor: IAccountInteractor,
) : ViewModel() {

    fun fetchAccountInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val info = accountInteractor.loadAccountInfo(123L)
                Log.d("zavanton", "zavanton - info: $info")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        AccountComponentHolder.clear()
    }
}

class AccountViewModelFactory @Inject constructor(
    private val accountInteractor: IAccountInteractor,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(accountInteractor) as T
    }
}
