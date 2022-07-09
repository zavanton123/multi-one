package ru.zavanton.transactions_impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.zavanton.transactions_impl.databinding.FragmentTransactionsBinding

class TransactionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        binding.tvTransaction.setOnClickListener {

        }

        return binding.root
    }
}
