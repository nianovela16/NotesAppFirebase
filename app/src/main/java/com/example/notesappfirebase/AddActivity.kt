package com.example.notesappfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        ref = FirebaseDatabase.getInstance().getReference(user?.uid.toString())

        save.setOnClickListener {
            savedata()
        }
    }

    private fun savedata() {
        val todo = inputTodo.text.toString()

        val noteId = ref.push().key.toString()
        val note =  Note(noteId,todo)


        ref.child(noteId).setValue(note).addOnCompleteListener {
            Toast.makeText(this, "Successs",Toast.LENGTH_SHORT).show()
            inputTodo.setText("")
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }
    }
}