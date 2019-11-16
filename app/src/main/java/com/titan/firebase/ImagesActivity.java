package com.titan.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.titan.firebase.models.Upload;

import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

public class ImagesActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mButtonChooseImage.setOnClickListener(ButtonChooseImage__OnClickListener);
        mButtonUpload.setOnClickListener(ButtonUpload__OnClickListener);
        mTextViewShowUploads.setOnClickListener(TextViewShowUploads__OnClickListener);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 2000);


                            final String msg = "Image uploaded";

                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadId).setValue(upload).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Error")
                                            .setContentText(msg + "\nSomething went wrong: " + e.getMessage())
                                            .show();

                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    new SweetAlertDialog(ImagesActivity.this)
                                            .setTitleText("Upload successful")
                                            .show();

                                    Timber.d("Sucesso");
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Upload")
                                    .setContentText(e.getMessage())
                                    .show();

                            Timber.d(e.getMessage() + "");


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else {

            new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Upload")
                    .setContentText("No file selected")
                    .show();
        }


    }


    Button.OnClickListener ButtonChooseImage__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            openFileChooser();
        }
    };

    Button.OnClickListener ButtonUpload__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mUploadTask != null && mUploadTask.isInProgress()) {
                new SweetAlertDialog(ImagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Upload")
                        .setContentText("Upload in progress")
                        .show();
            }
            else{
                uploadFile();
            }
        }
    };

    TextView.OnClickListener TextViewShowUploads__OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
}
