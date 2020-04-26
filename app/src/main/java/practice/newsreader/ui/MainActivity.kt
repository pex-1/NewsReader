package practice.newsreader.ui

import android.os.Bundle
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import practice.newsreader.R

class MainActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHostFragment).navigateUp()

}
