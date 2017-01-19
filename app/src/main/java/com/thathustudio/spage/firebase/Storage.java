package com.thathustudio.spage.firebase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thathustudio.spage.app.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by lequan on 12/12/2016.
 */
public class Storage
{
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReferenceFromUrl(Global.firebaseStorage);

    public static void upload(String fileName, Uri uri, final UploadCallback callback)
    {

        StorageReference mountainImagesRef = storageRef.child("images/" + fileName);
        try
        {
           /* FileInputStream stream = new FileInputStream(new File(filePath));
            UploadTask uploadTask = mountainImagesRef.putStream(stream);*/
            UploadTask uploadTask = mountainImagesRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    Log.e("firebaseStorage", exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    callback.onUploadSuccess(downloadUrl.toString());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void download(String name, OnSuccessListener<Uri> listener)
    {
        storageRef.child("images/" + name).getDownloadUrl()
                .addOnSuccessListener(listener)
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        Log.e("firebaseStorage", exception.getMessage());
                    }
                });
    }


    public static void displayImage(Context context, ImageView imageView, String imageName)
    {

        Glide.with(context).using(new FirebaseImageLoader())
                .load(storageRef.child("images/" + imageName))
                .into(imageView);
    }

}
