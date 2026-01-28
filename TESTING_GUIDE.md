# Testing Guide

This guide will help you test all the APIs using Swagger UI after deployment.

## Accessing Swagger UI

Once your application is deployed and running, navigate to:
```
<your-deployment-url>/swagger-ui.html
```

For example:
- Local: `http://localhost:8080/swagger-ui.html`
- Railway: `https://your-app.railway.app/swagger-ui.html`
- Render: `https://course-platform-api.onrender.com/swagger-ui.html`

## Step-by-Step Testing

### Phase 1: Test Public APIs (No Authentication Required)

These endpoints work immediately without any setup.

#### 1. List All Courses
- Expand `Courses` section
- Click on `GET /api/courses`
- Click "Try it out"
- Click "Execute"
- **Expected**: List of 2 courses (Physics 101 and Math 101)

#### 2. Get Course Details
- Click on `GET /api/courses/{courseId}`
- Click "Try it out"
- Enter courseId: `physics-101`
- Click "Execute"
- **Expected**: Full course details with topics and subtopics

#### 3. Search Courses
- Expand `Search` section
- Click on `GET /api/search`
- Click "Try it out"
- Enter query: `velocity`
- Click "Execute"
- **Expected**: Search results showing Physics course with matching content

**Try more searches:**
- `Newton` - should return Physics course
- `equation` - should return Math course
- `rate of change` - should return Math course

### Phase 2: User Registration and Authentication

#### 4. Register a New User
- Expand `Authentication` section
- Click on `POST /api/auth/register`
- Click "Try it out"
- Enter request body:
  ```json
  {
    "email": "student@example.com",
    "password": "securePassword123"
  }
  ```
- Click "Execute"
- **Expected**: 201 Created with user details

#### 5. Login
- Click on `POST /api/auth/login`
- Click "Try it out"
- Enter request body:
  ```json
  {
    "email": "student@example.com",
    "password": "securePassword123"
  }
  ```
- Click "Execute"
- **Expected**: 200 OK with JWT token
- **IMPORTANT**: Copy the token value (long string starting with `eyJ...`)

#### 6. Authorize Swagger
- Scroll to top of Swagger page
- Click the green "Authorize" button (🔓 icon)
- In the dialog, enter: `Bearer <your-copied-token>`
  - Example: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
  - Don't forget the word "Bearer" and the space!
- Click "Authorize"
- Click "Close"
- **Status**: The lock icons should now be closed (🔒)

### Phase 3: Test Authenticated APIs

Now that you're authenticated, you can test enrollment and progress tracking.

#### 7. Enroll in a Course
- Expand `Courses` section
- Click on `POST /api/courses/{courseId}/enroll`
- Click "Try it out"
- Enter courseId: `physics-101`
- Click "Execute"
- **Expected**: 201 Created with enrollment details
- **Note**: Save the `enrollmentId` from the response!

#### 8. Try Duplicate Enrollment (Should Fail)
- Try enrolling in `physics-101` again
- Click "Execute"
- **Expected**: 409 Conflict error "Already enrolled in this course"

#### 9. Enroll in Second Course
- Enroll in courseId: `math-101`
- Click "Execute"
- **Expected**: 201 Created with new enrollment

#### 10. Mark Subtopic as Completed
- Expand `Progress` section
- Click on `POST /api/subtopics/{subtopicId}/complete`
- Click "Try it out"
- Enter subtopicId: `speed`
- Click "Execute"
- **Expected**: 200 OK with completion details

#### 11. Mark More Subtopics
Mark a few more subtopics as complete:
- `velocity`
- `acceleration`
- `newtons-first-law`

#### 12. View Progress
- Click on `GET /api/enrollments/{enrollmentId}/progress`
- Click "Try it out"
- Enter the enrollmentId you saved from step 7
- Click "Execute"
- **Expected**: Progress report showing:
  - Total subtopics: 9
  - Completed subtopics: 4
  - Completion percentage: ~44.44%
  - List of completed items with timestamps

### Phase 4: Error Testing

Test error handling to ensure the API responds correctly to invalid requests.

#### 13. Test 404 Not Found
- Try to get a non-existent course: `GET /api/courses/invalid-course`
- **Expected**: 404 error with message "Course with id 'invalid-course' does not exist"

#### 14. Test 401 Unauthorized
- Click the "Authorize" button and click "Logout"
- Try to enroll in a course without authentication
- **Expected**: 401 Unauthorized error

#### 15. Test 403 Forbidden
- Re-authorize with your token
- Try to view progress for an enrollment you don't own:
  - Use enrollmentId: `999`
- **Expected**: 404 or 403 error

#### 16. Test 400 Bad Request
- Try to register with invalid email:
  ```json
  {
    "email": "not-an-email",
    "password": "test"
  }
  ```
- **Expected**: 400 Bad Request with validation error

## Testing Checklist

Use this checklist to verify all functionality:

- [ ] Listed all courses successfully
- [ ] Retrieved course details by ID
- [ ] Searched and found relevant results
- [ ] Registered a new user
- [ ] Logged in and received JWT token
- [ ] Authorized Swagger with JWT
- [ ] Enrolled in first course
- [ ] Duplicate enrollment was prevented (409)
- [ ] Enrolled in second course
- [ ] Marked subtopic as complete
- [ ] Marked multiple subtopics as complete
- [ ] Viewed progress with correct statistics
- [ ] Confirmed 404 for non-existent resources
- [ ] Confirmed 401 without authentication
- [ ] Confirmed proper error messages

## Common Issues

### Issue: "Authorization header missing"
**Solution**: Make sure you clicked "Authorize" and entered `Bearer <token>`

### Issue: "Invalid token"
**Solution**: Token might have expired (24 hours). Login again and get a new token.

### Issue: "Already enrolled"
**Solution**: This is expected behavior. Each user can only enroll once per course.

### Issue: "Forbidden" when marking subtopic complete
**Solution**: You must be enrolled in the course that contains the subtopic.

### Issue: Swagger page not loading
**Solution**:
- Check if application is running
- Verify the URL includes `/swagger-ui.html`
- Check browser console for errors

## Advanced Testing Scenarios

### Scenario 1: Complete Course Progress
1. Enroll in `physics-101`
2. Get course details to see all subtopic IDs
3. Mark all 9 subtopics as complete
4. View progress - should show 100% completion

### Scenario 2: Multiple User Accounts
1. Register and login as first user
2. Enroll in courses and mark progress
3. Logout (clear authorization)
4. Register and login as second user
5. Verify second user has independent enrollments and progress

### Scenario 3: Search Accuracy
Test various search queries:
- Single word: `force`, `equation`, `area`
- Multi-word: `rate of change`, `Newton's law`
- Partial match: `velo` should match `velocity`
- Case insensitive: `PHYSICS` should match `Physics`

## Performance Testing

For basic performance testing:

1. **Response Times**
   - Public endpoints should respond < 500ms
   - Authenticated endpoints should respond < 1000ms

2. **Concurrent Users**
   - Create multiple user accounts
   - Test simultaneous enrollments and progress updates

3. **Search Performance**
   - Test search with various query lengths
   - Verify results are returned quickly

## API Documentation Quality

Check that Swagger documentation includes:
- [ ] All endpoints are documented
- [ ] Request/response schemas are clear
- [ ] Authentication requirements are marked
- [ ] Error responses are documented
- [ ] Examples are provided

## Submission Checklist

Before submitting your assignment, verify:

- [ ] Application is publicly accessible
- [ ] Swagger UI loads at `/swagger-ui.html`
- [ ] All public endpoints work without authentication
- [ ] Registration and login work correctly
- [ ] JWT authentication is properly implemented
- [ ] All authenticated endpoints require valid token
- [ ] Search returns relevant results
- [ ] Enrollment prevents duplicates
- [ ] Progress tracking works correctly
- [ ] Error handling returns proper status codes
- [ ] README includes deployment URL
- [ ] Seed data loads automatically on first startup

## Sample Test Script

For automated testing, here's a sample cURL script:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

echo "1. Testing public course list..."
curl -X GET "$BASE_URL/api/courses"

echo "\n\n2. Testing search..."
curl -X GET "$BASE_URL/api/search?q=velocity"

echo "\n\n3. Registering user..."
REGISTER_RESPONSE=$(curl -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}')
echo $REGISTER_RESPONSE

echo "\n\n4. Logging in..."
LOGIN_RESPONSE=$(curl -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}')
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.token')
echo "Token: $TOKEN"

echo "\n\n5. Enrolling in course..."
ENROLL_RESPONSE=$(curl -X POST "$BASE_URL/api/courses/physics-101/enroll" \
  -H "Authorization: Bearer $TOKEN")
ENROLLMENT_ID=$(echo $ENROLL_RESPONSE | jq -r '.enrollmentId')
echo $ENROLL_RESPONSE

echo "\n\n6. Marking subtopic complete..."
curl -X POST "$BASE_URL/api/subtopics/speed/complete" \
  -H "Authorization: Bearer $TOKEN"

echo "\n\n7. Viewing progress..."
curl -X GET "$BASE_URL/api/enrollments/$ENROLLMENT_ID/progress" \
  -H "Authorization: Bearer $TOKEN"

echo "\n\nAll tests complete!"
```

Save as `test.sh`, make executable with `chmod +x test.sh`, and run with `./test.sh`

## Success Criteria

Your API passes all tests if:
1. All public endpoints return correct data
2. Authentication flow works end-to-end
3. Enrollment and progress tracking function correctly
4. Search returns relevant results
5. Error handling is appropriate
6. All tests can be run via Swagger UI

Good luck with testing!
