# Biriktir

**A simple savings tracker Android app** built with **Jetpack Compose**, **Clean Architecture**, **Hilt**, **Room**, **DataStore**, and **WorkManager**.

## 🚀 Features

* Create, view, and delete **saving goals** with target amounts
* Add and manage **entries** (amount + date + optional description) per goal
* **Progress screen** showing saved vs target amount
* **Profile screen** with editable name & salary date
* **Notification settings**: toggle monthly salary reminders
* **Monthly notifications** on salary day at 9:00 AM
* **Dark/Light themes** with runtime selection
* Clean, modern UI using Jetpack Compose

---

## 📐 Architecture

Clean Architecture layers:

* **Domain**: Entities, Repositories, UseCases
* **Data**: Room database, DAOs, DataStore, Repository implementations
* **Presentation**: Compose UI, ViewModels (HiltViewModel), state management

DI with Hilt, asynchronous work with WorkManager, persistence with Room & DataStore.

---

## 🏁 Getting Started

### Prerequisites

* Android Studio Bumblebee or newer
* Kotlin 1.7+
* Android SDK 21+

## 📦 Modules & Packages

```
app/
 ├── data/           # Room entities, DAOs, DataStore
 ├── domain/         # Models, repository interfaces, use-cases
 ├── presentation/   # Compose screens, components, ViewModels
 ├── worker/         # WorkManager workers for notifications
 └── di/             # Hilt modules
```

## 🎨 Theming

* Uses Jetpack Compose **Material3** `ColorScheme`
* User-selectable themes via `ThemeSelectionDialog`
* Persisted in DataStore and applied at runtime

---

## 🎨 Recording
https://github.com/user-attachments/assets/4502e2cc-78c8-4263-aeb0-7a48b9c9a478


## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
