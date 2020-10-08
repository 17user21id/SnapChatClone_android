package com.example.snapchat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance();
    var messageDisplayTextView:TextView? = null;
    var snapImageView:ImageView? = null;
    var URL:String? = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageDisplayTextView = findViewById(R.id.messageDisplayTextView);
        snapImageView = findViewById(R.id.snapImageView);
        messageDisplayTextView?.text = intent.getStringExtra("message");


//        val url = URL(intent.getStringExtra("imageURL"));
//        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        snapImageView?.setImageBitmap(bmp)

          URL = intent.getStringExtra("imageURL").toString();

//        Picasso.with(ViewSnapActivity.this).load(intent.getStringExtra("imageURL"))
//            .placeholder(R.drawable.image)
//            .into(snapImageView)

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drwable.image);
//        requestOptions.error(R.drawable.image);
//
//        Glide.with(ViewSnapActivity.this)
//            .load(URL)
//            .apply(requestOptions)
//            .into(snapImageView)

          Picasso.with(this).load(URL).into(snapImageView);



    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth?.currentUser?.uid.toString()).child("snaps").child(intent.getStringExtra("snapKey").toString()).removeValue();
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName").toString()).delete();
    }
}

private fun DatabaseReference.delete() {
    TODO("Not yet implemented")
}
