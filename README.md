# 🛰️ Canopy Sentinel 2.0: Satellite Deforestation Monitoring & Carbon Bank

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API_24+-green.svg)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-Apache_2.0-orange.svg)](https://opensource.org/licenses/Apache-2.0)

**Canopy Sentinel** is a specialized Android monitoring platform designed to combat global deforestation using real-time satellite telemetry. By integrating a **Live Carbon Bank**, the application transforms environmental conservation into a manageable nature-backed asset, allowing users to track both canopy health and its corresponding market value.

---

## 🚀 Key Features

* **🌍 Live Satellite Mapping:** High-resolution visualization of global forest regions (Amazon, Congo, Borneo) with color-coded deforestation "Analysis Zones".
* **💰 Sentinel Carbon Bank:** Real-time financial valuation of sequestered carbon based on canopy density and current carbon credit market rates (~$40/MT).
* **🕒 Historical Archive:** Interactive time-travel slider to compare current forest health against 12 months of archived satellite data.
* **⚡ Real-Time Alerts:** Live push-feed of deforestation detections with "D" (Deforestation) badges and severity categorization.
* **📑 Professional Reporting:** On-demand generation of PDF environmental reports including health trend charts and MT CO2e summaries.

---

## 🛠️ Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/) for modern, safe, and concise code.
* **Architecture:** MVVM (Model-View-ViewModel) for clean separation of UI and business logic.
* **Mapping:** [osmdroid](https://github.com/osmdroid/osmdroid) for high-performance satellite tile rendering.
* **Analytics:** [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for dynamic health and area-loss visualizations.
* **Persistence:** Android DataStore for secure session and preference management.

---

## 📦 Installation & Setup

1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/parvkajla/Canopy-Sentinel.git](https://github.com/parvkajla/Canopy-Sentinel.git)
    ```

2.  **Open in Android Studio**
    * Import the project into **Android Studio (Hedgehog or newer)**.
    * Allow Gradle to sync and download the necessary dependencies.

3.  **API Configuration**
    * The project uses `MockSatelliteDataProvider` for testing.
    * To use live data, replace the logic in `ForestDataRepository.kt` with your **Sentinel Hub API** credentials.

4.  **Run the App**
    * Connect an Android device (API 24+) or launch an emulator.
    * Press **Run** (`Shift + F10`).

---

## 📂 Project Structure

```text
app/src/main/java/com/erc/canopysentinalg/
├── data/
│   ├── model/         # Data models (DeforestationAlert, ForestStats, User)
│   └── repository/    # Local and Remote data handling
├── ui/
│   ├── adapter/       # RecyclerView adapters (AlertsAdapter)
│   ├── map/           # Satellite Map logic and time-travel slider
│   ├── analytics/     # Charting and PDF report generation
│   └── viewmodel/     # Business logic controllers (ForestViewModel)
└── MainActivity.kt    # Main navigation host

---
---

## 👥 Meet the Team

| Profile | Name | Role | GitHub |
| :---: | :--- | :--- | :---: |
| <img src="https://github.com/parvkajla.png" width="50px;"/> | **Parv Kajla** | Analytics & Map Integration | [@parvkajla](https://github.com/parvkajla) |
| <img src="https://github.com/parirastogi.png" width="50px;"/> | **Pari Rastogi** | UI/UX Designer | [@parirastogi](https://github.com/parirastogi) |
| <img src="https://github.com/VayuTripathi09.png" width="50px;"/> | **Vayu Nandan Tripathi** | Lead Android Developer | [@VayuTripathi09](https://github.com/VayuTripathi09) |

---

