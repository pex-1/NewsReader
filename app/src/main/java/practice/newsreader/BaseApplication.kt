package practice.newsreader

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import practice.newsreader.di.DaggerAppComponent

open class BaseApplication : DaggerApplication(){

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}