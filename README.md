# Personal Finance Manager – Backend System

A backend application designed to help users manage their personal finances through secure transaction management, budgeting, and analytics-driven insights.

---

##  Features

- User authentication using JWT
- Income and expense transaction management
- Category-based expense tracking
- Monthly budget management (overall and category-wise)
- Budget vs Actual spending analysis
- Monthly financial summaries (income, expenses, savings)
- Category-wise expense breakdown
- Monthly trends analysis
- Redis caching for optimized analytics performance

---

##  Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Security:** Spring Security + JWT  
- **Database:** Microsoft SQL Server  
- **Caching:** Redis  
- **ORM:** Spring Data JPA (Hibernate)  
- **Build Tool:** Maven  
- **API Testing:** Postman  

---

##  Architecture

The application follows a **layered architecture**:

- Controller Layer – Handles API requests and responses  
- Service Layer – Contains business logic  
- Repository Layer – Manages database interactions  

---

##  Performance Optimization

To improve performance of analytics APIs, Redis caching is implemented using Spring Cache.

- First request → Data fetched from database  
- Subsequent requests → Data served from Redis  

This reduces response time and database load significantly.

---

##  Security

- JWT-based authentication  
- Stateless session management  
- Protected APIs require valid token  

---

##  API Endpoints (Sample)

### Authentication
POST /api/auth/register
POST /api/auth/login

---

###  Transactions
POST /api/transactions
GET /api/transactions
PUT /api/transactions/{id}
DELETE /api/transactions/{id}


---

###  Budget
POST /api/budgets
GET /api/budgets


---

### Analytics

GET /api/analytics/monthly-summary?month=YYYY-MM
GET /api/analytics/category-summary
GET /api/analytics/monthly-trends
GET /api/analytics/budget-vs-actual

---

## 🧠 Redis Caching Flow

Client
↓
Spring Boot API
↓
Check Redis Cache
↓
┌───────────────┐
│ Cache Hit │ → Return Response
└───────────────┘
↓
┌───────────────┐
│ Cache Miss │ → Query Database
└───────────────┘
↓
Process Data
↓
Store in Redis
↓
Return Response







