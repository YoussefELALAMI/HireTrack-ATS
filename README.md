# 🧠 HireTrack – AI-Powered Recruitment CRM

**HireTrack** is an intelligent, full-stack recruitment management platform that helps companies streamline their hiring pipeline using **AI, automation, and data analytics**.  
It’s designed as a **SaaS MVP** built with **Angular**, **Spring Boot**, **Kafka**, and **PostgreSQL**, demonstrating modern enterprise architecture and DevOps integration.

---

## 🚀 Features

### 🧩 Core Features (MVP)
- **Job Management** – Create, edit, and manage job listings.  
- **Candidate Management** – Upload and parse resumes automatically using NLP.  
- **AI Matching** – Score candidates based on job description similarity.  
- **Recruiter Dashboard** – View jobs, candidates, and match scores in one place.

### ⚙️ Advanced Features (Planned / Nice-to-Haves)
- Real-time notifications using **Kafka**  
- Role-based access control (Admin, Recruiter, Manager)  
- AI-powered semantic search using **Sentence Transformers**  
- Analytics dashboard (diversity, time-to-hire, conversion rates)  
- Integration with Google Calendar for interview scheduling  
- Continuous Integration with **Jenkins** and code quality via **SonarQube**

---

## 🏗️ Architecture Overview

HireTrack follows a **modular microservice-inspired architecture**, separating concerns for scalability and maintainability.

```

Frontend (Angular)  →  REST API (Spring Boot)  →  Service Layer (Spring)  →  Database (PostgreSQL)
↘
Kafka (Event-driven updates & notifications)

````

- **Frontend:** Angular 17, TypeScript, RxJS, TailwindCSS  
- **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate  
- **Database:** PostgreSQL  
- **Messaging:** Apache Kafka  
- **DevOps:** Jenkins, SonarQube, Docker  
- **Version Control:** Git + GitHub Actions (CI)

---

## 🧰 Tech Stack

| Layer | Technology |
|-------|-------------|
| **Frontend** | Angular, TypeScript, TailwindCSS |
| **Backend** | Spring Boot, Java 17, Hibernate |
| **Database** | PostgreSQL |
| **Messaging** | Apache Kafka |
| **DevOps** | Jenkins, SonarQube, Docker |
| **Testing** | JUnit, Postman |
| **Version Control** | Git, GitHub |

---

## ⚡ Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/hiretrack.git
cd hiretrack
````

### 2. Backend Setup

```bash
cd backend
./mvnw spring-boot:run
```

* Runs the Spring Boot server at `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
ng serve
```

* Runs the Angular app at `http://localhost:4200`

---

## 🧠 Key Concepts Demonstrated

* **Enterprise-grade architecture:** layered separation (Controller → Service → Repository)
* **Event-driven design:** using Kafka for inter-service communication
* **AI integration:** basic resume parsing & job matching using NLP
* **Data visualization:** dashboard with metrics for recruiters
* **Clean code & CI/CD:** Jenkins pipelines with SonarQube code quality checks

---

## 📈 Roadmap

* [ ] Job and Candidate Management
* [ ] Resume Parsing & Scoring (MVP)
* [ ] Kafka-based Real-Time Notifications
* [ ] AI Semantic Matching (Sentence Transformers)
* [ ] Analytics Dashboard
* [ ] Role-Based Access Control
* [ ] Docker Compose Setup for Full Stack

---

## 🧑‍💻 How to Pitch This Project

> “Built a full-stack AI-powered recruitment CRM (SaaS) using Angular, Spring Boot, Kafka, and PostgreSQL — automating resume parsing, candidate scoring, and recruiter collaboration.
> Designed with event-driven architecture, CI/CD pipelines, and clean code principles.”

This phrasing immediately shows technical depth, business understanding, and professional-grade project design.

---

## 📜 License

This project is released under the **MIT License** — feel free to fork and build upon it.

---

## 💬 Contact

Created with ❤️ by Youssef EL ALAMI
🔗 [LinkedIn](https://www.linkedin.com/in/youssef-el-alami-/) | [Email](mailto:youremail@example.com)

```
