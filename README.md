# Personal Finance Tracker

This repository is the source code for a personal finance tracking web application. 

## Purpose

The intial idea is to provide the functionality to manage (add, delete, update, archive) your accounts and then to "snapshot" your account balances on a particular date in order to track your finances over time. 

The main purpose is to replace a spreadsheet that I have been using for many years however as the accounts I use change (archiving savings spaces or simply changing banks) the complexity of managing the spreadsheet increases. And hopefully over time I can add more functionality this way. 

## Prerequisites

- sbt 1.12.14
- Java 21.0.6
- docker (to run postgres)

## Run locally

Start the service 

```bash 
sbt run 
```

And then navigate to `http://localhost:9000`

Run postgres locally via docker