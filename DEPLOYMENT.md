# Deployment Guide

This guide covers deploying the Course Platform API to various cloud platforms.

## Prerequisites

Before deploying, ensure you have:
- A PostgreSQL database instance
- Environment variables configured
- The application builds successfully locally

## Option 1: Railway

Railway provides easy deployment with built-in PostgreSQL support.

### Steps:

1. **Install Railway CLI** (optional)
   ```bash
   npm install -g @railway/cli
   ```

2. **Create Railway Account**
   - Go to https://railway.app
   - Sign up with GitHub

3. **Create New Project**
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your repository

4. **Add PostgreSQL**
   - Click "New" → "Database" → "Add PostgreSQL"
   - Railway will automatically create a database

5. **Configure Environment Variables**

   Railway auto-configures `DATABASE_URL`, but you need to add:
   ```
   JWT_SECRET=your-256-bit-secret-key-change-this
   ```

6. **Deploy**
   - Push to your GitHub repository
   - Railway automatically builds and deploys
   - Access your app at the provided Railway URL

7. **Access Swagger**
   ```
   https://your-app.railway.app/swagger-ui.html
   ```

## Option 2: Render

Render offers free tier deployment for web services and PostgreSQL.

### Steps:

1. **Create Render Account**
   - Go to https://render.com
   - Sign up

2. **Create PostgreSQL Database**
   - Dashboard → "New" → "PostgreSQL"
   - Choose free tier
   - Note the Internal Database URL

3. **Create Web Service**
   - Dashboard → "New" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     ```
     Name: course-platform-api
     Environment: Docker
     Region: Choose nearest
     Branch: main
     ```

4. **Environment Variables**
   ```
   DATABASE_URL=<internal-database-url-from-step-2>
   JWT_SECRET=your-256-bit-secret-key
   PORT=8080
   ```

5. **Deploy**
   - Click "Create Web Service"
   - Wait for build to complete

6. **Access Application**
   ```
   https://course-platform-api.onrender.com/swagger-ui.html
   ```

## Option 3: Fly.io

Fly.io provides global deployment with free tier.

### Steps:

1. **Install Fly CLI**
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. **Login**
   ```bash
   fly auth login
   ```

3. **Create Fly App**
   ```bash
   fly launch
   ```
   - Choose app name
   - Select region
   - Don't deploy yet

4. **Create PostgreSQL**
   ```bash
   fly postgres create
   ```
   - Name: course-platform-db
   - Attach to your app:
   ```bash
   fly postgres attach course-platform-db
   ```

5. **Set Secrets**
   ```bash
   fly secrets set JWT_SECRET=your-256-bit-secret-key
   ```

6. **Deploy**
   ```bash
   fly deploy
   ```

7. **Access Application**
   ```bash
   fly open
   ```
   Then navigate to `/swagger-ui.html`

## Option 4: Heroku

Heroku is a classic PaaS platform (limited free tier).

### Steps:

1. **Install Heroku CLI**
   ```bash
   curl https://cli-assets.heroku.com/install.sh | sh
   ```

2. **Login**
   ```bash
   heroku login
   ```

3. **Create App**
   ```bash
   heroku create course-platform-api
   ```

4. **Add PostgreSQL**
   ```bash
   heroku addons:create heroku-postgresql:mini
   ```

5. **Configure Environment**
   ```bash
   heroku config:set JWT_SECRET=your-256-bit-secret-key
   ```

6. **Deploy**
   ```bash
   git push heroku main
   ```

7. **Access Application**
   ```bash
   heroku open
   ```

## Environment Variables Reference

All platforms require these environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection string | `jdbc:postgresql://host:5432/db` |
| `DATABASE_USERNAME` | DB username | `postgres` |
| `DATABASE_PASSWORD` | DB password | `securepassword` |
| `JWT_SECRET` | Secret for JWT signing (256+ bits) | `your-secure-secret-key` |
| `PORT` | Server port (usually auto-set) | `8080` |

## Database Migration

The application automatically:
1. Creates tables on first startup (Hibernate DDL auto)
2. Loads seed data if database is empty
3. Skips seeding if data exists

## Health Checks

Most platforms support health checks. Use:

**Endpoint**: `/actuator/health` (if Spring Actuator is added)

Or simply check: `/api/courses` (should return 200 OK)

## Troubleshooting

### Build Fails
- Ensure Java 17 is specified in build configuration
- Check Maven logs for dependency issues
- Verify `pom.xml` is valid

### Database Connection Fails
- Check `DATABASE_URL` format matches JDBC format
- Verify database allows connections from your app
- Check username/password are correct

### Application Crashes
- Check logs: `heroku logs --tail` (or platform equivalent)
- Verify JWT_SECRET is set
- Ensure PostgreSQL is running

### Seed Data Not Loading
- Check application logs on startup
- Verify `seed-data.json` is in resources folder
- Database might already have data (check course count)

## Performance Optimization

For production deployment:

1. **Database**
   - Add indexes on foreign keys
   - Enable connection pooling
   - Use read replicas for heavy read loads

2. **Application**
   - Enable caching (Spring Cache)
   - Configure proper JVM heap size
   - Use production profile settings

3. **Security**
   - Use strong JWT secrets (256+ bits)
   - Enable HTTPS only
   - Configure CORS properly
   - Rate limit authentication endpoints

## Monitoring

Recommended monitoring tools:
- **Application**: Spring Boot Actuator + Prometheus
- **Database**: Platform-provided metrics
- **Errors**: Sentry or Rollbar
- **Logs**: Platform logging or external service

## Scaling

To handle more traffic:

1. **Vertical Scaling**: Upgrade instance size
2. **Horizontal Scaling**: Add more instances (stateless design)
3. **Database**: Upgrade PostgreSQL tier or add replicas
4. **Caching**: Add Redis for frequently accessed data

## Cost Estimation

Approximate monthly costs (as of 2025):

| Platform | Free Tier | Paid Tier |
|----------|-----------|-----------|
| Railway | $5 credit/month | ~$20/month |
| Render | Limited free | ~$25/month |
| Fly.io | Limited free | ~$15/month |
| Heroku | Limited | ~$25/month |

## Continuous Deployment

Setup CI/CD:

1. Connect repository to platform
2. Enable auto-deployment on push to main
3. Configure build command: `mvn clean install`
4. Set start command: `java -jar target/*.jar`

## Backup Strategy

For production:
- Enable automated database backups (all platforms support this)
- Schedule: Daily backups, 7-day retention minimum
- Test restore process regularly

## Support

If deployment issues arise:
- Check platform status page
- Review application logs
- Consult platform documentation
- Check GitHub issues for known problems

Good luck with your deployment!
