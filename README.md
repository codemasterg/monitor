The 'monitor' project is a motion detection web application written in Java ..
=======

... when the Java web application detects motion it captures a photo then sends an email / text to a configurable 
distribution list and sounds an audible alarm.  It runs in the Jetty web server on Raspberry Pi, tested with Linux version:
  raspberrypi 3.12.22+ #691 PREEMPT Wed Jun 18 18:29:58 BST 2014 armv6l GNU/Linux

Dependencies (managed by Maven):
- PI4J to integrate passive infra-red sensor and illuminate / pulse LED.
- Spring core and Spring MVC to implement web controller and web service for this application.
- JSPs and JQuery to implement browser interface.
- Java Mail API so emails / texts can be sent when motion detected
- raspistill for photo capture
- MapDB for NoSQL data persistence
- aplay for audio playback (alarm)

The program utilizes an external property file so that email server, accounts, and  distribution are configurable.  It also allows the photo capture command and image file directory to be configured.  The time threshold for rearming the motion sensor after a motion detection is also configurable.

This project was created as a learning exercise to increase my understanding of Spring, Spring MVC, and JQuery.  It's also a useful home security device!

The design is basic MVC:

Browser --> Controller --> Service.  The web app lets you view various stats, see the most recent photo captures,
view the system log and enable / disable sending of email and capture of photos.  When disabled it pulses an LED so 
the user gets a visual inidcation that the sensor is disabled.  the LED also illuminates (solid) when motion detected.

The PIR sensor interface is built using the PI4J library (kudos to that project, it made the GPIO interface easy to integrate).  The sensor is an Observable (ref Observer Design Pattern); observers are:
   - Photo Taker
   - Databse Updater
   - EMail Sender

This separation of concerns helps keep things clean and loosely coupled (a good thing).

Planned enhancements:

- Operational Schedule: allows user to specify when sensor should be auto disabled / enabled.  This is 
  useful when you know you will be in the area being monitored on a regular schedule.
  
- Add USB triggered Nerf toy that activates when motion detected.
