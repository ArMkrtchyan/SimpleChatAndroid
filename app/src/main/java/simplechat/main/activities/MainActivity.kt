package simplechat.main.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import simplechat.main.R
import simplechat.main.databinding.ActivityMainBinding
import simplechat.main.interfacies.OnFragmentActionListener

class MainActivity : BaseActivity(), OnFragmentActionListener {

    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }


    override fun onBackPress() {
        onBackPressed()
    }

    override fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        }
    }

    override fun setPrimaryStatusBar() {
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
    }

}