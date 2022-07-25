package com.meng.expirationdate.view.fragment

import android.os.Bundle
import android.view.Gravity
import com.meng.expirationdate.R
import com.meng.expirationdate.base.BaseDialogFragment
import com.meng.expirationdate.databinding.FragmentDialogItemDetailBinding
import com.meng.expirationdate.room.ItemInfo
import com.meng.expirationdate.utils.Utils

class ItemDetailDialogFragment: BaseDialogFragment<FragmentDialogItemDetailBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_dialog_item_detail

    override fun onStart() {
        super.onStart()
        val layoutParams = dialog?.window?.attributes
        layoutParams?.gravity = Gravity.NO_GRAVITY
        layoutParams?.height = Utils.convertDpToPixelOfInt(370)
        layoutParams?.width = Utils.convertDpToPixelOfInt(280)
        layoutParams?.dimAmount = 0.8f
        dialog?.window?.attributes = layoutParams
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Bottom_Translucent_NoTitle_hasDimEnabled)
    }

    override fun initView() {
        arguments?.getParcelable<ItemInfo>("itemInfo")?.let {
            mBinding.bean = it
        }
    }

    override fun initData() {
    }

    fun setItemInfo(item: ItemInfo) {
        mBinding.bean = item
    }
}