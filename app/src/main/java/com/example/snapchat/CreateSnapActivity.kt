package com.example.snapchat

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_snap.*
import java.io.ByteArrayOutputStream
import java.util.*


class CreateSnapActivity : AppCompatActivity() {

    var createSnapActivity:ImageView? = null;
    var messageEditText: EditText? = null;
    var imageName:String = "";
    var NextButton: Button? = null;
    var CreateSnapButton:Button?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        createSnapActivity = findViewById(R.id.chooseImageClicked);
        messageEditText = findViewById(R.id.messageTextView);
        NextButton = findViewById(R.id.nextButton);
        CreateSnapButton = findViewById(R.id.button2);
        NextButton?.setEnabled(true);
    }

    fun getPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    fun chooseImageClicked(view: View) {
        imageName = UUID.randomUUID().toString() + ".jpg";
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1);
        }else{
            getPhoto();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data;

        if(requestCode ==1 && resultCode == Activity.RESULT_OK && data != null){
         try{
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage);
             createSnapActivity?.setImageBitmap(bitmap);

         }catch (e:Exception){
            e.printStackTrace();
         }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }

        }



    }



    fun nextClicked(view:View){
        // Get the data from an ImageView as bytes
        NextButton?.setEnabled(false);
        createSnapActivity?.setDrawingCacheEnabled(true)
        createSnapActivity?.buildDrawingCache()
        val bitmap = (createSnapActivity?.getDrawable() as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()
        FirebaseStorage.getInstance().getReference().child("images").child(imageName)
        val uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data);
        uploadTask.addOnFailureListener {
           Toast.makeText(this,"upLoadFailed", Toast.LENGTH_SHORT).show();
        }.addOnSuccessListener { taskSnapShot->

            val downloadUrl = taskSnapShot.storage.downloadUrl;
            val intent = Intent(this,chooseUserActivity::class.java);
            intent.putExtra("imageURL",downloadUrl.toString())
            intent.putExtra("imageName",imageName);
            intent.putExtra("message",messageTextView?.text.toString());
            startActivity(intent);


        }


    }



}