# ZCM Hymn Book

A native Android hymn book app for Zenith Christian Ministry — Kotlin, Jetpack
Compose, Material 3, Room, MVVM/repository architecture. Fully offline.

## What's in this project

- **Home** — search bar, Recently Viewed, Featured Hymns, All Hymns preview
- **Search** — real-time search by hymn number, title, lyrics, author, composer, category
- **Hymn Reader** — favorite, share, A-/A+ text size, Previous/Next navigation
- **Favorites** — persisted locally via Room
- **Categories** — Worship, Praise, Prayer, Communion, Evangelism, Thanksgiving,
  Holy Spirit, Christmas, Easter, Other
- **Settings** — theme (System/Light/Dark), text size, About/version
- 10 original, fictional sample hymns (not copyrighted content) so the app is
  fully testable out of the box
- Repository layer written so a future Supabase-backed admin system can be
  wired in later without touching the UI (see comments in `HymnRepository.kt`)

Application ID: `com.zcm.hymnbook`

## Getting your APK — no local Android Studio required

This repo includes a GitHub Actions workflow (`.github/workflows/build-apk.yml`)
that builds a debug APK in the cloud every time you push to `main`/`master`,
or whenever you trigger it manually.

**Steps:**

1. Create a new **public or private GitHub repository**.
2. Upload the entire contents of this project (everything in this zip) to
   that repository — either via `git push` or by dragging the files into
   the GitHub web UI.
3. Go to the repo's **Actions** tab. The "Build ZCM Hymn Book APK" workflow
   will run automatically. If it doesn't start automatically, click
   **Run workflow**.
4. Wait for the run to finish (a few minutes).
5. Open the completed run → scroll to **Artifacts** → download
   **ZCM-Hymn-Book-debug-apk**. That's a zip containing `app-debug.apk`.
6. Transfer `app-debug.apk` to your Honor X6c (email, cloud drive, or USB)
   and tap it to install. You'll need to allow "install unknown apps" for
   whichever app you use to open it (Settings → Apps → Special access).

No Play Store account, no Android Studio, no local SDK setup needed for this
path.

## Building locally instead (optional)

If you have Android Studio installed:

1. Open this folder as a project (`File → Open`).
2. Let Gradle sync (Android Studio will download the SDK/build tools it needs).
3. Click **Run**, or `Build → Build Bundle(s)/APK(s) → Build APK(s)`.
4. Find the APK at `app/build/outputs/apk/debug/app-debug.apk`.

## Project structure

```
app/src/main/java/com/zcm/hymnbook/
├── data/
│   ├── database/        HymnEntity, HymnDao, HymnDatabase, seed data
│   ├── model/            HymnCategory
│   ├── preferences/       DataStore-backed theme & text-size settings
│   └── repository/        HymnRepository (single source of truth)
├── ui/
│   ├── components/        Reusable composables (list item, search bar, etc.)
│   ├── navigation/         NavGraph + bottom navigation
│   ├── screens/            Home, Search results, Reader, Favorites,
│   │                       Categories, Category Detail, All Hymns, Settings
│   └── theme/               Color/Type/Theme (original burgundy & gold palette)
├── viewmodel/              HymnViewModel, SettingsViewModel
├── MainActivity.kt
└── ZcmHymnBookApp.kt        Application class, manual DI
```

## Known limitations (current version)

- Sample hymn content is placeholder/fictional text, not the ministry's
  actual hymn lyrics — replace via the database seed or (once built) an
  admin tool.
- No remote/admin backend yet — this version is fully local/offline by
  design. The repository layer is structured so Supabase (or any REST
  backend) can be added later; see comments in `HymnRepository.kt`.
- No automated tests included yet.
- App icon is an original vector design, not a professional graphic-design
  asset — swap `ic_launcher_foreground.xml` / `ic_launcher_background.xml`
  for a polished icon whenever you have one.
