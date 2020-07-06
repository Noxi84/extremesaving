## Project description

Extreme-saving is program which generates a PDF-report based on your financial data. You are free to use this application for your own finances. 

## Installation
1) Download [ExtremeSaving.jar](https://github.com/Noxi84/extremesaving/blob/master/ExtremeSaving.jar) and copy it to a folder on your computer.
2) Create a new file data.csv in the same folder. Add the headers and your financial data based on the example below.
3) Start ExtremeSaving.jar. Linux users can use [startup-wrapper.sh](https://github.com/Noxi84/extremesaving/blob/master/startup-wrapper.sh). 
4) FinancialReport.pdf containing the financial report is now created in the same folder.

## Configuration
All data is read from a CSV-file. The csv-content is separated by a comma-character. You can comment out a line by placing a # in front of the line.
The following header must be present at the first line of the CSV-file: #date,category,value.

| CSV Header | Description |
| ---------- | ----------- |
| date       | Date format is dd/mm/yyyy. January = 1 or 01, December = 12.| 
| category   | A category name.|
| value      | Value for the category and the date. Decimal separator is a dot. For example: 12.53. Decimals are not mandatory. |

## Example data.csv
[Download data.csv](https://github.com/Noxi84/extremesaving/blob/master/data.csv)
```
#date,category,value
01/01/2020,Food & Drinks,-17.25
01/01/2020,Work,1975.40
05/01/2020,Food & Drinks,-17.25
17/01/2020,Rent,-500.00
20/01/2020,Phone & Internet,-125.00
25/01/2020,Electricity,-86.36
31/01/2020,Garbage,-12.45
01/02/2020,Work,1975.40
15/02/2020,Rent,-500.00
25/02/2020,Electricity,-86.36
```

## Troubleshooting
If the FinancialReport.pdf is not generated there is probably a mistake in data.csv. Check console.log after executing ExtremeSaving.jar.

## Example FinancialReport.pdf
[Download FinancialReport.pdf](https://github.com/Noxi84/extremesaving/blob/master/FinancialReport.pdf)
___
![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/report-page-001.jpg)
___
![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/report-page-002.jpg)
___
## Charts explanation
### Overall chart
#### Blue line on chart
The blue line displays the balance over a timeline for all data in the CSV-file.
#### Green line on chart
The green line predicts the future balance based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses outlines are removed.
2) The average result per day is calculated.
4) A factor is added to each date: dates closer to today have a higher importance then past dates.
3) The result is added to each day in the future for a time period of 6 years.
#### Red line on chart
The red line is based on a daily average result of all expenses from the input data.
It is a representation on how long you can survive if all incomes stop and only expenses remain.

### Monthly chart
#### Blue bars on chart
The blue bars display the final balance of the corresponding month.
#### Green bars on chart
The green bars display the sum of all incomes of the corresponding month.
#### Red bars on chart
The red bars display the sum of all expenses of the corresponding month.
