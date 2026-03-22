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
