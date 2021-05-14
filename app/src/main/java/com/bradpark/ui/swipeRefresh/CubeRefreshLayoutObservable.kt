package com.bradpark.ui.swipeRefresh

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class CubeRefreshLayoutObservable (private val lottiePullToRefreshLayout: RefreshLayout) : Observable<LottiePullToRefreshes>() {
    override fun subscribeActual(observer: Observer<in LottiePullToRefreshes>) {
        val listener = Listener(lottiePullToRefreshLayout, observer)
        observer.onSubscribe(listener)
        lottiePullToRefreshLayout.onTriggerListener(listener.onPullToRefresh)
    }

    class Listener(private val lottiePullToRefreshLayout: RefreshLayout,
                   private val observer: Observer<in LottiePullToRefreshes>) : MainThreadDisposable() {

        val onPullToRefresh : () -> Unit = {
            if (!isDisposed) {
                observer.onNext(LottiePullToRefreshes)
            }
        }

        override fun onDispose() {
            lottiePullToRefreshLayout.removeOnTriggerListener(onPullToRefresh)
        }
    }
}

/**
 * Kotlin - extension function을 사용해 함수를 호출.
 *
 * Reactive programming
 * onComplete()를 발행하지 않음.
 *
 * @receiver CubeRefreshLayout
 * @return Observable<LottiePullToRefreshes>
 */
fun RefreshLayout.refreshes(): Observable<LottiePullToRefreshes> = CubeRefreshLayoutObservable(this)