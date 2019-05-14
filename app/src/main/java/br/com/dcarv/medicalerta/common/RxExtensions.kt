package br.com.dcarv.medicalerta.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.autoDispose(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}