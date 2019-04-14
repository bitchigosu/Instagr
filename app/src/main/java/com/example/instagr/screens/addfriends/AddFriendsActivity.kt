package com.example.instagr.screens.addfriends

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.instagr.R
import com.example.instagr.screens.common.BaseActivity
import com.example.instagr.models.User
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : BaseActivity(),
    FriendsAdapter.Listener {

    private lateinit var mUser: User
    private lateinit var mUsers: List<User>
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var mViewmodel: AddFriendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        mAdapter = FriendsAdapter(this)
        mViewmodel = initViewModel()

        back_image.setOnClickListener { finish() }
        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)

        mViewmodel.userAndFriends.observe(this, Observer {
            it?.let { (user, otherUsers) ->
                mUser = user
                mUsers = otherUsers
                mAdapter.update(mUsers, mUser.follows)

            }
        })
    }

    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        mViewmodel.setFollow(mUser.uid, uid, follow)
            .addOnSuccessListener { onSuccess() }
    }

    companion object {
        const val TAG = "AddFriendsActivity"
    }
}