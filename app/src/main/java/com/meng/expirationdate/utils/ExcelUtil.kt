package com.meng.expirationdate.utils

import android.content.Context
import com.meng.expirationdate.R
import com.meng.expirationdate.room.ItemInfo
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.format.Colour
import jxl.write.*
import java.io.File
import java.io.FileInputStream

object ExcelUtil {
    private val arial14font = WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD)
    private val arial14format = WritableCellFormat(arial14font)
    private val arial10font = WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD)
    private val arial10format = WritableCellFormat(arial10font)
    private val arial12font = WritableFont(WritableFont.ARIAL, 10)
    private val arial12format = WritableCellFormat(arial12font)
    private const val UTF8_ENCODING = "UTF-8"

    init {
        format()
    }

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private fun format() {
        try {
            arial14font.colour = Colour.LIGHT_BLUE
            arial14format.alignment = jxl.format.Alignment.CENTRE
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN)
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW)

            arial10format.alignment = jxl.format.Alignment.CENTRE
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN)
            arial10format.setBackground(Colour.GRAY_25)

            //对齐格式
            arial10format.alignment = jxl.format.Alignment.CENTRE
            arial12format.alignment = jxl.format.Alignment.CENTRE
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN)

        } catch (e : WriteException) {
            e.printStackTrace()
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    fun initExcel(filePath: String, sheetName: String, colName : java.util.ArrayList<String>) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            } else {
                return
            }
            val workbook = Workbook.createWorkbook(file)
            //设置表格的名字
            val sheet = workbook?.createSheet(sheetName, 0)
            //创建标题栏
            sheet?.addCell(Label(0, 0, filePath, arial14format) as WritableCell)
            for (col in colName.indices) {
                sheet?.addCell(Label(col, 0, colName[col], arial10format))
            }
            //设置行高
            sheet?.setRowView(0, 340)
            workbook?.write()
            workbook?.close()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param objList  待写入的list
     * @param fileName 绝对路径
     * @param mContext
     * @param writeFinishListener 写入完成后回调
     */
    fun writeObjListToExcel(objList: List<ItemInfo>?, fileName: String, mContext: Context, writeFinishListener: WriteFinishListener) {
        if (objList.isNullOrEmpty()) {
            CustomToast.showToast(mContext.getString(R.string.save_to_excel_no_data))
        } else {
            try {
                val setEncode = WorkbookSettings()
                setEncode.encoding = UTF8_ENCODING

                val inputStream = FileInputStream(File(fileName))
                val workbook = Workbook.getWorkbook(inputStream)
                val writeBook = Workbook.createWorkbook(File(fileName), workbook)
                val sheet = writeBook.getSheet(0)

                for (j in objList.indices) {
                    val itemInfo = objList[j]
                    val list = ArrayList<String>()
                    list.add(itemInfo.itemId.toString())
                    list.add(itemInfo.itemName)
                    list.add(itemInfo.itemNum.toString())
                    list.add(MyStringUtils.getRemark(itemInfo.itemDescription))
                    list.add(itemInfo.itemType.toString())
                    list.add(MyStringUtils.getItemType(itemInfo.itemType))
                    list.add(itemInfo.itemProductionDate ?: "")
                    list.add(itemInfo.itemExpirationDate ?: "")

                    for (i in list.indices) {
                        sheet.addCell(Label(i, j + 1, list[i], arial12format))
                        //设置列宽
                        sheet.setColumnView(i, list[i].length + if (list[i].length <= 4) 8 else 5)
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350)
                }

                writeBook.write()
                workbook.close()
                writeBook.close()
                inputStream.close()
                CustomToast.showToast(mContext.getString(R.string.save_to_excel_success))
                writeFinishListener.finishWrite()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 保存完成回调
     * */
    interface WriteFinishListener {
        fun finishWrite()
    }
}