package com.grishberg.dynamicconstraintlayout

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.view.AsyncLayoutInflater
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import com.grishberg.constraintlayoutex.ConstraintLayoutEx
import com.grishberg.constraintlayoutex.TwoRowStrategy

const val MODE_ASYNC_INFLATE = 0
const val MODE_SYNC_INFLATE = 1
const val MODE_ASYNC_INFLATE_WITH_STRATEGY = 2
const val MODE_SYNC_INFLATE_WITH_STRATEGY = 3

class MainActivity : AppCompatActivity() {
    private val omnibarConstraintSet by lazy {
        val cs = ConstraintSet()
        cs.clone(this, R.layout.omnibar)
        cs
    }
    private val asyncInflater by lazy { AsyncLayoutInflater(this) }
    private val container by lazy { findViewById<FrameLayout>(R.id.container) }
    private val constraintRoot by lazy { findViewById<ConstraintLayout>(R.id.constraintRoot) }
    private val startTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mode = intent.getIntExtra(EXTRA_INFLATE_MODE, 0)
        when (mode) {
            MODE_ASYNC_INFLATE -> inflateAsync()
            MODE_SYNC_INFLATE -> inflateBar()
            MODE_ASYNC_INFLATE_WITH_STRATEGY -> inflateAsync(true)
            MODE_SYNC_INFLATE_WITH_STRATEGY -> inflateBar(true)
        }

        container.viewTreeObserver.addOnPreDrawListener(PreDrawListener())
    }

    private fun inflateAsync(useStrategy: Boolean = false) {
        LayoutInflater.from(this).inflate(R.layout.stub, container)
        //val textView = findViewById<TextView>(R.id.stubText)
        //textView.text = "Some address line"
        asyncInflater.inflate(R.layout.omnibar, container) { view, resid, parent ->
            with(parent!!) {
                removeAllViews()
                addView(view)
                if (useStrategy) {
                    val cl = view as ConstraintLayoutEx
                    cl.strategy = TwoRowStrategy()
                }
            }
        }
    }

    private fun inflateBar(useStrategy: Boolean = false) {
        LayoutInflater.from(this).inflate(R.layout.omnibar, container)
        if (useStrategy) {
            val cl = findViewById<ConstraintLayoutEx>(R.id.constraintRoot)
            cl.strategy = TwoRowStrategy()
        }
    }

    private inner class PreDrawListener : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            val preDrawTime = System.currentTimeMillis() - startTime
            Log.e(TAG, "onPreDraw $preDrawTime")
            container.viewTreeObserver.removeOnPreDrawListener(this)
            return true
        }
    }

    private fun switchBarWithButtons() {
        val button1 = ImageButton(this)
        button1.id = R.id.button1
        button1.visibility = View.GONE
        button1.setImageResource(android.R.drawable.ic_dialog_map)

        val button2 = ImageButton(this)
        button2.id = R.id.button2
        button2.visibility = View.GONE
        button2.setImageResource(android.R.drawable.ic_dialog_alert)

        val button3 = ImageButton(this)
        button3.id = R.id.button3
        button3.visibility = View.GONE
        button3.setImageResource(android.R.drawable.ic_btn_speak_now)


        val button4 = ImageButton(this)
        button4.id = R.id.button4
        button4.visibility = View.GONE
        button4.setImageResource(android.R.drawable.ic_delete)


        val button5 = ImageButton(this)
        button5.id = R.id.button5
        button5.visibility = View.GONE
        button5.setImageResource(android.R.drawable.ic_input_add)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(constraintRoot)
        }
        constraintRoot.addView(button1)
        constraintRoot.addView(button2)
        constraintRoot.addView(button3)
        constraintRoot.addView(button4)
        constraintRoot.addView(button5)

        omnibarConstraintSet.applyTo(constraintRoot)
    }

    companion object {
        private val TAG = "PERF"
    }
}
