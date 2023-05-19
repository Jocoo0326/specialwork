package com.jocoo.swork.widget

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.allen.library.SuperTextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.blankj.utilcode.util.TimeUtils
import com.gdmm.core.extensions.observeWithLifecycle
import com.hjq.toast.Toaster
import com.jocoo.swork.R
import com.jocoo.swork.bean.GasInfo
import com.jocoo.swork.bean.GasUnitType
import com.jocoo.swork.bean.SensorDataType
import com.jocoo.swork.data.TIME_PATTERN
import com.jocoo.swork.data.enum.WorkType
import com.jocoo.swork.work.audit.WorkAuditState
import com.jocoo.swork.work.audit.WorkAuditViewModel
import com.lxj.xpopup.core.BottomPopupView
import java.util.*

class GasAddDialog(
    context: Context,
    private val id: String?,
    private val actViewModel: WorkAuditViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        val stvType = findViewById<SuperTextView>(R.id.stv_type)
        val stvValue = findViewById<SuperTextView>(R.id.stv_value)
        val stvUnit = findViewById<SuperTextView>(R.id.stv_unit)
        val stvTime = findViewById<SuperTextView>(R.id.stv_time)
        val stvPerson = findViewById<SuperTextView>(R.id.stv_person)
        val stvGroup = findViewById<SuperTextView>(R.id.stv_group)
        val stvStandard = findViewById<SuperTextView>(R.id.stv_standard)
        val stvLocation = findViewById<SuperTextView>(R.id.stv_location)
        if (actViewModel.workType == WorkType.Fire_Id) {
            stvGroup.visibility = View.GONE
            stvStandard.visibility = View.GONE
            stvLocation.visibility = View.GONE
        } else if (actViewModel.workType == WorkType.LimitSpace_Id ||
            actViewModel.workType == WorkType.Electric_Id
        ) {
            stvGroup.visibility = View.VISIBLE
            stvStandard.visibility = View.VISIBLE
            stvLocation.visibility = View.VISIBLE
        }
        fun GasInfo.updateContent(includeEdit: Boolean = false) {
            stvType.setRightString(gas_type_name)
            stvUnit.setRightString(unit_name)
            stvGroup.setRightString(group_name)
            stvTime.setRightString(analysis_time)
            if (includeEdit) {
                stvValue.editText.setText(concentration)
                stvStandard.editText.setText(standard)
                stvLocation.editText.setText(place)
                stvPerson.editText.setText(analysis_user)
            }
        }
        if (!id.isNullOrEmpty()) {
            actViewModel.state.observeWithLifecycle(viewLifecycleOwner) {
                it.getCurGasItem(id)?.updateContent(true)
            }
        } else {
            actViewModel.resetNewGasItem()
            actViewModel.state.observeWithLifecycle(viewLifecycleOwner) {
                it.newGasItem.updateContent(true)
            }
        }
        stvType.setOnClickListener {
            val list = actViewModel.state.value.gasConfig?.sensor_data_type
            val picker = OptionsPickerBuilder(context) { op1, _, _, _ ->
                list?.getOrNull(op1)?.let {
                    gasItemOrNew(id).gas_type_id = it.id
                    gasItemOrNew(id).gas_type_name = it.name
                    gasItemOrNew(id).updateContent()
                }
            }.build<SensorDataType>()
            picker.setPicker(list)
            if (!list.isNullOrEmpty()) {
                gasItemOrNew(id).let {
                    picker.setSelectOptions(list.indexOfFirst { it1 -> it1.id == it.gas_type_id })
                }
            }
            picker.show()
        }
        stvUnit.setOnClickListener {
            val list = actViewModel.state.value.gasConfig?.gas_unit_type
            val picker = OptionsPickerBuilder(context) { op1, _, _, _ ->
                list?.getOrNull(op1)?.let {
                    gasItemOrNew(id).unit_type = it.id
                    gasItemOrNew(id).unit_name = it.name
                    gasItemOrNew(id).updateContent()
                }
            }.build<GasUnitType>()
            picker.setPicker(list)
            if (!list.isNullOrEmpty()) {
                gasItemOrNew(id).let {
                    picker.setSelectOptions(list.indexOfFirst { it1 -> it1.id == it.unit_type })
                }
            }
            picker.show()
        }
        stvGroup.setOnClickListener {
            val list = actViewModel.state.value.gasConfig?.gas_group_type
            val picker = OptionsPickerBuilder(context) { op1, _, _, _ ->
                list?.getOrNull(op1)?.let {
                    gasItemOrNew(id).group_id = it.id
                    gasItemOrNew(id).group_name = it.name
                    gasItemOrNew(id).updateContent()
                }
            }.build<GasUnitType>()
            picker.setPicker(list)
            if (!list.isNullOrEmpty()) {
                gasItemOrNew(id).let {
                    picker.setSelectOptions(list.indexOfFirst { it1 -> it1.id == it.group_id })
                }
            }
            picker.show()
        }
        stvTime.setOnClickListener {
            val picker = TimePickerBuilder(context) { date, _ ->
                gasItemOrNew(id).analysis_time = TimeUtils.date2String(date, TIME_PATTERN)
                gasItemOrNew(id).updateContent()
            }.build()
            if (!id.isNullOrEmpty()) {
                gasItemOrNew(id).let {
                    val ca = Calendar.getInstance()
                    ca.time = TimeUtils.string2Date(it.analysis_time, TIME_PATTERN)
                    picker.setDate(ca)
                }
            }
            picker.show()
        }
        findViewById<View>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }
        findViewById<View>(R.id.btn_submit).setOnClickListener {
            val showExtraOptions = actViewModel.workType == WorkType.LimitSpace_Id ||
                actViewModel.workType == WorkType.Electric_Id
            val gas_type_name = stvType.rightString
            if (gas_type_name.isNullOrEmpty()) {
                Toaster.show("气体名称不能为空")
                return@setOnClickListener
            }
            val concentration = stvValue.editText.text.toString()
            if (concentration.isNullOrEmpty()) {
                Toaster.show("气体浓度不能为空")
                return@setOnClickListener
            }
            val unit_name = stvUnit.rightString
            if (unit_name.isNullOrEmpty()) {
                Toaster.show("气体单位不能为空")
                return@setOnClickListener
            }
            val standard = stvStandard.editText.text.toString()
            if (standard.isEmpty() && showExtraOptions) {
                Toaster.show("气体标准不能为空")
                return@setOnClickListener
            }
            val group_name = stvGroup.rightString
            if (group_name.isEmpty() && showExtraOptions) {
                Toaster.show("气体分组不能为空")
                return@setOnClickListener
            }
            val place = stvLocation.editText.text.toString()
            if (place.isEmpty() && showExtraOptions) {
                Toaster.show("气体部位不能为空")
                return@setOnClickListener
            }
            val analysis_time = stvTime.rightString
            if (analysis_time.isNullOrEmpty()) {
                Toaster.show("分析时间不能为空")
                return@setOnClickListener
            }
            val analysis_user = stvPerson.editText.text.toString()
            if (analysis_user.isEmpty()) {
                Toaster.show("分析人不能为空")
                return@setOnClickListener
            }
            val gas = gasItemOrNew(id)
            gas.let { gas ->
                gas.concentration = concentration
                gas.standard = standard
                gas.place = place
                gas.analysis_user = analysis_user
            }
            if (id.isNullOrEmpty()) {
                actViewModel.addGasItem()
            } else {
                actViewModel.gasModified(id, gas)
            }
            dismiss()
        }
    }

    private fun gasItemOrNew(id: String?): GasInfo {
        return actViewModel.state.value.getCurGasItem(id) ?: actViewModel.state.value.newGasItem
    }

    private fun WorkAuditState.getCurGasItem(id: String?): GasInfo? {
        return this.detail?.sensorDataList?.firstOrNull {
            it.id == id
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_gas_add
    }
}