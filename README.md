# 🅿️ Parking Lot & Ticket Service (Microservices Demo)

## 📌 Overview  
This project demonstrates **two Spring Boot microservices** communicating with each other:  

✅ **Parking Lot Service** – manages parking spots  
✅ **Ticket Service** – manages vehicle entry/exit tickets  

They are containerized with **Docker** and orchestrated using **Docker Compose**.  

---

## 📡 API Endpoints  

### ✅ Parking Lot Service (`http://localhost:8080/spots`)  

| Method | Endpoint            | Description |
|--------|---------------------|-------------|
| `GET`  | `/available`        | Get all available parking spots |
| `POST` | `/occupy/{spotId}` | Mark a parking spot as occupied |
| `POST` | `/free/{spotId}`   | Free an occupied parking spot |
| `POST` | `/reset`           | Free all parking spots |

#### Sample JSON Response  

**GET /spots/available**  
```json
[
  {
    "spotId": "SPOT-001",
    "level": 1,
    "type": "CAR",
    "occupied": false
  },
  {
    "spotId": "SPOT-002",
    "level": 1,
    "type": "BIKE",
    "occupied": false
  }
]
```

---

### ✅ Ticket Service (`http://localhost:8081/tickets`)  

| Method | Endpoint               | Description |
|--------|------------------------|-------------|
| `POST` | `/entry`               | Create a ticket for a vehicle |
| `GET`  | `/{ticketId}`          | Get ticket details |
| `POST` | `/exit/{ticketId}`     | Exit & close the ticket (frees parking spot) |
| `GET`  | `/history`             | Get all ticket history (optional filter by `licensePlate`) |
| `GET`  | `/active`              | Get all active (ongoing) tickets |

#### Sample Requests & Responses  

**Vehicle Entry**  
```bash
curl -X POST http://localhost:8081/tickets/entry \
  -H "Content-Type: application/json" \
  -d '{"licensePlate": "MH12AB1234", "vehicleType": "CAR"}'
```

✅ **Response:**  
```json
{
  "ticketId": "TICKET-001",
  "licensePlate": "MH12AB1234",
  "vehicleType": "CAR",
  "spotId": "SPOT-001",
  "entryTime": "2025-07-20T12:00:00"
}
```

---

**Get Ticket by ID**  
```bash
curl http://localhost:8081/tickets/TICKET-001
```

✅ **Response:**  
```json
{
  "ticketId": "TICKET-001",
  "licensePlate": "MH12AB1234",
  "vehicleType": "CAR",
  "spotId": "SPOT-001",
  "entryTime": "2025-07-20T12:00:00",
  "exitTime": null,
  "status": "ACTIVE"
}
```

---

**Vehicle Exit**  
```bash
curl -X POST http://localhost:8081/tickets/exit/TICKET-001
```

✅ **Response:**  
```text
Vehicle exited, spot SPOT-001 freed.
```

---

**Get Active Tickets**  
```bash
curl http://localhost:8081/tickets/active
```

✅ **Response:**  
```json
[
  {
    "ticketId": "TICKET-001",
    "licensePlate": "MH12AB1234",
    "vehicleType": "CAR",
    "spotId": "SPOT-001",
    "entryTime": "2025-07-20T12:00:00"
  }
]
```

---

**Get History (Optional Filter)**  
```bash
curl http://localhost:8081/tickets/history?licensePlate=MH12AB1234
```

✅ **Response:**  
```json
[
  {
    "ticketId": "TICKET-001",
    "licensePlate": "MH12AB1234",
    "vehicleType": "CAR",
    "spotId": "SPOT-001",
    "entryTime": "2025-07-20T12:00:00",
    "exitTime": "2025-07-20T14:00:00",
    "status": "COMPLETED"
  }
]
```

---

## 🚀 How It Works  

1. **Ticket Service** receives a **vehicle entry request**  
2. It calls **Parking Lot Service** to get the **first available spot**  
3. It **occupies the spot** and creates a ticket  
4. On vehicle exit, **Ticket Service** calls Parking Lot Service to **free the spot**  
5. Ticket is marked as completed  

---

## 🐳 Running the Project  

### 1️⃣ Build JAR files  

```bash
cd parking-lot-service
mvn clean package -DskipTests

cd ../ticket-service
mvn clean package -DskipTests
```

### 2️⃣ Start services with Docker  

```bash
docker-compose up -d --build
```

- Parking Lot Service → `http://localhost:8080`  
- Ticket Service → `http://localhost:8081`  

Stop containers:  
```bash
docker-compose down
```

---

## 🔮 Future Enhancements  

✅ **Eureka Server** for service discovery  
✅ **Kafka Integration** for event-driven notifications  
✅ **PostgreSQL/MySQL** instead of H2  

---
