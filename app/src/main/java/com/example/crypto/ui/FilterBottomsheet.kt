package com.example.crypto.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crypto.data.model.Filter
import com.example.crypto.databinding.BottomsheetFilterBinding
import com.example.crypto.databinding.FilterChipBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomsheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetFilterBinding? = null
    private val binding get() = _binding!!

    private val filters by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(ARGUMENT_KEY_FILTERS, Filter::class.java)
        } else {
            arguments?.getParcelableArray(ARGUMENT_KEY_FILTERS)
        } as? Array<Filter>
    }

    private var filterListener: ((selectedFilters: List<Int>?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filters?.forEach {
            val chip = FilterChipBinding.inflate(layoutInflater)
            chip.root.text = it.filterName
            chip.root.id = it.filterId
            chip.root.isChecked = it.isSelected
            binding.chipGroup.addView(chip.root)
        }

        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            filterListener?.invoke(checkedIds)
        }
    }

    fun attachFilterListener(filterListener: ((selectedFilters: List<Int>?) -> Unit)? = null) {
        this.filterListener = filterListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGUMENT_KEY_FILTERS = "filters"
        fun getInstance(filters: Array<Filter>): FilterBottomsheet {
            return FilterBottomsheet().apply {
                val bundle = Bundle()
                bundle.putParcelableArray(ARGUMENT_KEY_FILTERS, filters)
                arguments = bundle
            }
        }
    }

}