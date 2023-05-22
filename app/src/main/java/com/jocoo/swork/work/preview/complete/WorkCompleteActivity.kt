package com.jocoo.swork.work.preview.complete

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.setupActionBar
import com.gdmm.core.network.SessionManager
import com.hjq.toast.Toaster
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.ActivityWorkCompleteBinding
import com.jocoo.swork.util.toMain
import com.jocoo.swork.widget.CommonInputDialog
import com.jocoo.swork.widget.SignatureDialog
import com.jocoo.swork.widget.UploadImageViewModel
import com.jocoo.swork.widget.face.FaceCreateDialog
import com.jocoo.swork.widget.face.FaceViewModel
import com.lxj.xpopup.XPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Route(path = NavHub.WORK_COMPLETE)
@AndroidEntryPoint
class WorkCompleteActivity :
    BaseCompatActivity<ActivityWorkCompleteBinding, WorkCompleteState, WorkCompleteViewModel>() {

    private var needFace: Boolean = false
    override val viewModel: WorkCompleteViewModel by viewModels()
    private val uploadImageViewModel: UploadImageViewModel by viewModels()
    private val _userInputFlow = MutableSharedFlow<String>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val userInputFlow = _userInputFlow.asSharedFlow()

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var workId: String = ""

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var workType: Int = 0

    @JvmField
    @Autowired(name = COMM_KEY_3)
    var fireRank: String = ""

    private val faceViewModel: FaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            setupActionBar(toolbar)
            flSignature.setOnClickListener {
                if (needFace) {
                    faceViewModel.faceType = FaceViewModel.MATCH_FACE
                    faceViewModel.faceUserId =
                        SessionManager.getInstance(this@WorkCompleteActivity).userInfo?.user_id
                            ?: ""
                    XPopup.Builder(this@WorkCompleteActivity)
                        .enableDrag(false)
                        .dismissOnTouchOutside(false)
                        .asCustom(
                            FaceCreateDialog(this@WorkCompleteActivity, faceViewModel)
                        ).show()
                } else {
                    showSignatureDialog()
                }
            }
            tvComment.setOnClickListener {
                XPopup.Builder(this@WorkCompleteActivity).enableDrag(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(
                        CommonInputDialog(
                            this@WorkCompleteActivity, "意见", _userInputFlow
                        )
                    ).show()
            }
            btnDone.setOnClickListener {
                if (viewModel.comment.isEmpty()) {
                    Toaster.show("请输入完工验收意见")
                    return@setOnClickListener
                }
                if (needFace && viewModel.sign.isEmpty()) {
                    Toaster.show("请进行签名")
                    return@setOnClickListener
                }
                viewModel.setAccept(workId)
            }
        }
        viewModel.getFaceConfigs()
    }

    private fun showSignatureDialog() {
        XPopup.Builder(this@WorkCompleteActivity).enableDrag(false)
            .dismissOnTouchOutside(false)
            .asCustom(
                SignatureDialog(
                    this@WorkCompleteActivity,
                    uploadImageViewModel,
                    this@WorkCompleteActivity
                )
            ).show()

    }

    override fun bindListener() {
        uploadImageViewModel.uploadImageFlow.observeWithLifecycle(this) {
            viewModel.sign = it
            Glide.with(mBinding.ivSignature).load(it).into(mBinding.ivSignature)
        }
        userInputFlow.observeWithLifecycle(this) {
            viewModel.comment = it
            mBinding.tvComment.gravity = Gravity.START
            mBinding.tvComment.text = it
        }
        viewModel.setAcceptFlow.observeWithLifecycle(this) {
            Toaster.show("验收成功")
            toMain()
        }
        faceViewModel.faceFlow.observeWithLifecycle(this) {
            if (it == FaceViewModel.success_msg) {
                showSignatureDialog()
            }
        }
    }

    override fun onViewStateChange(state: WorkCompleteState) {
        state.faceConfigs?.filter {
            it.isSameWorkType(workType) && it.level == fireRank && it.approveTarget == "6"
        }?.let {
            needFace = true
        }
    }

    override fun getViewBinding() = ActivityWorkCompleteBinding.inflate(layoutInflater)

}