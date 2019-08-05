package com.example.instagr.screens.notifications

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagr.R
import com.example.instagr.screens.common.BaseActivity
import com.example.instagr.screens.common.setupAuthGuard
import com.example.instagr.screens.common.setupBottomNavigation
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : BaseActivity() {

    private lateinit var mAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setupBottomNavigation(3)
        Log.d(TAG, "onCreate: ")

        setupAuthGuard { uid ->
            mAdapter = NotificationsAdapter()
            notifications_recycler.layoutManager = LinearLayoutManager(this)
            notifications_recycler.adapter = mAdapter

            val viewModel = initViewModel<NotificationsViewModel>()
            viewModel.init(uid)
            viewModel.notifications.observe(this, Observer {
                it?.let {
                    mAdapter.updateNotifications(it)
                    viewModel.setNotifications(it)
                }
            })
        }
    }

    companion object {
        const val TAG = "LikesActivity"
    }
}
