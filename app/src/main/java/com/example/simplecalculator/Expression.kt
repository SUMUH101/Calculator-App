package com.example.simplecalculator

class Expression(val strExp: String) {
    var stringExp: String
    var answer= 0.0

    init {
        stringExp = strExp.replace(Regex("÷"), "/")
        stringExp= stringExp.replace(Regex("×"), "*")
        stringExp =stringExp.plus("$")
        calculate()
    }

    fun calculate(){
        val str= LexicalAnalyzer(stringExp)
        val finalExp= str.arrExp.toTypedArray()
        val postExp = PostfixCalculator(finalExp)
        answer= postExp.result


    }




}