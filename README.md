## High-Load Problem

At 10:00 AM, 100,000 users try to buy 5,000 tickets at the same time.

The system must:
- stay stable under heavy load
- prevent double booking

### Solution
Split the process:
1. **Reservation** — temporarily hold a seat
2. **Payment** — confirm and finalize the purchase

### Benefits
- Prevents race conditions and double booking
- Improves system stability under high load
- Ensures data consistency during peak traffic
