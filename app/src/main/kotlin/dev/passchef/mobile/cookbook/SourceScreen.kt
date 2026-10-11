package dev.passchef.mobile.cookbook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable fun SourceScreen(model: CookbookController) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String,vn: String) = words(vi,en,vn)
    var id by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    val sources = model.sources
    fun changed() { model.edit(); model.authorization.invalidate(); sources.invalidate() }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(t("Connect uses Google Sheets permission. Unlock and Connect again after an external consent screen locks the Cookbook.","Kết nối cần quyền Google Sheets. Nếu màn hình cấp quyền khóa Cookbook, mở khóa rồi Kết nối lại."))
        Text(t("Local metadata stays private and backup-excluded; it is not encrypted. Limits: 20 sources, 1000 rows, 32 columns.","Thông tin Menu lưu riêng và không sao lưu; chưa mã hóa. Giới hạn: 20 nguồn, 1000 hàng, 32 cột."))
        OutlinedTextField(id,{if(it.length<=300){changed();id=it}},label={Text(t("Menu URL or ID","URL hoặc ID Menu"))},enabled=!sources.busy,modifier=Modifier.fillMaxWidth().testTag("source-id"))
        Button(onClick={model.connect(id)},enabled=!sources.busy&&id.isNotBlank()){Text(t("Connect / load tabs","Kết nối / tải tab"))}
        Text(cookbookMessage(sources.status,vi))
        sources.metadata?.let { document ->
            Text(document.properties.title)
            document.sheets.forEach { tab -> TextButton(onClick={sources.choose(tab.properties)},enabled=!sources.busy){Text(tab.properties.title)} }
        }
        Text(t("Offline: paste your own tab-separated table. This does not connect to Google.","Ngoại tuyến: dán bảng phân cách bằng tab. Thao tác này không kết nối Google."))
        OutlinedTextField(title,{if(it.length<=200){changed();title=it}},label={Text(t("Local tab label","Tên tab cục bộ"))},enabled=!sources.busy,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(table,{if(it.length<=120000){changed();table=it}},label={Text(t("Tab-separated rows","Các hàng phân cách bằng tab"))},enabled=!sources.busy,modifier=Modifier.fillMaxWidth().testTag("source-table"),minLines=3)
        Button(onClick={model.offlineSource(id,title,table)},enabled=!sources.busy&&id.isNotBlank()&&title.isNotBlank()){Text(t("Preview columns","Xem trước cột"))}
        sources.draft?.let { draft -> key(draft) { MappingScreen(sources,draft) } }
        for (saved in sources.snapshots) {
            Text(saved.mapping.origin.title + " · " + saved.entries.size + " · " + java.text.DateFormat.getDateTimeInstance().format(java.util.Date(saved.refreshedAt)))
            var confirmRemove by remember(saved.mapping.origin.key) { mutableStateOf(false) }
            TextButton(enabled=!sources.busy,onClick={confirmRemove=true}){Text(t("Clear this local source","Xóa nguồn cục bộ này"))}
            if(confirmRemove) AlertDialog(onDismissRequest={confirmRemove=false},title={Text(t("Clear local cache only?","Chỉ xóa cache cục bộ?"))},text={Text(t("Google rows and spice jars remain intact.","Hàng Google và lọ Spices được giữ nguyên."))},confirmButton={TextButton(enabled=!sources.busy,onClick={model.removeSource(saved.mapping.origin);confirmRemove=false}){Text(t("Clear","Xóa"))}},dismissButton={TextButton(onClick={confirmRemove=false}){Text(t("Cancel","Hủy"))}})
        }
    }
}
