package com.grishberg.dynamicconstraintlayout

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

const val EXTRA_INFLATE_MODE = "INFLATE_MODE"

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        findViewById<View>(R.id.startInflateAsync).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_INFLATE_MODE, MODE_ASYNC_INFLATE)
            overridePendingTransition(0, 0)
            startActivity(intent)
        }

        findViewById<View>(R.id.startInflateAsyncStrategy).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_INFLATE_MODE, MODE_ASYNC_INFLATE_WITH_STRATEGY)
            overridePendingTransition(0, 0)
            startActivity(intent)
        }

        findViewById<View>(R.id.startInflateSync).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_INFLATE_MODE, MODE_SYNC_INFLATE)
            overridePendingTransition(0, 0)
            startActivity(intent)
        }

        findViewById<View>(R.id.startInflateSyncStrategy).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_INFLATE_MODE, MODE_SYNC_INFLATE_WITH_STRATEGY)
            overridePendingTransition(0, 0)
            startActivity(intent)
        }
    }
}
