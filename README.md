# **OTP View Library**

**Latest version: 1.0.0**

**OTP View** is a customizable, flexible library for implementing an OTP (One-Time Password) input field in Android applications. It allows developers to quickly integrate an OTP input view that is both functional and visually appealing. The library supports various customization options, such as border colors, text sizes, corner radius, and more, to fit different design needs.

---

## **Why Use OTP View?**

### **Key Features**:

1. **Customizable Appearance**:  
   The OTP input view provides numerous styling options, such as square color, corner radius, text size, text color, and border style, allowing for a highly customizable UI that can match your app's theme.

2. **Automatic OTP Processing**:  
   The view includes an option to automatically process the OTP when the user completes entering the characters, reducing the need for additional logic to handle OTP submission.

3. **Error Handling**:  
   It provides the ability to customize border colors on successful OTP entry or in case of an error (invalid OTP), helping to visually communicate the status of the OTP input.

4. **Flexible Layout**:  
   The OTP input supports flexible square count and layout customization, including the option to auto-size the squares and define the maximum number of squares per line.

5. **Multi-Type Input**:  
   The view supports different input types, such as numbers or password input, ensuring the OTP input is secure and appropriate for your app’s requirements.

6. **Text Styling Support**:  
   The library includes options to specify custom font families and text styles (e.g., bold, italic) for better typography control.

7. **Easy Integration**:  
   The OTP input view can be easily integrated into any layout and requires minimal setup, with all properties customizable directly from XML.

---

## **Installation**

Add the following to your `build.gradle` (app-level):

### Gradle (App Level)

```gradle
dependencies {
    implementation 'com.github.styropyr0:OTPView:v1.0.0'
}
```

or for `app:build.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.styropyr0:OTPView:v1.0.0")
}
```

### Project-level `settings.gradle`

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
or for `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

---

## **Getting Started**

### **Step 1: Add the OTP View to Your Layout**

Add the `OtpView` widget to your XML layout and customize its attributes. Here's an example (Not all attributes are required):

```xml
<com.matrix.otpview.OtpView
    android:id="@+id/otp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="25dp"
    android:gravity="center"
    app:autoSize="true"
    app:borderColor="#779AEB"
    app:borderWidth="2dp"
    app:cornerRadius="8dp"
    app:inputType="number"
    app:fontFamily="@font/dm_sans_medium"
    app:highlightColor="#2A5CC1"
    app:hint="*"
    app:textStyle="bold"
    app:textColor="#835EFF"
    app:maxCountPerLine="8"
    app:onCompleteBorderColor="#2EDB17"
    app:squareColor="#871EA0FD"
    app:squareCount="6"
    app:squareHeight="50dp"
    app:squareWidth="50dp"
    app:margins="8dp"
    app:autoProcess="true"
    app:shape="square" />
```

### **Step 2: Handle OTP Completion in Your Activity**

In your activity, listen for OTP completion and handle errors as needed.

```kotlin
val otpView = findViewById<OtpView>(R.id.otp)
otpView.apply {
    setSquareCount(6)
    setOnCompleteListener { otp ->
        if (!checkOTP(otp)) {
            onOtpError(true)
        }
    }
}

fun checkOTP(otp: String): Boolean {
    return otp == "123456"
}
```

---

## **Customization Attributes**

Here is a full list of customization attributes you can use with the `OtpView`:

- **squareColor**: The background color of each OTP square.
- **squareSize**: The size of each square.
- **cornerRadius**: The corner radius of each square for rounded edges.
- **squareCount**: The number of OTP digits (squares) to display.
- **textSize**: The size of the text inside each square.
- **autoProcess**: Automatically processes the OTP once the required number of digits is entered.
- **borderWidth**: The width of the border around each square.
- **borderColor**: The color of the border around each square.
- **textColor**: The color of the text inside the squares.
- **hint**: A placeholder or hint that appears in the square before the user enters the OTP.
- **squareHeight**: The height of each square.
- **squareWidth**: The width of each square.
- **margins**: The margin between each OTP square.
- **autoSize**: Automatically adjusts the size of the OTP squares to fit the available space.
- **maxCountPerLine**: The maximum number of OTP squares per line.
- **highlightColor**: The color of a square when it is focused or active.
- **shape**: The shape of the OTP squares. Options: `"square"` (default) or `"circle"`.
- **inputType**: Defines the input type for the OTP (e.g., `"number"` for numeric input).
- **onCompleteBorderColor**: The border color when the OTP is successfully completed.
- **onOTPErrorBorderColor**: The border color when the OTP is invalid.
- **fontFamily**: The font family used for the text in the OTP squares.
- **textStyle**: The text style (e.g., `"bold"`, `"italic"`, or `"normal"`).

---

## **Implementation Details**

### **OTP View**

`OtpView` is a custom view designed to handle OTP input in Android. The view consists of a series of squares, each representing a digit of the OTP. The view supports various customization options, such as square size, color, text size, border width, and more. It also features:

- **Auto-Processing**: When the user finishes entering all OTP digits, the view can automatically trigger the completion event.
- **Error Handling**: The view allows you to define custom behavior for when the OTP is completed or invalid, such as changing the border color.
- **Flexible Layout**: You can define the number of squares (digits), square size, and text style directly from XML.

---

## **Advantages of Using OTP View**

- **Highly Customizable**: The library offers a wide range of options to control the appearance and behavior of the OTP input.
- **Simple Integration**: Add the view to your XML layout and handle OTP logic in just a few lines of code.
- **Error Handling**: It provides built-in error handling with customizable visual cues to guide the user.
- **Modern UI Elements**: Leverages modern design principles to provide an intuitive and polished OTP input experience.
- **Automatic Processing**: The OTP view can automatically process the OTP when all digits are entered, simplifying user interaction.

---

## **Contribution**

Contributions are welcome! If you encounter any bugs or have feature requests, feel free to open issues or submit pull requests.

---

## **License**

This project is licensed under the **MIT License**. See the `LICENSE` file for details.

---

## **Notes**

- Always ensure that the correct OTP validation logic is in place to secure the application.
- The view works best when the `squareCount` attribute is set according to the expected OTP length.

---
