################# Cash Management #################

What it does ? 
----------------------
1. Maintains a Cash Account and its Credit/Debit transactions.
2. An in memory database maintains the data for Accounts and transactions. 
3. On application startup, the database tables and 3 basic accounts are created in the database tables.
4. Each cash account has a credit limit (borrow capacity), threshold (minimum balance).
5. A user can, 
   - View account details, given an account ID. (POST /cash-management/accounts)
   - credit the Account (POST /cash-management/credit)
   - Debit the account, provided the account has sufficient funds (checks against current balance, credit limit and threshold) (POST /cash-management/debit)
   - View all the transactions (GET /cash-management/transactions)
   - View transactions for a given account Id (POST /cash-management/transactions)
 
 
 Assumptions
 ---------------
 1. values hardcoded - Credit Limit, Threshold, Currency.
 2. The credit/debit transaction is considered as one leg of the main transaction. 
 3. Ideally, the instruction for this leg of the transaction could be received on a messaging platform (IBM MQ or Apache Kafka).
 4. Credit/Debit to one given cash account.