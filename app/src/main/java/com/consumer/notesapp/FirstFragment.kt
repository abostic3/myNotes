package com.consumer.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.consumer.notesapp.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)
        adapter = NoteListAdapter { note ->
            val b = Bundle().apply { putLong("noteId", note.id); putBoolean("isSecret", note.isSecret) }
            findNavController().navigate(R.id.SecondFragment, b)
        }
        binding.recyclerNotes.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recyclerNotes.adapter = adapter

        val showSecret = arguments?.getBoolean("showSecret", false) ?: false
        if (showSecret) {
            viewModel.secretNotes.observe(viewLifecycleOwner) { updateList(it) }
        } else {
            viewModel.notes.observe(viewLifecycleOwner) { updateList(it) }
        }
    }

    private fun updateList(notes: List<Note>?) {
        if (notes.isNullOrEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.recyclerNotes.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.recyclerNotes.visibility = View.VISIBLE
            adapter.setItems(notes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
