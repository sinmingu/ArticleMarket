package com.smg.itemmarket.home;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.smg.itemmarket.DBKey;
import com.smg.itemmarket.R;
import com.smg.itemmarket.Util;
import com.smg.itemmarket.databinding.ActivityAddArticleBinding;

public class AddArticleActivity extends AppCompatActivity {

    private ImageView photoImageView;
    private Button imageAddButton, submitButton;
    private EditText titleEditText, priceEditText;
    private Uri selectedUri = null;

    private FirebaseAuth auth;
    private DatabaseReference articleDB;
    private FirebaseStorage storage;

    private String title = "";
    private String price = "";
    private String sellerId = "";

    private final static int MSG_VIEW_INIT = 1;
    private final static int MSG_STORAGE_ERROR = 2;
    private final static int MSG_STORAGE_SUCCES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        auth = FirebaseAuth.getInstance();
        articleDB = FirebaseDatabase.getInstance().getReference(DBKey.DB_ARTICLES);
        storage = FirebaseStorage.getInstance();

        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEditText.getText().toString();
                price = priceEditText.getText().toString();
                if(auth.getCurrentUser() != null) {
                    sellerId = auth.getCurrentUser().getUid();
                }

                // 이미지가 있으면 업로드 과정을 추가
                if(selectedUri != null) {
                    uploadPhoto(selectedUri);
                } else {
                    uploadArticle(sellerId, title, price, "");
                }


            }
        });

        imageAddButton = (Button) findViewById(R.id.imageAddButton);
        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 퍼미션이 허용되었을 경우
                    startContentProvider();
                } else {

                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 교육용 팝업이 필요할 경우
                        showPermissionCotextPopup();
                    } else {
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1010);
                    }
                }
            }
        });

        // test line
        storage.getReference().child("article/photo").child("기초한자 1006.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.getStackTrace();
                }
                Util.log("smgsmg");
            }
        });
    }

    // 권한에 대한 요청이 들어올 경우 callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1010:
                if(grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider();
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void startContentProvider() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2020);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 2020:
                Uri uri = data.getData();
                if(uri != null) {
                    photoImageView.setImageURI(uri);
                    selectedUri = uri;
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showPermissionCotextPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한이 필요합니다.");
        builder.setMessage("사진을 가져오기 위해 필요합니다.");
        builder.setPositiveButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1010);
            }
        });
    }

    private void uploadPhoto(Uri uri) {
        // 어떤이름으로 저정할지 지정
        String fileName = System.currentTimeMillis() + ".png";
        storage.getReference().child("article/photo").child(fileName).putFile(uri).addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    // 업로드 과정이 완료된 상태
                    storage.getReference().child("article/photo").child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            selectedUri = uri;
                            handler.sendEmptyMessage(MSG_STORAGE_SUCCES);
                        }
                    });

                    storage.getReference().child("article/photo").child(fileName).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            handler.sendEmptyMessage(MSG_STORAGE_ERROR);
                        }
                    });

                } else {
                    handler.sendEmptyMessage(MSG_STORAGE_ERROR);
                }
            }
        });

    }

    private void uploadArticle(String sellerId, String title, String price, String imageUri) {
        ArticleModel articleModel = new ArticleModel(sellerId, title, System.currentTimeMillis(), price, imageUri);
        articleDB.push().setValue(articleModel);
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_VIEW_INIT:
                    break;
                case MSG_STORAGE_ERROR:
                    errorToast();
                    break;
                case MSG_STORAGE_SUCCES:
                    uploadArticle(sellerId, title, price, selectedUri.toString());
                    break;
                default:
                    break;
            }
        }
    };


    private void errorToast() {
        Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
    }
}
