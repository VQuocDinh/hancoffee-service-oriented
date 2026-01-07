# HanCoffee — Service-Oriented Application

A service-oriented coffee shop system consisting of:
- HanCoffeeBE — backend services (APIs)
- HanCoffeeFE — web frontend
- HanCoffee_AndroidApp — Android mobile client
- docker-compose.yml — orchestrates services for local development

This README explains the project overview, architecture, setup, usage, development workflows, and contribution guidelines.

---

## Table of contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quickstart (Docker Compose)](#quickstart-docker-compose)
- [Running services individually](#running-services-individually)
  - [Backend (HanCoffeeBE)](#backend-hancoffeebe)
  - [Frontend (HanCoffeeFE)](#frontend-hancoffeef)
  - [Android app (HanCoffee_AndroidApp)](#android-app-hancoffee_androidapp)
- [Configuration & Environment variables](#configuration--environment-variables)
- [API](#api)
- [Database & Migrations](#database--migrations)
- [Testing](#testing)
- [Development workflow](#development-workflow)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License & Contact](#license--contact)

---

## Project overview

HanCoffee is a modular, service-oriented application for managing a coffee shop. The repository separates concerns into independent components:

- HanCoffeeBE: REST APIs and service logic (orders, products, users, authentication, etc.)
- HanCoffeeFE: Web client (likely a React/Vue/Angular single-page app)
- HanCoffee_AndroidApp: Mobile client for Android
- Docker Compose to run dependencies (databases, message brokers) and service containers for local integration testing

This structure allows teams to develop, test, and deploy services independently.

---

## Architecture

High-level architecture (textual):

- Clients
  - Web (HanCoffeeFE)
  - Mobile (HanCoffee_AndroidApp)
- Backend services (HanCoffeeBE)
  - REST API
  - Business logic (orders, users, products, inventory, payments)
- Infrastructure
  - Database (Postgres / MySQL / MongoDB — check HanCoffeeBE `.env` or `docker-compose.yml`)
  - Optional: Redis / RabbitMQ / other services (see `docker-compose.yml`)

Services communicate via HTTP/REST. Persistent data is stored in the configured database.

---

## Prerequisites

Install these on your machine for local development:

- Git
- Docker & Docker Compose (for running with containers)
- Node.js (LTS) and npm/yarn — for frontend/backend installs if running locally without Docker
- Android Studio (for Android app development)
- Java SDK (required by Android Studio)

---

## Quickstart (Docker Compose)

There is a top-level `docker-compose.yml` to orchestrate the system for local testing.

1. Clone the repo:
   ```bash
   git clone https://github.com/VQuocDinh/hancoffee-service-oriented.git
   cd hancoffee-service-oriented
   ```

2. Copy or create environment files if present (example):
   ```bash
   # Create .env or service-specific .env files as required by docker-compose.yml
   cp example.env .env
   ```

3. Start services:
   ```bash
   docker-compose up --build
   ```

4. Stop services:
   ```bash
   docker-compose down
   ```

Notes:
- If ports are in use, update `docker-compose.yml`.
- Check logs for services:
  ```bash
  docker-compose logs -f hancoffee-be
  ```

---

## Running services individually

If you prefer to run services outside Docker (during development), follow the per-component instructions.

### Backend (HanCoffeeBE)

1. Change directory:
   ```bash
   cd HanCoffeeBE
   ```

2. Install dependencies:
   ```bash
   npm install
   # or
   yarn install
   ```

3. Create environment variables:
   - Create a `.env` file (or copy `.env.example` if exists) with values for:
     - PORT
     - DATABASE_URL / DB_HOST / DB_USER / DB_PASS / DB_NAME
     - JWT_SECRET (for auth)
     - Any other variables referenced in the backend

4. Run the server:
   ```bash
   npm run dev   # or `npm start` depending on package scripts
   ```

5. The API base URL will usually be `http://localhost:PORT` (check console output).

Where to find API routes:
- Inspect the `routes`, `controllers`, or `src` directory inside HanCoffeeBE to find endpoints and controllers.

### Frontend (HanCoffeeFE)

1. Change directory:
   ```bash
   cd HanCoffeeFE
   ```

2. Install dependencies:
   ```bash
   npm install
   # or
   yarn install
   ```

3. Set frontend configuration:
   - Create a `.env` or `config` with `REACT_APP_API_URL` (or equivalent) pointing to the backend.

4. Run locally:
   ```bash
   npm start  # or yarn start
   ```

5. The dev server typically runs at `http://localhost:3000`.

### Android app (HanCoffee_AndroidApp)

1. Open the `HanCoffee_AndroidApp` project in Android Studio.
2. Configure API base URL / environment via `gradle.properties` or in app `buildConfig`.
3. Build and run on emulator or device.

---

## Configuration & Environment variables

Each service may require environment variables. Common items:

- PORT — service port
- DATABASE_URL / DB_HOST / DB_PORT / DB_USER / DB_PASS / DB_NAME
- JWT_SECRET — secret for signing tokens
- NODE_ENV — development/production
- REDIS_URL / RABBITMQ_URL — if used

Best practices:
- Keep secrets out of git. Use `.env` and add it to `.gitignore`.
- Use Docker secrets or environment managers for production.

Example `.env` (backend):
```env
PORT=4000
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASS=password
DB_NAME=hancoffee
JWT_SECRET=replace_with_secure_secret
```

---

## API

This README contains a general guide. For a full API reference, I can scan the HanCoffeeBE route/controller files and generate an exhaustive endpoint list with request/response examples. Typical API areas you can expect:

- Authentication
  - POST /auth/login
  - POST /auth/register
  - POST /auth/refresh
- Users
  - GET /users
  - GET /users/:id
  - PUT /users/:id
- Products / Menu
  - GET /products
  - GET /products/:id
  - POST /products
- Orders
  - POST /orders
  - GET /orders/:id
  - GET /orders (list)
- Payments
  - POST /payments (or integration hooks)

To produce a precise API spec, tell me if you want a:
- Plain list of routes (fast)
- OpenAPI / Swagger v3 document (machine-readable)
- Postman collection

I can generate any of the above after reading the backend route files.

---

## Database & Migrations

- Check HanCoffeeBE for migration setup (e.g., `knex`, `sequelize`, `typeorm`, or raw SQL).
- If migrations exist:
  - Run them locally:
    ```bash
    # Example for knex/sequelize
    npx knex migrate:latest
    # or
    npx sequelize db:migrate
    ```
- If not, check `scripts/` or `db/` directories for SQL files to initialize schema.

---

## Testing

- Unit / integration tests (if present) typically live under `tests` or `__tests__`.
- Typical commands:
  ```bash
  npm test
  # or
  npm run test
  ```
- For frontend, run:
  ```bash
  npm test
  ```

If the project does not yet have automated tests, adding basic test coverage for backend routes and key components is recommended.

---

## Development workflow

- Branching:
  - Feature branches: `feature/<short-description>`
  - Bugfix branches: `bugfix/<short-description>`
  - Use pull requests for review
- Commits:
  - Keep atomic commits with clear messages (consider a convention like Conventional Commits)
- Local dev:
  - Use Docker Compose when you want a full environment
  - Run services individually for fast iteration

---

## Deployment

A few options depending on your target environment:
- Containerize services and deploy via Docker Compose on a VM or Docker Swarm
- Use a Kubernetes cluster and create manifests/Helm charts
- Deploy backend to a cloud provider (Heroku, AWS ECS, DigitalOcean App Platform)
- Host frontend as static assets on Netlify/Vercel or as a container behind a CDN

Recommended steps:
1. Create production environment variables and secrets manager entries
2. Build artifacts:
   - Frontend: `npm run build`
   - Backend: build image with Dockerfile
3. Use CI to run tests, build images, and push to registry
4. Deploy via your orchestrator and run migrations in a maintenance window

---

## Contributing

Thank you for considering contributing!

Guidelines:
1. Fork the repository
2. Create a topic branch:
   ```bash
   git checkout -b feature/awesome-feature
   ```
3. Make changes and run tests
4. Create a pull request describing the change and motivation
5. Ensure changes include tests and documentation updates where appropriate

Please include:
- Clear title and description in PR
- Screenshots or logs when relevant
- Migration instructions if database schema changes

If you want contribution templates or issue templates, I can add them.

---

## Troubleshooting & FAQ

- If services fail to connect to DB: confirm DB container is up and `DATABASE_URL` matches credentials.
- If ports conflict: modify mapped ports in `docker-compose.yml`.
- If authentication fails: verify `JWT_SECRET` and token issuance flows.

---

## License & Contact

- Add your preferred license file (e.g., MIT) to the repo if you want the project to be open-source.
- For questions or further work, contact: [VQuocDinh](https://github.com/VQuocDinh)

---

If you want, I can now:
- Extract the backend routes and produce a complete API reference (endpoints, params, sample requests/responses).
- Generate an OpenAPI (Swagger) document based on the backend code.
- Add example `.env` and `docker-compose.override.yml` for local development.

Which of these should I do next?
