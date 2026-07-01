# AuthApp — Register / Login / Dashboard (Firebase Auth)

A minimal Android app (Kotlin + Jetpack Compose) with:
- **Register** screen (name, email, password) — creates a Firebase user
- **Login** screen (email, password) — signs in with Firebase
- **Dashboard** screen — shows the logged-in user's name/email, with a **Logout** button
- Auth state persists automatically (Firebase keeps the session), so a returning user who's already logged in goes straight to the Dashboard.

## ⚠️ Required setup before this will run

The app currently ships with a **placeholder** `app/google-services.json` — it will fail to build/run until you replace it with the real config for your `gravuregrid` Firebase project.

### 1. Register the Android app in Firebase
1. Go to the [Firebase Console](https://console.firebase.google.com/) → open the **gravuregrid** project.
2. Click **Add app → Android**.
3. Enter package name: `com.example.gravuregrid`
   (If you want a different package name, change it in `app/build.gradle.kts` under `namespace`/`applicationId` **and** in `AndroidManifest.xml`'s package references, then re-register with that name.)
4. Download the generated **`google-services.json`**.
5. Replace `app/google-services.json` in this project with the downloaded file.

### 2. Enable Email/Password sign-in
In the Firebase Console: **Authentication → Sign-in method → Email/Password → Enable → Save**.
(If this isn't enabled, register/login calls will fail with a "sign-in method disabled" error.)

### 3. Open and run
1. Open the `AuthApp/` folder in **Android Studio** (Koala/Ladybug or newer recommended).
2. Let Gradle sync (it will download the Firebase BoM, Auth SDK, Compose, and Navigation libraries).
3. Run on an emulator or device with Google Play services (required for Firebase).

## Project structure
```
app/src/main/java/com/example/authapp/
├── MainActivity.kt          # Nav graph: login -> register -> dashboard
├── AuthViewModel.kt         # Exposes login/register/logout to Compose screens
├── AuthRepository.kt        # Wraps FirebaseAuth (createUser/signIn/signOut)
└── ui/
    ├── screens/
    │   ├── LoginScreen.kt
    │   ├── RegisterScreen.kt
    │   └── DashboardScreen.kt
    └── theme/Theme.kt
```

## How auth works
- `AuthRepository` calls `FirebaseAuth.createUserWithEmailAndPassword(...)` on register and `signInWithEmailAndPassword(...)` on login, then stores the user's display name via `updateProfile`.
- `AuthViewModel.isLoggedIn()` checks `FirebaseAuth.getInstance().currentUser` at app start, so a session persists across app restarts until Logout is tapped.
- The Dashboard's Logout button (both the app-bar icon and the button at the bottom) calls `FirebaseAuth.signOut()` and returns to the Login screen.

## Building & testing without Android Studio (cloud/online options)

You don't need Android Studio installed to get a working APK. This project includes a **GitHub Actions** workflow (`.github/workflows/build-apk.yml`) that builds the app in the cloud.

### Steps
1. **Create a free GitHub account** (if you don't have one) at github.com.
2. **Create a new repository** and push this project's contents to it (GitHub's web UI lets you drag-and-drop upload the unzipped folder if you don't want to use git commands — use "Add file → Upload files").
3. Before pushing, replace `app/google-services.json` with the **real** file from your Firebase console (see setup steps above) — otherwise register/login won't actually connect to Firebase, though the app will still build.
4. Once pushed, go to the **Actions** tab in your GitHub repo. The "Build Debug APK" workflow runs automatically (or click **Run workflow** to trigger it manually).
5. When it finishes (a few minutes), open the completed run and download the **app-debug-apk** artifact — this is a zip containing your installable `app-debug.apk`.

### Testing the APK
- **Easiest: install on your own Android phone.** Download the APK to your phone (e.g. via email, Google Drive, or the GitHub Actions page on mobile), tap it, and allow "install unknown apps" when prompted. This gives you the real, full experience.
- **No phone? Use a browser-based emulator:** Upload the `.apk` to [appetize.io](https://appetize.io) (free tier available) — it runs a real Android emulator in your browser so you can tap through Register → Login → Dashboard → Logout without installing anything locally.

### Alternative: an online IDE with a terminal
If you'd rather edit code in the cloud too (not just build), open this project in **Gitpod** (gitpod.io) or **GitHub Codespaces** — both give you a full Linux terminal in the browser where you can run `gradle assembleDebug` yourself, same as the Actions workflow does automatically.

## Customizing

- **Password rules / validation**: edit the checks in `AuthRepository.register`.
- **Extra profile fields** (e.g. phone number, avatar): store them in **Cloud Firestore** keyed by `FirebaseAuth.currentUser.uid` — ask if you'd like this wired in.
- **Forgot password**: Firebase supports `sendPasswordResetEmail(email)` — easy to add as a follow-up.
- **Google/Phone sign-in**: can be added alongside email/password using the same `AuthRepository` pattern.
