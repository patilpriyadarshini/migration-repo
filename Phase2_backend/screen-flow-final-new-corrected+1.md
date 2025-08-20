# Application Screen Flow Documentation

## Summary
- **Total screens analyzed**: 17
- **Application purpose**: Credit card management system for financial institutions providing complete lifecycle management of credit card accounts, transactions, and user administration
- **Main user workflows**: 
  - **Authentication Flow**: Login → User Type Determination → Menu Selection
  - **Account Management**: Account View/Update, Card Listing/Selection/Update
  - **Credit card management (list, view, update)
  - **Transaction Processing**: Transaction List/View/Add, Bill Payment
  - **Reporting**: Monthly/Yearly/Custom Date Range Reports
  - **User Administration**: User List/Add/Update/Delete (Admin Only)

## Screen Inventory
| Screen ID | BMS Map  | Program | Transaction | Purpose |
|-----------|----------|---------|-------------|---------|
| SCREEN-001 | COSGN0A | COSGN00C | CC00 | User Authentication |
| SCREEN-002 | COMEN1A | COMEN01C | CM00 | Main Menu (Regular Users) |
| SCREEN-003 | COADM1A | COADM01C | CA00 | Admin Menu (Admin Users) |
| SCREEN-004 | CACTVWA | COACTVWC | CAVW | Account View |
| SCREEN-005 | CACTUPA | COACTUPC | CAUP | Account Update |
| SCREEN-006 | CCRDLIA | COCRDLIC | CCLI | Card Listing |
| SCREEN-007 | CCRDSLA | COCRDSLC | CCDL | Card Detail View |
| SCREEN-008 | COCRDUP | COCRDUPC | CCUP | Card Update |
| SCREEN-009 | COTRN0A | COTRN00C | CT00 | Transaction List |
| SCREEN-010 | COTRN1A | COTRN01C | CT01 | Transaction View |
| SCREEN-011 | COTRN2A | COTRN02C | CT02 | Transaction Add |
| SCREEN-012 | COBIL0A | COBIL00C | CB00 | Bill Payment |
| SCREEN-013 | CORPT0A | CORPT00C | CR00 | Transaction Reports |
| SCREEN-014 | COUSR0A | COUSR00C | CU00 | User List (Admin) |
| SCREEN-015 | COUSR1A | COUSR01C | CU01 | Add User (Admin) |
| SCREEN-016 | COUSR2A | COUSR02C | CU02 | Update User (Admin) |
| SCREEN-017 | COUSR3A | COUSR03C | CU03 | Delete User (Admin) |

## Detailed Screen Analysis

### SCREEN-001: User Authentication (COSGN00)
**Screen Purpose**: Application entry point for user login and authentication

**User Interaction Flow**:
1. User accesses system → System displays login screen with CardDemo branding
2. User enters User ID (8 characters) → System validates field is not empty
3. User enters Password (8 characters, hidden) → System validates field is not empty
4. User presses Enter → System authenticates against USRSEC file
5. System validates credentials → Routes to appropriate menu based on user type
6. User presses F3 → System displays thank you message and exits

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| USERID | Input | User entry | User ID for authentication (8 characters) |
| PASSWD | Input | User entry | Password for authentication (8 characters, hidden) |
| TRNNAME | Output | System | Current transaction ID (CC00) |
| PGMNAME | Output | System | Current program name (COSGN00C) |
| CURDATE | Output | System | Current date (MM/DD/YY format) |
| CURTIME | Output | System | Current time (HH:MM:SS format) |
| APPLID | Output | System | Application ID from CICS |
| SYSID | Output | System | System ID from CICS |
| ERRMSG | Output | System | Error messages for validation failures |

**Navigation Conditions**:
- COSGN00 → Valid Admin User → COADM01 (Admin Menu)
- COSGN00 → Valid Regular User → COMEN01 (Main Menu)
- COSGN00 → Invalid Credentials → COSGN00 (Error message displayed)
- COSGN00 → F3 → Exit Application
- COSGN00 → Empty User ID → COSGN00 (Error: "Please enter User ID ...")
- COSGN00 → Empty Password → COSGN00 (Error: "Please enter Password ...")

### SCREEN-002: Main Menu (COMEN01)
**Screen Purpose**: Primary navigation hub for regular users to access all available functions

**User Interaction Flow**:
1. User logs in successfully → System displays main menu with available options
2. User views numbered menu options (1-10) → System shows option descriptions
3. User enters option number → System validates selection is within range
4. User presses Enter → System transfers to selected function
5. User presses F3 → System returns to login screen

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| OPTION | Input | User entry | Menu selection (1-10) |
| OPTN001-012 | Output | COMEN02Y copybook | Dynamic menu option text |
| TRNNAME | Output | System | Current transaction ID (CM00) |
| PGMNAME | Output | System | Current program name (COMEN01C) |
| CURDATE | Output | System | Current date |
| CURTIME | Output | System | Current time |
| ERRMSG | Output | System | Error messages for invalid selections |

**Navigation Conditions**:
- COMEN01 → Option 1 → COACTVW (Account View)
- COMEN01 → Option 2 → COACTUP (Account Update)
- COMEN01 → Option 3 → COCRDLI (Credit Card List)
- COMEN01 → Option 4 → COCRDSL (Credit Card View)
- COMEN01 → Option 5 → COCRDUP (Credit Card Update)
- COMEN01 → Option 6 → COTRN00 (Transaction List)
- COMEN01 → Option 7 → COTRN01 (Transaction View)
- COMEN01 → Option 8 → COTRN02 (Transaction Add)
- COMEN01 → Option 9 → CORPT00 (Transaction Reports)
- COMEN01 → Option 10 → COBIL00 (Bill Payment)
- COMEN01 → F3 → COSGN00 (Login Screen)
- COMEN01 → Invalid Option → COMEN01 (Error: "Please enter a valid option number...")

### SCREEN-003: Admin Menu (COADM01)
**Screen Purpose**: Administrative navigation hub for admin users with additional user management functions

**User Interaction Flow**:
1. Admin user logs in → System displays admin menu with all options including user management
2. User views numbered admin options → System shows administrative function descriptions
3. User enters option number → System validates admin privileges and selection
4. User presses Enter → System transfers to selected administrative function
5. User presses F3 → System returns to login screen

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| OPTION | Input | User entry | Admin menu selection |
| OPTN001-012 | Output | COADM02Y copybook | Dynamic admin menu option text |
| TRNNAME | Output | System | Current transaction ID (CA00) |
| PGMNAME | Output | System | Current program name (COADM01C) |
| CURDATE | Output | System | Current date |
| CURTIME | Output | System | Current time |
| ERRMSG | Output | System | Error messages for invalid selections |

**Navigation Conditions**:
- COADM01 → Option 1 → COUSR00C (List Users)
- COADM01 → Option 2 → COUSR01C (Add User)
- COADM01 → Option 3 → COUSR02C (Update User)
- COADM01 → Option 4 → COUSR03C (Delete User)
- COADM01 → F3 → COSGN00 (Login Screen)
- COADM01 → Invalid Option → COADM01 (Error: "Please enter a valid option number...")

### SCREEN-004: Account View (COACTVW)
**Screen Purpose**: Display comprehensive account and customer information in read-only format

**User Interaction Flow**:
1. User enters from menu → System displays account search screen
2. User enters Account Number (11 digits) → System validates format and existence in CXACAIX file - cross reference file.
3. User presses Enter → System retrieves account data from ACCTDAT and CUSTDAT file
4. System displays account details → User reviews account and customer information
5. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACCTSID | Input | User entry | Account number for lookup (11 digits) |
| ACSTTUS | Output | ACCTDAT file | Account active status (Y/N) |
| ADTOPEN | Output | ACCTDAT file | Account open date |
| AEXPDT | Output | ACCTDAT file | Account expiration date |
| AREISDT | Output | ACCTDAT file | Account reissue date |
| ACRDLIM | Output | ACCTDAT file | Credit limit amount |
| ACSHLIM | Output | ACCTDAT file | Cash credit limit amount |
| ACURBAL | Output | ACCTDAT file | Current account balance |
| ACRCYDB | Output | ACCTDAT file | Current cycle debit |
| ACSTNUM | Output | CUSTDAT file | Customer ID |
| ACSTSSN | Output | CUSTDAT file | Customer SSN |
| ACSTDOB | Output | CUSTDAT file | Customer date of birth |
| ACSTFCO | Output | CUSTDAT file | Customer FICO score |
| ACSFNAM | Output | CUSTDAT file | Customer first name |
| ACSMNAM | Output | CUSTDAT file | Customer middle name |
| ACSLNAM | Output | CUSTDAT file | Customer last name |
| ACSADL1 | Output | CUSTDAT file | Customer address line 1 |
| ACSADL2 | Output | CUSTDAT file | Customer address line 2 |
| ACSCITY | Output | CUSTDAT file | Customer city |
| ACSSTTE | Output | CUSTDAT file | Customer state |
| ACSZIPC | Output | CUSTDAT file | Customer ZIP code |
| ACSCTRY | Output | CUSTDAT file | Customer country |
| ACSPHN1 | Output | CUSTDAT file | Customer phone 1 |
| ACSPHN2 | Output | CUSTDAT file | Customer phone 2 |
| ACSGOVT | Output | CUSTDAT file | Government issued ID reference |
| ACSEFTC | Output | CUSTDAT file | EFT account ID |
| ACSPFLG | Output | CUSTDAT file | Primary card holder flag (Y/N) |

**Navigation Conditions**:
- COACTVW → Valid Account → COACTVW (Display account details)
- COACTVW → Invalid Account → COACTVW (Error: "Account ID NOT found")
- COACTVW → F3 → Previous Menu (COMEN01)
- COACTVW → Empty Account ID → COACTVW (Error: "Account ID can NOT be empty")

### SCREEN-005: Account Update (COACTUP)
**Screen Purpose**: Modify account and customer information with field-level editing capabilities

**User Interaction Flow**:
1. User enters from menu → System displays account update screen
2. User enters Account Number → System retrieves and displays current data from in CXACAIX file - cross reference file, ACCTDAT and CUSTDAT file
3. User modifies editable fields → System tracks changes
4. User presses Enter → System validates changes and updates ACCTDAT and CUSTDAT files
5. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACCTSID | Input | User entry | Account number for update |
| ACSTTUS | Input/Output | ACCTDAT file | Account active status (editable) |
| OPNYEAR/OPNMON/OPNDAY | Input/Output | ACCTDAT file | Account open date components (editable) |
| EXPYEAR/EXPMON/EXPDAY | Input/Output | ACCTDAT file | Account expiration date components (editable) |
| RISYEAR/RISMON/RISDAY | Input/Output | ACCTDAT file | Account reissue date components (editable) |
| ACRDLIM | Input/Output | ACCTDAT file | Credit limit amount (editable) |
| ACSHLIM | Input/Output | ACCTDAT file | Cash credit limit amount (editable) |
| ACURBAL | Input/Output | ACCTDAT file | Current account balance (editable) |
| ACRCYCR | Input/Output | ACCTDAT file | Current cycle credit (editable) |
| ACRCYDB | Input/Output | ACCTDAT file | Current cycle debit (editable) |
| AADDGRP | Input/Output | ACCTDAT file | Account group ID (editable) |
| ACSTNUM | Input/Output | CUSTDAT file | Customer ID (editable) |
| ACTSSN1/ACTSSN2/ACTSSN3 | Input/Output | CUSTDAT file | Customer SSN components (editable) |
| DOBYEAR/DOBMON/DOBDAY | Input/Output | CUSTDAT file | Customer date of birth components (editable) |
| ACSTFCO | Input/Output | CUSTDAT file | Customer FICO score (editable) |
| ACSFNAM | Input/Output | CUSTDAT file | Customer first name (editable) |
| ACSMNAM | Input/Output | CUSTDAT file | Customer middle name (editable) |
| ACSLNAM | Input/Output | CUSTDAT file | Customer last name (editable) |
| ACSADL1 | Input/Output | CUSTDAT file | Customer address line 1 (editable) |
| ACSADL2 | Input/Output | CUSTDAT file | Customer address line 2 (editable) |
| ACSCITY | Input/Output | CUSTDAT file | Customer city (editable) |
| ACSSTTE | Input/Output | CUSTDAT file | Customer state (editable) |
| ACSZIPC | Input/Output | CUSTDAT file | Customer ZIP code (editable) |
| ACSCTRY | Input/Output | CUSTDAT file | Customer country (editable) |
| ACSPH1A/ACSPH1B/ACSPH1C | Input/Output | CUSTDAT file | Customer phone 1 components (editable) |
| ACSPH2A/ACSPH2B/ACSPH2C | Input/Output | CUSTDAT file | Customer phone 2 components (editable) |
| ACSGOVT | Input/Output | CUSTDAT file | Government issued ID reference (editable) |
| ACSEFTC | Input/Output | CUSTDAT file | EFT account ID (editable) |
| ACSPFLG | Input/Output | CUSTDAT file | Primary card holder flag (editable) |

**Navigation Conditions**:
- COACTUP → Valid Account → COACTUP (Display editable account details)
- COACTUP → Successful Update → COACTUP (Confirmation message)
- COACTUP → Update Error → COACTUP (Error message displayed)
- COACTUP → F3 → Previous Menu
- COACTUP → Invalid Account → COACTUP (Error: "Account ID NOT found")

### SCREEN-006: Card Listing (COCRDLI)
**Screen Purpose**: Display paginated list of credit cards with search and selection capabilities

**User Interaction Flow**:
1. User enters from menu → System displays card search screen with first 7 cards
2. User enters Account Number or Card Number (optional) → System filters results
3. User presses Enter → System displays up to 7 cards per page
4. User selects card by entering S in the select option → System navigates to card details
5. User selects card by entering U in the select option → system navigates to card update
6. User uses F7/F8 for pagination → System displays previous/next page
7. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACCTSID | Input | User entry | Account number filter (optional) |
| CARDSID | Input | User entry | Card number filter (optional) |
| PAGENO | Output | System | Current page number |
| CRDSEL1-7 | Input | User entry | Card selection indicators |
| ACCTNO1-7 | Output | CARDDAT file | Account numbers for displayed cards |
| CRDNUM1-7 | Output | CARDDAT file | Card numbers for displayed cards |
| CRDSTS1-7 | Output | CARDDAT file | Card status (Active Y/N) |

**Navigation Conditions**:
- COCRDLI → Card Selection S → COCRDSL (Card Detail View)
- COCRDLI → Card Selection U → COCRDUP (Card Update View)
- COCRDLI → F7 → COCRDLI (Previous page)
- COCRDLI → F8 → COCRDLI (Next page)
- COCRDLI → F3 → Previous Menu
- COCRDLI → Enter with filters → COCRDLI (Filtered results)
- COCRDLI → No cards found → COCRDLI (No results message)

### SCREEN-007: Card Detail View (COCRDSL)
**Screen Purpose**: Display detailed information for a specific credit card

**User Interaction Flow**:
1. User enters from card listing or menu → System displays card search screen
2. User enters Account Number and Card Number → System validates card existence
3. User presses Enter → System retrieves card data from CARDDAT file
4. System displays card details → User reviews card information
5. User presses F3 → System returns to previous screen

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACCTSID | Input | User entry | Account number for card lookup |
| CARDSID | Input | User entry | Card number for lookup |
| CRDNAME | Output | CARDDAT file | Name on card |
| CRDSTCD | Output | CARDDAT file | Card active status (Y/N) |
| EXPMON | Output | CARDDAT file | Card expiration month |
| EXPYEAR | Output | CARDDAT file | Card expiration year |

**Navigation Conditions**:
- COCRDSL → Valid Card → COCRDSL (Display card details)
- COCRDSL → Invalid Card → COCRDSL (Error: "Card not found")
- COCRDSL → F3 → Previous Screen (COCRDLI or menu)
- COCRDSL → Empty fields → COCRDSL (Error: "Required fields missing")

### SCREEN-008: Card Update (COCRDUP)
**Screen Purpose**: Modify credit card information with validation and update capabilities

**User Interaction Flow**:
1. User enters from menu → System displays card update screen
2. User enters Account Number and Card Number → System retrieves current card data
3. User modifies editable card fields → System tracks changes
4. User presses F5 → System validates and updates CARDDAT file
5. User presses F3 → System returns to previous menu
6. User presses F12 → System clears the screen and awaits new user inputs

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACCTSID | Input | User entry | Account number for card update |
| CARDSID | Input | User entry | Card number for update |
| CRDNAME | Input/Output | CARDDAT file | Name on card (editable) |
| CRDSTCD | Input/Output | CARDDAT file | Card active status (editable) |
| EXPMON | Input/Output | CARDDAT file | Card expiration month (editable) |
| EXPYEAR | Input/Output | CARDDAT file | Card expiration year (editable) |

**Navigation Conditions**:
- COCRDUP → Valid Card → COCRDUP (Display editable card details)
- COCRDUP → F5 Successful Update → COCRDUP (Confirmation message)
- COCRDUP → Update Error → COCRDUP (Error message displayed)
- COCRDUP → F3 → Previous Menu
- COCRDUP → F12 → COCRDUP (clears screen)
- COCRDUP → Invalid Card → COCRDUP (Error: "Card not found")

### SCREEN-009: Transaction List (COTRN00)
**Screen Purpose**: Display paginated list of transactions with search and selection capabilities

**User Interaction Flow**:
1. User enters from menu → System displays transaction search screen with first 10 transactions
2. User enters Transaction ID (optional) → System filters results
3. User presses Enter → System displays up to 10 transactions per page
4. User selects transaction by entering selection S → System navigates to transaction details
5. User uses F7/F8 for pagination → System displays previous/next page
6. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| TRNIDIN | Input | User entry | Transaction ID filter (optional) |
| PAGENUM | Output | System | Current page number |
| SEL0001-10 | Input | User entry | Transaction selection indicators |
| TRNID01-10 | Output | TRANSACT file | Transaction IDs for displayed transactions |
| TDATE01-10 | Output | TRANSACT file | Transaction dates |
| TDESC01-10 | Output | TRANSACT file | Transaction descriptions |
| TAMT001-10 | Output | TRANSACT file | Transaction amounts |

**Navigation Conditions**:
- COTRN00 → Transaction Selection → COTRN01 (Transaction View)
- COTRN00 → F7 → COTRN00 (Previous page)
- COTRN00 → F8 → COTRN00 (Next page)
- COTRN00 → F3 → Previous Menu
- COTRN00 → Enter with filter → COTRN00 (Filtered results)
- COTRN00 → Enter with Selection S → COTRN01 (transaction view)
- COTRN00 → No transactions found → COTRN00 (No results message)

### SCREEN-010: Transaction View (COTRN01)
**Screen Purpose**: Display detailed information for a specific transaction

**User Interaction Flow**:
1. User enters from transaction listing or menu → System displays transaction search screen
2. User enters Transaction ID → System validates transaction existence
3. User presses Enter → System retrieves transaction data from TRANSACT file
4. System displays transaction details → User reviews complete transaction information
5. User presses F3 → System returns to previous screen
6. User presses F5 → System navigates to transaction listing

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| TRNIDIN | Input | User entry | Transaction ID for lookup |
| TRNID | Output | TRANSACT file | Transaction ID |
| CARDNUM | Output | TRANSACT file | Card number used |
| TTYPCD | Output | TRANSACT file | Transaction type code |
| TCATCD | Output | TRANSACT file | Transaction category code |
| TRNSRC | Output | TRANSACT file | Transaction source |
| TDESC | Output | TRANSACT file | Transaction description |
| TRNAMT | Output | TRANSACT file | Transaction amount |
| TORIGDT | Output | TRANSACT file | Original transaction date |
| TPROCDT | Output | TRANSACT file | Processing date |
| MID | Output | TRANSACT file | Merchant ID |
| MNAME | Output | TRANSACT file | Merchant name |
| MCITY | Output | TRANSACT file | Merchant city |
| MZIP | Output | TRANSACT file | Merchant ZIP code |

**Navigation Conditions**:
- COTRN01 → Valid Transaction → COTRN01 (Display transaction details)
- COTRN01 → Invalid Transaction → COTRN01 (Error: "Transaction not found")
- COTRN01 → F3 → Previous Screen
- COTRN01 → F4 → COTRN01 (Clear screen)
- COTRN01 → F5 → COTRN00 (Browse transactions)
- COTRN01 → Empty Transaction ID → COTRN01 (Error: "Transaction ID required")

### SCREEN-011: Transaction Add (COTRN02)
**Screen Purpose**: Create new transactions with comprehensive validation and confirmation

**User Interaction Flow**:
1. User enters from menu → System displays transaction add screen
2. User enters Account Number or Card Number → System validates account/card
3. User fills in transaction details (type, category, amount, dates, merchant info) → System validates each field
4. User enters confirmation (Y/N) → System validates all data
5. User presses Enter with Y confirmation → System creates transaction in TRANSACT file
6. User presses F3 → System returns to previous menu
7. User presses F5 → System copies last transaction data

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACTIDIN | Input | User entry | Account number (alternative to card number) |
| CARDNIN | Input | User entry | Card number (alternative to account number) |
| TTYPCD | Input | User entry | Transaction type code |
| TCATCD | Input | User entry | Transaction category code |
| TRNSRC | Input | User entry | Transaction source |
| TDESC | Input | User entry | Transaction description |
| TRNAMT | Input | User entry | Transaction amount (-99999999.99 to 99999999.99) |
| TORIGDT | Input | User entry | Original transaction date (YYYY-MM-DD) |
| TPROCDT | Input | User entry | Processing date (YYYY-MM-DD) |
| MID | Input | User entry | Merchant ID |
| MNAME | Input | User entry | Merchant name |
| MCITY | Input | User entry | Merchant city |
| MZIP | Input | User entry | Merchant ZIP code |
| CONFIRM | Input | User entry | Confirmation to add transaction (Y/N) |

**Navigation Conditions**:
- COTRN02 → Valid Data + Y Confirmation → COTRN02 (Transaction created, success message)
- COTRN02 → Valid Data + N Confirmation → COTRN02 (Transaction not created)
- COTRN02 → Invalid Data → COTRN02 (Error messages displayed)
- COTRN02 → F3 → Previous Menu
- COTRN02 → F4 → COTRN02 (Clear all fields)
- COTRN02 → F5 → COTRN02 (Copy last transaction data)
- COTRN02 → Missing required fields → COTRN02 (Validation errors)

### SCREEN-012: Bill Payment (COBIL00)
**Screen Purpose**: Process full balance payments for credit card accounts

**User Interaction Flow**:
1. User enters from menu → System displays bill payment screen
2. User enters Account ID → System validates account and retrieves current balance
3. System displays current balance → User reviews amount to be paid
4. User enters confirmation (Y/N) → System validates confirmation
5. system reads card cross reference file using account id to get the card number
6. User presses Enter with Y confirmation → System creates payment transaction and subtract the entered amount from balance
7. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| ACTIDIN | Input | User entry | Account ID for payment |
| CURBAL | Output | ACCTDAT file | Current account balance to be paid |
| CONFIRM | Input | User entry | Confirmation to make payment (Y/N) |

**Navigation Conditions**:
- COBIL00 → Valid Account + Positive Balance → COBIL00 (Display balance for confirmation)
- COBIL00 → Valid Account + Y Confirmation → COBIL00 (Payment processed, balance zeroed)
- COBIL00 → Valid Account + N Confirmation → COBIL00 (Payment cancelled)
- COBIL00 → Zero Balance → COBIL00 (Error: "You have nothing to pay")
- COBIL00 → Invalid Account → COBIL00 (Error: "Account ID NOT found")
- COBIL00 → F3 → Previous Menu
- COBIL00 → F4 → COBIL00 (Clear screen)
- COBIL00 → Empty Account ID → COBIL00 (Error: "Acct ID can NOT be empty")

### SCREEN-013: Transaction Reports (CORPT00)
**Screen Purpose**: Generate transaction reports with flexible date range options

**User Interaction Flow**:
1. User enters from menu → System displays report options screen
2. User selects report type (Monthly/Yearly/Custom) → System enables appropriate fields
3. For Custom reports, user enters start and end dates → System validates date format
4. User enters confirmation (Y/N) → System validates selection
5. User presses Enter with Y confirmation → System submits batch job for report generation
6. User presses F3 → System returns to previous menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| MONTHLY | Input | User entry | Monthly report selection |
| YEARLY | Input | User entry | Yearly report selection |
| CUSTOM | Input | User entry | Custom date range report selection |
| SDTMM | Input | User entry | Start date month (for custom reports) |
| SDTDD | Input | User entry | Start date day (for custom reports) |
| SDTYYYY | Input | User entry | Start date year (for custom reports) |
| EDTMM | Input | User entry | End date month (for custom reports) |
| EDTDD | Input | User entry | End date day (for custom reports) |
| EDTYYYY | Input | User entry | End date year (for custom reports) |
| CONFIRM | Input | User entry | Confirmation to submit report (Y/N) |

**Navigation Conditions**:
- CORPT00 → Valid Selection + Y Confirmation → CORPT00 (Report job submitted)
- CORPT00 → Valid Selection + N Confirmation → CORPT00 (Report not submitted)
- CORPT00 → Invalid Date Range → CORPT00 (Date validation errors)
- CORPT00 → No Selection → CORPT00 (Error: "Please select a report type")
- CORPT00 → F3 → Previous Menu
- CORPT00 → Custom dates validation → CORPT00 (Date format errors if invalid)

### SCREEN-014: User List (COUSR00)
**Screen Purpose**: Display paginated list of system users with search and management capabilities (Admin Only)

**User Interaction Flow**:
1. Admin user enters from admin menu → System displays user search screen with fist 10 users
2. User enters User ID filter (optional) → System filters results
3. User presses Enter → System displays up to 10 users per page
4. User selects user and enters U (Update) or D (Delete) → System navigates to appropriate function
5. User uses F7/F8 for pagination → System displays previous/next page
6. User presses F3 → System returns to admin menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| USRIDIN | Input | User entry | User ID filter (optional) |
| PAGENUM | Output | System | Current page number |
| SEL0001-10 | Input | User entry | User selection indicators (U/D) |
| USRID01-10 | Output | USRSEC file | User IDs for displayed users |
| FNAME01-10 | Output | USRSEC file | First names |
| LNAME01-10 | Output | USRSEC file | Last names |
| UTYPE01-10 | Output | USRSEC file | User types (A=Admin, U=User) |

**Navigation Conditions**:
- COUSR00 → User Selection + U → COUSR02 (Update User)
- COUSR00 → User Selection + D → COUSR03 (Delete User)
- COUSR00 → F7 → COUSR00 (Previous page)
- COUSR00 → F8 → COUSR00 (Next page)
- COUSR00 → F3 → COADM01 (Admin Menu)
- COUSR00 → Enter with filter → COUSR00 (Filtered results)
- COUSR00 → Invalid selection → COUSR00 (Error: "Invalid selection")

### SCREEN-015: Add User (COUSR01)
**Screen Purpose**: Create new system users with role assignment (Admin Only)

**User Interaction Flow**:
1. Admin user enters from admin menu → System displays add user screen
2. User enters first name and last name → System validates name fields
3. User enters User ID (8 characters) → System validates uniqueness
4. User enters Password (8 characters, hidden) → System validates format
5. User enters User Type (A=Admin, U=User) → System validates type
6. User presses Enter → System creates user in USRSEC file
7. User presses F3 → System returns to admin menu
8. User presses F4 → System clears screen 

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| FNAME | Input | User entry | First name of new user |
| LNAME | Input | User entry | Last name of new user |
| USERID | Input | User entry | User ID for new user (8 characters) |
| PASSWD | Input | User entry | Password for new user (8 characters, hidden) |
| USRTYPE | Input | User entry | User type (A=Admin, U=User) |

**Navigation Conditions**:
- COUSR01 → Valid Data → COUSR01 (User created successfully)
- COUSR01 → Duplicate User ID → COUSR01 (Error: "User ID already exists")
- COUSR01 → Invalid User Type → COUSR01 (Error: "Invalid user type")
- COUSR01 → Missing Required Fields → COUSR01 (Validation errors)
- COUSR01 → F3 → COADM01 (Admin Menu)
- COUSR01 → F4 → COUSR01 (Clear all fields)
- COUSR01 → F12 → Exit Application

### SCREEN-016: Update User (COUSR02)
**Screen Purpose**: Modify existing user information and permissions (Admin Only)

**User Interaction Flow**:
1. Admin user enters from user list or admin menu → System displays user update screen
2. User enters User ID → System retrieves and displays current user data
3. User modifies editable fields (name, password, type) → System tracks changes
4. User presses Enter → System fetches user data for editing
5. User presses F5 → System saves changes to USRSEC file
6. User presses F3 → System saves and exits to admin menu
7. User presses F4 → System clears screen 


**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| USRIDIN | Input | User entry | User ID to update |
| FNAME | Input/Output | USRSEC file | First name (editable) |
| LNAME | Input/Output | USRSEC file | Last name (editable) |
| PASSWD | Input/Output | USRSEC file | Password (editable, hidden) |
| USRTYPE | Input/Output | USRSEC file | User type (editable) |

**Navigation Conditions**:
- COUSR02 → Valid User ID → COUSR02 (Display user data for editing)
- COUSR02 → Invalid User ID → COUSR02 (Error: "User not found")
- COUSR02 → F3 → COADM01 (exit to admin menu)
- COUSR02 → F4 → COUSR02 (Clear fields)
- COUSR02 → F5 → COUSR02 (Save changes)
- COUSR02 → F12 → COADM01 (exit to admin menu)

- COUSR02 → Validation errors → COUSR02 (Error messages displayed)

### SCREEN-017: Delete User (COUSR03)
**Screen Purpose**: Remove users from the system with confirmation (Admin Only)

**User Interaction Flow**:
1. Admin user enters from user list or admin menu → System displays user delete screen
2. User enters User ID → System retrieves and displays user data for confirmation
3. User presses Enter → System fetches user data for review
4. User reviews user information → System displays user details
5. User presses F5 → System deletes user from USRSEC file with confirmation
6. User presses F3 → System returns to admin menu

**Screen Fields**:
| Field Name | Type | Data Source | Description |
|------------|------|-------------|-------------|
| USRIDIN | Input | User entry | User ID to delete |
| FNAME | Output | USRSEC file | First name (display only) |
| LNAME | Output | USRSEC file | Last name (display only) |
| USRTYPE | Output | USRSEC file | User type (display only) |

**Navigation Conditions**:
- COUSR03 → Valid User ID → COUSR03 (Display user data for confirmation)
- COUSR03 → Invalid User ID → COUSR03 (Error: "User not found")
- COUSR03 → F3 → COADM01 (Admin Menu)
- COUSR03 → F4 → COUSR03 (Clear fields)
- COUSR03 → F5 → COUSR03 (Delete user with confirmation)
- COUSR03 → Delete confirmation → COUSR03 (User deleted successfully)


