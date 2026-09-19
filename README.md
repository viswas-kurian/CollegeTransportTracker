# College Transport Tracker 

A simple Android application designed to help college students easily access and manage college transportation information.

## Features

-  View available buses
-  View bus routes and stops
-  View bus schedules
-  View driver contact information
-  Add new buses
-  Add new routes
-  Add new schedules
-  Directly contact bus drivers through the phone dialer
-  Local data storage using SQLite
-  Modern and user-friendly Android interface

## Technologies Used

- Java
- XML
- Android Studio
- SQLite
- Android SDK
- CardView

## Main Modules

### Bus Details
Displays available buses along with their route, driver and contact information.

### Bus Routes
Displays available routes and their associated stops.

### Bus Schedule
Displays departure and arrival times for each bus.

### Driver Contacts
Displays driver information and allows users to open the phone dialer directly.

### Transport Management
Administrators/users can add:
- New buses
- New routes
- New schedules

## Database

The application uses SQLite for local data storage.

The database contains:

- `buses`
- `routes`
- `schedules`

## Project Structure

```text
CollegeTransportTracker/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/collegetransporttracker/
│           │       ├── MainActivity.java
│           │       ├── BusDetailsActivity.java
│           │       ├── RoutesActivity.java
│           │       ├── ScheduleActivity.java
│           │       ├── DriverActivity.java
│           │       ├── AddBusActivity.java
│           │       ├── AddRouteActivity.java
│           │       ├── AddScheduleActivity.java
│           │       └── DatabaseHelper.java
│           │
│           └── res/
│               ├── layout/
│               ├── drawable/
│               └── values/
│
├── README.md
└── .gitignore
