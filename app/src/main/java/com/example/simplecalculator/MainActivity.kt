package com.example.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity() {
    private var strNumber=StringBuilder()
    private lateinit var textView: TextView
    var strExp = ""


    private lateinit var numberButtons: Array<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeComponents()
        //disables keyboard so user must use GUI buttons for input
        textView.setShowSoftInputOnFocus(false)
        //Remove cursor
        textView.isCursorVisible=false

    }

    private fun initializeComponents() {
        textView = findViewById(R.id.textView)
        numberButtons= arrayOf(zeroBTN,oneBTN,twoBTN,threeBTN,fourBTN,fiveBTN,sixBTN,sevenBTN,eightBTN,nineBTN,addBTN,subtractBTN,divideBTN,multiplyBTN,exponentBTN,clearBTN,equalsBTN,pointBTN,openParenBTN,closedParenBTN,sinBTN,cosBTN,tanBTN)
        for (i in numberButtons){
            i.setOnClickListener { numberButtonClick(i) }
        }
        backspaceBTN.setOnClickListener{imageButtonClick(backspaceBTN)}
    }

    private fun imageButtonClick(img:ImageButton){
        if(img==backspaceBTN){
            var sub = textView.text
            if(strNumber.length > 1)
            {
                strNumber.replace(0,strNumber.length,strNumber.substring(0,strNumber.length-1))
                textView.text=strNumber

            }else if(strNumber.length<=1)
            {
                strNumber.replace(0,strNumber.length,"")
                textView.text=strNumber
            }
        }
    }
    private fun numberButtonClick(btn:Button) {
        //functionality for clear button
        if (btn==clearBTN){
            strNumber.replace(0,strNumber.length,"")
            textView.text=strNumber
        }else if(btn==equalsBTN)
        {
//            strExp= strNumber.toString()
//            val exp = Expression(strExp)
//
//            textView.text=exp.answer.toString()
//            strNumber.replace(0,strNumber.length,textView.text.toString())
//
//            strExp=""
            try{
                strExp= strNumber.toString()
                val exp = Expression(strExp)
                textView.text=exp.answer.toString()
                strNumber.replace(0,strNumber.length,textView.text.toString())
            }catch (e: NoSuchElementException){
                strExp=""
                strNumber.clear()
                textView.text= "Invalid Expression"

            }


        }else{
            strNumber.append(btn.text)
            textView.text=strNumber
        }

    }









}