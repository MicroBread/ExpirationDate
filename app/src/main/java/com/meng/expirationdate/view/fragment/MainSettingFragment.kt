package com.meng.expirationdate.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.meng.expirationdate.BuildConfig
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseFragment
import com.meng.expirationdate.databinding.FragmentMainSettingBinding
import com.meng.expirationdate.event.PublicMsgEvent
import com.meng.expirationdate.room.DBManager
import com.meng.expirationdate.utils.CustomToast
import com.meng.expirationdate.utils.ExcelUtil
import com.meng.expirationdate.utils.onClickNoAnim
import com.meng.expirationdate.viewmodel.MainSettingViewModel
import com.meng.expirationdate.widget.AlertMsgDialog
import java.io.File


/**
 * 首页-设置页
 * */
class MainSettingFragment: BaseFragment<FragmentMainSettingBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_main_setting

    private val mViewModel by lazy {
        createVM<MainSettingViewModel>()
    }

    override fun initView() {
        mBinding.apply {
            vm = mViewModel
        }

        mBinding.rlClear.onClickNoAnim {
            //删除所有数据
            AlertMsgDialog.showMsgDialog(activity, "", getString(R.string.delete_all_confirm_msg), true, getString(R.string.cancel), null, getString(R.string.sure)) {
                //数据库删除所有条目
                DBManager.getItemsDAO()?.getAllItems()?.forEach {
                    DBManager.getItemsDAO()?.deleteItem(it)
                }
                PublicMsgEvent.dataChangeEvent(1) //通知数据更新
                CustomToast.showToast(getString(R.string.delete_success))
            }


        }

        mBinding.rlExcel.onClickNoAnim {
            //导出数据到excel文件
            AlertMsgDialog.showMsgDialog(activity, "", getString(R.string.save_to_excel_confirm), true, getString(R.string.cancel), null, getString(R.string.sure)) {
                context?.let { context ->
                    //顶部标题
                    val titles = arrayListOf(getString(R.string.id), getString(R.string.name), getString(R.string.number),
                        getString(R.string.remark), getString(R.string.sort_id), getString(R.string.sort),
                        getString(R.string.production_date), getString(R.string.expiration_date))
                    //文件存储路径
                    val fileName = activity?.externalCacheDir?.path + "/item_info_" + System.currentTimeMillis() + ".xls"
                    //保存所有数据到文件
                    ExcelUtil.initExcel(fileName, getString(R.string.item_info), titles)
                    ExcelUtil.writeObjListToExcel(DBManager.getItemsDAO()?.getAllItems(), fileName, context, object : ExcelUtil.WriteFinishListener {
                        override fun finishWrite() {
                            //保存完成后打开对应文件夹
                            activity?.externalCacheDir?.path?.takeIf { path -> path.isNotBlank() }?.let { path ->
                                val file = File(path)
                                if (file.exists()) {
                                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        val uri: Uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
                                        intent.setDataAndType(uri,"file/*")
                                    } else {
                                        intent.setDataAndType(Uri.fromFile(file), "file/*")
                                    }
                                    try {
                                        startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    override fun initData() {
        mViewModel.versionName.set(BuildConfig.VERSION_NAME)
    }

}