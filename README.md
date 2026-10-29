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

