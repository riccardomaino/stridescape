# Stridescape Project

This is a summary for the [Stridescape project relation](<./Relazione Descrizione Progetto - Bussolino e Maino.pdf>)

## Overview
Stridescape is a mobile application developed for tracking walking activities. The app allows users to monitor their steps, calories burned, distance traveled, and total activity time. The walked path is displayed on a map, and users can view weekly, monthly, and yearly statistics through various graphs. The app also maintains a history of walks with all relevant statistics and allows users to review their routes on a map.

## Features
- **Activity Tracking**: Track steps, calories, distance, and time.
- **Map Integration**: Display routes on a map in real-time.
- **Statistics**: View detailed statistics in weekly, monthly, and yearly formats.
- **History**: Access past walks and their respective data.
- **Notifications**: Show real-time data during walks in a notification.
- **Permissions Management**: Handle location, notifications, and physical activity data permissions.

## Technical Details
- **Architecture**: MVVM (Model-View-ViewModel) and Clean Architecture for modularity and maintainability.
- **Development Tools**: Developed using Android Studio with Kotlin.
- **Concurrency**: Utilizes Kotlin coroutines for asynchronous operations.
- **Reactive Programming**: Implements Flow for reactive data streams.
- **Local Storage**: Uses Room database for data persistence.
- **State Management**: Manages UI state with State and StateFlow.
- **Preferences DataStore**: Stores user preferences using Preferences DataStore.
- **Services**: Includes a foreground service for continuous background tracking.
- **Location Tracking**: Utilizes broadcast receivers and the fused location provider for location updates.
- **Compatibility**: Supports Android SDK 28 (Android 9.0 Pie) and above.
- **Testing**: Tested on various Android emulators and physical devices.

## Permissions
- **Location**: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_BACKGROUND_LOCATION` for real-time and background location tracking.
- **Activity Recognition**: `ACTIVITY_RECOGNITION` for step detection.
- **Foreground Service**: `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_LOCATION`, `FOREGROUND_SERVICE_HEALTH` for continuous background tracking.
- **Notifications**: `POST_NOTIFICATIONS` for Android 13 and above.

## Group
- **Group Name**: Gruppo Roero
- **Members**: Riccardo Maino, Matteo Bussolino

## Submission Dates
- **Proposal Submission**: June 17, 2024
- **Proposal Acceptance**: June 25, 2024

## Contact
- **Department**: Dipartimento di Informatica, C.so Svizzera 185
- **Riccardo Maino**: riccardo.maino@edu.unito.it
- **Matteo Bussolino**: matteo.bussolino@edu.unito.it
