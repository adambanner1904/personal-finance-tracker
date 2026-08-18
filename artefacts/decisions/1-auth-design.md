# Authentication Strategy

## Context

Before implementing the sign up and log in features I need to have a clear idea on how I am going to handle authenticating a user, log them in and check they are authorised to access the authenticated pages. 

## Plan

The plan is have a separate table *user_sessions* that keeps track of session_id's for users that can be used to keep track of 'are they logged in?' and 'when does the access expire?'. The session_id can be stored in the users cookies and retrieved on every request to verify they are still logged in. 

To make this happen we can take advantage of Play's Actions to ensure that every request has similar handling, we retrieve the session_id from the request in the cookie's, we then check this against the database and provided that is fine and not expired, we can retrieve the user id from the database and use that to show whatever it is they are requesting for. 

Potential pitfalls: 

- User is requesting to an authenticated page but has no session id in request (route to log in page flashing you cannot access this page with NotAuthorised)
- User has session_id but is not in the database (remove cookie (if neccessary?) and route to log in page)
- User has session_id but has expired (remove cookie, delete row from database and route to log in page)

### Data model

The db schema for user_sessions will be: 

- session_id (uuid?)
- user_id (long)
- created_at (timestampz)
- expires_at (timestampz)
- last_seen_at (timestampz) - not sure needed

### AuthenticatedAction flow (pitfalls listed above)

1. Retrieve SessionId from Cookie <- can fail if None
2. Use to retrieve data from DB <- can fail if no entry or is expired
3. Update expires_at and/or last_seen_at if it expires soon
4. Return augmented AuthenticatedRequest[?]

### Log in flow

1. User -> GET /log-in
2. User -> POST /log-in (provided form validation passes)
3. Load user and check password_hash matches <- if no match or no user Redirect to log in 
4. Create new *user_sessions* row with user_id (everything else should be created db side)
5. Db returns session_id (at minimum) and inserts into session cookie
6. Redirect to authenticated landing page 

### Sign up flow

1. User -> GET /sign-up
2. User -> POST /sign-up
3. Validate fields, create user in db <- can fail for email uniqueness constraint
4. Use auth service to log in user (using user_id) to avoid code duplication

### Log out flow

1. User -> POST /log-out
2. Read *session_id*
3. Try and delete the row in *user_sessions*
4. Clear Play session cookie of all data
5. Redirect to /index (flashing you have been logged out)

## Plan for implementation

1. Create schema and sql for migration
2. Create repository for *user_sessions* (create (user_id, expires_at), read, update (expiry time), delete)
3. Create AuthService (createUser, logInUser, getSession, deleteSession, createSession, updateSession)
4. Create AuthenticatedAction returning AuthenticatedRequest (mostly just with the userId attached)