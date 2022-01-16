package com.example.chatapp.model

import android.net.Uri


class UserMessage(
    var message: String? =null, var senderId:String?=null, var imageUri: Uri?=null
) {
}