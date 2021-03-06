package com.example.instagr.screens.share

import android.net.Uri
import com.example.instagr.common.SingleLiveEvent
import com.example.instagr.data.FeedPostsRepository
import com.example.instagr.data.UsersRepository
import com.example.instagr.models.FeedPost
import com.example.instagr.models.User
import com.example.instagr.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks

class ShareViewModel(private val feedPostRepo: FeedPostsRepository,
                     private val usersRepo: UsersRepository,
                     onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    private val _shareCompletedEvent = SingleLiveEvent<Unit>()
    val shareCompletedEvent = _shareCompletedEvent
    val user = usersRepo.getUser()

    fun share(user: User, imageUri: Uri?, caption: String) {
        if (imageUri != null) {
            usersRepo.uploadUserImage(user.uid, imageUri).onSuccessTask { downloadUrl ->
                Tasks.whenAll(usersRepo.setUserImage(user.uid, downloadUrl!!),
                              feedPostRepo.createFeedPost(user.uid, mkFeedPost(user, caption, downloadUrl.toString())))
            }.addOnSuccessListener {
                _shareCompletedEvent.call()
            }.addOnFailureListener(onFailureListener)
        }
    }

    private fun mkFeedPost(user: User, caption: String, imageDownloadUrl: String): FeedPost {
        return FeedPost(uid = user.uid,
                        username = user.username,
                        image = imageDownloadUrl,
                        caption = caption,
                        photo = user.photo)
    }
}