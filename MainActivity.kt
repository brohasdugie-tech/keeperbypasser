package com.example.devicemanager

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var report: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 36, 36, 36)
        }

        val title = TextView(this).apply {
            text = "Device Manager Toolkit"
            textSize = 24f
            gravity = Gravity.CENTER_VERTICAL
        }
        content.addView(title)

        val intro = TextView(this).apply {
            text = "Galaxy Tab S10 FE+ (SM-X620) 진단 도구\n읽기 전용 진단이며 관리 정책을 변경하거나 우회하지 않습니다."
            textSize = 15f
            setPadding(0, 18, 0, 18)
        }
        content.addView(intro)

        fun addButton(label: String, action: () -> Unit) {
            content.addView(Button(this).apply {
                text = label
                setOnClickListener { action() }
            })
        }

        addButton("기기 관리자 / Device Owner 진단") { runDiagnostics() }
        addButton("기기 관리자 설정 열기") {
            openSettings("android.settings.DEVICE_ADMIN_SETTINGS")
        }
        addButton("보안 및 개인정보 보호 설정 열기") {
            openSettings(Settings.ACTION_SECURITY_SETTINGS)
        }
        addButton("개발자 옵션 열기") {
            openSettings(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
        }
        addButton("진단 보고서 공유") { shareReport() }

        report = TextView(this).apply {
            textSize = 13f
            setPadding(0, 20, 0, 20)
            text = "진단을 실행해 주세요."
        }
        content.addView(report)

        val scroll = ScrollView(this)
        scroll.addView(content)
        setContentView(scroll)
        runDiagnostics()
    }

    private fun runDiagnostics() {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val active = try {
            dpm.activeAdmins?.map { it.flattenToShortString() } ?: emptyList()
        } catch (e: Exception) {
            listOf("조회 실패: ${e.javaClass.simpleName}")
        }

        val owner = try { dpm.isDeviceOwnerApp(packageName) } catch (_: Exception) { false }
        val profileOwner = try { dpm.isProfileOwnerApp(packageName) } catch (_: Exception) { false }

        val pm = packageManager
        val matches = try {
            @Suppress("DEPRECATION")
            pm.getInstalledPackages(0)
                .map { it.packageName }
                .filter {
                    val s = it.lowercase(Locale.ROOT)
                    s.contains("webkeeper") || s.contains("keeper") ||
                    s.contains("mdm") || s.contains("sejong") ||
                    s.contains("device") && s.contains("manage")
                }.distinct().sorted()
        } catch (e: Exception) {
            listOf("패키지 조회 제한 또는 실패: ${e.javaClass.simpleName}")
        }

        val text = buildString {
            appendLine("Device Manager Toolkit 진단 보고서")
            appendLine("생성 시각: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            appendLine("기기: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            appendLine("Android: ${android.os.Build.VERSION.RELEASE} (SDK ${android.os.Build.VERSION.SDK_INT})")
            appendLine("앱 자체가 Device Owner: $owner")
            appendLine("앱 자체가 Profile Owner: $profileOwner")
            appendLine()
            appendLine("현재 사용자에게 보이는 활성 기기 관리자:")
            if (active.isEmpty()) appendLine("- 표시된 항목 없음")
            else active.forEach { appendLine("- $it") }
            appendLine()
            appendLine("이름으로 검색된 관련 패키지(완전한 목록이 아닐 수 있음):")
            if (matches.isEmpty()) appendLine("- 검색 결과 없음")
            else matches.forEach { appendLine("- $it") }
            appendLine()
            appendLine("주의: Android 앱은 다른 앱의 Device Owner 여부나 조직 MDM 서버 등록 상태를 항상 읽을 수 없습니다.")
            appendLine("이 보고서는 삭제, 비활성화, 권한 변경을 수행하지 않았습니다.")
        }
        report.text = text
    }

    private fun openSettings(action: String) {
        try { startActivity(Intent(action)) }
        catch (_: Exception) {
            report.append("\n\n해당 설정 화면을 이 Android 버전에서 열 수 없습니다.")
        }
    }

    private fun shareReport() {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Device Manager Toolkit 진단 보고서")
            putExtra(Intent.EXTRA_TEXT, report.text.toString())
        }
        startActivity(Intent.createChooser(send, "진단 보고서 공유"))
    }
}
