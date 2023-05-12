package com.jocoo.swork.work.preview

import android.os.Bundle
import androidx.activity.viewModels
import com.gdmm.core.BaseCompatActivity
import com.jocoo.swork.databinding.ActivityWorkPreviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkPreviewActivity :
    BaseCompatActivity<ActivityWorkPreviewBinding, WorkPreviewState, WorkPreviewViewModel>() {

    override val viewModel: WorkPreviewViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: WorkPreviewState) {

    }

    override fun getViewBinding() = ActivityWorkPreviewBinding.inflate(layoutInflater)

}