package com.prashant.masterbuddy;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by tanmay.agnihotri on 5/19/18.
 */

public class UploadVideoActivity extends AppCompatActivity{


    private static final int SELECT_VIDEO = 3;
    private EditText edtTitle, edtDescription;
    private Button btnUpload;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_action_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionColor)));

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        btnUpload = findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTitle.getText().toString().trim().length() != 0 && edtDescription.getText().toString().trim().length() != 0){
                    chooseVideo();
                }else{
                    Toast.makeText(UploadVideoActivity.this, "Please enter Title And Description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                UploadVideoService.startServiceUpload(this, selectedImageUri, edtTitle.getText().toString(), edtDescription.getText().toString(), 25);
                Toast.makeText(this, "Uploading video " + edtTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                edtTitle.setText("");
                edtDescription.setText("");
            }
        }
    }
}


