package com.jocoo.swork.work.audit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.gdmm.core.BaseCompatActivity
import com.jocoo.swork.R
import com.jocoo.swork.databinding.ActivityWorkAuditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkAuditActivity :
    BaseCompatActivity<ActivityWorkAuditBinding, WorkAuditState, WorkAuditViewModel>() {

    override val viewModel: WorkAuditViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindListener() {

    }

    override fun onViewStateChange(state: WorkAuditState) {

    }

    override fun getViewBinding() = ActivityWorkAuditBinding.inflate(layoutInflater)

}