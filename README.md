# 🕒 Timely SDK

A professional, lightweight Android Push Notification SDK with built-in notification stacking, automatic permission handling, and secure backend integration.

## 🚀 Features
- **Easy Integration**: Initialize with just one line of code.
- **Auto-Permissions**: Automatically handles `POST_NOTIFICATIONS` permission requests on Android 13+.
- **Notification Stacking**: Smart notification IDs ensure messages stack instead of overwriting each other.
- **Agnostic Architecture**: Works with any backend URL and App ID.
- **Secure**: Built-in support for FCM token reporting to your private registry.

---

## 📦 Installation

Add the JitPack repository to your `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Azizkhuja:timely:v1.0.5'
}
```

---

## 🛠️ Usage

### 1. Requirements
Ensure your app is connected to Firebase and has the `google-services.json` file in the `app/` directory.

### 2. Initialization
Initialize the SDK in your `MainActivity.kt` (or your application class):

```kotlin
import io.timely.sdk.Timely

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Timely
        Timely.initialize(
            activity = this,
            baseUrl = "https://your-backend.onrender.com",
            appId = "your.package.name"
        )
    }
}
```

### 3. Handling Notifications
The SDK automatically handles incoming messages. You just need to register the `TimelyMessagingService` in your `AndroidManifest.xml` (the SDK does this automatically via manifest merger, but ensure no conflicts exist).

---

## 🔐 Privacy & Security
- **Data Collection**: The SDK only reports the FCM token and the provided `appId` to your `baseUrl`.
- **No Background tracking**: The SDK is only active when a token is refreshed or a message is received.
- **Open Connection**: Ensure your `baseUrl` is using `https` for production.

---

## 📄 License
Licensed under the ISC License. (c) 2025 Azizkhuja.
