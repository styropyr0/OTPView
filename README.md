# **OtpView Library**

**Latest Version**: 1.0.0

**OtpView** is a customizable OTP (One Time Password) input view for Android. It allows users to enter OTPs in a secure, visually appealing way with full flexibility in terms of appearance and behavior. You can easily customize the number of input squares, square colors, text size, and many other aspects to fit your app's design.

<div style="display: flex; justify-content: center; gap: 10px;">
    <img src="./images/scr1.png" alt="Screenshot 1" width="200"/>
    <img src="./images/scr2.png" alt="Screenshot 2" width="200"/>
    <img src="./images/scr3.png" alt="Screenshot 3" width="200"/>
</div>

---



## **Installation**

To use **OtpView** in your project, add the following to your `build.gradle`:

### Gradle (App Level)

```gradle
dependencies {
    implementation 'com.github.styropyr0:OTPView:1.0.0'
}
```
or for `app:build.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.styropyr0:OTPView:1.0.0")
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

### **Step 1: Add the OtpView to Your Layout**

To add the `OtpView` to your layout file, use the following XML code:

```xml
    <com.matrix.otpview.OtpView
        android:id="@+id/otp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="25dp"
        android:gravity="center"
        app:autoSize="false"
        app:borderColor="#F21767"
        app:borderWidth="2dp"
        app:cornerRadius="18dp"
        app:shape="rectangle"
        app:inputType="number"
        app:fontFamily="@font/dm_sans_medium"
        app:highlightColor="#5B04E0"
        app:hintTextColor="@color/white"
        app:hint="*"
        app:textStyle="bold"
        app:maxCountPerLine="6"
        app:textColor="#D10F2C"
        app:onCompleteBorderColor="#2EDB17"
        app:squareColor="#B1EF7AB6"
        app:squareCount="4"
        app:squareHeight="55dp"
        app:squareWidth="55dp" />
```

### **Step 2: Initialize and Customize in Your Activity/Fragment**

In your activity or fragment, you can access and customize the `OtpView` as follows:

```kotlin
val otpView = findViewById<OtpView>(R.id.otp)
otpView.apply {
    setSquareCount(6)
    setOnCompleteListener { otp ->
        if (!checkOTP(otp)) {
            otpView.onOtpError(true)
        }
    }
}
```

---

## **Public Methods in OtpView**

`OtpView` provides several public methods to customize the appearance and behavior of the OTP input view. Below are the descriptions and examples for each method.

---

### **`setSquareCount(int count)`**

**Description**:  
Sets the number of OTP input fields (squares).

**Parameters**:
- `count`: The number of squares (e.g., 4 for a 4-digit OTP).

**Usage**:
```kotlin
otpView.setSquareCount(6)
```

---

### **`setSquareColor(int color)`**

**Description**:  
Sets the color of the OTP input fields (squares).

**Parameters**:
- `color`: The color (e.g., `Color.RED`, `#FF5733`).

**Usage**:
```kotlin
otpView.setSquareColor(Color.parseColor("#871EA0FD"))
```

---

### **`setSquareSize(float size)`**

**Description**:  
Sets the size of the OTP input squares.

**Parameters**:
- `size`: The size in pixels (e.g., 50f for 50px).

**Usage**:
```kotlin
otpView.setSquareSize(50f)
```

---

### **`setCornerRadius(float radius)`**

**Description**:  
Sets the corner radius for each OTP square.

**Parameters**:
- `radius`: The corner radius in pixels (e.g., 8f for 8px).

**Usage**:
```kotlin
otpView.setCornerRadius(8f)
```

---

### **`setTextSize(float size)`**

**Description**:  
Sets the text size inside the OTP squares.

**Parameters**:
- `size`: The text size in pixels (e.g., 16f).

**Usage**:
```kotlin
otpView.setTextSize(16f)
```

---

### **`setTextColor(int color)`**

**Description**:  
Sets the color of the text inside the OTP squares.

**Parameters**:
- `color`: The color for the text (e.g., `Color.BLACK`, `#835EFF`).

**Usage**:
```kotlin
otpView.setTextColor(Color.parseColor("#835EFF"))
```

---

### **`setHighlightColor(int color)`**

**Description**:  
Sets the color that will highlight the OTP input field while typing.

**Parameters**:
- `color`: The color for highlighting the input field (e.g., `#2A5CC1`).

**Usage**:
```kotlin
otpView.setHighlightColor(Color.parseColor("#2A5CC1"))
```

---

### **`setHint(String hint)`**

**Description**:  
Sets a hint for the OTP input fields (usually displayed when empty).

**Parameters**:
- `hint`: The hint string (e.g., "*").

**Usage**:
```kotlin
otpView.setHint("*")
```

---

### **`setOnCompleteListener(OnOTPCompleteListener listener)`**

**Description**:  
Sets a listener to be called when the OTP is completed.

**Parameters**:
- `listener`: The listener to handle the OTP completion.

**Usage**:
```kotlin
otpView.setOnCompleteListener { otp ->
    if (!checkOTP(otp)) {
        otpView.onOtpError(true)
    }
}
```

---

### **`onOtpError(boolean hasError)`**

**Description**:  
Indicates if there is an error with the OTP input (e.g., incorrect OTP).

**Parameters**:
- `hasError`: `true` for error, `false` otherwise.

**Usage**:
```kotlin
otpView.onOtpError(true)
```

---

### **`setAutoProcess(boolean autoProcess)`**

**Description**:  
Enables or disables auto-processing of the OTP once the user completes the input.

**Parameters**:
- `autoProcess`: `true` to enable auto-processing, `false` to disable.

**Usage**:
```kotlin
otpView.setAutoProcess(true)
```

---

### **`setInputType(String inputType)`**

**Description**:  
Sets the input type (e.g., numeric or text) for the OTP field.

**Parameters**:
- `inputType`: "number" or "text".

**Usage**:
```kotlin
otpView.setInputType("number")
```

---

### **`setBorderColor(int color)`**

**Description**:  
Sets the color of the border around the OTP input field.

**Parameters**:
- `color`: The color for the border (e.g., `Color.BLUE`, `#779AEB`).

**Usage**:
```kotlin
otpView.setBorderColor(Color.parseColor("#779AEB"))
```

---

### **`setBorderWidth(float width)`**

**Description**:  
Sets the width of the border around the OTP input squares.

**Parameters**:
- `width`: The border width (e.g., `2f` for 2px).

**Usage**:
```kotlin
otpView.setBorderWidth(2f)
```

---

### **`setSquareHeight(float height)`**

**Description**:  
Sets the height of the OTP input squares.

**Parameters**:
- `height`: The height in pixels (e.g., `50f` for 50px).

**Usage**:
```kotlin
otpView.setSquareHeight(50f)
```

---

### **`setSquareWidth(float width)`**

**Description**:  
Sets the width of the OTP input squares.

**Parameters**:
- `width`: The width in pixels (e.g., `50f` for 50px).

**Usage**:
```kotlin
otpView.setSquareWidth(50f)
```

---

### **`setMaxCountPerLine(int count)`**

**Description**:  
Sets the maximum number of OTP input squares per line.

**Parameters**:
- `count`: The number of squares to display per line.

**Usage**:
```kotlin
otpView.setMaxCountPerLine(8)
```

---

### **`setAutoSize(boolean autoSize)`**

**Description**:  
Enables or disables automatic resizing of the OTP input squares.

**Parameters**:
- `autoSize`: `true` to enable, `false` to disable.

**Usage**:
```

kotlin
otpView.setAutoSize(true)
```

---

## **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
