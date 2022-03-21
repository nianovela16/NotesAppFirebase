package com.example.notesappfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mCtx: Context
    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<com.example.notesappfirebase.Note>
    lateinit var listView: ListView
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        btnAdd.setOnClickListener {
         val add = Intent(this, AddActivity::class.java)
         startActivity(add)
        }

        userpp.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        name.setText(user?.displayName)

        progressBar2.startShimmerAnimation()
        swipe_to_refresh_layout.setOnRefreshListener{
            Toast.makeText(this, "Page Refreshed", Toast.LENGTH_SHORT).show()

            swipe_to_refresh_layout.isRefreshing = false
            recreate()
        }

        if (user != null){
            val ivProvile = findViewById<CircleImageView>(R.id.userpp)

            if (user.photoUrl != null){

                Picasso.get().load(user.photoUrl).into(ivProvile)
            }else{

                Picasso.get().load("https://i.picsum.photos/id/1025/4951/33 Q 01.jpg?hmac=_aGh5AtoOChip_iaMo8ZvvytfEojcgqbCH7dzaz-H8Y").into(ivProvile)
            }

        }

        ref = FirebaseDatabase.getInstance().getReference(user?.uid.toString())
        list = mutableListOf()
        listView = findViewById(R.id.listView)



        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    list.clear()
                    for (h in p0.children){
                        val user = h.getValue(com.example.notesappfirebase.Note::class.java)
                        list.add(user!!)
                    }
                    val adapter = Adapter(this@MainActivity,R.layout.note,list)
                    listView.adapter = adapter
                }
            }


        })
    }
}