package com.facundojaton.mobilenativefirebasetask.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facundojaton.mobilenativefirebasetask.controllers.FBDatabaseController
import com.facundojaton.mobilenativefirebasetask.controllers.SessionController
import com.facundojaton.mobilenativefirebasetask.data.model.DatabaseItem
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ListViewModel : ViewModel() {

    enum class PushResult {
        SUCCESS, FAILED, WAITING
    }

    enum class DeleteResult {
        SUCCESS, FAILED, WAITING
    }

    private var reference: DatabaseReference? =
        SessionController.userId?.let { FBDatabaseController.getListItemsReference(it) }
    private var items: ArrayList<DatabaseItem> = ArrayList()

    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _itemsList = MutableLiveData<MutableList<DatabaseItem>>()
    val itemsList: LiveData<MutableList<DatabaseItem>>
        get() = _itemsList

    private var _newItemWordCount = MutableLiveData<Int>()
    val newItemCharCount: LiveData<Int>
        get() = _newItemWordCount

    private var _pushResult = MutableLiveData<PushResult>()
    val pushResult: LiveData<PushResult>
        get() = _pushResult

    private var _deleteResult = MutableLiveData<DeleteResult>()
    val deleteResult: LiveData<DeleteResult>
        get() = _deleteResult

    init {
        _itemsList.value = ArrayList()
        setListenerOnItems()
    }

    private fun setListenerOnItems() {
        //ToDo: Check nullability
        reference?.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(itemSnap: DataSnapshot, previousName: String?) {
                if (itemSnap.exists()) {
                    val item =
                        itemSnap.getValue(DatabaseItem::class.java)
                    onGroupMessageChildMoved(item)
                }
            }

            override fun onChildChanged(itemSnap: DataSnapshot, previousName: String?) {
                if (itemSnap.exists()) {
                    val item =
                        itemSnap.getValue(DatabaseItem::class.java)
                    onGroupMessageChildChanged(item)
                }
            }

            override fun onChildAdded(itemSnap: DataSnapshot, previousName: String?) {
                if (itemSnap.exists()) {
                    val item =
                        itemSnap.getValue(DatabaseItem::class.java)
                    onChildAdded(item)
                }
            }

            override fun onChildRemoved(itemSnap: DataSnapshot) {
                if (itemSnap.exists()) {
                    val item =
                        itemSnap.getValue(DatabaseItem::class.java)
                    onChildRemoved(item)
                }
            }
        })
    }

    private fun onChildRemoved(item: DatabaseItem?) {
        item?.let {
            items.remove(item)
            updateDataSet()
        }
    }

    private fun onChildAdded(item: DatabaseItem?) {
        item?.let {
            items.add(it)
            updateDataSet()
        }
    }

    private fun updateDataSet() {
        _itemsList.value = items
    }

    private fun onGroupMessageChildMoved(item: DatabaseItem?) {
        //item?.let { _itemsList.value?.add(it) }
    }

    private fun onGroupMessageChildChanged(item: DatabaseItem?) {
        //ToDo update items
    }

    fun setCharCount(count: Int) {
        _newItemWordCount.value = count
    }

    fun createNewItem(text: String) {
        SessionController.userId?.let { uid ->
            uiScope.launch {
                _pushResult.value = PushResult.WAITING
                val result = setNewItemOnDatabase(uid, text)
                if (result) {
                    Log.d("ACTIVITY", "Success writing")
                    _pushResult.value = PushResult.SUCCESS
                } else {
                    _pushResult.value = PushResult.FAILED
                    Log.e("ACTIVITY", "ERROR")
                }
            }
        }
    }

    private suspend fun setNewItemOnDatabase(userId: String, text: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val ref = FBDatabaseController.pushItem(userId)
                //ToDo: check nullability
                val newItem = DatabaseItem(ref.key!!, text)
                ref.setValue(newItem).await()
                true
            } catch (e: Exception) {
                false
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun deleteItem(databaseItemId: String) {
        SessionController.userId?.let { uid ->
            uiScope.launch {
                _deleteResult.value = DeleteResult.WAITING
                val result = deleteItemOnDatabase(uid, databaseItemId)
                if (result) {
                    Log.d("ACTIVITY", "Success deleting")
                    _deleteResult.value = DeleteResult.SUCCESS
                } else {
                    _deleteResult.value = DeleteResult.FAILED
                    Log.e("ACTIVITY", "ERROR")
                }
            }
        }
    }

    private suspend fun deleteItemOnDatabase(userId: String, databaseItemId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                FBDatabaseController.getItemReference(userId, databaseItemId)
                    .removeValue()
                    .await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}
