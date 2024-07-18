# Supermarket-App
This project aims at the development of a comprehensive billing system app for a supermarket.

Functionalities and Working of each Classes:

  Database Connection:
•	The `DatabaseConnection` class provides JDBC connectivity that allows the application to be better integrated with the database, ensuring that the access is robust and consistent. 
•	It creates and controls an instance of a connection very much required for appropriate interaction with the database. The database scheme of the application consists of five pivotal tables, which are: `products`, `customers`, `sales`, `bill_items`, and `bill`. 
•	This setup thus allows the application to save and retrieve crucial data that pertains to the products, customers, sales transactions, and bill details. 


Main:
•	The Main class serves as the entry point for the supermarket billing system application.
•	It initializes essential components, such as database connections, user interfaces, and key application functionalities. 
•	The Main class provides the overarching structure and execution logic that ties together different parts of the system, ensuring smooth operation and effective management of supermarket operations from start to finish.


Login:
•	The Login class manages the authentication and access control for users interacting with the supermarket billing system. 
•	It provides functionality for validating user credentials.

Products:
•	The Product class contains all the information regarding products and their behaviors in the supermarket billing system. 
•	It represents one product and has properties like the ID of the Product, the Name, Quantity, and Price. This class provides functionalities to easily add, update products from the database for easy inventory management. 
•	Also supports functionalities like: get all products, obtain the information on a particular product given its ID, and compute the total number of sold units of each product. 

Product Frame:
•	The ProductFrame class serves as the graphical user interface (GUI) for managing products within the supermarket billing system. 
•	It provides functionalities for adding new products, updating existing ones, viewing all products, and displaying products sold in a specified month. 
•	This class integrates with the Product class backend to perform operations such as fetching product details, populating product data into a table format for user interaction, and handling user inputs for product management tasks.

Customer:
•	The `Customer` represents individual customers with properties such as customer ID, name, and contact details. 
•	The class provides methods for creating new customers, updating existing customer information, and retrieving customer data from the database. 

Customer GUI:
•	Within the supermarket billing system, the CustomerGUI class functions as the graphical user interface for managing customer data. 
•	With options for entering and displaying customer details such customer ID, name, and contact information, it offers a user-friendly interface.
•	The ability to add new customers, update current customer records, and view all customers that are recorded in the database is integrated into this class. 

Bill Items:

•	The BillItem class represents individual items within a customer's bill in the supermarket billing system.
•	It encapsulates details such as the bill ID, product ID, quantity purchased, and total cost for purchased item.
•	This class provides methods to create and manage bill items, including calculations for item totals based on quantity and product prices. 
•	It also includes functionality to save bill items to the database, ensuring accurate record-keeping of transactions.


Bill:

•	The Bill class manages and represents a customer's bill in the supermarket billing system. 
•	It encapsulates essential attributes such as the customer ID, total amount due, and the date of the transaction. This class provides methods to save bill details into the database, ensuring accurate recording of transactions. 
•	It supports operations to retrieve and display bill information, facilitating effective customer service and transaction management. 
•	By linking with BillItem instances, it enables comprehensive reporting and analysis of customer purchases over time.

Billing Frame:

•	The BillingFrame class serves as the graphical user interface (GUI) for managing supermarket billing operations. 
•	It provides functionalities for adding items to a customer's bill, fetching customer details if the customer is already been registered otherwise it directs to the customer GUI, and finalizing the bill transaction.
•	This class integrates with a database via JDBC to fetch product details, update product quantities upon purchase, and store transaction records. 
•	It dynamically updates the bill summary area with detailed itemized lists, calculates the total amount due, and displays it. 

