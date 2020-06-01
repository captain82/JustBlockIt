package com.example.blocked

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private var database = AppDatabase.getDatabase(application)

    private val compositeDisposable = CompositeDisposable()

    val contactList:MutableLiveData<List<ContactModel>> = MutableLiveData()

    fun insertContact(contact:String) = compositeDisposable.add(
        Completable.fromAction {
            database?.contactDao()?.insert(ContactModel(contact))}
            .doOnError { Log.i("error", it.localizedMessage) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())

    fun getContacts() = compositeDisposable.add(
        database!!.contactDao().query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                contactList.value = it
            }
    )
}