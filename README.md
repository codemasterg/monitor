monitor
=======

Complete Java web application.  When it detects motion it captures a photo then sends an email / text to a configurable 
distribution list.  Runs on Raspberry Pi, tested with Linux version:
  raspberrypi 3.12.22+ #691 PREEMPT Wed Jun 18 18:29:58 BST 2014 armv6l GNU/Linux

Dependencies (managed by Maven):
- PI4J to integrate passive infra-red sensor and illuminate LED.
- Spring core and Spring MVC to implement web controller and web service for this application.
- JSPs and JQuery to implement browser interface.
- Java Mail API so emails / texts can be sent when motion detected
- raspistill for photo capture
- MapDB for NoSQL data persistence

Utilizes external dependency file so that email server, accounts, and  distribution are configurable.  Also allows 
photo capture command and image file directory to be configured.  The time threshold for rearming the motion 
sensor after a motion detection is also configurable.

This project was created as a learning exercise to increase my understanding of Spring, Spring MVC, and JQuery.  It's also 
usefule home security device!

The design is basic MVC:

Browser --> Controller --> Service.  The web app lets you view various stats, see the most recent photo captures,
view the system log and enable / disable sending of email and capture of photos.  When disabled pulses an LED so 
user gets a visual inidcation of it being disabled.  LED also illuminates (solid) when motion detected.

The PIR sensor is built on the PI4J library (kudos to that project, it made the GPIO interface so easy to integrate).
The sensor is an Observable (ref Observer Design Pattern); observables are:
   - Photo Taker
   - Databse Updater
   - EMail Sender

This separation of concerns helped keep things clean and loosely coupled (a good thing).

Planned enhancements:

- Operational Schedule: allows user to specify when sensor should be auto disabled / enabled.  This is 
  useful when you know you will be in the area being monitored on a regular basis.
  
- Add USB triggered Nerf toy that activates when motion detected.
