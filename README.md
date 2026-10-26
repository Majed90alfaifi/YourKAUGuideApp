# Your KAU Guide 🤖

**An Android chatbot that answers King Abdulaziz University students' questions, in Arabic, about the Faculty of Computing and Information Technology (FCIT) and other university services.**

Students can type a question in Arabic or tap quick-reply buttons to browse common topics such as study plans, tracks, exams and faculty contacts. Questions are answered by a custom-trained Arabic NLP model served from a Python backend: **[KAUGuideModel](https://github.com/Majed90alfaifi/KAUGuideModel)**.

## 🎓 Graduation Project

This project was developed as a graduation project at King Abdulaziz University.

## 🚀 Features

- **Arabic chat:** ask questions in natural Arabic and get instant answers.
- **Quick-reply buttons:** browse common topics without typing:
  - Exam schedules and classroom locations
  - Changing majors and transferring to FCIT
  - Course equivalency and graduation requirements
  - Study plans for IT, Information Systems and Computer Science
  - Tracks and courses
  - Faculty email addresses
- **Feedback page:** students can send suggestions, which are saved to Firebase Realtime Database.
- **Faculty social link:** opens FCIT's Twitter account in the app, or in the browser if the app isn't installed.
- **Friendly error handling:** shows a network-error message in the chat when the server can't be reached.

## 🏗️ Architecture

```
┌──────────────────────┐   POST /getresponse   ┌─────────────────────────────┐
│  Android app (Java)  │ ────────────────────► │  KAUGuideModel (Flask API)  │
│                      │ ◄──────────────────── │  NLTK + ISRI stemmer        │
│  Chat · Buttons      │     reply (text)      │  PyTorch intent classifier  │
│  Feedback            │                       └─────────────────────────────┘
└──────────┬───────────┘
           │ feedback
           ▼
  Firebase Realtime Database
```

1. The student sends a message from the chat screen.
2. The app posts it as form data (`msg`) to the KAUGuideModel API.
3. The backend tokenizes and stems the Arabic text, classifies its intent with a PyTorch model, and returns a matching answer.
4. The reply appears in the chat.

See the [KAUGuideModel README](https://github.com/Majed90alfaifi/KAUGuideModel#readme) for details of the model and the API.

## 🛠️ Tech Stack

**Android app**
- Java, Android SDK (min SDK 26, target SDK 33)
- RecyclerView chat UI, View Binding
- android-async-http for API requests
- Firebase Realtime Database (feedback)
- Material Components, sdp/ssp for responsive sizing

**Backend ([KAUGuideModel](https://github.com/Majed90alfaifi/KAUGuideModel))**
- Python, Flask
- PyTorch, NLTK, NumPy
- ISRI Arabic stemmer

## 📱 Screens

| Screen | Purpose |
|---|---|
| Splash | Animated loading screen shown at launch |
| Chat | Main conversation with the bot and quick-reply buttons |
| Feedback | Send suggestions and open FCIT's Twitter account |

## ⚙️ Getting Started

### Prerequisites

- Android Studio
- A device or emulator running Android 8.0 (API 26) or later
- A Firebase project, for the feedback feature

### 1. Clone the repository

```bash
git clone https://github.com/Majed90alfaifi/YourKAUGuideApp.git
```

### 2. Configure Firebase

Replace `app/google-services.json` with the config file from your own Firebase project, and enable **Realtime Database**.

### 3. Point the app at the backend

The API URL is set in `MainActivity.java`:

```java
private final String URL = "https://yourkauguide.pythonanywhere.com/getresponse";
```

To use a local server, run [KAUGuideModel](https://github.com/Majed90alfaifi/KAUGuideModel) on your computer and change the URL to `http://<your-computer-ip>:5000/getresponse`.

### 4. Build and run

Open the project in Android Studio, let Gradle sync, then run the `app` configuration.

## 📂 Project Structure

```
app/src/main/java/com/kau/yourkauguideapp/
├── activities/
│   ├── SplashActivity.java          # Launch screen
│   ├── MainActivity.java            # Chat screen and API calls
│   └── FeedbackActivity.java        # Feedback form (Firebase) and Twitter link
├── MessageRVAdapter.java            # Chat bubbles and quick-reply buttons
├── ChatsModel.java                  # Chat message model
├── ButtonOptionsHandler.java        # Main quick-reply menu
├── PlansButtonOptionsHandler.java   # Study plan options
├── TraksButtonOptionsHandler.java   # Track options
└── EmailsButtonOptionsHandler.java  # Faculty email options
```

## 💡 Project Goal

The goal of Your KAU Guide is to make university information easier and faster for students to access through a conversational AI interface.

## 🔗 Related

- **[KAUGuideModel](https://github.com/Majed90alfaifi/KAUGuideModel)**: the Arabic NLP model and Flask API behind this app.
