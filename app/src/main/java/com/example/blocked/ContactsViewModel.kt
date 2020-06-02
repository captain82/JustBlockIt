package com.example.blocked

import android.app.Application
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blocked.local.AppDatabase
import com.example.blocked.local.ContactModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private var database = AppDatabase.getDatabase(application)
    private val compositeDisposable = CompositeDisposable()
    val contactList: MutableLiveData<List<ContactModel>> = MutableLiveData()

    fun insertContact(contact: String) = compositeDisposable.add(
        Completable.fromAction {
            database?.contactDao()?.insert(
                ContactModel(
                    contact,
                    Color.argb(30, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                )
            )
        }
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

    fun deleteContact(contact: String) = compositeDisposable.add(
        Completable.fromAction { database?.contactDao()?.deleteContact(contact) }
            .doOnError { Log.i("error", it.localizedMessage) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}