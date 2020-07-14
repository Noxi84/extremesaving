## Project description

Extreme-saving is program which generates a PDF-report based on your financial data. You are free to use this application for your own finances. 

## Installation
1) Download [ExtremeSaving.jar](https://github.com/Noxi84/extremesaving/blob/master/ExtremeSaving.jar) and copy it to a folder on your computer.
2) Download [startup-wrapper.sh](https://github.com/Noxi84/extremesaving/blob/master/startup-wrapper.sh) (Linux) or [startup-wrapper.bat](https://github.com/Noxi84/extremesaving/blob/master/startup-wrapper.bat) (Windows) into the same folder.
3) Create a new file data.csv in the same folder. Add the headers and your financial data based on the example below.
4) Run startup-wrapper.sh (Linux) or startup-wrapper.bat (Windows).
5) FinancialReport.pdf containing the financial report is now created in the same folder.

## Configuration
All data is read from a data.csv. The csv-content is separated by a comma-character. Lines can be commented out by placing a # in front of the line.
The header must be present at the first line of the CSV-file: #date,category,value. The order of the columns can be changed by changing the header.

| CSV Header | Description | Example |
| ---------- | ----------- | ------- |
| date       | Date format is 'd/m/yyyy'. | 5/1/2020 = 5 January 2020 | 
| category   | Name of the group or category. | Food & Drinks |
| value      | Value format is '#.##'. Decimals are not mandatory. | 12.53 |

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
If FinancialReport.pdf is corrupt or not created there is probably a mistake in data.csv. Check console.log after executing ExtremeSaving.jar.
If you encounter a bug, you can send a message. Please attach your CSV-file to reproduce the issue.

## Example FinancialReport.pdf
[Download FinancialReport.pdf](https://github.com/Noxi84/extremesaving/blob/master/FinancialReport.pdf)
___
![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/FinancialReport-1.jpg)
___
![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/FinancialReport-2.jpg)
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
