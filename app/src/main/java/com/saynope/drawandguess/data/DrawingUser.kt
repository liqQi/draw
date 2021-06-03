package com.saynope.drawandguess.data

data class DrawingUser constructor(
    var drawingUserId: String,
    var drawingUserNickName: String,
    var question: Question
)