# Random Quote API

A tiny HTTP service in Java (no dependencies) for INF 345. Each request to
`/quote` returns one random quote from a built-in list.

## Endpoints
- `GET /` welcome message
- `GET /healthz` health check, returns `OK`
- `GET /quote` a random quote

## Run
    scripts/run.sh
The port comes from the `PORT` environment variable, default **8080**.
Example: `PORT=9000 scripts/run.sh`

## Test
    scripts/test.sh
Prints `TESTS: n/n` and exits 0 on success. Requires JDK 11+.
