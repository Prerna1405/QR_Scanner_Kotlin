package com.example.internship_project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pickImageButton = findViewById<Button>(R.id.pickImageButton)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val qrCodeImage = findViewById<ImageView>(R.id.qrCodeImage)
        val selectedImageView = findViewById<ImageView>(R.id.selectedImageView)

        pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        generateButton.setOnClickListener {
            if (selectedImageUri != null) {
                val imageUri = saveImageToExternalStorage(selectedImageUri!!)
                if (imageUri != null) {
                    generateAndShowQRCode(imageUri.toString(), qrCodeImage)
                } else {
                    Toast.makeText(this, "Image saving failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            val selectedImageView = findViewById<ImageView>(R.id.selectedImageView)
            selectedImageUri?.let { uri ->
                selectedImageView.setImageURI(uri)
            }
        }
    }

    private fun saveImageToExternalStorage(uri: Uri): Uri? {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val filename = "img_${UUID.randomUUID()}.jpg"
        val file = File(getExternalFilesDir(null), filename)

        FileOutputStream(file).use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
            output.flush()
        }

        return FileProvider.getUriForFile(this, "$packageName.provider", file)
    }

    private fun generateAndShowQRCode(content: String, qrCodeImage: ImageView) {
        val qrBitmap = generateQRCode(content)
        qrCodeImage.setImageBitmap(qrBitmap)
    }

    private fun generateQRCode(content: String): Bitmap {
        val width = 500
        val height = 500
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }
}
