package com.saynope.drawandguess.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException

object MD5Utils {


    fun  MD5(s:String):String {
        val md5 : MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
        val byteArray: ByteArray

        try {
            byteArray = s.toByteArray(charset("UTF-8"))
            val md5Bytes = md5.digest(byteArray)
            val hexValue = StringBuffer()
            for (i in md5Bytes.indices) {
                val `val` = md5Bytes[i].toInt() and 0xff
                if (`val` < 16) {
                    hexValue.append("0")
                }
                hexValue.append(Integer.toHexString(`val`))
            }
            return hexValue.toString()
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return ""
        }
    }
}
