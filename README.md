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


# OAuth2 config

App id api://eec81012-d926-45e4-ae61-871f5d5b35f3

Scope 
openid profile api://eec81012-d926-45e4-ae61-871f5d5b35f3/users.write.all

login link
https://login.microsoftonline.com/0cb0fbf6-09ad-4c56-81fe-44c8576c323b/oauth2/v2.0/authorize?response_type=code&redirect_uri=http://localhost:8080/auth/authorize&response_mode=query&state=12345&scope=openid profile api://eec81012-d926-45e4-ae61-871f5d5b35f3/users.write.all&client_id=eec81012-d926-45e4-ae61-871f5d5b35f3