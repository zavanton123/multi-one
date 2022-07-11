package ru.zavanton.transactions_impl.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.zavanton.transactions_impl.databinding.FragmentTransactionsBinding
import ru.zavanton.transactions_impl.di.TransactionComponentHolder
import javax.inject.Inject

class TransactionsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TransactionsViewModelFactory
    private val viewModel by viewModels<TransactionsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TransactionComponentHolder
            .accessTransactionComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        binding.tvTransaction.setOnClickListener {
            viewModel.fetchTransactionInfo()
        }

        return binding.root
    }
}
