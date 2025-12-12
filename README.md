Shopping Cart Pricing Service:

Project Overview

The Shopping Cart Pricing Service is a robust Spring Boot application designed to accurately calculate the total cost of a customer’s shopping cart. 
The pricing logic is driven by database-backed pricing model that considers the customer's type, their revenue (for professional clients), 
and the specific products and quantities in the cart.


Architecture and Design

 Database-Driven Pricing Model

 price table, stores all pricing rules.

 Price Table Structure

| Column | Purpose | Example Value |
| :--- | :--- | :--- |
| product_type | Product identifier | HIGH_END_PHONE, LAPTOP |
| client_type | Type of customer | INDIVIDUAL, PROFESSIONAL |
| revenue_threshold | Price applies if client's annual revenue $\geq$ this value | 0, 10000000, 10000001 |
| price | Actual unit price | 1000.00 |


 Repository Layer: Finding the Best Price

The PriceRepository uses a specific query to retrieve  applicable price rule for a given client and product.

Core Pricing Query:


@Query("""
    SELECT p FROM PriceEntity p
    WHERE p.productType = :productType
      AND p.clientType = :clientType
      AND p.revenueThreshold <= :revenue
    ORDER BY p.revenueThreshold DESC
""")
Optional<PriceEntity> findBestPrice(...);



 Service Layer: CartService Flow

The CartService encapsulates the main business logic.

1.  Extracts and validates client/cart data.
2.  Iterates over cart items using the Java Stream API for functional clarity.
3.  For each item, queries the PriceRepository for the best applicable price.
4.  Calculates (unit price × quantity) for the line total.
5.  Reduces (sums) all line totals to get the final cart amount.

-----

API Endpoints

 Calculate Cart Total

This is the primary endpoint for calculating the total cost.

| Method | Path | Description |
| :--- | :--- | :--- |
| POST | /api/cart/total | Calculates the total cost based on the request payload. |

 Request Structure (CartRequest)

The request body is a JSON object defined by the CartRequest DTO.

json
{
  "client": {
    "clientType": "PROFESSIONAL",
    "revenue": 11000000
  },
  "items": [
    {
      "productType": "HIGH_END_PHONE",
      "quantity": 2
    },
    {
      "productType": "LAPTOP",
      "quantity": 3
    }
  ]
}


 Header-Based Versioning

The API uses a custom header for versioning, separating it from the URL path. This allows for clean transition and management of future pricing strategies.

| Header | Example Value |
| :--- | :--- |
| X-API-VERSION | 1 |

-----

Exception Handling

Exception handling is centralized using a @ControllerAdvice global handler to ensure standard, predictable error responses.

| Exception Type | HTTP Code | Meaning |
| :--- | :--- | :--- |
| ResourceNotFoundException | 404 Not Found | Thrown if no pricing rule exists for the product/client/revenue combination. |
| MethodArgumentNotValidException | 400 Bad Request | Input data failed DTO validation (e.g., negative quantity). |
| Generic Exception | 500 Internal Server Error | Unexpected server error. |

-----

 Local Development & Inspection

 H2 Console: The service uses an in-memory H2 Database for local development and testing, initialized with pricing data from data.sql.

-----



