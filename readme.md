#README template

Extreme-saving is a hobby project I created to manage my own financial status.
All data can be put into one or more CSV-files.

After running the application a PDF file is generated with the Extreme-Saving report.

##config.properties
You can setup properties in config.properties
| Property name                                     | Description                                                                | Example value
| ------------------------------------------------- | -------------------------------------------------------------------------- | ---------------
| data.csv.dateFormat1                              | 1st possible dateformat in CSV.                                            | d/M/yyyy
| data.csv.dateFormat2                              | 2nd possible dateformat in CSV.                                            | d-M-yyyy
| data.csv.dataFolder                               | Path where the CSV file(s) are located.                                    | /home/kris/Dropbox/extremesaving/data/
| data.csv.splitBy                                  | Character how CSV file is split.                                           | ,
| data.csv.header.date                              | Header in csv for date column. Content must match 1 of the 2 date formats. | date
| data.csv.header.value                             | Header in csv for value column. Content must be a numeric value.           | value 
| data.csv.header.category                          | Header in csv for category column.                                         | category
| data.csv.header.description                       | Header in csv for description column. Content won't be used in PDF report. | description
| pdf.location                                      | Pdf export location.                                                       | /home/kris/Dropbox/extremesaving/report.pdf
| chart.monthBar.location                           | Temporary chart file location.                                             | /tmp/month-barchart.png
| chart.goalLine.location                           | Temporary chart file location.                                             | /tmp/goal-linechart.png
| chart.goalLine.estimation.filterDates.enabled     | Setting for calculation green line on lineChart.                           | true
| chart.goalLine.estimation.removeOutliners.enabled | Setting for calculation green line on lineChart.                           | true
| goals                                             | Setting how far the green line on lineChart should be calculated           | 1000,3000,5000,15000,30000,60000,85000,100000,120000,140000,170000,200000,250000,300000,500000

##Green line on line chart
The green line on the linechart represents a future prediction based on your input data.
It first splits up all data into incomes and expenses.
For both incomes and expenses all outlines are removed based on the golden ratio.
This can be enabled or disbled with property chart.goalLine.estimation.removeOutliners.enabled.

After removing the outliners from all data the average daily result is calculated for each day.
The last day has a bigger factor then yesterday. Yesterday has a bigger factor then the day before and so on.
This can be enabled or disabled with property chart.goalLine.estimation.filterDates.enabled.

This will result in an average daily result which will be added to each future day. This is represented in the green line.

##Red line on line chart
The redline is based on a daily average result of all expenses from the input data.
This is some sort of a representation to how long you can survive if all incomes would stop and only expenses would continue.

