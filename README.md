# 둠스데이 트래커 안드로이드 앱

이 프로젝트는 **Pydroid 3이 필요 없는 네이티브 Android APK 프로젝트**입니다. Google 뉴스 한국어 RSS에서 검색 결과를 받아옵니다. 뉴스 검색은 인터넷 연결이 필요합니다.

## 갤럭시폰에서 APK 만들기 (GitHub 이용)
1. GitHub 계정을 만들고 새 **공개 또는 비공개 저장소**를 생성합니다.
2. ZIP 압축을 풀어 나온 **프로젝트 안의 파일과 폴더**를 GitHub 저장소 최상위에 업로드합니다. `.github/workflows/build-apk.yml`도 반드시 업로드해야 합니다. (GitHub 모바일 웹에서 숨김 폴더 업로드가 어려울 수 있어 PC 사용을 권장합니다.)
3. 저장소의 **Actions → Build Android APK → Run workflow**를 누릅니다. `main` 브랜치가 있어야 합니다. 첫 업로드 시 자동 실행될 수도 있습니다.
4. 빌드가 성공하면 해당 실행의 **Artifacts → DoomsdayTracker-APK**를 내려받고 압축을 풉니다.
5. 갤럭시에서 `app-debug.apk`를 열고, Android에서 요청할 경우 해당 파일 관리 앱의 **이 출처 허용**을 켠 후 설치합니다. 본인이 만든 APK인지 확인한 경우에만 설치하세요.

## Android Studio (PC가 있는 경우)
Android Studio에서 프로젝트 폴더 열기 → Gradle 동기화 → Build → Build APK(s) → `app/build/outputs/apk/debug/app-debug.apk`.

## 주의
- 이 ZIP 자체는 APK가 아니라 APK를 **만드는 소스 프로젝트**입니다.
- 현재 환경에는 Android SDK/Gradle이 없어 실제 APK 빌드 검증은 못 했습니다.
- Google 뉴스 RSS가 차단되거나 형식이 변경되면 뉴스 검색이 실패할 수 있습니다.
- 기사 본문 가져오기, 영어 기사 자동 번역, AI 요약, 앱 종료 후 푸시 알림은 **미구현**입니다. 공식/루머 자동 판별도 미구현입니다.
- 무비띵크 버튼은 해당 키워드의 YouTube 검색으로 연결합니다.
- 앱은 휴대폰에서 직접 Google 뉴스 RSS를 요청하므로 별도 Python 서버는 필요하지 않습니다.
