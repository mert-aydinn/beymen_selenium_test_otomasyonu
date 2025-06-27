# Beymen Test Automation Project

This is a Selenium WebDriver test automation project for testing the Beymen.com website using Java, TestNG, and Page Object Pattern.

## Project Structure

The project follows Object-Oriented Programming (OOP) principles and implements the Page Object Pattern for better maintainability and reusability.

```
beymen-test-automation/
├── src/
│   ├── main/java/com/beymen/
│   │   ├── pages/          # Page Object classes
│   │   │   ├── BasePage.java
│   │   │   ├── HomePage.java
│   │   │   ├── SearchResultsPage.java
│   │   │   └── ProductDetailPage.java
│   │   └── utils/          # Utility classes
│   │       ├── DriverManager.java
│   │       └── ConfigReader.java
│   └── test/
│       ├── java/com/beymen/tests/
│       │   ├── BaseTest.java
│       │   └── BeymenSearchTest.java
│       └── resources/
│           ├── test-data.properties
│           └── log4j2.xml
├── pom.xml
├── testng.xml
└── README.md
```

## Technologies Used

- **Java 11** - Programming language
- **Selenium WebDriver 4.16.1** - Web automation framework
- **TestNG 7.8.0** - Testing framework
- **Maven** - Build and dependency management
- **WebDriverManager 5.6.2** - Automatic driver management
- **Log4j 2.21.1** - Logging framework
- **Page Object Pattern** - Design pattern for test automation

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome browser (latest version)

## Setup and Installation

1. Clone this repository:
```bash
git clone https://github.com/yourusername/beymen-test-automation.git
cd beymen-test-automation
```

2. Install dependencies:
```bash
mvn clean install
```

## Test Scenario

The test automates the following steps on www.beymen.com:

1. Open www.beymen.com website
2. Verify that the home page is opened
3. Enter "kazak" (sweater) in the search box
4. Clear the entered "kazak" text from the search box
5. Enter "gömlek" (shirt) in the search box
6. Press the Enter key
7. Select a random product from the search results

## Configuration

Test data and configuration can be modified in `src/test/resources/test-data.properties`:

```properties
# Parameterized search terms
search.term.first=kazak
search.term.second=gömlek

# Browser configuration
browser.name=chrome
```

## Running Tests

### Using Maven
```bash
mvn clean test
```

### Using TestNG directly
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run specific test class
```bash
mvn test -Dtest=BeymenSearchTest
```

## Test Reports

- Console logs are displayed during test execution
- Detailed logs are saved in `logs/beymen-test.log`
- TestNG reports are generated in `target/surefire-reports/`

## Design Patterns and OOP Principles

### Page Object Pattern
- Each web page has a corresponding Page Object class
- Page elements and interactions are encapsulated within page classes
- Improves code maintainability and reduces duplication

### OOP Principles Applied
- **Encapsulation**: Page elements and methods are private/protected
- **Inheritance**: All page classes extend BasePage, all tests extend BaseTest
- **Abstraction**: Common functionality abstracted in base classes
- **Polymorphism**: Page methods return appropriate page objects

### Singleton Pattern
- DriverManager uses ThreadLocal Singleton pattern for WebDriver management

## Project Features

- ✅ Parameterized test data using properties file
- ✅ Comprehensive logging with Log4j
- ✅ Automatic WebDriver management
- ✅ Explicit and implicit waits for stability
- ✅ Random product selection from search results
- ✅ Cookie handling
- ✅ Cross-browser support (Chrome, Firefox, Edge)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Author

[Your Name]

## Acknowledgments

- Beymen.com for providing the test website
- Selenium WebDriver community 