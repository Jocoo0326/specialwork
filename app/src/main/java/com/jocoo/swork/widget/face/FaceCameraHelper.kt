package com.jocoo.swork.widget.face

import android.view.Surface
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class FaceCameraHelper {

    private var previewView: PreviewView? = null
    private var act: FragmentActivity? = null

    fun attach(activity: FragmentActivity, preview: PreviewView) {
        this.act = activity
        this.previewView = preview
        initView()
    }

    fun initView() {
        this.previewView?.let {
            it.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            it.scaleType = PreviewView.ScaleType.FIT_CENTER
        }
    }

    fun startPreview(callback: () -> Unit) {
        this.act?.let {
            val listenableFuture = ProcessCameraProvider.getInstance(it)
            listenableFuture.addListener({
                val cameraProvider = listenableFuture.get()
                val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build()
                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(it, cameraSelector, preview)
                    preview.setSurfaceProvider(this.previewView?.surfaceProvider)
                    callback()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(it))
        }
    }


}