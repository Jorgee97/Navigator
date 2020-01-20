# Navigator

# Steps to run the application
All my tests were made using the Android emulator with the API 27 Android 8.1.O

To validate the functionallity of the LocationService I used the extended controllers from the emulator to mock a route.

# Apis being Used
I used MapBox SDK for the map showing and rendering of the sprites to the map

# Development process
I didn't went for an architecture such as MVP or MVVM because the application felt short to do such approach.

I handle most of the map methods on a single activity and I created a Service to keep track of the location updates and the speed of the device

Also I used a "custom" style map to load my sprites to the MapBox SDK so I can pretty much resamble the given screenshot example.

