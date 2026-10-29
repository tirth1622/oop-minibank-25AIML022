# MiniBank - Object Oriented Programming (CEUE203)
## Comprehensive Semester Project: Part B (Practicals 1 to 14)

---

## 1. Project Overview

**MiniBank** is a production-grade, multi-tiered console banking application designed, engineered, and assembled across 14 laboratory sessions. Each practical adds a foundational object-oriented programming paradigm, evolving the application from a simple menu shell into a fault-tolerant, multithreaded, serialized, generic, and test-driven banking core.

---

## 2. Practical-by-Practical Mapping (Part B)

| Practical | Core OOP Concepts & Deliverables | Primary Files & Artifacts |
|---|---|---|
| **Practical 1** | Application skeleton, `BankInfo` Record, `MenuOption` Enum, `Scanner` input, `switch` expression | `minibank.model.BankInfo`, `minibank.model.MenuOption`, `minibank.MiniBank`, `minibank.demo.Lab01Demo` |
| **Practical 2** | Encapsulation, `Customer` with auto-generated ID, `Account` encapsulation (private fields, balance modified only via `deposit`/`withdraw`, no public balance setter) | `minibank.model.Customer`, `minibank.model.Account`, `minibank.demo.Lab02Demo` |
| **Practical 3** | `toString()` override, `equals()` & `hashCode()` contract on `accountNumber`, static nested `Address` class, `Cloneable` `clone()` on `Customer`, `instanceof` | `minibank.model.Account`, `minibank.model.Customer.Address`, `minibank.demo.Lab03Demo` |
| **Practical 4** | Boundary validation with compiled `Pattern` regex (mobile, email, PAN, IFSC), `TransactionType` enum, `Command` record, `CommandParser`, `StatementFormatter` via `StringBuilder` | `minibank.util.Validator`, `minibank.model.Command`, `minibank.util.CommandParser`, `minibank.util.StatementFormatter`, `minibank.demo.Lab04Demo` |
| **Practical 5** | Inheritance & Run-time Polymorphism: Abstract `Account` base class with abstract `interestRate()` & `canWithdraw()`, subclasses `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, pattern matching `instanceof` | `minibank.model.SavingsAccount`, `minibank.model.CurrentAccount`, `minibank.model.FixedDepositAccount`, `minibank.demo.Lab05Demo` |
| **Practical 6** | Interfaces & Packaging: `Transactable`, `InterestBearing` with default `yearlyInterest()`, `@FunctionalInterface` `WithdrawRule` (anonymous class + lambda), marker `Premium`, package structure, runnable JAR | `minibank.model.interfaces.*`, `minibank.demo.Lab06Demo`, `package_jar.bat` |
| **Practical 7** | Custom Metadata & Reflection: `@Id`, `@Positive`, `@MaxLength` annotations in `model.annotation`, `@Override`, `@FunctionalInterface`, `AnnotationValidator` via reflection | `minibank.model.annotation.*`, `minibank.util.AnnotationValidator`, `minibank.demo.Lab07Demo` |
| **Practical 8** | **Milestone 1 Checkpoint**: Checked Exception Hierarchy (`BankException`, `InsufficientFundsException` with `shortfall`, `AccountNotFoundException`, `InvalidAmountException`), `transfer()`, `BankingSession` (`AutoCloseable` + try-with-resources) | `minibank.exception.*`, `minibank.util.BankingSession`, `minibank.demo.Lab08Demo` |
| **Practical 9** | Concurrency: `AccountWorker` Runnable, `synchronized` deposit/withdraw, thread safety, race condition demonstration and elimination, thread lifecycle | `minibank.concurrency.AccountWorker`, `minibank.model.Account`, `minibank.demo.Lab09Demo` |
| **Practical 10** | Concurrency II: `TransactionProcessor` with `ExecutorService` (fixed pool of 4), `TransactionBuffer` Producer-Consumer (`wait()`/`notifyAll()`), deadlock reproduction and fix (lock smaller `accountNumber` first) | `minibank.concurrency.TransactionProcessor`, `minibank.concurrency.TransactionBuffer`, `minibank.concurrency.DeadlockDemo`, `minibank.demo.Lab10Demo` |
| **Practical 11** | Persistence & Java NIO: `Serializable` model with `serialVersionUID`, `StatePersister` (`ObjectOutputStream`/`ObjectInputStream`), `TransactionLog` (`Files.write` CREATE/APPEND), `ReportGenerator` (`Files.newDirectoryStream`, `BufferedReader`, `readAttributes`) | `minibank.persistence.StatePersister`, `minibank.persistence.TransactionLog`, `minibank.persistence.ReportGenerator`, `minibank.demo.Lab11Demo` |
| **Practical 12** | **Milestone 2 Checkpoint**: Collections Framework: `AccountStore` (`HashMap<String, Account>`, `ArrayList<String>` history, `sortedByBalance()` with `Comparator`), `Comparable<Account>`, `ConcurrentHashMap` | `minibank.service.AccountStore`, `minibank.model.Account`, `minibank.demo.Lab12Demo` |
| **Practical 13** | Generics & Meta-Data Access: Generic `Repository<T>`, key extraction by reflection on `@Id`, `@SafeVarargs saveAll(T... items)`, bounded generic method `<N extends Number> double average(N[] values)`, reuse for `Customer` | `minibank.repository.Repository`, `minibank.demo.Lab13Demo` |
| **Practical 14** | **Milestone 3 (Final Capstone)**: `BankService` wiring repository, validation, and exceptions; console menu integration; startup state load & exit persistence; batch thread processing; EOD report; automated JUnit test suite; `@Audit` reflective logging | `minibank.service.BankService`, `minibank.MiniBank`, `minibank.test.MiniBankTestSuite`, `minibank.demo.Lab14Demo` |

---

## 3. Directory Layout

```
D:\MiniBank\
├── bin/                             # Compiled Java class binaries
├── data/                            # Persistent binary storage (accounts.dat)
├── logs/                            # Append-only transaction audit logs & EOD reports
├── src/
│   └── minibank/
│       ├── MiniBank.java            # Main Interactive Application (Practical 1 & 14)
│       ├── model/
│       │   ├── BankInfo.java        # Record (Practical 1)
│       │   ├── MenuOption.java      # Enum (Practical 1)
│       │   ├── TransactionType.java # Enum (Practical 4)
│       │   ├── Command.java         # Record (Practical 4)
│       │   ├── Customer.java        # Entity & Static Nested Address (Practical 2, 3, 11)
│       │   ├── Account.java         # Abstract Base Account (Practical 2, 3, 5, 8, 9, 11, 12)
│       │   ├── SavingsAccount.java  # Subclass (Practical 5)
│       │   ├── CurrentAccount.java  # Subclass (Practical 5)
│       │   └── FixedDepositAccount.java # Subclass (Practical 5)
│       ├── model/annotation/
│       │   ├── Id.java              # Marker annotation for PK (Practical 7, 13)
│       │   ├── Positive.java        # Field validator > 0 (Practical 7)
│       │   ├── MaxLength.java       # String length limit (Practical 7)
│       │   └── Audit.java           # Reflective service audit log (Practical 14)
│       ├── model/interfaces/
│       │   ├── Transactable.java    # Core transaction interface (Practical 6)
│       │   ├── InterestBearing.java # Default method yearlyInterest() (Practical 6)
│       │   ├── WithdrawRule.java    # Functional interface (Practical 6)
│       │   └── Premium.java         # Marker interface (Practical 6)
│       ├── exception/
│       │   ├── BankException.java   # Base checked exception (Practical 8)
│       │   ├── InsufficientFundsException.java # Exception with shortfall (Practical 8)
│       │   ├── AccountNotFoundException.java   # Exception (Practical 8)
│       │   ├── InvalidAmountException.java     # Exception (Practical 8)
│       │   └── DailyLimitExceededException.java # Supplementary (Practical 8)
│       ├── util/
│       │   ├── Validator.java       # Compiled Pattern regexes (Practical 4)
│       │   ├── CommandParser.java   # Line tokeniser (Practical 4)
│       │   ├── StatementFormatter.java # StringBuilder statement (Practical 4)
│       │   ├── AnnotationValidator.java # Reflection validator (Practical 7)
│       │   ├── BankingSession.java  # AutoCloseable resource (Practical 8)
│       │   └── AuditLogger.java     # Reflective method logger (Practical 14)
│       ├── repository/
│       │   └── Repository.java      # Generic type-safe Repository<T> (Practical 13)
│       ├── concurrency/
│       │   ├── AccountWorker.java   # Runnable worker (Practical 9)
│       │   ├── TransactionProcessor.java # Fixed thread pool (Practical 10, 12)
│       │   ├── TransactionBuffer.java    # Wait/Notify buffer (Practical 10)
│       │   └── DeadlockDemo.java         # Deadlock simulation & fix (Practical 10)
│       ├── persistence/
│       │   ├── StatePersister.java  # Object serialization save/load (Practical 11)
│       │   ├── TransactionLog.java  # Append-only NIO logger (Practical 11)
│       │   └── ReportGenerator.java # DirectoryStream EOD aggregator (Practical 11)
│       ├── service/
│       │   ├── AccountStore.java    # Collections store & Comparator (Practical 12)
│       │   └── BankService.java     # Capstone facade service (Practical 14)
│       ├── demo/                    # Individual Lab Part B Demonstrations
│       │   ├── Lab01Demo.java ... Lab14Demo.java
│       │   └── AllLabsRunner.java   # Interactive menu to run any lab demo
│       └── test/
│           └── MiniBankTestSuite.java # Automated verification suite
├── compile.bat                      # Builds all Java sources
├── run.bat                          # Launches interactive console app
├── run_demos.bat                    # Launches lab demo selector
├── test.bat                         # Runs automated test suite
├── package_jar.bat                  # Assembles runnable JAR minibank.jar
├── run_jar.bat                      # Runs minibank.jar via java -jar
└── README.md
```

---

## 4. How to Compile and Execute

### A. Quick Start via Windows Batch Files:
1. **Compile the Entire Project:**
   Double click `compile.bat` or run:
   ```cmd
   compile.bat
   ```
2. **Run Interactive MiniBank Application:**
   ```cmd
   run.bat
   ```
3. **Run Practical Part B Individual Demonstrator (Labs 1 to 14):**
   ```cmd
   run_demos.bat
   ```
4. **Run Automated Test Suite:**
   ```cmd
   test.bat
   ```
5. **Package into Standalone Runnable JAR:**
   ```cmd
   package_jar.bat
   ```
6. **Launch Runnable JAR:**
   ```cmd
   run_jar.bat
   ```

### B. Command-Line Instructions (Manual):
```cmd
cd /d D:\MiniBank

# 1. Compile all Java sources
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

# 2. Run Main Application
java -cp bin minibank.MiniBank

# 3. Run Practical Demonstrations
java -cp bin minibank.demo.AllLabsRunner

# 4. Run Automated Test Suite
java -cp bin minibank.test.MiniBankTestSuite

# 5. Package and run JAR
jar cfe minibank.jar minibank.MiniBank -C bin .
java -jar minibank.jar
```

---

## 5. Key Questions / Analysis / Viva Solutions

### Practical 1:
- **Q1: Role of JVM and what javac produces:**
  - `javac` compiles `.java` source code into platform-independent `.class` bytecode.
  - The JVM (Java Virtual Machine) loads bytecode, verifies it, and executes it via Just-In-Time (JIT) compilation and interpretation onto native hardware.
- **Q2: Switch expression vs switch statement:**
  - Switch statements execute branches for side-effects and require explicit `break` statements (prone to fallthrough bugs).
  - Switch expressions produce a result value, use arrow syntax `->` without fallthrough, and enforce compiler exhaustiveness.
- **Q3: Enum vs Record:**
  - An `enum` represents a fixed, closed set of known constants (such as menu options or transaction types).
  - A `record` is a compact, immutable data-carrier with compiler-generated constructor, getters, `equals()`, `hashCode()`, and `toString()`.

### Practical 2:
- **Q1: Encapsulation and private fields:**
  - Encapsulation hides internal object state from direct external manipulation. Making fields private and exposing controlled methods (`deposit`, `withdraw`) ensures business invariants are always enforced.
- **Q2: Why accountNumber is final:**
  - `accountNumber` defines identity; once assigned, an account number must never mutate.
- **Q3: Static vs instance field in ID generation:**
  - An instance field belongs to a single object. A static field belongs to the class and is shared across all instances, allowing counter increments across creations.

### Practical 3:
- **Q1: Why equals() and hashCode() must be overridden together:**
  - The hashCode contract mandates that if two objects are equal according to `equals()`, they MUST produce the exact same `hashCode()`. Failing this breaks hash-based collections (`HashMap`, `HashSet`).
- **Q2: Default Object.toString() vs overridden:**
  - Default returns `ClassName@hexHashCode`. Overriding provides meaningful human-readable diagnostic text.
- **Q3: Static nested class vs inner class:**
  - A static nested class does not hold an implicit reference to an enclosing instance, saving memory and avoiding memory leaks. An inner class is bound to an enclosing instance.

### Practical 4:
- **Q1: String vs StringBuilder vs StringBuffer:**
  - `String` is immutable. `StringBuilder` is mutable and fast (non-synchronized), ideal for single-threaded string construction. `StringBuffer` is synchronized (thread-safe but slower).
- **Q2: Regex [6-9][0-9]{9} and anchors:**
  - Matches a 10-digit number starting with 6, 7, 8, or 9. Anchors (`^` for start, `$` for end) ensure the ENTIRE input matches rather than a partial substring.

### Practical 5:
- **Q1: Abstract class vs concrete class:**
  - An abstract class cannot be instantiated directly and serves as a blueprint with abstract and implemented methods. Concrete classes can be instantiated.
- **Q2: Dynamic Method Dispatch (Run-time Polymorphism):**
  - Java resolves method calls at runtime based on the actual object instance type rather than the reference type.

### Practical 6:
- **Q1: Interface vs Abstract Class:**
  - Interfaces define pure capability contracts and support multiple inheritance. Abstract classes can maintain state (instance variables) and constructors.
- **Q2: Functional Interface and Lambdas:**
  - An interface with exactly one abstract method is a functional interface; lambdas provide concise inline implementations.
- **Q3: Default methods:**
  - Allow adding new capabilities to existing interfaces without breaking existing implementing classes.

### Practical 7:
- **Q1: Meta-annotations @Retention and @Target:**
  - `@Retention(RUNTIME)` ensures annotations remain available in bytecode for runtime reflection. `@Target` specifies where the annotation can be placed (e.g. `FIELD`, `METHOD`).
- **Q2: Reflection and getDeclaredFields():**
  - Reflection inspects classes and members at runtime. `getDeclaredFields()` retrieves all declared fields regardless of access modifiers (`private`, `protected`, `public`).

### Practical 8:
- **Q1: Checked vs Unchecked Exceptions:**
  - Checked exceptions (`extends Exception`) must be declared or caught at compile-time (used for recoverable domain errors). Unchecked exceptions (`extends RuntimeException`) represent programming bugs.
- **Q2: Purpose of finally block:**
  - Always executes after `try`/`catch`, regardless of whether an exception occurred, ensuring reliable cleanup.
- **Q3: Try-with-resources:**
  - Automatically calls `close()` on any resource implementing `AutoCloseable` even if exceptions are thrown.

### Practical 9 & 10:
- **Q1: Race Condition:**
  - Occurs when multiple threads concurrently read and modify shared mutable state without proper synchronization, causing lost updates.
- **Q2: Thread Pool benefits:**
  - Reuses existing threads, controls resource consumption, eliminates thread creation/destruction overhead.
- **Q3: Deadlock cause and lock-ordering prevention:**
  - Deadlock happens when threads hold locks and wait cyclically for locks held by others. Consistent global lock ordering (e.g., locking smaller `accountNumber` first) breaks the circular wait condition.

### Practical 11:
- **Q1: Serialization and serialVersionUID:**
  - Serialization converts object state to byte streams. `serialVersionUID` verifies sender and receiver maintain binary compatibility during deserialization.
- **Q2: NIO Path/Files vs old File:**
  - NIO provides non-blocking, scalable, symbolic link-aware file operations with clear exception reporting and streaming directory iteration (`DirectoryStream`).

### Practical 12:
- **Q1: List vs Set vs Map:**
  - `List`: Ordered collection allowing duplicates.
  - `Set`: Collection of unique elements.
  - `Map`: Key-value pairs with unique keys.
- **Q2: Comparable vs Comparator:**
  - `Comparable`: Defines natural ordering implemented inside the class (`compareTo`).
  - `Comparator`: External strategy defining custom ordering (`compare`).

### Practical 13:
- **Q1: Generics vs Object:**
  - Generics enforce compile-time type safety and eliminate runtime `ClassCastException` and manual casting.
- **Q2: Bounded type parameter `<N extends Number>`:**
  - Restricts type arguments to subclasses of `Number`, allowing numeric method calls (`doubleValue()`).
- **Q3: Type Erasure:**
  - The compiler strips generic type information during compilation to ensure backwards compatibility with older JVMs.

### Practical 14:
- **Q1: Layered Architecture:**
  - Separates concerns into Model (domain entities), Repository (persistence), Service (business logic), and UI/Presentation (console interaction), enabling testability and modularity.
- **Q2: JUnit Exception Assertion:**
  - `assertThrows(ExpectedException.class, () -> methodCall())` verifies expected exceptions are triggered.
