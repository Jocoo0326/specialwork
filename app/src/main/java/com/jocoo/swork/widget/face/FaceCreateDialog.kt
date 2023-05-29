package com.jocoo.swork.widget.face

import android.graphics.*
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.view.PreviewView
import androidx.fragment.app.FragmentActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.google.android.material.appbar.MaterialToolbar
import com.jocoo.swork.R
import com.jocoo.swork.util.scale
import com.jocoo.swork.util.toBase64String
import com.lxj.xpopup.impl.FullScreenPopupView

class FaceCreateDialog(
    private val activity: FragmentActivity,
    private val viewModel: FaceViewModel
) : FullScreenPopupView(activity) {

    private var viewMask: ImageView? = null

    private fun initViewMask() {
        viewMask?.let {
            val bitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            canvas.drawColor(Color.WHITE)
            paint.color = 0x00000000
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawCircle(it.width * 0.5f, it.height * 0.35f, it.width * 0.4f, paint)
            it.setImageBitmap(bitmap)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            smartDismiss()
        }
        viewMask = findViewById(R.id.view_mask)
        viewMask?.post { initViewMask() }
        val tvTips = findViewById<TextView>(R.id.tv_tips)
        val preview = findViewById<PreviewView>(R.id.view_finder)
        val cameraHelper = FaceCameraHelper()
        cameraHelper.attach(this.activity, preview)
        cameraHelper.startPreview {
            viewModel.startFace()
        }
        viewModel.faceFlow.observeWithLifecycle(this) {
            if (it == FaceViewModel.success_msg || it == FaceViewModel.no_permission_msg) {
                dismiss()
            } else {
                tvTips.text = if (it == FaceViewModel.start_msg) "请保持人脸在框内" else it
                preview.bitmap?.let { bm ->
                    viewModel.requestFace(bm.scale(720, 720).toBase64String())
                } ?: viewModel.startFace()
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.face_dialog
    }
}