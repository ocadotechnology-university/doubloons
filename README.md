# Doubloons

Web platform allowing employees to acknowledge their team members.

# Setup
This example requires a database. To set up local database in docker simply run `./start-db.sh`. It will launch a docker instance of postgres exposed on `5432` bootstrapped with contents of the `db-scheme.sql` file.

# Usage
1) To run back-end server, execute:
```
sbt> reload
sbt> run
```
2) To run React app, navigate to `frontend` directory and execute:
```
npm install
npm start
```
