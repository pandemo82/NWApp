
package com.example.nwapp

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_third.*
import org.w3c.dom.Text
import java.io.File
import java.io.InputStream
import kotlin.random.Random


class ThirdFragment : Fragment() {
    var dataSnapshotPS:DataSnapshot? = null
    var answer: String? = null
    var randomInt: Int? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomInt = 6
        var key = "H31_$randomInt"
//        var key = "H31_6"
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(key)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshotPS = dataSnapshot
                answer = dataSnapshot.child("answer").value.toString()
                setNextProblem(dataSnapshot,view)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        view.findViewById<Button>(R.id.nextButton).setOnClickListener {
            randomInt = Random.nextInt(25)+1
            var key = "H31_$randomInt"
            //var key = "R1_2"
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference(key)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshotPS = dataSnapshot
                    val value = dataSnapshot.child("problemText").value
                    setNextProblem(dataSnapshot,view)
                    answer = dataSnapshot.child("answer").value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        view.findViewById<Button>(R.id.select1Button).setOnClickListener {
            hantei("ア",view)
        }
        view.findViewById<Button>(R.id.select2Button).setOnClickListener {
            hantei("イ",view)
        }
        view.findViewById<Button>(R.id.select3Button).setOnClickListener {
            hantei("ウ",view)
        }
        view.findViewById<Button>(R.id.select4Button).setOnClickListener {
            hantei("エ",view)
        }
        view.findViewById<Button>(R.id.ExplanationButton).setOnClickListener {
            view.findViewById<Button>(R.id.ExplanationButton).visibility = View.INVISIBLE

            view.findViewById<ImageView>(R.id.maru).visibility = View.INVISIBLE
            view.findViewById<ImageView>(R.id.batu).visibility = View.INVISIBLE
            view.findViewById<ImageView>(R.id.problemView).setImageDrawable(null)

            var value = dataSnapshotPS!!.child("explanation1").value
            view.findViewById<TextView>(R.id.select1Text).append("\n" + value.toString())
            view.findViewById<TextView>(R.id.select1Text).setTextColor(Color.RED)

            value = dataSnapshotPS!!.child("explanation2").value
            view.findViewById<TextView>(R.id.select2Text).append("\n" + value.toString())
            view.findViewById<TextView>(R.id.select2Text).setTextColor(Color.RED)


            value = dataSnapshotPS!!.child("explanation3").value
            view.findViewById<TextView>(R.id.select3Text).append("\n" + value.toString())
            view.findViewById<TextView>(R.id.select3Text).setTextColor(Color.RED)

            value = dataSnapshotPS!!.child("explanation4").value
            view.findViewById<TextView>(R.id.select4Text).append("\n" + value.toString())
            view.findViewById<TextView>(R.id.select4Text).setTextColor(Color.RED)

        }
    }

    fun hantei(selectAnswer: String, view: View) {
        view.findViewById<Button>(R.id.ExplanationButton).visibility = View.VISIBLE
        //正解、不正解の判定　、画像表示

        view.findViewById<ImageView>(R.id.maru).visibility = View.VISIBLE
        Log.d("TAGFBANSWER", "$selectAnswer , $answer")

        if (dataSnapshotPS!!.child("answer").value == selectAnswer){
            view.findViewById<ImageView>(R.id.maru).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.batu).visibility = View.INVISIBLE
        }else{
            view.findViewById<ImageView>(R.id.batu).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.maru).visibility = View.INVISIBLE
        }

        view.findViewById<Button>(R.id.select1Button).visibility = View.INVISIBLE
        view.findViewById<Button>(R.id.select2Button).visibility = View.INVISIBLE
        view.findViewById<Button>(R.id.select3Button).visibility = View.INVISIBLE
        view.findViewById<Button>(R.id.select4Button).visibility = View.INVISIBLE
    }

    fun setNextProblem(dataSnapshot: DataSnapshot ,view: View){
        Log.d("TAGFBIMAGETEST","callsetnextp")

        view.findViewById<Button>(R.id.select1Button).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.select2Button).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.select3Button).visibility = View.VISIBLE
        view.findViewById<Button>(R.id.select4Button).visibility = View.VISIBLE

        view.findViewById<Button>(R.id.ExplanationButton).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.maru).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.batu).visibility = View.INVISIBLE

        val value = dataSnapshot.child("problemText").value
        view.findViewById<TextView>(R.id.problemText).text = value.toString()
        view.findViewById<TextView>(R.id.problemText).setTextColor(Color.BLACK)

        val problemPath1 = dataSnapshot.child("imageExists").value
        Log.d("imageExists",problemPath1.toString()+" ")

        if (problemPath1 != ""){
            //val reference = FirebaseStorage.getInstance().getReference("images/h31am10a4.png")
            val reference = FirebaseStorage.getInstance().getReference("images/h31am${randomInt}p1.png")
            Glide.with(this /* context */)
                .load(reference)
                .into(view.findViewById(R.id.problemView))
            Log.d("TAGFBIMAGETEST1",problemPath1.toString()+" ")
        }else{
            view.findViewById<ImageView>(R.id.problemView).setImageDrawable(null)
            Log.d("TAGFBIMAGETEST2","TESTNULLSET"+" ")
        }


        val select1Text = dataSnapshot.child("select1").value
        view.findViewById<TextView>(R.id.select1Text).text = "ア  $select1Text"
        view.findViewById<TextView>(R.id.select1Text).setTextColor(Color.BLACK)

        val select2Text = dataSnapshot.child("select2").value
        view.findViewById<TextView>(R.id.select2Text).text = "イ  $select2Text"
        view.findViewById<TextView>(R.id.select2Text).setTextColor(Color.BLACK)

        val select3Text = dataSnapshot.child("select3").value
        view.findViewById<TextView>(R.id.select3Text).text = "ウ  $select3Text"
        view.findViewById<TextView>(R.id.select3Text).setTextColor(Color.BLACK)

        val select4Text = dataSnapshot.child("select4").value
        view.findViewById<TextView>(R.id.select4Text).text = "エ  $select4Text"
        view.findViewById<TextView>(R.id.select4Text).setTextColor(Color.BLACK)

        val warekiText = dataSnapshot.key.toString()
        view.findViewById<TextView>(R.id.warekiText).text = " 平成31年 問${warekiText[warekiText.length-1]}"

    }
}
