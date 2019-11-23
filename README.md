# WeatherCheck

# Introduction

Welcome to my WeatherCheck application.  This is a simple application designed to allow a user to easily and quickly check
his or her weather based on the phone's location using networks and cell phone towers.  When the user opens the app, it
automatically determines is location, provided the appropriate permissions have been granted.  It then makes an API call to a
free weather service using the latitude/longitude information obtained.  The API returns our call in a JSON format, which the
application parses.  It then displays the city, temperature (in Fahrenheit) and an image representing the outside weather
condition.

Additional functionality was added that opens up a new Activity, and allows the user to enter a new city.  The application then
calls the API with this new city information, and displays the new city name, temperature and outside weather conditions.

# Installation

To install, please clone or download this repository.  Open in Android Studio and build.  This application is designed to run on
Lollipop or later.
