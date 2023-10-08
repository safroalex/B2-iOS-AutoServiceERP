# B1-PostgreSQLAutoServiceERP : Car Service Database with PyQt5 and PostgreSQL GUI

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/safroalex/B1-PostgreSQLAutoServiceERP/blob/main/LICENSE)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/332eac3df6be4cdf8a9391f1aa6914b6)](https://app.codacy.com/gh/safroalex/B1-PostgreSQLAutoServiceERP/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

## Description
This repository serves as a portfolio project aimed at creating a database system for a car service center. The primary focus is on learning and implementing PostgreSQL for database operations and PyQt5 for crafting a Graphical User Interface (GUI).

## Features

### PostgreSQL
- **Table Creation**: Implemented essential tables to store information about cars, technicians, and services.
- **Relationships Between Tables**: Set up relationships to ensure data integrity.
- **Data Retrieval**: Queries for extracting necessary information are available.
- **Data Insertion**: Supports the insertion of new records into tables.
- **Data Deletion**: Functionality for deleting records is implemented.
- **Data Modification**: Capability to update existing records.
- **Views**: Complex queries are simplified into views for convenience.
- **Stored Procedures**: Utilized for automating routine tasks.
- **Triggers**: Automatic actions upon certain database events.
- **Cursors**: Employed for more complicated data selection operations.

### PyQt5 GUI
- **Directories**: Interfaces for inputting, modifying, and deleting data on technicians, cars, and services.
- **Work Assignment**: The main window features an interface allowing the operator to assign services to technicians.
- **Operator Metrics**: Ability to view overall service cost and technician performance metrics.

## Tools Used
- PostgreSQL
- Python
- PyQt5

## How to Use
1. Install PostgreSQL and set up the database using the SQL scripts found in the `sql` folder.
2. Install Python dependencies by running `pip install -r requirements.txt`.
3. Run `main.py` to launch the graphical interface.

## License

MIT License. For more information, see the [LICENSE](LICENSE) file.

---

For any questions or suggestions, feel free to reach out at `github::safroalex`.
