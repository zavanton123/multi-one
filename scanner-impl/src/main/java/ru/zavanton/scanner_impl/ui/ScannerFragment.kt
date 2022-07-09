package ru.zavanton.scanner_impl.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import ru.zavanton.mylibrary.ACCOUNTS_SCREEN
import ru.zavanton.mylibrary.TRANSACTIONS_SCREEN
import ru.zavanton.scanner_impl.databinding.FragmentScannerBinding
import ru.zavanton.scanner_impl.di.ScannerComponentInjector
import javax.inject.Inject

class ScannerFragment : Fragment() {

    @Inject
    lateinit var factory: ScannerViewModelFactory

    private val viewModel by viewModels<ScannerViewModel> {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ScannerComponentInjector
            .getScannerComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentScannerBinding.inflate(inflater, container, false)

        binding.tvDemo.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri(Uri.parse(ACCOUNTS_SCREEN))
                .build()
            findNavController().navigate(request)
        }

        binding.tvDemo.setOnLongClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri(Uri.parse(TRANSACTIONS_SCREEN))
                .build()
            findNavController().navigate(request)

            // viewModel.loadData()
            true
        }

        return binding.root
    }
}
