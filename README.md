# KickScore Live - Football Live Scores

A modern Android application for live football scores, match details, league standings, and real-time updates.

## Features

- 📱 **Live Scores**: Real-time match scores and updates
- ⚽ **Match Details**: Comprehensive match information including lineups, events, and statistics
- 🏆 **League Standings**: Complete league tables and tournament information
- 📊 **Team & Player Stats**: Detailed performance analytics
- 🔍 **Search**: Find teams, leagues, and matches easily
- ⭐ **Favorites**: Star teams and matches for quick access
- 🔔 **Push Notifications**: Get alerts for goals, match start, and final results
- 🎨 **Modern UI**: Dark-first design with Material Design 3

## Architecture

Built with modern Android development best practices:

- **Architecture**: MVI (Model-View-Intent) with unidirectional data flow
- **UI**: Jetpack Compose for declarative UI
- **Networking**: Retrofit 2 + OkHttp3 with coroutines
- **Database**: Room with Paging 3 for efficient data loading
- **Reactive Programming**: Coroutines and Flow throughout
- **Dependency Injection**: Hilt for clean dependency management
- **State Management**: StateFlow with sealed classes for robust state handling

## Tech Stack

- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose
- **Architecture Components**: ViewModel, Room, Paging 3
- **Networking**: Retrofit 2, OkHttp3, WebSocket
- **Async**: Coroutines, Flow
- **DI**: Hilt
- **Testing**: JUnit 5, MockK, Compose UI Testing
- **Build**: Gradle with Kotlin DSL

## Package Structure

```
com.kickscore.live/
├── data/           # Data layer (DTOs, API, Database)
├── domain/         # Business logic (Entities, Use Cases)
├── ui/             # Presentation layer (Compose, ViewModels)
├── di/             # Dependency injection modules
└── utils/          # Utilities and extensions
```

## Design System

- **Primary Color**: #12A150 (Kick Green)
- **Accent Color**: #2E7BF6 (Electric Blue)
- **Typography**: Inter font family
- **Dark Theme**: Primary with optional light theme
- **Components**: Reusable Compose components with consistent styling

## Development

### Prerequisites
- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- Android SDK 34+

### Build & Run
```bash
# Clone the repository
git clone <repository-url>

# Open in Android Studio
# Sync project with Gradle files
# Run the app on device/emulator
```

### Testing
```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests
./gradlew connectedAndroidTest

# UI tests
./gradlew connectedDebugAndroidTest
```

## Credits

Portions of requirements derived from reference project by friend (used with permission dated 2025-01-13). No code or assets were copied. Implementation © 2025 Kev. See NOTICE.md for details.

## License

This project is proprietary software. All rights reserved.

---
🤖 Generated with [Claude Code](https://claude.ai/code)