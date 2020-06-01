package com.example.blocked

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    var database = AppDatabase.getDatabase(application)

    val compositeDisposable = CompositeDisposable()

    fun insertContact(contact:String) = compositeDisposable.add(
        Completable.fromAction {
            database?.contactDao()?.insert(ContactModel(contact))}
            .doOnError { Log.i("error", it.localizedMessage) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    )

    fun getContacts() = compositeDisposable.add(
        database!!.contactDao().query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i("contacts" , it.toString())
            }
    )
}