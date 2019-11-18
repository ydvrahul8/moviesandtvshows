package com.example.myapplication.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import com.example.myapplication.R


object GenericMethods {

    private var mProgressDialog: ProgressDialog? = null
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    fun showProgressDialog(ctx: Context, isCancelable: Boolean) {
        mProgressDialog = ProgressDialog(ctx)
        mProgressDialog!!.setCancelable(isCancelable)
        mProgressDialog!!.setMessage(ctx.getString(R.string.loading))
        mProgressDialog!!.show()
    }

    fun showProgressDialog(ctx: Context, msg: String, isCancelable: Boolean) {
        mProgressDialog = ProgressDialog(ctx)
        mProgressDialog!!.setCancelable(isCancelable)
        mProgressDialog!!.setMessage(msg)
        mProgressDialog!!.show()
    }

    fun closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing)
        //        mProgressDialog.cancel();
            mProgressDialog!!.dismiss()
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}