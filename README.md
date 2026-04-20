## Concept: High-Load Problem

### Scenario
Imagine a popular artist releases a new album and ticket sales open at **10:00 AM**.  
At **10:00:01**, over **100,000 users** access the website simultaneously, while only **5,000 seats** are available.

### Challenges
The system must:
- Handle extreme traffic without crashing
- Prevent the same seat from being sold to multiple users

### Solution Approach

The process is divided into two stages:

1. **Reservation**
   - Temporarily locks a seat for a user
   - Has a limited expiration time (e.g., 5–10 minutes)

2. **Payment**
   - Confirms the purchase
   - Permanently assigns the seat to the user

### Benefits
- Prevents race conditions and double booking
- Improves system stability under high load
- Ensures data consistency during peak traffic
