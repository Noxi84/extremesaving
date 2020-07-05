#README template

##Project description

Extreme-saving is a hobby project I created to manage my own financial status.
After running the application a PDF file is generated containing the Financial report.

#####Green line on line chart
The green line on the linechart represents a future prediction based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses all outlines are removed based on the golden ratio.
2) Calculate the average daily result per day.
3) This will result in an average daily result which will be added to each future day.
   The last day has a bigger factor then yesterday. Yesterday has a bigger factor then the day before and so on.

#####Red line on line chart
The redline is based on a daily average result of all expenses from the input data.
This is some sort of a representation to how long you can survive if all incomes would stop and only expenses would continue.
Green line on line chart
The green line on the linechart represents a future prediction based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses all outlines are removed based on the golden ratio.
2) Calculate the average daily result per day.
3) This will result in an average daily result which will be added to each future day.
   The last day has a bigger factor then yesterday. Yesterday has a bigger factor then the day before and so on.

##Red line on line chart
The redline is based on a daily average result of all expenses from the input data.
This is some sort of a representation to how long you can survive if all incomes would stop and only expenses would continue.

##Configuration
All data read from a single CSV-file.

#####You can setup properties in config.properties

| Property name                                     | Description                                                                | Example value
| ------------------------------------------------- | -------------------------------------------------------------------------- | ---------------
| data.csv.dataFolder                               | Path where the CSV file(s) are located.                                    | /home/kris/Dropbox/extremesaving/data/
| data.csv.header.date                              | Header in csv for date column. Content must match 1 of the 2 date formats. | date
| data.csv.header.value                             | Header in csv for value column. Content must be a numeric value.           | value 
| data.csv.header.category                          | Header in csv for category column.                                         | category
| data.csv.header.description                       | Header in csv for description column. Content won't be used in PDF report. | description
| pdf.location                                      | Pdf export location.                                                       | /home/kris/Dropbox/extremesaving/report.pdf
| chart.monthBar.location                           | Temporary chart file location.                                             | /tmp/month-barchart.png
| chart.goalLine.location                           | Temporary chart file location.                                             | /tmp/goal-linechart.png

