package com.jocoo.swork.ui.staff.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gdmm.core.BaseCompatActivity
import com.gdmm.core.extensions.observeWithLifecycle
import com.gdmm.core.extensions.simpleActionBar
import com.hjq.toast.Toaster
import com.jocoo.swork.bean.AppEvent
import com.jocoo.swork.bean.AppEventType
import com.jocoo.swork.data.COMM_KEY_1
import com.jocoo.swork.data.COMM_KEY_2
import com.jocoo.swork.data.COMM_KEY_3
import com.jocoo.swork.data.NavHub
import com.jocoo.swork.databinding.StaffDetailActivityBinding
import com.jocoo.swork.widget.face.FaceCreateDialog
import com.jocoo.swork.widget.face.FaceViewModel
import com.lxj.xpopup.XPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@Route(path = NavHub.STAFF_DETAIL)
@AndroidEntryPoint
class StaffDetailActivity :
    BaseCompatActivity<StaffDetailActivityBinding, StaffDetailState, StaffDetailViewModel>() {

    override val viewModel: StaffDetailViewModel by viewModels()
    private val faceViewModel: FaceViewModel by viewModels()

    @Inject
    lateinit var mEventFlow: MutableSharedFlow<AppEvent>

    @JvmField
    @Autowired(name = COMM_KEY_1)
    var name: String = ""

    @JvmField
    @Autowired(name = COMM_KEY_2)
    var isFace: Int = 0

    @JvmField
    @Autowired(name = COMM_KEY_3)
    var workerId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            simpleActionBar(toolbar)
            siName.setRightText(name)
            if (isFace == 1) {
                siFace.setRightText("已认证")
                btnEnterFaceId.visibility = View.GONE
            } else {
                siFace.setRightText("未认证")
                btnEnterFaceId.visibility = View.VISIBLE
            }
            btnEnterFaceId.setOnClickListener {
                faceViewModel.faceType = FaceViewModel.CREATE_FACE
                faceViewModel.faceUserId = workerId
                XPopup.Builder(this@StaffDetailActivity)
                    .enableDrag(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(
                        FaceCreateDialog(this@StaffDetailActivity, faceViewModel)
                    ).show()
            }
        }
    }

    override fun bindListener() {
        faceViewModel.faceFlow.observeWithLifecycle(this) {
            if (it.msg == FaceViewModel.success_msg) {
                mBinding.siFace.setRightText("已认证")
                mBinding.btnEnterFaceId.visibility = View.GONE
                Toaster.show("人脸信息录入成功")
                mEventFlow.tryEmit(AppEvent(AppEventType.CREATE_FACE))
            }
        }
    }

    override fun onViewStateChange(state: StaffDetailState) {

    }

    override fun getViewBinding() = StaffDetailActivityBinding.inflate(layoutInflater)

}