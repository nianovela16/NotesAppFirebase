package com.example.notesappfirebase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Adapter(val mCtx: Context, val layoutResId: Int, val  list: List<Note>)
    : ArrayAdapter<Note>(mCtx,layoutResId,list){

    private lateinit var auth : FirebaseAuth


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)


        val textNote = view.findViewById<TextView>(R.id.textNote)
        val textUpdate = view.findViewById<TextView>(R.id.update)
        val textDelete = view.findViewById<TextView>(R.id.delete)

        val note = list[position]



        textNote.text = note.note
        textDelete.setOnClickListener {
            Deleteinfo(note)
        }

        textUpdate.setOnClickListener {
            showUpdateDialog(note)
        }

        return view
    }

    private fun showUpdateDialog(note: Note) {
        val builder = AlertDialog.Builder(mCtx)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update, null)

        val textNote = view.findViewById<EditText>(R.id.inputNote)

        textNote.setText(note.note)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            val dbUsers = FirebaseDatabase.getInstance().getReference(user?.uid.toString())

            val catatan = textNote.text.toString().trim()


            if (catatan.isEmpty()){
                textNote.error = "please enter note"
                textNote.requestFocus()
                return@setPositiveButton
            }

            val note = Note(note.id,catatan)

            dbUsers.child(note.id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }


    private fun Deleteinfo(note: Note) {
        auth = FirebaseAuth.getInstance()
        val todo = auth.currentUser

        val mydatabase = FirebaseDatabase.getInstance().getReference(todo?.uid.toString())
        mydatabase.child(note.id).removeValue()
        Toast.makeText(mCtx,"Deleted,Swipe To Refresh",Toast.LENGTH_SHORT).show()

    }

}
