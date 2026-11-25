# Device Manager Toolkit

Galaxy Tab S10 FE+ (SM-X620), Android 16을 대상으로 한 읽기 전용 진단 앱 프로젝트입니다.

## 포함 기능
- 현재 Android가 노출하는 활성 기기 관리자 목록 조회
- 앱 자체의 Device Owner / Profile Owner 여부 확인
- 이름에 `webkeeper`, `keeper`, `mdm`, `sejong`, `device` 및 `manage`가 포함된 설치 패키지 검색
- Android 설정 화면 바로가기
- 텍스트 진단 보고서 공유

## 제한
- 일반 앱은 다른 패키지의 Device Owner / Profile Owner 여부나 조직 MDM 서버의 등록 상태를 완전히 조회할 수 없습니다.
- 이 앱은 다른 앱의 관리자 권한을 해제하거나 MDM 정책을 우회하지 않습니다.
- 중고 기기에 이전 조직의 관리 등록이 남아 있다면 이전 관리기관 또는 판매자가 서버 측 등록 해제를 해야 할 수 있습니다.

## APK 빌드
1. Android Studio에서 이 폴더를 엽니다.
2. Android SDK Platform 36 및 JDK 17을 설치합니다.
3. Gradle 동기화를 완료합니다.
4. Build > Build Bundle(s) / APK(s) > Build APK(s)를 선택합니다.
5. APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.
