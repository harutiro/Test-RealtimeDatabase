package io.github.harutiro.test_realtimedatabase

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    val TAG = "AuthDebug"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        // Initialize Firebase Auth
        auth = Firebase.auth

        val email = "hogehoge@example.com"
        val password = "passpass"

        findViewById<Button>(R.id.newAuthButton).setOnClickListener {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        Log.d(TAG,user?.email.toString())
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }
        }


        findViewById<Button>(R.id.loginButton).setOnClickListener {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        Log.d(TAG, user?.email.toString())

//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }
        }


        findViewById<Button>(R.id.authCheckButton).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in
                Log.d(TAG,user.email.toString())
            } else {
                // No user is signed in
                Log.d(TAG,"NoAccount")

            }
        }

        findViewById<Button>(R.id.authLogoutButton).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }




    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
        }
    }
}