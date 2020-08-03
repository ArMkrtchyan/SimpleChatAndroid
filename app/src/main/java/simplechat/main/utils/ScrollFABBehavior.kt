package simplechat.main.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener

class ScrollFABBehavior(context: Context?, attrs: AttributeSet?) : FloatingActionButton.Behavior(context, attrs) {

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int,
        dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        Log.i("DyConsumedTag", "dyconsuming: $dyConsumed")
        Log.i("DyConsumedTag", "dyunconsuming: $dyUnconsumed")

        if (dyConsumed > 0 && child.isVisible()) child.hide(object : OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton) {
                super.onHidden(fab)
                child.invisible()
            }
        })
        else if (dyConsumed < 0 && !child.isVisible()) child.show()
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View,
        target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}