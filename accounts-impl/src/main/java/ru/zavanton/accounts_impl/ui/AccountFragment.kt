package ru.zavanton.accounts_impl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.zavanton.accounts_impl.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.tvAccount.setOnClickListener {

        }
        return binding.root
    }
}
