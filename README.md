# Shazam

This app has been created together with Charles. It belongs to a project, which tries to track lorries with the help of IoT devices.
An Arduino enhanced with a GMS as well as a GPS module sends the location information to the _Blynk_ cloud. The cloud provides a 
simple REST-API to fetch data conveniently. This app serves as visualization for gathered data. It does so by conducting the 
API and request the data from it which is then send to the app via a JSON string. The app parsed the data and stores it in a local 
database. Serving as the only source-of-truth, the database updates all other components including the UI asynchronously.
The locations are either displayed in a recycler-view or directly in _Google Maps_.


