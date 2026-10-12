# Smart Pantry Manager

## 1. Application Description

**Smart Pantry Manager** is a native Android application developed using Java to help users manage ingredients stored in their pantry and discover recipes based on the ingredients they have available.

The application allows users to record pantry ingredients, quantities, measurement units and expiry dates. Users can also browse recipes, view preparation instructions and check the ingredients required for each recipe.

The main purpose of the application is to improve pantry organisation, support meal planning, encourage the use of leftover ingredients and help reduce food waste.

## 2. Database Option and Justification

**Database selected: SQLite**

Smart Pantry Manager uses SQLite as its local database, accessed through Android's `SQLiteOpenHelper` class.

SQLite was selected for the following reasons:

- **Built into Android:** SQLite is supported by Android and does not require a separate database server.
- **Offline access:** Users can store and access pantry and recipe information without an internet connection.
- **Structured data storage:** SQLite supports tables for storing pantry items, recipes and recipe ingredients.
- **Lightweight:** It is suitable for a mobile application that manages a relatively small amount of data.
- **CRUD operations:** It supports creating, reading, updating and deleting pantry records.

SQLite is appropriate for this application because the core features can operate locally on the user's device. However, data is not automatically synchronised between different devices or stored on a remote server.

## 3. Main Features

### 3.1 Pantry Management
Users can add, view, edit and delete pantry ingredients. Each ingredient can include its name, quantity, measurement unit and expiry date.

### 3.2 Recipe Catalogue
Users can browse the available recipes and select a recipe to view more information.

### 3.3 Recipe Details
The application displays recipe preparation instructions and the ingredients and quantities required for each recipe.

### 3.4 Ingredient-Based Recipe Suggestions
The recipe-matching functionality compares ingredients available in the pantry with the requirements of each recipe. A recipe can be suggested when the required ingredients are available in sufficient quantities and compatible units, depending on the matching rules implemented in the application.

### 3.5 Expiry-Date Support
Expiry dates help users keep track of pantry ingredients and identify items that may be approaching or past their expiry dates.

### 3.6 Local Data Storage
The application uses SQLite to store pantry and recipe information locally on the Android device.

*Note: The features available during assessment depend on the version of the application successfully built and run.*

## 4. Technologies Used

- **Platform:** Android
- **Programming Language:** Java
- **User Interface:** Android XML layouts and Activities
- **Database:** SQLite
- **Database Helper:** `SQLiteOpenHelper`
- **List Display:** RecyclerView and adapters
- **Development Environment:** Android Studio

## 5. System Requirements

Before running the application, ensure that you have:

- Android Studio installed on your computer.
- Access to the Smart Pantry Manager GitHub repository.
- The Android SDK version required by the project.
- An Android emulator or a physical Android device.
- An internet connection for downloading the project and resolving Gradle dependencies when necessary.

## 6. Setup and Run Instructions

### Step 1: Access the GitHub Repository

Open the project repository:

[Smart Pantry Manager – GitHub](https://github.com/Nobuhle99/Smart-Pantry-Manager)

You can download the repository as a ZIP file using GitHub's **Code → Download ZIP** option, or clone it using Git.

```bash
git clone https://github.com/Nobuhle99/Smart-Pantry-Manager.git
```

If the repository is private, sign in with a GitHub account that has permission to access it.

### Step 2: Open the Project in Android Studio

1. Launch Android Studio.
2. Select **Open**.
3. Navigate to the downloaded or cloned project folder.
4. Select the project folder and open it.
5. Wait for Android Studio to load the project and complete Gradle synchronisation.
6. If Android Studio requests any required SDK components, install them.

### Step 3: Start an Android Emulator

1. In Android Studio, select **Tools → Device Manager**.
2. Start an existing Android Virtual Device or create a new one.
3. Wait for the emulator to finish starting and display the Android home screen.

Alternatively, connect a physical Android device with USB debugging enabled and allow the computer to access it.

### Step 4: Build the Application

1. In Android Studio, select **Build → Make Project**.
2. Wait for the build process to complete.
3. If the build fails, open the **Build** window and review the reported errors before proceeding.

### Step 5: Run the Application

1. Select the `app` run configuration in Android Studio.
2. Select the running emulator or connected Android device.
3. Click the green **Run** button.
4. Wait for Android Studio to install and launch the application.
5. Follow the welcome screen, if displayed, to enter Smart Pantry Manager.

### Step 6: Test the Main Features

To explore the application:

1. Open the pantry screen.
2. Add an ingredient and provide its quantity and measurement unit.
3. Enter an expiry date if the option is available.
4. View the saved ingredient in the pantry list.
5. Open the recipe catalogue.
6. Select a recipe to view its ingredients and preparation instructions.
7. Explore the suggested-recipes feature, if available in the current build.

For recipe-matching tests, use ingredient names, quantities and units that are compatible with the recipe requirements.

## 7. Troubleshooting

### Gradle Synchronisation or Build Errors
Check the Android Studio Build window for the specific error. Ensure the required Android SDK components are installed and allow Gradle synchronisation to finish.

### Emulator Does Not Start
Open **Tools → Device Manager**, start the emulator and wait for the Android home screen before running the application.

### Application Fails to Launch
Check the **Run** window and **Logcat** for the underlying error. A launch error does not necessarily mean that the project failed to compile.

### Recipes or Ingredients Are Missing
Check whether the recipe catalogue has been initialised and review any error messages. Avoid clearing application data if saved pantry records need to be preserved.

## 8. Data Storage Notes

- Pantry and recipe information is stored locally using SQLite.
- Data saved on one device is not automatically available on another device.
- Uninstalling the application or clearing its storage may remove locally saved pantry data.
- The GitHub repository stores the project source code, while the installed application's local database is stored separately on the device.

## 9. Project Information

**Application Name:** Smart Pantry Manager  
**Application Type:** Native Android Application  
**Repository:** [Nobuhle99/Smart-Pantry-Manager](https://github.com/Nobuhle99/Smart-Pantry-Manager)  
**Primary Purpose:** Pantry management and ingredient-based recipe suggestions to support meal planning and reduce food waste.


