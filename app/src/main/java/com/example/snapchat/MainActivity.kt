package com.example.snapchat
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var emailEditText:EditText? = null;
    var passwordEditText:EditText?=null;
    val mAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailTextView);
        passwordEditText = findViewById(R.id.passwordTextView);

        if(mAuth.currentUser != null){
            logIn();
        }


    }

    fun goClicked(view: View){
        //check if we can log in the user
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                   logIn();

                } else {
                 //sign up the user.
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(
                        this
                    ){task->
                     if(task.isSuccessful){
                        logIn();
                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailEditText?.text.toString())
                     }
                     else{
                        Toast.makeText(this, "Login Failed Try Again", Toast.LENGTH_SHORT).show();
                    }
                    } // ...
                }
                // ...
            }

    }

    fun logIn(){
        //move to the next activity
        val intent = Intent(this,SnapsActivity::class.java);
        startActivity(intent);
    }


}