# ORM
We made a fairly capable ORM utilizing Generics and Reflection, which has most capabilities needed in a basic ORM.
The ORM specifies one interface for the data-class and one abstract class for the model which has the Database specific 
functionality. The main reason for this split is that this application has both server and client part written in Java, 
which allowed us to reuse some functionality in the data-class like validation in both parts.

The abstract class encapsulates the data-class which allows us to only interface with one object on the server where we 
fetch and save the data to the database. To use the ORM you would have to have the data-class implement the data-class 
interface which could have an intermediate abstract class where you would implement common behaviour. This allows a user 
of the ORM to use their own Validation library or choose to populate attributes in another way than using reflection.

The data-model only requires functionality for validating, and getting and setting of properties. While the 
implementation of the BaseRecord ORM abstract class only needs to describe it's data-class type and a table name. 
Optionally you would define relations in this class as well as the ability to change the primary key column which is `id`
by default.