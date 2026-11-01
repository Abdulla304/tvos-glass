package com.tvos.glass.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun loadInstalledApps(favorites: Set<String>): List<AppModel> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val appsMap = mutableMapOf<String, AppModel>()

        val tvIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        val tvResolveInfos = pm.queryIntentActivities(tvIntent, 0)
        for (resolveInfo in tvResolveInfos) {
            val app = createAppModel(pm, resolveInfo, favorites)
            if (app.packageName != context.packageName) {
                appsMap[app.packageName] = app
            }
        }

        val standardIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val standardResolveInfos = pm.queryIntentActivities(standardIntent, 0)
        for (resolveInfo in standardResolveInfos) {
            val pkg = resolveInfo.activityInfo.packageName
            if (!appsMap.containsKey(pkg) && pkg != context.packageName) {
                appsMap[pkg] = createAppModel(pm, resolveInfo, favorites)
            }
        }

        appsMap.values.sortedBy { it.label.lowercase() }
    }

    private fun createAppModel(
        pm: PackageManager,
        resolveInfo: ResolveInfo,
        favorites: Set<String>
    ): AppModel {
        val pkg = resolveInfo.activityInfo.packageName
        val label = resolveInfo.loadLabel(pm).toString()
        val icon = resolveInfo.loadIcon(pm)
        return AppModel(
            packageName = pkg,
            activityName = resolveInfo.activityInfo.name,
            label = label,
            icon = icon,
            isFavorite = favorites.contains(pkg)
        )
    }

    fun launchApp(app: AppModel): Boolean {
        return try {
            val intent = context.packageManager.getLeanbackLaunchIntentForPackage(app.packageName)
                ?: context.packageManager.getLaunchIntentForPackage(app.packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                context.startActivity(it)
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
