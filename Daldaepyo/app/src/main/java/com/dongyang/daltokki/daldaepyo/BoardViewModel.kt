package com.dongyang.daltokki.daldaepyo

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {}

/*{
    private val daldaepyoRepository = DaldaepyoRepository()
    private val notice: LiveData<ContactsContract.Contacts.Data>
        get() = baeminRepository._notice

    fun loadBaeminNotice(page: Int){
        baeminRepository.loadBaeminNotice(page)
    }

    fun getAll(): LiveData<ContactsContract.Contacts.Data> {
        return notice
    }
}*/