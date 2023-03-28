# KeyStore App

The KeyStore app shows how the android Keystore system works. It encrypts user provided data and then decrypts the latest encrypted data and shows it to the user. 
The app primarily converts the user input into a byteArray and then uses specified algorithms to facilitate the encryt and decrypt processes of the data using the generated Key and created encryption & decryption Ciphers. 

# Tools

The app is built using   
* Kotlin
* Jetpack Compose

# Screenshots

<img width="1384" alt="app screenshots" src="https://user-images.githubusercontent.com/33720666/226583404-06261051-4a89-4472-b297-13626357fc61.png">

# Architecture 

Though small the Keystore app is built using the standard MVVM architecture. The architecture helps in managing the UI state & hoisting the UI state to the ViewModel

# Configuration 

No configuration is required
