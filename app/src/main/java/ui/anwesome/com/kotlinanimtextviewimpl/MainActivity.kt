package ui.anwesome.com.kotlinanimtextviewimpl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ui.anwesome.com.kotlinanimtextview.AnimTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AnimTextView.create(this)
        AnimTextView.addText("hello")
        AnimTextView.addText("world")
        AnimTextView.addText("worlo world")
        AnimTextView.addText("stag not")
        AnimTextView.addText("Stag not")
        AnimTextView.addText("worlo world")
        AnimTextView.addText("couple allowed")
        AnimTextView.addOnOpenCloseListener({Toast.makeText(this,"opened",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"closed",Toast.LENGTH_SHORT).show()})
        AnimTextView.show(this)
    }
}
