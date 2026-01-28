# Assignment Submission Guide

This document provides a quick reference for submitting the Backend Intern Assignment.

## Submission Requirements

You need to provide:
1. GitHub repository link
2. Deployed application URL
3. Notes about your approach

## Pre-Submission Checklist

Before submitting, verify:

### Functionality
- [ ] Application is publicly deployed and accessible
- [ ] Swagger UI is available at `/swagger-ui.html`
- [ ] Seed data loads automatically on first startup
- [ ] All public endpoints work without authentication
- [ ] Registration and login return JWT tokens
- [ ] JWT authentication works for protected endpoints
- [ ] Search is case-insensitive and supports partial matches
- [ ] Users cannot enroll in the same course twice
- [ ] Progress tracking works correctly
- [ ] All error responses include proper status codes and messages

### Documentation
- [ ] README includes deployment URL
- [ ] README has clear setup instructions
- [ ] Code is well-organized and readable
- [ ] Swagger documentation is complete

### Testing
- [ ] Tested all endpoints via Swagger UI
- [ ] Verified error handling
- [ ] Confirmed data persistence after restart
- [ ] Tested on a fresh database

## GitHub Repository Structure

Your repository should contain:

```
course-platform-api/
├── src/
│   ├── main/
│   │   ├── java/com/courseplatform/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── seed-data.json
├── pom.xml
├── Dockerfile
├── README.md
├── DEPLOYMENT.md
├── TESTING_GUIDE.md
├── .gitignore
└── .env.example
```

## Deployment Checklist

### Railway (Recommended)
- [ ] Created Railway account
- [ ] Connected GitHub repository
- [ ] Added PostgreSQL database
- [ ] Set JWT_SECRET environment variable
- [ ] Deployment successful
- [ ] Swagger UI accessible

### Alternative Platform (Render/Fly.io/Heroku)
- [ ] Created account
- [ ] Set up PostgreSQL database
- [ ] Configured environment variables
- [ ] Deployed application
- [ ] Verified functionality

## Testing Your Deployment

Use the Swagger UI to test:

1. **Public Access** (no authentication needed)
   - GET `/api/courses` - List all courses
   - GET `/api/courses/physics-101` - Get course details
   - GET `/api/search?q=velocity` - Search courses

2. **Authentication**
   - POST `/api/auth/register` - Register new user
   - POST `/api/auth/login` - Get JWT token
   - Click "Authorize" and enter token

3. **Authenticated Operations**
   - POST `/api/courses/physics-101/enroll` - Enroll in course
   - POST `/api/subtopics/speed/complete` - Mark subtopic complete
   - GET `/api/enrollments/{id}/progress` - View progress

## Sample Submission Message

When you DM the recruiter, include:

```
Subject: Backend Intern Assignment Submission

Hello,

I have completed the Backend Intern Assignment. Here are the details:

GitHub Repository: https://github.com/your-username/course-platform-api

Deployed Application: https://your-app.railway.app

Swagger UI: https://your-app.railway.app/swagger-ui.html

Notes on Approach:
- Used Spring Boot 3.2.1 with Java 17
- Implemented JWT authentication with Spring Security
- PostgreSQL with JPA/Hibernate for data persistence
- Seed data loads automatically on first startup
- Search implemented using PostgreSQL LIKE queries for case-insensitive partial matching
- All APIs documented with Swagger/OpenAPI
- Deployed on Railway with automated PostgreSQL provisioning

Key Implementation Highlights:
1. Clean architecture with separation of concerns (controllers, services, repositories)
2. Comprehensive error handling with proper HTTP status codes
3. Idempotent subtopic completion (safe to mark same subtopic multiple times)
4. Enrollment uniqueness constraint prevents duplicates at database level
5. Progress calculation includes completion percentage

Testing Instructions:
1. Visit the Swagger UI at the URL above
2. Test public endpoints immediately (courses, search)
3. Register a test user and login to get JWT token
4. Click "Authorize" and enter: Bearer <token>
5. Test enrollment and progress tracking features

All requirements have been implemented and tested. The application is ready for review.

Thank you for the opportunity!

Best regards,
[Your Name]
```

## Common Issues and Solutions

### Issue: Seed data not loading
**Check**: Application logs should show "Loading seed data..." message
**Solution**: Ensure seed-data.json is in src/main/resources/

### Issue: JWT token not working
**Check**: Token format should be `Bearer eyJ...`
**Solution**: Make sure "Bearer " prefix is included in authorization header

### Issue: Database connection fails
**Check**: DATABASE_URL environment variable
**Solution**: Verify PostgreSQL is running and connection string is correct

### Issue: Build fails on deployment
**Check**: Java version and Maven configuration
**Solution**: Ensure Java 17 is specified in deployment settings

## Evaluation Criteria (from Assignment)

Your submission will be evaluated on:

1. **Data Modeling & Relationships** (20%)
   - Entity design and relationships
   - Database schema quality
   - Use of constraints and indexes

2. **Search Behavior** (20%)
   - Case-insensitive search works
   - Partial matches return results
   - Relevant results are returned

3. **Authentication & Authorization** (20%)
   - JWT implementation is secure
   - Protected endpoints require authentication
   - Unauthorized access is prevented

4. **Business Logic** (20%)
   - Enrollment prevents duplicates
   - Progress tracking is accurate
   - Data consistency is maintained

5. **Code Structure & Readability** (10%)
   - Clean code organization
   - Proper separation of concerns
   - Meaningful naming

6. **Ease of Testing via Swagger** (5%)
   - All endpoints documented
   - Swagger UI works correctly
   - Easy to test all features

7. **README Clarity** (5%)
   - Clear setup instructions
   - Deployment guide included
   - Architecture explained

## Bonus Points

You can earn bonus points for:
- Implementing Elasticsearch for advanced search
- Adding pagination to course list
- Including unit tests
- Performance optimizations
- Additional useful features

## Time Management

Target time: ~8 hours

Suggested breakdown:
- Project setup & entities: 1.5 hours
- Authentication & security: 1.5 hours
- Course & search APIs: 2 hours
- Enrollment & progress: 2 hours
- Testing & documentation: 1 hour

**Note**: If you're spending significantly more time, you may be over-engineering. Focus on core requirements first.

## Final Checks Before Submission

Run through this final checklist:

- [ ] Pushed all code to GitHub
- [ ] README contains deployment URL
- [ ] Swagger UI is accessible and working
- [ ] Tested all endpoints end-to-end
- [ ] Verified error handling
- [ ] Environment variables are configured
- [ ] No sensitive data (passwords, secrets) committed
- [ ] Application logs show no critical errors
- [ ] Seed data loaded successfully
- [ ] Database persists data correctly

## Support

If you encounter issues:
1. Check the DEPLOYMENT.md guide
2. Review TESTING_GUIDE.md for testing help
3. Check application logs for errors
4. Verify environment variables are set correctly
5. Ensure PostgreSQL is accessible

## Deadline

**Submission Deadline**: Wednesday, February 4th, 2026

Submit before the deadline with all required materials.

## Questions?

If you have questions about the assignment:
- Review the original assignment document
- Check the README for technical details
- Refer to DEPLOYMENT.md for deployment help
- Contact the recruiter if you need clarification

Good luck with your submission!
