# README template

## Project description

Extreme-saving is a hobby project I created to manage my own financial status.
After running the application a PDF file is generated containing the Financial report.

##### Green line on line chart
The green line on the linechart represents a future prediction based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses all outlines are removed based on the golden ratio.
2) Calculate the average daily result per day.
3) This will result in an average daily result which will be added to each future day.
   The last day has a bigger factor then yesterday. Yesterday has a bigger factor then the day before and so on.

##### Red line on line chart
The red line is based on a daily average result of all expenses from the input data.
This can be a representation on how long you can survive before you will run out of money if all incomes stop and only expenses continue.
Green line on line chart
The green line on the linechart represents a future prediction based on your input data.
1) All data is split up into incomes and expenses. For both incomes and expenses all outlines are removed based on the golden ratio.
2) Calculate the average daily result per day.
3) This will result in an average daily result which will be added to each future day.
   The last day has a bigger factor then yesterday. Yesterday has a bigger factor then the day before and so on.

## Red line on line chart
The redline is based on a daily average result of all expenses from the input data.
This is some sort of a representation to how long you can survive if all incomes would stop and only expenses would continue.

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