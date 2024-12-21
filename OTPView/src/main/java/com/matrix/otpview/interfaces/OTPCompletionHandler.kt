package com.matrix.otpview.interfaces

fun interface OTPCompletionHandler {
    fun onComplete(otp: String)
}