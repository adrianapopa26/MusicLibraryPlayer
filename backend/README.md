# Music Library Player

Spring Boot REST API for managing a music library with Deezer integration.

## Stack

- Java, Spring Boot 2.6.7, Gradle
- MySQL (primary data), MongoDB (comments)
- JWT auth, Stripe payments, WebSocket, Spring Batch

## Run

**Requirements:** MySQL on `localhost:3306`, MongoDB running locally.

```bash
# Create the database
mysql -u root -p -e "CREATE DATABASE music_player;"

# Run
./gradlew bootRun
```

Server starts on `http://localhost:8088`.

On first start (`app.bootstrap=true`) the app seeds two accounts:

| Username | Password  | Role     |
|----------|-----------|----------|
| alex     | WooHoo1!  | ADMIN    |
| alex1    | WooHoo1!  | EMPLOYEE |

## Key Endpoints

| Method | Path                          | Description              |
|--------|-------------------------------|--------------------------|
| POST   | /api/auth/sign-in             | Login → JWT token        |
| POST   | /api/auth/sign-up             | Register                 |
| GET    | /api/tracksAPI                | Search tracks via Deezer |
| PUT    | /api/tracksAPI/purchase/{id}  | Purchase a track         |
| PUT    | /api/playlists/create/{id}    | Create playlist for user |
| POST   | /api/payment                  | Initiate Stripe checkout |
| WS     | /topic/greetings              | WebSocket feed           |

## TODO

- [ ] Move secrets (Stripe key, JWT secret, DB password) to environment variables
- [ ] Add JWT refresh token support
- [ ] Verify Stripe payment before writing to `user_tracks`
- [ ] Replace hardcoded bootstrap emails with placeholders
- [ ] Fix cron schedule (`0 10 2 19 5 ?` fires once a year — likely unintentional)
- [ ] Expand WebSocket usage beyond the greeting stub
- [ ] Add missing service/controller tests
- [ ] Decide batch job behavior: full replace vs. incremental import

## Notes

- JWT expires after 24h; include as `Authorization: Bearer <token>`
- Batch CSV import (`Tracks.csv`) is disabled by default (`spring.batch.job.enabld=false`)
- Credentials in `application.properties` are for local dev only — do not commit real secrets
