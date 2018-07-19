package com.prashant.masterbuddy

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

class UploadVideoActivity : AppCompatActivity() {
    private var edtTitle: EditText? = null
    private var edtDescription: EditText? = null
    private var btnUpload: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_video_layout)
        val actionBar = supportActionBar
        actionBar!!.setIcon(R.drawable.ic_action_logo)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionColor)))

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnUpload = findViewById(R.id.btnUpload)

        btnUpload!!.setOnClickListener {
            if (edtTitle!!.text.toString().trim { it <= ' ' }.length != 0 && edtDescription!!.text.toString().trim { it <= ' ' }.length != 0) {
                chooseVideo()
            } else {
                Toast.makeText(this@UploadVideoActivity, "Please enter Title And Description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                println("SELECT_VIDEO")
                val selectedImageUri = data.data
                UploadVideoService.startServiceUpload(this, selectedImageUri, edtTitle!!.text.toString(), edtDescription!!.text.toString(), 25)
                Toast.makeText(this, "Uploading video " + edtTitle!!.text.toString(), Toast.LENGTH_SHORT).show()
                edtTitle!!.setText("")
                edtDescription!!.setText("")
            }
        }
    }

    companion object {


        private val SELECT_VIDEO = 3
    }
}


