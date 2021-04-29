package com.example.simplecalculator

import java.util.ArrayDeque

class PostfixCalculator(val finalExp:Array<String>) {
    var result=0.0
    init{
        result= calculate(finalExp)
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
}