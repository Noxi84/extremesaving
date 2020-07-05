## Project description

Extreme-saving is a hobby project I created to manage my own financial status.
After running the application a PDF file is generated containing the Financial report.

##### Blue line on chart
The blue line displays the balance over a timeline for all data in the CSV-file.

##### Green line on chart
The green line predicts the future balance based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses all outlines are removed.
2) The average daily result per day is calculated.
4) Add a factor to the dates: dates closer to today have a higher importance then past dates.
3) This result is added to each day in the future for a time period of 6 years.

## Red line on chart
The red line is based on a daily average result of all expenses from the input data.
This can be a representation on how long you can survive if all incomes stop and only expenses remain.

## Configuration
All data read from a single CSV-file.
The data is split by a comma separator. You can add comments by adding a # before the line.
The following header must be present at the first line: #date,category,value.

## Example data.csv

```
#date,category,value
01/01/2020,Food & Drinks,-17.25
01/01/2020,Work Salary,1975.40
05/01/2020,Food & Drinks,-17.25
17/01/2020,Rent,-500.00
20/01/2020,Phone & Internet,-125.00
25/01/2020,Electricity,-86.36
31/01/2020,Garbage,-12.45
01/02/2020,Work Salary,1975.40
15/02/2020,Rent,-500.00
25/02/2020,Electricity,-86.36
```

## Example report.pdf
[Download report.pdf](https://github.com/Noxi84/extremesaving/blob/master/report.pdf)

![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/report-page-001.jpg)
![Screenshot](https://github.com/Noxi84/extremesaving/blob/master/report-page-002.jpg)