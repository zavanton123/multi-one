package ru.zavanton.scanner_impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.zavanton.scanner_impl.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentScannerBinding.inflate(inflater, container, false)

        return binding.root
    }
}
