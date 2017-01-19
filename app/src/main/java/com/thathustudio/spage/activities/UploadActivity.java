package com.thathustudio.spage.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.thathustudio.spage.R;
import com.thathustudio.spage.firebase.Storage;
import com.thathustudio.spage.firebase.UploadCallback;

import java.io.FileNotFoundException;

public class UploadActivity extends AppCompatActivity
{

    static final int OPEN_GALLERY = 1;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = (ImageView) findViewById(R.id.imageView);

        Button btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), OPEN_GALLERY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case OPEN_GALLERY:
                {
                    Uri uri = data.getData();
                    if (uri != null)
                    {
                        Storage.upload("post9", uri, new UploadCallback()
                        {
                            @Override
                            public void onUploadSuccess(String url)
                            {
                                Log.e("firebaseStorage", url);

                                // display image to ImageView
                                try
                                {
                                    Storage.displayImage(UploadActivity.this, imageView, "post9");
                                }
                                catch (Exception e)
                                {
                                    Log.e("firebaseStorage", e.getMessage());
                                }
                            }
                        });
                    }

                }
            }
        }
    }
}
