package ru.zavanton.transactions_impl.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.zavanton.transactions_api.ITransactionInteractor
import javax.inject.Inject

class TransactionsViewModel constructor(
    private val transactionInteractor: ITransactionInteractor,
) : ViewModel() {

    fun fetchTransactionInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val info = transactionInteractor.loadTransactionInfo(123L)
                Log.d("zavanton", "zavanton - info: $info")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // todo - clear the component
    }
}

class TransactionsViewModelFactory @Inject constructor(
    private val transactionInteractor: ITransactionInteractor,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionsViewModel(transactionInteractor) as T
    }
}
