package com.example.simplecalculator

class LexicalAnalyzer(val strExp: String) {
    var counter=0
    var lexeme= StringBuilder()
    var nextChar:Char='n'
    var charClass = ""
    var arrExp = ArrayList<String>()
    var nextToken=0

    init{
        getChar(strExp)
        do{
            lex()
        }while(nextToken!=999)

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
}