# CycleGuard Backend

The CycleGuard Backend repository is an IntelliJ Gradle project. It uses Java 17 (Amazon Corretto 17.0.12), along with Gradle 8.2 to install required libraries. It is an intermediate interface between the mobile app and the database, for data validation and authenticaion before accessing the database. It interfaces with AWS DynamoDB to store information.

For now, documentation is left as JSDoc-formatted comments on classes, methods, and instance variables. Eventually, we will use these comments to generate a documentation page with software such as Doxygen.

## Running the backend

The backend requires an internet connection to access the DynamoDB database, along with an AWS key to access the correct tables.

### DynamoDB access keys

Two environment variables `AWS_ACCESS_KEY` and `AWS_SECRET_ACCESS_KEY` must be used to interface with DynamoDB- ask Jason for these variables.
