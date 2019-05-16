package br.com.dcarv.medicalerta

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulersRule: TestRule {

    private val TEST_SCHEDULER = Schedulers.trampoline()

    override fun apply(base: Statement?, description: Description?): Statement {
        return object: Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { TEST_SCHEDULER }

                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { TEST_SCHEDULER }
                RxJavaPlugins.setComputationSchedulerHandler { TEST_SCHEDULER }
                RxJavaPlugins.setNewThreadSchedulerHandler { TEST_SCHEDULER }

                base?.evaluate()

                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
            }
        }
    }
}