package com.facundojaton.mobilenativefirebasetask.ui.list

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.facundojaton.mobilenativefirebasetask.R
import com.facundojaton.mobilenativefirebasetask.SwipeToDelete
import com.facundojaton.mobilenativefirebasetask.adapters.ItemsListAdapter
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import com.facundojaton.mobilenativefirebasetask.databinding.CustomDialogAddItemBinding
import com.facundojaton.mobilenativefirebasetask.databinding.ListFragmentBinding

class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private lateinit var listViewModel: ListViewModel
    private var listAdapter = ItemsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        binding.viewModel = listViewModel
        binding.rvList.adapter = listAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDelete(listAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvList)

        binding.btnBack.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    SessionController.logout()
                    this.findNavController()
                        .navigate(ListFragmentDirections.actionListFragmentToLoginFragment())
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.btnProfile.setOnClickListener {
            this.findNavController()
                .navigate(ListFragmentDirections.actionListFragmentToProfileFragment())
        }

        listViewModel.itemsList.observe(viewLifecycleOwner, {
            listAdapter.setListItems(it)
        })

        binding.btnAddListItem.setOnClickListener {
            showAddItemDialog()
        }

        listAdapter.onItemSwipe = { databaseItem ->
            listViewModel.deleteItem(databaseItem)
        }
    }

    private fun showAddItemDialog() {
        context?.let { it ->
            val dialog = Dialog(it)
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                val dialogBinding = CustomDialogAddItemBinding.inflate(layoutInflater)
                setContentView(dialogBinding.root)
                window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(false)
                listViewModel.setCharCount(0)
                dialogBinding.etNewItemContent.doOnTextChanged { text, _, _, _ ->
                    text?.length?.let { length -> listViewModel.setCharCount(length) }
                }
                listViewModel.newItemCharCount.observe(viewLifecycleOwner, { characterCount ->
                    val content = "$characterCount/40"
                    dialogBinding.tvCharacterCount.text = content
                })
                listViewModel.pushResult.observe(viewLifecycleOwner, { result ->
                    when (result) {
                        ListViewModel.PushResult.SUCCESS -> {
                            dismiss()
                        }
                        //ToDo: handle error messages
                    }
                })
                dialogBinding.btnCancel.setOnClickListener {
                    dismiss()
                }
                dialogBinding.btnConfirm.setOnClickListener {
                    listViewModel.createNewItem(dialogBinding.etNewItemContent.text.toString())
                }
                show()
            }
        }
    }


}