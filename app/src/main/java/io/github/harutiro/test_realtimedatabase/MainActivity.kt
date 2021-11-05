package io.github.harutiro.test_realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.text.isEmpty as isEmpty

class MainActivity : AppCompatActivity() {

    val TAG = "debag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      書き込み
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("OK")

//      読み込み
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


//        ===================================================

//        書き込み
        val database2: DatabaseReference = Firebase.database.reference
        data class User(
            val username: String? = null,
            val email: String? = null
        ) {
            // Null default values create a no-argument default constructor, which is needed
            // for deserialization from a DataSnapshot.
        }

        fun writeNewUser(userId: String, name: String, email: String) {
            val user = User(name, email)

            database2.child("users").child(userId).setValue(user)
        }

        findViewById<Button>(R.id.outButton).setOnClickListener {
            val userName = findViewById<EditText>(R.id.outText).text.toString()
            if (userName.isNotEmpty()){
                writeNewUser(userName,"harutiro","hogehoge@exsample.com")
                Snackbar.make(findViewById(android.R.id.content),"OK", Snackbar.LENGTH_SHORT).show()

            }else{
                Snackbar.make(findViewById(android.R.id.content),"NO", Snackbar.LENGTH_SHORT).show()

            }
        }

//        読み込み

        findViewById<Button>(R.id.inButton).setOnClickListener {
            database2.child("users").child("test").child("email").get().addOnSuccessListener {
                Log.i(TAG, "Got value ${it.value}")
                findViewById<TextView>(R.id.inText).text = it.value.toString()
            }.addOnFailureListener{
                Log.e(TAG, "Error getting data", it)
            }
            Snackbar.make(findViewById(android.R.id.content),"OK", Snackbar.LENGTH_SHORT).show()

        }


    }
}