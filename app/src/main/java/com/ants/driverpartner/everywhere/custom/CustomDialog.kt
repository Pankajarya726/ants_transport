//package com.ants.driverpartner.everywhere.custom
//
//import android.app.Activity
//import android.app.Dialog
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//
//import com.ants.driverpartner.everywhere.R
//
//class CustomDialog : Activity() {
//
//    private var buttonClick: Button? = null
//
//    public override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.custom_dialog_main)
//
//        buttonClick = findViewById(R.id.buttonClick) as Button
//
//        // add listener to button
//        buttonClick!!.setOnClickListener {
//            // Create custom dialog object
//            val dialog = Dialog(this@CustomDialog)
//            // Include dialog.xml file
//            dialog.setContentView(R.layout.dialog)
//            // Set dialog title
//            dialog.setTitle("Custom Dialog")
//
//            // set values for custom dialog components - text, image and button
//            val text = dialog.findViewById(R.id.textDialog) as TextView
//            text.text = "Custom dialog Android example."
//            val image = dialog.findViewById(R.id.imageDialog) as ImageView
//            image.setImageResource(R.drawable.image0)
//
//            dialog.show()
//
//            val declineButton = dialog.findViewById(R.id.declineButton) as Button
//            // if decline button is clicked, close the custom dialog
//            declineButton.setOnClickListener(object : OnClickListener() {
//                fun onClick(v: View) {
//                    // Close dialog
//                    dialog.dismiss()
//                }
//            })
//        }
//
//    }
//
//}