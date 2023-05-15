package com.jocoo.swork.work.audit.safety

import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jocoo.swork.R
import com.jocoo.swork.bean.CheckInfo

class AuditSafetyAdapter(
    var readyToSign: Boolean = false,
) : BaseQuickAdapter<CheckInfo, BaseViewHolder>(
    R.layout.work_audit_safetymeasures_item
) {

    init {
        addChildClickViewIds(R.id.iv_signature)
    }

    fun signMode(b: Boolean) {
        readyToSign = b
        notifyDataSetChanged()
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        viewHolder.getView<CheckBox>(R.id.cb_selected)
            .setOnCheckedChangeListener { _, isChecked ->
                getItem(viewHolder.bindingAdapterPosition).isSelected = isChecked
            }
    }

    override fun convert(holder: BaseViewHolder, item: CheckInfo) {
        val checkBox = holder.getView<CheckBox>(R.id.cb_selected)
        val container = holder.getView<View>(R.id.container)
        checkBox.apply {
            isChecked = item.isSelected
            isEnabled = !readyToSign
        }
        when {
            item.isConfirmed -> {
                checkBox.visibility = View.INVISIBLE
                container.setBackgroundResource(R.color.grey_500)
                container.alpha = 0.8f
            }

            readyToSign && !item.isSelected -> {
                checkBox.visibility = View.VISIBLE
                container.setBackgroundResource(R.color.grey_500)
                container.alpha = 0.4f
            }

            else -> {
                checkBox.visibility = View.VISIBLE
                container.setBackgroundResource(R.color.white)
                container.alpha = 1.0f
            }
        }
        holder.setText(R.id.tv_number, "${holder.bindingAdapterPosition + 1}")
        holder.setText(R.id.tv_content, item.content)
        holder.setText(R.id.tv_involved, item.isHasStr)
        Glide.with(holder.itemView).load(item.sign).into(holder.getView(R.id.iv_signature))
    }
}