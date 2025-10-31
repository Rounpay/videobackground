package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<MaterialButton>(R.id.materialButton).setOnClickListener { v ->
            //   higherOrderFunction()
            //  extensionFun()
           /* val user = User()
            user.name = "Prabhat"
            user.name = "Yadav"*/
            val b = delegationImplementation("\nWelcome, GFG!")

            Newfeature(b).mymessage()
            Newfeature(b).mymessageline()
        }

    }

    private fun extensionFun() {
        val name = "prabhat yadav"
        println(name.capitalizeWords())
    }

    // Higher-order function
    private fun higherOrderFunction() {
        val sum = performOperation(5, 3) { x, y -> x + y } // lambda function
        val multiply = performOperation(5, 3) { x, y -> x * y }
        println("Sum------------->: $sum")         // Sum: 8
        println("Multiply------------->: $multiply") // Multiply: 15
    }

    fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }

    // Extension function for String
    fun String.capitalizeWords(): String {
        return this.split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
    }
}

sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val msg: String) : Result()
    object Loading : Result()
}

fun handle(result: Result) {
    when (result) {
        is Result.Success -> println("Data: ${result.data}")
        is Result.Error -> println("Error: ${result.msg}")
        is Result.Loading -> println("Loading...")
    }
}

class User {
    var name: String by Delegates.observable("<No Name>") { _, old, new ->
        println("Name changed from $old → $new")
    }
}

interface deleGation {
    fun mymessage()
    fun mymessageline()
}

class delegationImplementation(val y: String) : deleGation {
    override fun mymessage() {
        print(y)
    }

    override fun mymessageline() {
        println(y)
    }
}

class Newfeature(m: deleGation) : deleGation by m {
    override fun mymessage() {
        print("GeeksforGeeks")
    }
}