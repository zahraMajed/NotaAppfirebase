package com.example.notaappfirebase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //my views
    lateinit var edNotes: EditText
    lateinit var btnSumbmit: Button
    val db = Firebase.firestore
    var TAG="iAmMainActivity"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //step 2:
        edNotes=findViewById(R.id.edNots)
        btnSumbmit=findViewById(R.id.btnSubmit)

        getNote()

        btnSumbmit.setOnClickListener(){
            if(edNotes.text.isNotEmpty()){
                val note = hashMapOf (
                    "note" to edNotes.text.toString()
                )
                db.collection("Notes").add(note)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        Toast.makeText(applicationContext, "data saved successfully!", Toast.LENGTH_SHORT).show()
                        getNote()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }else
                Toast.makeText(applicationContext, "please fill the missing entry!", Toast.LENGTH_SHORT).show()
            edNotes.text.clear()
        }//end btnSum listener

    }//endonCreate()

    fun getNote(){
        db.collection("Notes").get()
            .addOnSuccessListener { collection ->
                var NoteList= arrayListOf<List<Any>>()
                for (document in collection){
                    document.data.map { (k,v)->
                        NoteList.add(listOf(document.id,v))
                    }
                }//end for
                rv_main.adapter=RecyclerAdapter(this@MainActivity,NoteList)
                rv_main.layoutManager= LinearLayoutManager(this@MainActivity) 
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                Toast.makeText(applicationContext, "error!", Toast.LENGTH_SHORT).show()
            }
    }//end getNote()

    fun updataNote(id:String){
        val alert= AlertDialog.Builder(this)
        alert.setTitle("update note")
        alert.setMessage("Enter your updated note below ")

        val editNote=EditText(this)
        editNote.hint="Enter new note"
        alert.setView(editNote)

        alert.setNegativeButton("Save" , DialogInterface.OnClickListener(){
                dialog, which ->
            if(editNote.text.isNotEmpty()){
                val note = hashMapOf (
                    "note" to editNote.text.toString()
                )
                db.collection("Notes").document(id).set(note, SetOptions.merge())
                getNote()
            }

        })//end setNegativeButton

        alert.setPositiveButton("Cancel" , DialogInterface.OnClickListener(){
                dialog, which -> dialog.cancel()
        })//end positiveButton

        alert.create().show()
    }

    fun delNote(id:String){
        db.collection("Notes").document(id).delete()
        getNote()
    }

}//end class