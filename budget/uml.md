A: Aggregate
E: Entity
V: VO
I: Interface
ENUM: Java Enum
TIME: Java Time API

<공통 식별자>

TransactionId :I
CategoryId :I
BudgetId :I

<도메인 규칙이 존재해야 하는 객체>

Transaction :A, E
Category :E
Categories :A, collection
Budget :A, E

Won :V
Memo :V
Tag :V
Tags :V collection
BudgetVersion :V

TransactionType :ENUM

<객체 관계>

Transaction

* TransactionId
* TransactionType
* CategoryId
* Won
* Memo
* Tags
* Instant occurredAt

Tags

* Collection<Tag>

Categories

* Collection<Category>

Category

* CategoryId
* CategoryName
* Instant createdAt

Budget

* BudgetId(Year, Month, int version)
* Year
* Month
* int version
* Won
* Instant createdAt


```mermaid
classDiagram
    direction LR

    class Transaction {
        <<Aggregate Root>>
        -TransactionId id
        -TransactionType type
        -CategoryId categoryId
        -Won amount
        -Memo memo
        -Tags tags
        -Instant occurredAt
    }

    class Category {
        <<Aggregate Root>>
        -CategoryId id
        -String name
        -Instant createdAt
    }

    class Categories {
        <<Domain Collection>>
        -Collection~Category~ values
        +contains(CategoryId) boolean
        +find(CategoryId) Category
        +excluding(CategoryId) Categories
    }

    class Budget {
        <<Aggregate Root>>
        -BudgetId id
        -BudgetPeriod period
        -int version
        -Won amount
        -Instant createdAt
        +includes(Instant, ZoneId) boolean
        +isExceededBy(Won) boolean
        +usageRateOf(Won) decimal
    }

    class BudgetPeriod {
        <<Value Object>>
        -Year year
        -Month month
        +startsAt(ZoneId) Instant
        +endsAt(ZoneId) Instant
        +includes(Instant, ZoneId) boolean
        +next() BudgetPeriod
        +previous() BudgetPeriod
    }

    class TransactionId {
        <<interface>>
    }

    class CategoryId {
        <<interface>>
    }

    class BudgetId {
        <<interface>>
    }

    class Won {
        <<Value Object>>
        -long value
        +plus(Won) Won
        +minus(Won) Won
    }

    class Memo {
        <<Value Object>>
        -String value
    }

    class Tag {
        <<Value Object>>
        -String value
    }

    class Tags {
        <<Value Object Collection>>
        -Collection~Tag~ values
        +contains(Tag) boolean
    }

    class TransactionType {
        <<enumeration>>
        INCOME
        EXPENSE
    }

    Transaction --> TransactionId : identified by
    Transaction --> TransactionType : classified as
    Transaction --> CategoryId : references
    Transaction *-- Won : amount
    Transaction *-- Memo : memo
    Transaction *-- Tags : tags

    Tags *-- "0..*" Tag : contains

    Category --> CategoryId : identified by
    Categories o-- "0..*" Category : collects

    Budget --> BudgetId : identified by
    Budget *-- BudgetPeriod : applies to
    Budget *-- Won : amount
```