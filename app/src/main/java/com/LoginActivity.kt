package com

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import androidx.core.net.toUri

class LoginActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private lateinit var textureView: TextureView
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide system UI for fullscreen video
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_login)
        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = this
    }

    private fun playVideo(surface: Surface) {
        val uri = "android.resource://$packageName/${R.raw.video}".toUri()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@LoginActivity, uri)
            setSurface(surface)
            isLooping = true
            setVolume(0f, 0f)
            setOnPreparedListener {
                adjustVideoScaling(it)
                start()
            }
            prepareAsync()
        }
    }

    private fun adjustVideoScaling(mp: MediaPlayer) {
        val videoWidth = mp.videoWidth.toFloat()
        val videoHeight = mp.videoHeight.toFloat()
        val viewWidth = textureView.width.toFloat()
        val viewHeight = textureView.height.toFloat()

        val scaleX = viewWidth / videoWidth
        val scaleY = viewHeight / videoHeight
        val scale = scaleX.coerceAtLeast(scaleY) // crop center

        textureView.scaleX = scale * (videoWidth / viewWidth)
        textureView.scaleY = scale * (videoHeight / viewHeight)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        playVideo(Surface(surface))
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        mediaPlayer?.release()
        mediaPlayer = null
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }
}
