# CoTrip Mobile App

A modern Android travel planning application built with Kotlin, Jetpack Compose, and Supabase.

> **Note**: This project has been migrated from React Native to native Android development for better performance, simpler OAuth integration, and improved development experience.

## Features

- � **Authentication**: Email/password and Google OAuth sign-in using Supabase
- �🗺️ **Trip Planning**: Create, edit, and manage travel itineraries
- 👥 **Collaboration**: Share trips with friends and family
- � **Modern UI**: Built with Jetpack Compose and Material Design 3
- 🏗️ **Clean Architecture**: MVVM pattern with Repository pattern
- � **Real-time Updates**: Supabase real-time subscriptions
- � **Dark Mode**: Automatic dark/light theme support

## Tech Stack

### Core
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Design system
- **Android Architecture Components** - MVVM, ViewModel, Navigation

### Networking & Backend
- **Supabase** - Backend as a Service (auth, database, real-time)
- **Retrofit** - HTTP client for REST API calls
- **OkHttp** - HTTP client with logging
- **Kotlin Serialization** - JSON serialization

### Dependency Injection
- **Hilt** - Dependency injection framework

### Authentication
- **Supabase Auth** - User authentication and session management
- **Google Sign-In** - OAuth authentication

### Storage
- **DataStore** - Local preferences storage

### Image Loading
- **Coil** - Image loading library
- **Session Persistence**: Stay logged in across app restarts

## Get started

### Prerequisites

- Node.js (v18 or higher)
- npm or yarn
- Expo CLI (`npm install -g @expo/cli`)
- Supabase account and project

### Setup

1. **Install dependencies**

   ```bash
   npm install
   ```

2. **Configure Supabase**

   - Create a Supabase project at [supabase.com](https://supabase.com)
   - Go to Settings > API in your Supabase dashboard
   - Copy your project URL and anon key
   - Create a `.env` file from the example:

   ```bash
   cp .env.example .env
   ```

   - Update `.env` with your credentials:

   ```env
   EXPO_PUBLIC_SUPABASE_URL=https://your-project.supabase.co
   EXPO_PUBLIC_SUPABASE_ANON_KEY=your-anon-key-here
   ```

   **⚠️ Important**: Never commit the `.env` file to version control!

3. **Configure Google OAuth (Optional)**

   To enable Google Sign-In:

   **A. Google Cloud Console Setup:**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the Google+ API
   - Go to Credentials and create OAuth 2.0 Client ID (Web application type)
   - Add authorized redirect URI: `https://your-project.supabase.co/auth/v1/callback`

   **B. Supabase Dashboard Setup:**
   - Go to Authentication > Providers in your Supabase Dashboard
   - Enable Google provider
   - Add your Google Client ID and Secret from Google Cloud Console
   - **Important:** The credentials must be set in BOTH Google Cloud Console AND Supabase Dashboard

   **C. App Configuration (Optional):**
   - Add Google credentials to your `.env` (these are for app-side validation):

   ```env
   EXPO_PUBLIC_GOOGLE_CLIENT_ID=your-google-client-id
   EXPO_PUBLIC_GOOGLE_CLIENT_SECRET=your-google-client-secret
   ```

   **Note:** If you get "missing oauth secret" error, it means the Google provider isn't properly configured in Supabase Dashboard. See `SUPABASE_GOOGLE_SETUP.md` for detailed troubleshooting.

4. **Start the development server**

   ```bash
   npx expo start
   ```

5. **Run the app**

   - **Development build**: [development build](https://docs.expo.dev/develop/development-builds/introduction/)
   - **Android emulator**: [Android emulator](https://docs.expo.dev/workflow/android-studio-emulator/)
   - **iOS simulator**: [iOS simulator](https://docs.expo.dev/workflow/ios-simulator/)
   - **Expo Go**: Download the [Expo Go](https://expo.dev/go) app and scan the QR code

## App Structure

```
src/
├── components/
│   └── GoogleSignInButton.tsx # Google OAuth sign-in button
├── config/
│   └── supabase.ts          # Supabase client configuration
├── contexts/
│   └── AuthContext.tsx      # Authentication context and hooks
├── navigation/
│   └── AppNavigator.tsx     # App navigation setup
├── screens/
│   ├── HomeScreen.tsx       # Main dashboard
│   ├── LoginScreen.tsx      # User login
│   ├── SignUpScreen.tsx     # User registration
│   ├── ProfileScreen.tsx    # User profile details
│   ├── TripScreen.tsx       # Trip details view
│   └── CreateTripScreen.tsx # Trip creation
└── ...
```

## Authentication Flow

1. **Unauthenticated Users**: See Login/Sign Up screens
2. **Sign Up**: Create account with email, password, and full name
3. **Email Verification**: Users receive verification email from Supabase
4. **Sign In**: Login with verified credentials
5. **Authenticated State**: Access to main app features and profile

## Key Components

- **AuthContext**: Manages authentication state and provides auth methods
- **Navigation**: Conditional rendering based on authentication status
- **Secure Storage**: Automatic token persistence using Expo SecureStore
- **Profile Screen**: Displays user account information and sign-out option

## Development

### Available Scripts

- `npm start` - Start Expo development server
- `npm run android` - Run on Android emulator
- `npm run ios` - Run on iOS simulator
- `npm run web` - Run in web browser
- `npm run lint` - Run ESLint

### Environment Variables

Create a `.env` file based on `.env.example` and fill in your Supabase credentials.

## Integration with Backend

This mobile app works in conjunction with the CoTrip-Backend API:
- Authentication is handled directly with Supabase
- Trip management uses the backend API with JWT tokens
- User profile data comes from Supabase user metadata

## Security Features

- JWT token-based authentication
- Secure token storage with Expo SecureStore
- Automatic token refresh
- Session persistence across app restarts
- Input validation and error handling

## Join the community

Join our community of developers creating universal apps.

- [Expo on GitHub](https://github.com/expo/expo): View our open source platform and contribute.
- [Discord community](https://chat.expo.dev): Chat with Expo users and ask questions.
