package com.consumer.notesapp

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.consumer.notesapp.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoteViewModel
    private lateinit var encryptionHelper: EncryptionHelper
    private lateinit var listAdapter: ListEditorAdapter
    private var noteId: Long = -1
    private var suppressPreview: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(NoteViewModel::class.java)
        encryptionHelper = EncryptionHelper(requireContext())

        noteId = arguments?.getLong("noteId", -1) ?: -1
        val isSecretMode = arguments?.getBoolean("isSecret", false) ?: false

        listAdapter = ListEditorAdapter()
        binding.recyclerList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerList.adapter = listAdapter

        binding.radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            val isMarkdown = checkedId == R.id.radio_markdown
            binding.editMarkdown.visibility = if (isMarkdown) View.VISIBLE else View.GONE
            binding.textPreview.visibility = if (isMarkdown) View.VISIBLE else View.GONE
            binding.recyclerList.visibility = if (isMarkdown) View.GONE else View.VISIBLE
            if (isMarkdown) renderPreview()
        }

        binding.editMarkdown.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(newText: String) { if (!suppressPreview) renderPreview() }
        })

        binding.btnSave.setOnClickListener { saveNote() }
        binding.btnDelete.setOnClickListener { deleteNote() }

        if (noteId != -1L) loadNote(noteId, isSecretMode)
    }

    private fun renderPreview() {
        val md = binding.editMarkdown.text.toString()
        val parser: Parser = Parser.builder().build()
        val document: Node = parser.parse(md)
        val renderer: HtmlRenderer = HtmlRenderer.builder().build()
        val bodyHtml = renderer.render(document)
        val css = """
            body { font-family: sans-serif; padding:12px; color:#212121; }
            img { max-width: 100%; height: auto; display:block; margin:8px 0; }
            pre { background:#f5f5f5; padding:8px; overflow:auto; }
            code { background:#eee; padding:2px 4px; border-radius:4px; }
            h1,h2,h3 { color:#1565c0; }
            blockquote { color:#555; border-left:4px solid #ddd; padding-left:8px; margin-left:0; }
        """.trimIndent()
        val html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/><style>$css</style></head><body>$bodyHtml</body></html>"
        binding.textPreview.settings.loadsImagesAutomatically = true
        binding.textPreview.settings.javaScriptEnabled = false
        binding.textPreview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
    }

    private fun loadNote(id: Long, secret: Boolean) {
        viewModel.getNoteLive(id).observe(viewLifecycleOwner) { n ->
            n?.let { note ->
                suppressPreview = true
                binding.editTitle.setText(note.title)
                binding.checkboxSecret.isChecked = note.isSecret
                binding.btnDelete.visibility = View.VISIBLE
                if (note.isSecret) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val decrypted = encryptionHelper.get("note_${note.id}")
                        launch(Dispatchers.Main) { binding.editMarkdown.setText(decrypted) }
                    }
                } else {
                    binding.editMarkdown.setText(note.content)
                }
                suppressPreview = false
                renderPreview()
            }
        }
    }

    private fun saveNote() {
        val title = binding.editTitle.text.toString()
        val isMarkdown = binding.radioGroupType.checkedRadioButtonId == R.id.radio_markdown
        val secret = binding.checkboxSecret.isChecked
        val content = if (isMarkdown) binding.editMarkdown.text.toString() else listAdapter.getJoined(false)
        val now = System.currentTimeMillis()
        if (noteId == -1L) {
            // New note: let Room generate the id, then store secrets keyed by the generated id
            val note = Note(0L, title, if (secret) "" else content, secret, now, now)
            lifecycleScope.launch {
                val newId = viewModel.insertAndReturnId(note)
                if (secret) withContext(Dispatchers.IO) { encryptionHelper.put("note_$newId", content) }
                findNavController().navigateUp()
            }
        } else {
            val note = Note(noteId, title, if (secret) "" else content, secret, now, now)
            if (secret) lifecycleScope.launch(Dispatchers.IO) { encryptionHelper.put("note_$noteId", content) }
            viewModel.update(note)
            findNavController().navigateUp()
        }
    }

    private fun deleteNote() {
        if (noteId == -1L) return
        val note = Note(noteId, null, null, false, 0, 0)
        viewModel.delete(note)
        lifecycleScope.launch(Dispatchers.IO) { encryptionHelper.remove("note_$noteId") }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
