# Event-Driven Ticket System

Backend system for seat reservation under high concurrency.  
Designed to solve real-world problems like double booking, race conditions, and time-based state consistency.

---

## Problems & Solutions

### 1. Double Booking (Race Conditions)

**Problem:**  
Multiple users attempt to reserve the same seat at the same time.

**Solution:**  
- Optimistic locking using `@Version`
- Conflict detection via `ObjectOptimisticLockingFailureException`

**Result:**  
Only one user successfully reserves the seat, others receive a safe failure.

---

### 2. Temporary Reservations (Seat Holding)

**Problem:**  
Users may abandon the booking process, leaving seats blocked.

**Solution:**  
- Seats move to `RESERVED` state with timestamp
- 10-minute expiration window
- Background job releases expired reservations

**Result:**  
System avoids dead seats and maintains availability.

---

### 3. Inconsistent State During Payment

**Problem:**  
Seat state can become invalid if payment fails or is interrupted.

**Solution:**  
- Controlled state transitions:
  - `AVAILABLE → RESERVED → SOLD`
- Rollback logic on payment failure

**Result:**  
Seat state always remains consistent.

---

### 4. Concurrent Updates

**Problem:**  
Multiple updates to the same entity can overwrite each other.

**Solution:**  
- Optimistic locking (`@Version`)
- Transaction boundaries

**Result:**  
Safe concurrent modifications without data corruption.

---

### 5. Time-Based State Validation

**Problem:**  
A reserved seat might appear unavailable even after expiration.

**Solution:**  
- Runtime validation (`isSeatActuallyAvailable`)
- Scheduled cleanup (`@Scheduled`)

**Result:**  
Accurate seat availability at all times.

---

## Core Features

- JWT authentication
- Seat reservation with expiration
- Simulated payment processing
- Automatic cleanup of expired reservations
- Concurrency-safe booking

---

## Tech Stack

- Java 25
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Redis
