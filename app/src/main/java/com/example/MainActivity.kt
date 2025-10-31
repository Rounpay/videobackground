package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.MovingBorderGlowView


import com.example.myapplication.R


class MainActivity : AppCompatActivity() {
     lateinit var movingBorder: MovingBorderGlowView
     lateinit var movingBorder1: MovingBorderGlowView
     lateinit var movingBorder2: MovingBorderGlowView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        movingBorder = findViewById(R.id.movingBorder1)
        movingBorder1 = findViewById(R.id.movingBorder2)
        movingBorder2 = findViewById(R.id.movingBorder3)
        //movingBorder.setBaseColor(color = Color.BLACK);
       // movingBorder.setHighlightColor(Color.YELLOW);
     //   movingBorder.setStrokeWidth(8f);


       // movingBorder1.setBaseColor(color = Color.BLACK);
       // movingBorder1.setHighlightColor(Color.YELLOW);
       // movingBorder1.setStrokeWidth(8f);

       // movingBorder2.setBaseColor(color = Color.BLACK);
     //   movingBorder2.setHighlightColor(Color.YELLOW);
        //movingBorder2.setStrokeWidth(8f);
    }

}