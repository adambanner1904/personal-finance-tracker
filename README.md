# Personal Finance Tracker

This repository is the source code for a personal finance tracking web application. 

## Purpose

The intial idea is to provide the functionality to manage (add, delete, update, archive) your accounts and then to "snapshot" your account balances on a particular date in order to track your finances over time. 

The main purpose is to replace a spreadsheet that I have been using for many years however as the accounts I use change (archiving savings spaces or simply changing banks) the complexity of managing the spreadsheet increases. And hopefully over time I can add more functionality this way. 

## Prerequisites

- sbt 1.12.14
- Java 21.0.6
- docker 29.6.2
- docker-compose 5.3.1

## Run locally

Start the service 

```bash 
sbt run 
```

And then navigate to `http://localhost:9000`

### Postgres

This project is using a postgres docker container on port `5432`. You can start this container by running

```bash
docker-compose up -d
```

You can validate that postgres is set up locally by running the following command: 

```bash 
PGPASSWORD=finance psql -h localhost -p 5432 -U pft_app -d personal_finance_tracker -c "SELECT 1;"
```

which will make sure that you can actually connect to the database locally. 

## Database Schema

![](docs/image.png)