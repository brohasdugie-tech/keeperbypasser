# Build the APK without Google Play or local Android SDK

This project includes a GitHub Actions workflow that builds the debug APK on GitHub's hosted Ubuntu runner.

1. Create a GitHub repository and upload the contents of this project (not the enclosing folder).
2. Open the repository's **Actions** tab.
3. Select **Build Device Manager Toolkit APK**.
4. Choose **Run workflow**.
5. When the run completes, open the run page and download the artifact named `DeviceManagerToolkit-debug-apk`.
6. Extract the artifact ZIP; it contains `app-debug.apk`.
7. Transfer the APK to the Galaxy Tab S10 FE+ and install it, if Android permits installation from that source.

The workflow installs JDK 17, Android SDK Platform 36, Build Tools 36.0.0, and Gradle 8.11.1 on the hosted runner. It does not require Google Play on the tablet.

This is a diagnostic app only. It does not remove or bypass another organization's Device Owner, MDM, or security policy.
