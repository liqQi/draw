package com.saynope.drawandguess.repo

data class ApiResponse<T>(val code:Int, val message:String = "", val t:T){
    companion object{
        const val RESULT_SUCCESS:Int = 200
        const val RESULT_FAIL:Int = 900

        fun buildResponse(code: Int,message:String):ApiResponse<EmptyBody>{
            return ApiResponse(code,message,EmptyBody())
        }
    }
}