# Hadi Dental Clinic & Prosthesis — Android app

This is an Android Studio project built with Kotlin and Jetpack Compose. It uses the blue/white branding and clinic details from the supplied poster.

## Included
- Home, Services, Booking, and Contact screens
- Services: General Dentistry; Orthodontic Treatment; Dental Implants; Prosthesis (Artificial Teeth); Teeth Cleaning & Whitening; Emergency Dental Care
- Clinic phone: 70006637905
- Address: Ali Larijani Chowk, Sonapah, Beerwah – 193411
- Owned & managed by: Barket Ali Malik (the dental hygienist)
- Appointment request form with patient name, phone, service, preferred date/time, and notes
- Firestore submission integration for online appointment requests

## Important: enable live online bookings
The app source is ready, but online requests will not reach the clinic until you connect a Firebase project:

1. Create a Firebase project at https://console.firebase.google.com/.
2. Add an Android app with package name `com.hadidental.clinic`.
3. Download `google-services.json` and put it in the `app/` folder (same level as `app/build.gradle.kts`).
4. In Firebase, enable **Cloud Firestore**.
5. Set up Firestore security rules before launch. Do not leave the database in test mode. Use a secure, validated write-only booking endpoint or appropriately restricted rules; never expose admin credentials in the app.
6. Open this folder in Android Studio, allow Gradle sync, then Build > Build Bundle(s) / APK(s) > Build APK(s).

Bookings are stored in the Firestore `appointments` collection with status `pending`. The clinic should confirm each request directly; a submitted request is not a confirmed appointment.

## Before publishing
- Verify the clinic details and phone number.
- Add clinic opening hours and appointment slot availability if desired.
- Configure Firebase security rules and a privacy notice for patient data.
- Test booking submission on a real Android phone.
- Create a signed release APK/AAB in Android Studio.

## Requirements
- Android Studio with Android SDK 35
- JDK 17
