package simplechat.main.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import simplechat.main.R
import simplechat.main.databinding.ActivityImageBinding

class ImageActivity : BaseActivity() {
    private lateinit var dataBinding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = 0
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_image)
        val imageStr = intent?.getStringExtra("Image")
        Glide.with(this).load(Uri.parse(imageStr)).into(dataBinding.photoView)
        dataBinding.back.setOnClickListener {
            finish()
        }
    }


}