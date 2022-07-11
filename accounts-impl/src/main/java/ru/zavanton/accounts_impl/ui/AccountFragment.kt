package ru.zavanton.accounts_impl.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.zavanton.accounts_impl.databinding.FragmentAccountBinding
import ru.zavanton.accounts_impl.di.AccountComponentHolder
import javax.inject.Inject

class AccountFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AccountViewModelFactory
    private val viewModel by viewModels<AccountViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AccountComponentHolder
            .getAccountComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.tvAccount.setOnClickListener {
            viewModel.fetchAccountInfo()
        }
        return binding.root
    }
}
