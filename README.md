# Smart Drobi

A specialized Android application for streamlined bridge inspection and reporting, developed for one of Andalas University's PKM (Pekan Kreativitas Mahasiswa) team with drone integration capabilities.

<p align="center">
<img width="3840" height="2160" alt="Modern App Portfolio Mockup Presentation-3-2" src="https://github.com/user-attachments/assets/b4b49fcc-9cfc-4131-83ea-69759fdbbeaf" />
</p>

## 🌉 Overview

Smart Drobi revolutionizes bridge inspection processes by combining traditional field inspection methods with cutting-edge drone technology. The application enables field inspectors to efficiently document bridge conditions, identify structural issues, and generate comprehensive reports through an intuitive mobile interface.

## 🎓 Project Background

In **2023**, I had the opportunity to developed this application for one of **Andalas University's PKM (Pekan Kreativitas Mahasiswa)** teams. The project represents a innovative approach to infrastructure inspection, combining academic research with practical field application needs.

## ✨ Key Features

### 📋 Comprehensive Bridge Inspection
- **Dynamic Inspection Forms**: Highly flexible forms with customizable question sets
- **Multiple Response Types**: Support for Yes/No answers, detailed text input, and numerical data
- **Progressive Documentation**: Step-by-step inspection workflow for thorough bridge assessment

### 📸 Multi-Source Photo Documentation
- **Gallery Integration**: Upload existing photos from device storage
- **Built-in Camera**: Capture new photos using device camera with optimized settings
- **Drone Camera Feed**: Real-time MJPEG streaming from connected drone cameras via IP address
- **Contextual Photography**: Attach photos to specific inspection questions for detailed documentation

### 🚁 Advanced Drone Integration
- **Real-Time Video Feed**: Live MJPEG stream display from drone cameras
- **IP-Based Connection**: Connect to drone cameras using network IP addresses
- **Remote Documentation**: Capture footage from hard-to-reach bridge areas

### 🗺️ Intelligent Bridge Management
- **Bridge Registration**: Add new bridges with comprehensive metadata
- **Interactive Map Selection**: Pinpoint exact bridge locations using Google Maps
- **Automatic Address Retrieval**: GPS coordinates automatically converted to readable addresses
- **Geographic Data**: Precise location tracking for accurate bridge identification

### 📊 Data Management
- **Offline Capability**: Local data storage ensures functionality without internet connection
- **Report Persistence**: All inspection data saved locally with Room database
- **Data Synchronization**: Seamless upload when network connectivity is restored

## 🏗️ Technical Architecture

### Architecture Patterns
- **Repository Pattern**: Clean data access abstraction layer
- **MVVM (Model-View-ViewModel)**: Clear separation of UI and business logic
- **Modular Design**: Scalable architecture for future feature expansion

### Core Technologies

#### Development Framework
- **Platform**: Native Android development
- **Language**: Kotlin for modern, expressive programming
- **UI Framework**: Traditional Android View system

#### Navigation & User Flow
- **Fragment-Based Architecture**: Modular UI components with efficient memory management
- **Bottom Navigation**: Intuitive app section navigation
- **Navigation Components**: NavController and NavGraph for structured navigation flow

#### Data & Persistence
- **Local Database**: Room SQLite abstraction for robust offline storage
- **Reactive Programming**: Flow and LiveData for responsive data handling
- **Asynchronous Operations**: Kotlin Coroutines for efficient background processing

#### Advanced UI Components
- **Dynamic Forms**: Multi-Type ViewHolders with Nested RecyclerView architecture
- **Custom Adapters**: For example, one of the adapters, `BridgeCheckFormFieldsAdapter` supporting various question formats:
  - Header sections
  - Text input fields
  - Boolean questions with/without image attachments
  - Multi-field text responses
- **Real-Time Video**: MjpegView for live drone camera feed display

#### Location & Mapping
- **Google Maps Integration**: SupportMapFragment for interactive map experience
- **Location Services**: LocationServices and FusedLocationProviderClient for GPS
- **Address Resolution**: Geocoder for coordinate-to-address conversion
- **Precise Positioning**: Accurate bridge location identification and storage

## 📱 Usage Workflow

### Bridge Registration
1. **Add New Bridge**: Fill out bridge information form
2. **Location Selection**: Use interactive map to pinpoint exact location
3. **Data Validation**: Automatic address retrieval and coordinate confirmation

### Inspection Process
1. **Select Bridge**: Choose from registered bridges list
2. **Fill Inspection Form**: Complete dynamic questionnaire
3. **Document Issues**: Attach photos from gallery, camera, or drone
4. **Submit Report**: Generate comprehensive inspection report

### Drone Operations
1. **Connect Drone**: Enter drone camera IP address
2. **Live Feed**: View real-time MJPEG video stream
3. **Capture Documentation**: Save key footage for inspection reports

## 🔬 Research & Development

### Innovation Aspects
- **Drone-Mobile Integration**: Novel approach to combining UAV technology with mobile inspection workflows
- **Dynamic Form Generation**: Flexible inspection protocols adaptable to different bridge types
- **Offline-First Design**: Reliable operation in remote bridge locations with limited connectivity

### Technical Contributions
- **Custom Video Streaming**: MJPEG implementation for drone camera integration
- **Geographic Data Management**: Precise location tracking and address resolution
- **Modular Architecture**: Scalable design for future infrastructure inspection applications

## 🙏 Acknowledgments

- **Andalas University** for supporting innovative student research through the PKM program
- **Smart Drobi Team Members** for giving me opportunity to develop this app
- **Android Developer Community** for excellent development resources and libraries

---
