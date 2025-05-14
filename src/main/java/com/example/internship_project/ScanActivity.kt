package com.example.internship_project

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var scannedImage: ImageView
    private lateinit var scanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        resultText = findViewById(R.id.resultText)
        scannedImage = findViewById(R.id.scannedImage)
        scanButton = findViewById(R.id.scanButton)

        scanButton.setOnClickListener {
            IntentIntegrator(this).apply {
                setPrompt("Scan QR Code")
                setBeepEnabled(true)
                setOrientationLocked(false)
                initiateScan()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                resultText.text = result.contents

                // Extract filename from QR content
                val lines = result.contents.split("\n")
                val imageLine = lines.find { it.startsWith("ImageFile:") }

                imageLine?.let {
                    val filename = it.substringAfter("ImageFile:").trim()
                    val file = File(filesDir, filename)
                    if (file.exists()) {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        scannedImage.setImageBitmap(bitmap)
                    } else {
                        Toast.makeText(this, "Image not found locally", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
