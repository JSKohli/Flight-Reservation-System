# Flight-Reservation-System
This project was my final project for Computer Science II class. It is a simulator for viewing, searching, and booking flights. Although the requirement was only to implement a few OOP concepts, I tried to make it bigger in an attempt to learn JAVA.

### Improvements
* The code I submitted for had quite a few bugs which have now been removed.
* The program now has user input validation at all steps.
* The flight ticket had terrible formatting. Now it has a better layout.


### Present Status
At present the program begins by reading data from a text file(flight.txt) and displaying all the information. 

![List of all available flights](https://raw.githubusercontent.com/JSKohli/Flight-Reservation-System/extension/Screenshots/ScreenShot1.jpg)


Then it asks the user for query details such as TO, FROM, FLYING DATE. It searches through all the flights and displays the ones that match the search data. The user selects a flight and is taken to Passenger Info Page. 

![Query Details](https://raw.githubusercontent.com/JSKohli/Flight-Reservation-System/extension/Screenshots/ScreenShot2.jpg)


The user enters the passenger info, selects a seat, and confirms booking. 

![Passenger Info form](https://raw.githubusercontent.com/JSKohli/Flight-Reservation-System/extension/Screenshots/ScreenShot3.jpg)

![Airplane seat map](https://raw.githubusercontent.com/JSKohli/Flight-Reservation-System/extension/Screenshots/ScreenShot4.jpg)


The program finally generates the ticket in the form of a text file (FlightTicket.txt). 

![Flight ticket](https://raw.githubusercontent.com/JSKohli/Flight-Reservation-System/extension/Screenshots/ScreenShot5.jpg)


### Features to be added
* Passenger list in flight objects to be added
* Sort by flight prices
* Payment Details Page
* Possibly introduce a database and get rid of text files
* Possible introduce GUI using JAVA FX
