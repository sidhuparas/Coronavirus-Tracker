package com.parassidhu.coronavirusapp.ui.about

import android.animation.ObjectAnimator
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow
import com.parassidhu.coronavirusapp.R

class AboutPopup(context: Context) : BlurPopupWindow(context) {
    override fun createContentView(parent: ViewGroup): View {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.about_popup, parent, false)
        val lp = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.BOTTOM
        view.layoutParams = lp
        view.visibility = View.INVISIBLE
        return view
    }

    override fun onShow() {
        super.onShow()
        contentView.viewTreeObserver
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    contentView.visibility = View.VISIBLE
                    val height = contentView.measuredHeight
                    ObjectAnimator.ofFloat(contentView, "translationY", height.toFloat(), 0f)
                        .setDuration(animationDuration).start()
                }
            })
    }

    override fun createShowAnimator(): ObjectAnimator? {
        return null
    }

    override fun createDismissAnimator(): ObjectAnimator {
        val height = contentView.measuredHeight
        return ObjectAnimator.ofFloat(contentView, "translationY", 0f, height.toFloat())
            .setDuration(animationDuration)
    }

    class Builder(context: Context?) : BlurPopupWindow.Builder<AboutPopup>(context) {
        override fun createPopupWindow(): AboutPopup {
            return AboutPopup(mContext)
        }

        init {
            setScaleRatio(0.25f).setBlurRadius(8f).setTintColor(0x30000000)
        }
    }
}