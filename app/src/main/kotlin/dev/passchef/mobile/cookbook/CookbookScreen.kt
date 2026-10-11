package dev.passchef.mobile.cookbook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable fun CookbookScreen(model: CookbookController, lock: () -> Unit) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    var page by remember { mutableStateOf("list") }
    var recipe by remember { mutableStateOf("") }
    var menu by remember { mutableStateOf<MenuOrigin?>(null) }
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") }
    var pageIndex by remember { mutableStateOf(0) }
    LaunchedEffect(query,filter) { pageIndex=0 }
    fun navigate(next: String) { model.back(); recipe = ""; menu = null; page = next }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(t("Cookbook", "Cookbook"), style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TextButton(onClick = { navigate("list") }) { Text(t("Menu", "Menu")) }
            TextButton(onClick = { navigate("manual") }) { Text(t("Cook", "Nấu")) }
            TextButton(onClick = { navigate("sources") }) { Text(t("Sources", "Nguồn")) }
            TextButton(onClick = lock) { Text(t("Lock", "Khóa")) }
        }
        if (model.selected != null) {
            val entry = model.selected!!
            Text(entry.label); Text(entry.username)
            Text(entry.origin.title + " · " + entry.row)
            Text(entry.recipe)
            if(entry.url.isNotEmpty()) Text(entry.url)
            if(entry.notes.isNotEmpty()) Text(entry.notes)
            if (dev.passchef.mobile.recipe.RecipeParser.parse(entry.recipe)?.tag != null) Text(t("Verification tag included (checked only when cooking)","Có tag xác minh (kiểm tra khi nấu)"))
            DishPanel(model)
            Button(onClick = model::copyUsername, enabled = entry.username.isNotEmpty()) { Text(t("Copy username", "Copy tên đăng nhập")) }
            TextButton(onClick = model::back) { Text(t("Back", "Quay lại")) }
        } else when (page) {
            "manual" -> {
                OutlinedTextField(recipe, { if (it.length <= 4096) { model.edit(); recipe = it } }, label = { Text(t("Paste Recipe", "Dán Recipe")) }, modifier = Modifier.fillMaxWidth().testTag("manual-recipe"))
                Text(t("Optional saved Menu context", "Menu đã lưu (tùy chọn)"))
                TextButton(onClick = { model.edit(); menu = null }) { Text(t("No Menu", "Không có Menu")) }
                model.sources.snapshots.forEach { saved -> TextButton(onClick = { model.edit(); menu = saved.mapping.origin }) { Text(saved.mapping.origin.title) } }
                Text(menu?.title ?: t("Legacy Recipe without a tag", "Recipe cũ không có tag"))
                Button(onClick = { model.cook(recipe, menu?.menuId) }, enabled = !model.cooking && recipe.isNotEmpty(), modifier = Modifier.testTag("manual-cook")) { Text(t("Cook Dish", "Nấu Dish")) }
                DishPanel(model)
            }
            "sources" -> SourceScreen(model)
            else -> {
                Text(cookbookMessage(model.sources.status, vi))
                OutlinedTextField(query, { if(it.length<=4096) query = it }, label = { Text(t("Search label / username", "Tìm tên / tên đăng nhập")) }, modifier = Modifier.fillMaxWidth().testTag("menu-search"))
                Row { for ((id,en,vn) in listOf(Triple("all","All","Tất cả"),Triple("favorites","Favorites","Yêu thích"),Triple("recent","Recent","Gần đây"))) TextButton(onClick = { filter = id }) { Text(t(en,vn)) } }
                val entries = model.sources.snapshots.flatMap { saved ->
                    when (filter) {
                        "favorites" -> saved.entries.filter { it.id in saved.favorites }
                        "recent" -> saved.recent.mapNotNull { id -> saved.entries.find { it.id == id } }
                        else -> saved.entries
                    }
                }
                val matches = searchEntries(entries, query)
                if (matches.isEmpty()) Text(t(if (entries.isEmpty()) "No saved entries. Cook manually or add a source." else "No search results.", if (entries.isEmpty()) "Chưa lưu mục nào. Nấu thủ công hoặc thêm nguồn." else "Không có kết quả."))
                val lastPage=(matches.size-1).coerceAtLeast(0)/100
                val shownPage=minOf(pageIndex,lastPage)
                Text(t("Entries: ","Số mục: ")+matches.size+" · "+(shownPage+1)+"/"+(lastPage+1))
                Row {
                    TextButton(onClick={pageIndex=(shownPage-1).coerceAtLeast(0)},enabled=shownPage>0){Text(t("Previous","Trước"))}
                    TextButton(onClick={pageIndex=shownPage+1},enabled=shownPage<lastPage){Text(t("Next","Sau"))}
                }
                matches.drop(shownPage*100).take(100).forEach { entry ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            TextButton(onClick = { model.open(entry) },enabled=!model.sources.busy) { Text(entry.label) }
                            Text(entry.username); Text(entry.origin.title + " · " + entry.row)
                            val starred = model.sources.snapshots.find { it.mapping.origin == entry.origin }?.favorites?.contains(entry.id) == true
                            TextButton(onClick = { model.sources.favorite(entry) },enabled=!model.sources.busy) { Text(t(if(starred)"Unfavorite" else "Favorite",if(starred)"Bỏ yêu thích" else "Yêu thích")) }
                        }
                    }
                }
            }
        }
    }
}

@Composable fun DishPanel(model: CookbookController) {
    val vi = LocalCookbookVietnamese.current
    if (model.cooking) Text(words(vi,"Cooking…","Đang nấu…"))
    if (model.code != null) Text(cookbookMessage(model.code,vi), modifier = Modifier.testTag("cook-error"))
    if (model.warning) Text(words(vi,"Legacy Recipe: no verification tag.","Recipe cũ: không có tag xác minh."))
    if (model.hasDish()) {
        Text(if(model.revealed) model.dish else "••••••••",modifier = Modifier.testTag("dish-display"))
        Button(onClick = model::reveal,modifier = Modifier.testTag("dish-reveal")) { Text(words(vi,"Reveal for 10 seconds","Hiện trong 10 giây")) }
        Button(onClick = model::copyDish,modifier = Modifier.testTag("dish-copy")) { Text(words(vi,"Copy Dish","Copy Dish")) }
    }
    model.clipboardStatus?.let { Text(cookbookMessage(it,vi)) }
}
