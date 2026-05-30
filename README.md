# MediLabo

Hello and welcome to Anne Rutecki's MediLabo project for openclassrooms Java and Spring course.

To get this application running, clone this repository and open it in the code editor of your choice. Open the terminal and make sure you are in the root directly. Also ensure you have docker downloaded. Type "docker compose up" and viola!

Username: admin

Password: admin123

A few notes about how this application is "green" and environmentally friendly.
Combining microservice with a monorepo creates an efficient workflow. One reason is thayt they eliminate build waste. Since it stores all services in a single repository, it reduces the friction and resources that would traditionally be drained from several individual repositories. Monorepos consume less electricity, thus saving the planet and killing less trees! Using this method compared to monoliths is beneficial because monoliths can cause bottlenecks in speed, scalability, and maintenace. Monoliths require more computing resources than neessary and wasstes significant CPU and memory on idle or underused services since the entire application needs to be scaled. Monoliths also must load the entire framework, libraries, and runtime environment into memory, leading to an increased consumption of memory per container. Monorepositories execute less computaation energy and this are more suitable for green coding.

The data in the SQL database only has one table and is very small, this is great for data efficiency and is naturally normalized.
The data in the noSQL database was useful because they have a definite schema and does not join with any other information and is more performant for the use case of this project. Spring data was used to create the database and CRUD operations.
