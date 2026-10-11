package dev.passchef.mobile.cookbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable fun MappingScreen(sources: CookbookSources,draft: MappingDraft) {
    val vi=LocalCookbookVietnamese.current
    val header = draft.rows.firstOrNull().orEmpty()
    val hasHeader=header.any { MenuTable.headerRole(it)!=null }
    var firstRow by remember { mutableStateOf(if(hasHeader)"2" else "1") }
    var columns by remember { mutableStateOf(ColumnRole.entries.associateWith { role ->
        if(role==ColumnRole.RECIPE) MenuTable.proposal(draft.rows)?.plus(1)?.toString().orEmpty()
        else header.indexOfFirst { MenuTable.headerRole(it)==role }.takeIf { it>=0 }?.plus(1)?.toString().orEmpty()
    }) }
    var manualLabels by remember { mutableStateOf<Map<Int,String>>(emptyMap()) }
    Text(words(vi,"Suggestions need your confirmation. Column numbers start at 1; blank means ignored.","Bạn cần xác nhận đề xuất. Số cột bắt đầu từ 1; để trống để bỏ qua."))
    OutlinedTextField(firstRow,{if(it.length<=4)firstRow=it},label={Text(words(vi,"First data row","Hàng dữ liệu đầu tiên"))},modifier=Modifier.fillMaxWidth())
    for(role in ColumnRole.entries) OutlinedTextField(columns[role].orEmpty(),{value->if(value.length<=2)columns=columns+(role to value)},label={Text(when(role){ColumnRole.RECIPE->"Recipe";ColumnRole.LABEL->words(vi,"Label","Tên");ColumnRole.USERNAME->words(vi,"Username","Tên đăng nhập");ColumnRole.URL->"URL";ColumnRole.NOTES->words(vi,"Notes","Ghi chú")})},modifier=Modifier.fillMaxWidth().testTag("column-${role.name}"))
    Text(words(vi,"Preview (original row numbers)","Xem trước (số hàng gốc)"))
    draft.rows.mapIndexed { i,row->i+1 to row }.drop((firstRow.toIntOrNull()?:1).coerceIn(1,MAX_ROWS)-1).take(5).forEach { (row,cells)->
        Text("$row: "+cells.joinToString(" | ") { it.take(100) + if(it.length>100) "…" else "" })
        OutlinedTextField(manualLabels[row].orEmpty(),{if(it.length<=MAX_CELL)manualLabels=manualLabels+(row to it)},label={Text(words(vi,"Optional custom label","Tên tùy chỉnh (tùy chọn)"))},modifier=Modifier.fillMaxWidth())
    }
    Button(onClick={
        val chosen=columns.filterValues { it.isNotBlank() }.mapValues { (_,value)->(value.toIntOrNull()?:0)-1 }
        sources.confirm(chosen,firstRow.toIntOrNull()?:0,manualLabels.filterValues { it.isNotBlank() })
    },enabled=!sources.busy,modifier=Modifier.testTag("confirm-columns")){Text(words(vi,"Confirm mapping and save cache","Xác nhận cột và lưu cache"))}
}
