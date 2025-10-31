package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cropImageLauncher: ActivityResultLauncher<CropImageContractOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val uriContent: Uri? = result.uriContent
                binding.imageGet.setImageURI(uriContent)

                val filePath = uriContent?.let { FileUtils.getFilePathFromUri(this, it, true) }
                Log.d("CropResult", "Cropped Image Path: $filePath")
                filePath?.let { uploadImageToServer(File(it)) }
                binding.rotationCounter.text = "Rotation: ${result.rotation}°"
            } else {
                Log.e("CropResult", "Error: ${result.error}")
                Toast.makeText(this, "Crop failed: ${result.error}", Toast.LENGTH_LONG).show()
            }
        }

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                startCropImage(uri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        binding.Android15.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun startCropImage(uri: Uri) {
        cropImageLauncher.launch(
            CropImageContractOptions(
                uri = uri,
                cropImageOptions = CropImageOptions().apply {
                    cropShape = CropImageView.CropShape.RECTANGLE
                    fixAspectRatio = true
                    aspectRatioX = 1
                    aspectRatioY = 1
                    guidelines = CropImageView.Guidelines.ON
                    activityTitle = "Crop Image"
                    toolbarColor = getColor(R.color.purple_500)
                    toolbarBackButtonColor = getColor(R.color.white)
                    toolbarTitleColor = getColor(R.color.white)
                    cropMenuCropButtonTitle = "Done"

                    // Rotation + flipping
                    allowRotation = true
                    allowFlipping = true
                    allowCounterRotation = true
                    showCropOverlay = true
                    showProgressBar = true
                    autoZoomEnabled = true

                }
            )
        )
    }

    private fun updateRotationCounter(cropImageView: CropImageView) {
        binding.rotationCounter.text = "Rotation: ${cropImageView.rotatedDegrees}°"
    }

    private fun updateExpectedImageSize(cropImageView: CropImageView) {
        binding.cropSizeInfo.text = "Crop Size: ${cropImageView.expectedImageSize()}"
    }

    private fun uploadImageToServer(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        RetrofitClient.instance.uploadFile(body)
            .enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val uploaded = response.body()
                        Log.d("Upload", "Success: ${uploaded?.location}")
                        Toast.makeText(
                            this@MainActivity,
                            "Uploaded: ${uploaded?.location}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("Upload", "Failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    Log.e("Upload", "Error: ${t.message}")
                }
            })
    }
}
