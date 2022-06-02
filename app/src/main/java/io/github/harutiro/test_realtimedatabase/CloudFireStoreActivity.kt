package io.github.harutiro.test_realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CloudFireStoreActivity : AppCompatActivity() {

    val TAG = "debag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_fire_store)

        val db = Firebase.firestore
        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        findViewById<Button>(R.id.serachButton).setOnClickListener {
            db.collection("users")
                .whereEqualTo("first","hello")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        Log.d(TAG, "${document.id} => ${document.data.size}")

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }


            val query = db.collection("users")
                .whereEqualTo("first","hello")

            Log.d(TAG,query.toString())



        }


    }
}