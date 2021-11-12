package io.github.harutiro.test_realtimedatabase

import android.content.Intent
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
import com.google.firebase.ktx.Firebase
import java.net.IDN
import java.util.*
import org.json.JSONException
import com.google.firebase.FirebaseError

class MainActivity : AppCompatActivity() {

    val TAG = "debag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.mapButton).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


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



        fun writeNewUser(userId: String, name: String, email: String) {
            val user = User(name, email)

            database2.child("users").child(userId).setValue(user)
        }

        findViewById<Button>(R.id.outButton).setOnClickListener {
            val id = findViewById<EditText>(R.id.outIdText).text.toString()
            val email = findViewById<EditText>(R.id.outEmailText).text.toString()
            val name = findViewById<EditText>(R.id.outUserNameText).text.toString()

            if (id.isNotEmpty()){
                writeNewUser(id,name,email)
                Snackbar.make(findViewById(android.R.id.content),"OK", Snackbar.LENGTH_SHORT).show()

            }else{
                Snackbar.make(findViewById(android.R.id.content),"NO", Snackbar.LENGTH_SHORT).show()

            }
        }

//        読み込み

        findViewById<Button>(R.id.inButton).setOnClickListener {
            val id = findViewById<EditText>(R.id.inIdText).text.toString()

            database2.child("user").child(id).child("email").get().addOnSuccessListener {
                Log.i(TAG, "Got value ${it.value}")
                findViewById<TextView>(R.id.inText).text = it.value.toString()
            }.addOnFailureListener{
                findViewById<TextView>(R.id.inText).text = "一致なし"
                Log.e(TAG, "Error getting data", it)
            }
            Snackbar.make(findViewById(android.R.id.content),"OK", Snackbar.LENGTH_SHORT).show()

        }


//        ============================================================
//      配列のようにデータを読み込む
        val database3 = Firebase.database.getReference("users")

        findViewById<Button>(R.id.testButton).setOnClickListener {
            database3.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d(TAG, "===============================================")

                    var output = ""

                    for (dataSnapshot in snapshot.children) {

                        val sender = dataSnapshot.child("email").value as String?
                        val body = dataSnapshot.child("username").value as String?
                        Log.d(TAG, String.format("id:%s, email:%s, username:%s",dataSnapshot.key, sender, body))
                        output += String.format("id:%s, email:%s, username:%s \n",dataSnapshot.key, sender, body)
                    }

                    findViewById<TextView>(R.id.inText).text = output
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }


    }
}