package com.example.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


//region Notes
//Removing cursor functionality for now to reduce erroneous input
//Might return it later so users can input new values/expression in betweeen previous input
//Must add functionality so that cursor stays in new position rather than returning to default location
//calculator has right to left associativity for operators of same precedence ,maybe change to l->r since thats the standard
//endregion
//region To Do
//create equal button functionality
//create update display
//create error checking for bad input
//endregion
class MainActivity : AppCompatActivity() {
    private var strNumber=StringBuilder()
    private lateinit var textView: TextView
    var charClass = ""
    var nextChar:Char='n'
    var strExp = ""
    var counter=0
    var nextToken=0
    var lexeme= StringBuilder()
    var exp: Array<String> = emptyArray()
    var arrExp = ArrayList<String>()

    private lateinit var nineBTN: Button
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
        nineBTN = findViewById(R.id.nineBTN)
        numberButtons= arrayOf(zeroBTN,oneBTN,twoBTN,threeBTN,fourBTN,fiveBTN,sixBTN,sevenBTN,eightBTN,nineBTN,addBTN,subtractBTN,divideBTN,multiplyBTN,exponentBTN,clearBTN,equalsBTN,pointBTN)
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
//                if(sub.toString()=="Enter an expression"){
//                    textView.text=""
//                    return
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
            strExp= strNumber.toString()
            strExp = strExp.replace(Regex("÷"), "/")
            strExp = strExp.replace(Regex("×"), "*")

            //val array = Array(sbString.length) { sbString[it].toString() }
            //val array = sbString.split(",").toTypedArray()
            //val array = arrayOf("11","-","1")
            strExp=strExp.plus("$")
            getChar(strExp)
            do{
                lex()
            }while(nextToken!=999)
            val finalExp: Array<String> = arrExp.toTypedArray()

            textView.text=calculate(finalExp).toString()
            strNumber.replace(0,strNumber.length,textView.text.toString())
            counter=0
            arrExp.clear()
            strExp=""
            nextToken=0

            //strExp.replace(0,strExp.length,textView.text.toString()+',')
        }else{
            strNumber.append(btn.text)
            textView.text=strNumber
        }

    }
    fun isOperator(ch: Char): Boolean {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/'  || ch == '(' || ch == ')'|| ch =='^'
    }
    fun addChar(){
        lexeme.append(nextChar)

    }
    fun NextChar(strExp: String): Char {
        val ret= strExp[counter]
        counter++
        return ret
    }
    fun getChar(strExp: String) {
        nextChar=NextChar(strExp)
        if ((nextChar) != '$') {
            if (nextChar.isLetter()) {
                charClass = "letter"
            } else if (nextChar.isDigit() || nextChar=='.') {
                charClass = "digit"
            } else charClass = "other"
        } else {
            charClass = "end" //'$' signifies end of string
        }
    }
    fun lex(): Int{
        lexeme.clear()

        when(charClass){
            "letter"->{
                addChar()
                getChar(strExp)
                while(charClass=="letter"){
                    addChar()
                    getChar(strExp)
                }
                if((lexeme.toString()=="sin") or (lexeme.toString()=="cos") or (lexeme.toString()=="tan")){
                    arrExp.add(lexeme.toString())
                }

            }
            "digit"->{
                addChar()
                getChar(strExp)
                while(charClass=="digit"){
                    addChar()
                    getChar(strExp)
                }
                arrExp.add(lexeme.toString())
            }
            "other"->{
                while(isOperator(nextChar)){
                    addChar()
                    getChar(strExp)
                }
                if(lexeme.length==1)
                {
                    arrExp.add(lexeme.toString())
                }else if(lexeme.length>1){
                    for(i in lexeme.toString()){
                        arrExp.add(i.toString())
                    }
                }

            }
            "end"->{
                nextToken=999
            }
        }
        return nextToken
    }
    fun calculate(args : Array<String>) : Double {

        val opStack = ArrayDeque<String>()
        val postfix = ArrayDeque<String>()

        for(arg in args) {

            // if left parenthesis, push to op stack
            if(arg.equals("("))
                opStack.push(arg)

            // if right parenthesis
            else if(arg.equals(")")){

                // until we find a left parenthesis
                while(!opStack.peek().equals("(")){

                    // pop all previous ops and push to postfix queue
                    postfix.push(opStack.pop())
                    if(opStack.isEmpty())
                        break
                }

                // pop the left parenthesis
                opStack.pop()

            }

            // if an operator
            else if(pemdas(arg) > 0){

                if(!opStack.isEmpty()){

                    // if we have previous operators in the op stack
                    // with greater precedence, pop and push those
                    // to the postfix queue
                    while(pemdas(opStack.peek()) >= pemdas(arg)){
                        postfix.push(opStack.pop())
                        if(opStack.isEmpty())
                            break
                    }

                }

                // push operator to the op stack
                opStack.push(arg)

            }

            else{

                // push number to the postfix queue
                // (if it's not a number, we'll know when calculating final postfix expression)
                postfix.push(arg)

            }

        }

        // push any remaining ops to the postfix queue
        while(opStack.size > 0){

            postfix.push(opStack.pop())

        }

        return calcPostfix(postfix)

    }

    fun calcPostfix(postfix : ArrayDeque<String>) : Double{

        val stack = ArrayDeque<Double>()
        var term : String

        print("POSTFIX: ")
        while(!postfix.isEmpty()){

            term = postfix.pollLast()
            print(term)
            if(pemdas(term) == 0){
                stack.push(term.toDouble())
            }
            else
                stack.push(calculateOp(term, stack))

        }
        println()
        return stack.pop()

    }

    fun calculateOp(op : String, stack : ArrayDeque<Double>) : Double{

        var term = stack.pop()

        // addition and multiplication can be simplified to += and *=
        // but I chose not to, so that we can see how postfix actually works
        if (op == "+") term = stack.pop() + term
        else if (op == "-") term = stack.pop() - term
        else if (op == "*") term = stack.pop() * term
        else if (op == "/") term = stack.pop() / term
        else if (op == "^") term = Math.pow(stack.pop(), term)
        else if (op == "sin") term = Math.sin(term)
        else if (op == "cos") term = Math.cos(term)
        else if (op == "tan") term = Math.tan(term)

        return term

    }

    // using a function instead of a map
// because of kotlin's strictness on possible nulls
    fun pemdas(op : String) : Int {

        if(op.equals("-")) return 2
        else if(op.equals("+")) return 2
        else if(op.equals("/")) return 3
        else if(op.equals("*")) return 3
        else if(op.equals("^")) return 4
        else if(op.equals("sin")) return 5
        else if(op.equals("cos")) return 5
        else if(op.equals("tan")) return 5
        return 0

    }

    fun putVariable(args : Array<String>, variable : String, value : Double) : Array<String>{

        var index = 0
        while(index < args.size){
            if(args[index].equals(variable))
                args[index] = "" + value
            index++
        }

        return args

    }

//    private fun convertDivMult(btn: Button): String {
//        var str=""
//        if (btn.text == "÷") {
//            str= "/"
//        } else if (btn.text == "×") {
//            str= "*"
//        }
//        return str
//    }
//
//    fun isNumber(s: String?): Boolean {
//        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
//    }
//
//


}