# Calendar Application - Electronic Monthly Planner

## Overview

This Java-based calendar application presents an interactive electronic calendar in a classic monthly table format. Users can browse through months and years, view specific dates, and manage appointments for selected days. The design emphasizes simplicity, clarity, and data integrity.

## Features

- Displays any month of any year in a 4-6 row, 7 column grid.
- Table columns represent days of the week (Sunday to Saturday).
- The calendar automatically adjusts to month lengths and starting weekdays.
- Allows users to select any date and add textual appointments.
- Appointments are stored without specific time slots, but users may include time manually in the note.
- Appointments for a specific day can be viewed, edited, and saved via a popup dialog.
- Efficient data management using `HashMap`, mapping dates to notes.
- Fully utilizes `java.util.Calendar` for date calculations and navigation.

## User Interface

The UI includes:

- **Year and Month Selector**: A control panel that allows users to choose which month and year to display.
- **Calendar Grid**: A (4-6)x7 table representing the chosen month.
- **Day Interaction**: Clicking a day opens a dialog showing the appointments for that date, where users can add or edit text.

## Technical Details

- Appointments are stored using Java collections:
  - `HashMap<LocalDate, List<String>>`
- Important `Calendar` methods used:
  Calendar c = Calendar.getInstance();
  c.set(year, month, day); // Set a specific date
  c.get(Calendar.YEAR);    // Get year
  c.get(Calendar.MONTH);   // Get month (0-based)
  c.get(Calendar.DAY_OF_MONTH); // Get day of month
  c.get(Calendar.DAY_OF_WEEK);  // Get weekday (1 = Sunday)

## Technologies Used

* Java – The application is built using Java SE (Standard Edition), leveraging object-oriented programming principles.
* JavaFX – Used for designing the graphical user interface (GUI).
* Scene Builder – Employed to create and design the JavaFX FXML layout files visually.
* FXML – UI components are defined in FXML and linked to Java controllers.
* Java Collections (HashMap) – Used to manage and store appointment data efficiently.

## The project was completed as part of the Advanced Programming in Java course - 20554 (maman 14). Final grade: 100.

