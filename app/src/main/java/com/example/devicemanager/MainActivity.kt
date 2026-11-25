package com.example.devicemanager

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        layout.addView(TextView(this).apply {
            text = "Device Manager Toolkit\nSM-X620 · Android 16\n읽기 전용 기기 관리 진단"
            textSize = 22f
        })
        fun button(label: String, action: () -> Unit) {
            layout.addView(Button(this).apply { text = label; setOnClickListener { action() } })
        }
        button("기기 관리자 진단", ::diagnose)
        button("기기 관리자 설정") { open("android.settings.DEVICE_ADMIN_SETTINGS") }
        button("보안 설정") { open(Settings.ACTION_SECURITY_SETTINGS) }
        button("개발자 옵션") { open(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS) }
        button("진단 보고서 공유") {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, report.text.toString())
                putExtra(Intent.EXTRA_SUBJECT, "Device Manager Toolkit Report")
            }, "보고서 공유"))
        }
        report = TextView(this).apply { textSize = 14f; setPadding(0, 20, 0, 0) }
        layout.addView(report)
        setContentView(ScrollView(this).apply { addView(layout) })
        diagnose()
    }
    private fun open(action: String) {
        try { startActivity(Intent(action)) } catch (_: Exception) {
            report.append("\n설정 화면을 열 수 없습니다.")
        }
    }
    private fun diagnose() {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admins = try {
            @Suppress("DEPRECATION")
            dpm.activeAdmins?.map { it.flattenToShortString() } ?: emptyList()
        } catch (e: Exception) { listOf("조회 실패: ${e.javaClass.simpleName}") }
        report.text = buildString {
            appendLine("Device Manager Toolkit 진단 보고서")
            appendLine("시간: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            appendLine("기기: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            appendLine("Android: ${android.os.Build.VERSION.RELEASE} (SDK ${android.os.Build.VERSION.SDK_INT})")
            appendLine("\n활성 기기 관리자:")
            if (admins.isEmpty()) appendLine("- 표시된 항목 없음") else admins.forEach { appendLine("- $it") }
            appendLine("\n일반 앱은 다른 앱의 Device Owner 상태나 조직 MDM 서버 등록 여부를 완전히 조회할 수 없습니다.")
            appendLine("이 앱은 다른 앱의 권한 해제나 관리 정책 우회를 수행하지 않습니다.")
        }
    }
}
