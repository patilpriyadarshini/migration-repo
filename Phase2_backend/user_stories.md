# CardDemo Credit Card Management System - User Stories

## Table of Contents
1. [Authentication and Access Control](#authentication-and-access-control)
2. [Account Management](#account-management)
3. [Card Management](#card-management)
4. [Transaction Management](#transaction-management)
5. [Bill Payment](#bill-payment)
6. [Reporting](#reporting)
7. [User Administration](#user-administration)
8. [Interest and Balance Management](#interest-and-balance-management)
9. [Data Validation and Integrity](#data-validation-and-integrity)

---

## Authentication and Access Control

### US-001: User Login Authentication
**As a** system user (regular user or admin)  
**I want to** log into the CardDemo system with my credentials  
**So that** I can access the appropriate functions based on my user type

**Acceptance Criteria:**
- User ID must be exactly 8 characters and cannot be empty (*Source: validation-rules+1.md, RULE-VAL-003*)
- Password must be exactly 8 characters and cannot be empty (*Source: validation-rules+1.md, RULE-VAL-004*)
- System authenticates against USRSEC file and routes to appropriate menu based on user type (*Source: screen-flow-final-new-corrected+1.md, SCREEN-001*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-001; validation-rules+1.md, RULE-VAL-003, RULE-VAL-004*

### US-002: Role-Based Menu Access
**As a** regular user  
**I want to** access the main menu with standard functions  
**So that** I can perform account, card, and transaction operations

**Acceptance Criteria:**
- System displays 10 menu options for regular users (*Source: screen-flow-final-new-corrected+1.md, SCREEN-002*)
- Menu selection must be a valid option number (1-10) (*Source: screen-flow-final-new-corrected+1.md, SCREEN-002*)
- Invalid selections display appropriate error messages (*Source: screen-flow-final-new-corrected+1.md, SCREEN-002*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-002*

### US-003: Administrative Menu Access
**As an** admin user  
**I want to** access the admin menu with user management functions  
**So that** I can perform administrative tasks in addition to standard operations

**Acceptance Criteria:**
- System displays admin menu with user management options for admin users (*Source: screen-flow-final-new-corrected+1.md, SCREEN-003*)
- Admin users can access user list, add, update, and delete functions (*Source: screen-flow-final-new-corrected+1.md, SCREEN-003*)
- System validates admin privileges before allowing access to administrative functions (*Source: screen-flow-final-new-corrected+1.md, SCREEN-003*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-003*

---

## Account Management

### US-004: Account Information Viewing
**As a** system user  
**I want to** view comprehensive account and customer information  
**So that** I can review account details, balances, and customer data

**Acceptance Criteria:**
- Account ID must be 11 digits, numeric, and cannot be zero or empty (*Source: validation-rules+1.md, RULE-VAL-006, RULE-VAL-007*)
- System retrieves data from ACCTDAT and CUSTDAT files (*Source: screen-flow-final-new-corrected+1.md, SCREEN-004*)
- Display includes account status, balances, credit limits, and complete customer information (*Source: screen-flow-final-new-corrected+1.md, SCREEN-004*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-004; validation-rules+1.md, RULE-VAL-006, RULE-VAL-007*

### US-005: Account Information Updates
**As a** system user  
**I want to** modify account and customer information  
**So that** I can keep account data current and accurate

**Acceptance Criteria:**
- FICO score must be between 300 and 850 when updated (*Source: validation-rules+1.md, RULE-VAL-008*)
- SSN validation: first 3 digits cannot be 000, 666, or 900-999; middle 2 digits must be 01-99; last 4 digits must be numeric (*Source: validation-rules+1.md, RULE-VAL-009, RULE-VAL-010, RULE-VAL-011*)
- State code must be valid US state abbreviation and zip code must be valid for the state (*Source: validation-rules+1.md, RULE-VAL-012, RULE-VAL-013*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-005; validation-rules+1.md, RULE-VAL-008 through RULE-VAL-013*

---

## Card Management

### US-006: Card Listing and Search
**As a** system user  
**I want to** view a paginated list of credit cards with search capabilities  
**So that** I can find and select specific cards for viewing or updating

**Acceptance Criteria:**
- System displays up to 7 cards per page with pagination controls (F7/F8) (*Source: screen-flow-final-new-corrected+1.md, SCREEN-006*)
- Optional filters by Account Number or Card Number (*Source: screen-flow-final-new-corrected+1.md, SCREEN-006*)
- Card selection options: 'S' for view, 'U' for update (*Source: screen-flow-final-new-corrected+1.md, SCREEN-006*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-006*

### US-007: Card Detail Viewing
**As a** system user  
**I want to** view detailed information for a specific credit card  
**So that** I can review card status, expiration, and cardholder information

**Acceptance Criteria:**
- Card number must be 16 digits, numeric, and cannot be empty, spaces, or zeros (*Source: validation-rules+1.md, RULE-VAL-014, RULE-VAL-015*)
- System retrieves card data from CARDDAT file (*Source: screen-flow-final-new-corrected+1.md, SCREEN-007*)
- Display includes card name, status, and expiration information (*Source: screen-flow-final-new-corrected+1.md, SCREEN-007*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-007; validation-rules+1.md, RULE-VAL-014, RULE-VAL-015*

### US-008: Card Information Updates
**As a** system user  
**I want to** modify credit card information  
**So that** I can update card details like name, status, and expiration

**Acceptance Criteria:**
- Card name must contain only alphabetic characters and spaces, cannot be empty (*Source: validation-rules+1.md, RULE-VAL-016, RULE-VAL-017*)
- Card status must be 'Y' or 'N' and cannot be empty (*Source: validation-rules+1.md, RULE-VAL-018, RULE-VAL-019*)
- Expiry month must be between 1-12, expiry year must be between 1950-2099 (*Source: validation-rules+1.md, RULE-VAL-020, RULE-VAL-021, RULE-VAL-022*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-008; validation-rules+1.md, RULE-VAL-016 through RULE-VAL-022*

---

## Transaction Management

### US-009: Transaction Listing and Search
**As a** system user  
**I want to** view a paginated list of transactions with search capabilities  
**So that** I can find and review specific transactions

**Acceptance Criteria:**
- System displays up to 10 transactions per page with pagination controls (F7/F8) (*Source: screen-flow-final-new-corrected+1.md, SCREEN-009*)
- Optional filter by Transaction ID (*Source: screen-flow-final-new-corrected+1.md, SCREEN-009*)
- Transaction selection with 'S' navigates to detailed view (*Source: screen-flow-final-new-corrected+1.md, SCREEN-009*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-009*

### US-010: Transaction Detail Viewing
**As a** system user  
**I want to** view comprehensive details for a specific transaction  
**So that** I can review transaction information including merchant details

**Acceptance Criteria:**
- Transaction ID must be provided and exist in TRANSACT file (*Source: screen-flow-final-new-corrected+1.md, SCREEN-010*)
- Display includes card number, type, category, amount, dates, and merchant information (*Source: screen-flow-final-new-corrected+1.md, SCREEN-010*)
- Navigation options to return to transaction listing or clear screen (*Source: screen-flow-final-new-corrected+1.md, SCREEN-010*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-010*

### US-011: Transaction Creation
**As a** system user  
**I want to** create new transactions with comprehensive validation  
**So that** I can record financial activities accurately

**Acceptance Criteria:**
- Transaction type and category codes must be numeric (*Source: validation-rules+1.md, RULE-VAL-023, RULE-VAL-024*)
- Transaction amount must be in format -99999999.99 with proper sign, digits, and decimal (*Source: validation-rules+1.md, RULE-VAL-025*)
- Origin and process dates must be in YYYY-MM-DD format (*Source: validation-rules+1.md, RULE-VAL-026, RULE-VAL-027*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-011; validation-rules+1.md, RULE-VAL-023 through RULE-VAL-030*

### US-012: Transaction Authorization Validation
**As a** system user  
**I want to** ensure transactions are validated against business rules  
**So that** only authorized transactions are processed

**Acceptance Criteria:**
- Transaction amount cannot exceed available credit limit (*Source: business_rules_catalog+2+1.md, RULE-DECISION-002*)
- Transactions cannot be processed after account expiration date (*Source: business_rules_catalog+2+1.md, RULE-DECISION-003*)
- Card number must exist in card cross-reference file and account must exist in account master file (*Source: validation-rules+1.md, RULE-VAL-036, RULE-VAL-037*)

**Source Traceability:** *business_rules_catalog+2+1.md, RULE-DECISION-002, RULE-DECISION-003; validation-rules+1.md, RULE-VAL-036, RULE-VAL-037*

---

## Bill Payment

### US-013: Bill Payment Processing
**As a** system user  
**I want to** process full balance payments for credit card accounts  
**So that** customers can pay their outstanding balances

**Acceptance Criteria:**
- Account must have a positive balance to allow payment (*Source: business_rules_catalog+2+1.md, RULE-DECISION-007*)
- Payment amount equals the full current account balance (*Source: business_rules_catalog+2+1.md, RULE-CALC-008*)
- Confirmation must be 'Y' or 'N' before processing (*Source: validation-rules+1.md, RULE-VAL-041*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-012; business_rules_catalog+2+1.md, RULE-DECISION-007, RULE-CALC-008; validation-rules+1.md, RULE-VAL-041*

---

## Reporting

### US-014: Transaction Report Generation
**As a** system user  
**I want to** generate transaction reports with flexible date ranges  
**So that** I can analyze transaction patterns and generate business reports

**Acceptance Criteria:**
- Report types include Monthly, Yearly, and Custom date range options (*Source: screen-flow-final-new-corrected+1.md, SCREEN-013*)
- Custom reports require valid start and end dates in proper format (*Source: screen-flow-final-new-corrected+1.md, SCREEN-013*)
- Confirmation required before submitting batch job for report generation (*Source: screen-flow-final-new-corrected+1.md, SCREEN-013*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-013*

---

## User Administration

### US-015: User Listing and Management
**As an** admin user  
**I want to** view a paginated list of system users  
**So that** I can manage user accounts and permissions

**Acceptance Criteria:**
- System displays up to 10 users per page with pagination controls (F7/F8) (*Source: screen-flow-final-new-corrected+1.md, SCREEN-014*)
- Optional filter by User ID (*Source: screen-flow-final-new-corrected+1.md, SCREEN-014*)
- User selection options: 'U' for update, 'D' for delete (*Source: screen-flow-final-new-corrected+1.md, SCREEN-014*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-014*

### US-016: User Creation
**As an** admin user  
**I want to** create new system users with role assignment  
**So that** I can provide access to authorized personnel

**Acceptance Criteria:**
- First name and last name cannot be empty or contain only spaces (*Source: validation-rules+1.md, RULE-VAL-001, RULE-VAL-002*)
- User ID must be exactly 8 characters and unique (*Source: validation-rules+1.md, RULE-VAL-003*)
- User type must be 'A' (Admin) or 'U' (User) (*Source: validation-rules+1.md, RULE-VAL-005*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-015; validation-rules+1.md, RULE-VAL-001 through RULE-VAL-005*

### US-017: User Information Updates
**As an** admin user  
**I want to** modify existing user information and permissions  
**So that** I can maintain accurate user accounts and access levels

**Acceptance Criteria:**
- User ID must exist in USRSEC file for updates (*Source: screen-flow-final-new-corrected+1.md, SCREEN-016*)
- All validation rules for user creation apply to updates (*Source: validation-rules+1.md, RULE-VAL-001 through RULE-VAL-005*)
- Changes are saved to USRSEC file upon confirmation (*Source: screen-flow-final-new-corrected+1.md, SCREEN-016*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-016; validation-rules+1.md, RULE-VAL-001 through RULE-VAL-005*

### US-018: User Deletion
**As an** admin user  
**I want to** remove users from the system with confirmation  
**So that** I can maintain security by removing unauthorized access

**Acceptance Criteria:**
- User ID must exist in USRSEC file for deletion (*Source: screen-flow-final-new-corrected+1.md, SCREEN-017*)
- System displays user information for confirmation before deletion (*Source: screen-flow-final-new-corrected+1.md, SCREEN-017*)
- Deletion requires explicit confirmation (F5 key) (*Source: screen-flow-final-new-corrected+1.md, SCREEN-017*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-017*

---

## Interest and Balance Management

### US-019: Monthly Interest Calculation
**As a** system administrator  
**I want to** calculate monthly interest charges on transaction category balances  
**So that** appropriate interest is applied to customer accounts

**Acceptance Criteria:**
- Interest calculation uses formula: (TRANSACTION-CATEGORY-BALANCE * INTEREST-RATE) / 1200 (*Source: business_rules_catalog+2+1.md, RULE-CALC-001*)
- Interest is only calculated when non-zero interest rate exists (*Source: business_rules_catalog+2+1.md, RULE-THRESHOLD-009*)
- Computed interest is added to account current balance (*Source: business_rules_catalog+2+1.md, RULE-CALC-006*)

**Source Traceability:** *business_rules_catalog+2+1.md, RULE-CALC-001, RULE-CALC-006, RULE-THRESHOLD-009*

### US-020: Account Balance Updates
**As a** system administrator  
**I want to** update account balances based on transaction processing  
**So that** account balances accurately reflect all financial activities

**Acceptance Criteria:**
- Current balance updated by adding transaction amount (*Source: business_rules_catalog+2+1.md, RULE-CALC-004*)
- Positive amounts added to current cycle credit, negative amounts to current cycle debit (*Source: business_rules_catalog+2+1.md, RULE-CALC-004*)
- Transaction amounts aggregated by account, type, and category for interest calculation (*Source: business_rules_catalog+2+1.md, RULE-CALC-005*)

**Source Traceability:** *business_rules_catalog+2+1.md, RULE-CALC-004, RULE-CALC-005*

---

## Data Validation and Integrity

### US-021: Date and Time Validation
**As a** system user  
**I want to** ensure all date inputs are properly validated  
**So that** data integrity is maintained across the system

**Acceptance Criteria:**
- Month values must be between 1 and 12 (*Source: validation-rules+1.md, RULE-VAL-031*)
- Day values must be between 1 and 31, with February limited to 1-28 (*Source: validation-rules+1.md, RULE-VAL-032, RULE-VAL-033*)
- Date format validations ensure proper YYYY-MM-DD structure (*Source: validation-rules+1.md, RULE-VAL-026, RULE-VAL-027*)

**Source Traceability:** *validation-rules+1.md, RULE-VAL-026, RULE-VAL-027, RULE-VAL-031, RULE-VAL-032, RULE-VAL-033*

### US-022: Phone Number Validation
**As a** system user  
**I want to** ensure phone numbers are properly validated  
**So that** contact information is accurate and properly formatted

**Acceptance Criteria:**
- Phone area code must be a valid US area code (*Source: validation-rules+1.md, RULE-VAL-038*)
- Phone number components must be numeric and within valid ranges (*Source: validation-rules+1.md, RULE-VAL-039*)
- Phone line number code cannot be zero (*Source: validation-rules+1.md, RULE-VAL-039*)

**Source Traceability:** *validation-rules+1.md, RULE-VAL-038, RULE-VAL-039*

### US-023: Yes/No Field Validation
**As a** system user  
**I want to** ensure Yes/No fields contain only valid values  
**So that** boolean data is consistently represented

**Acceptance Criteria:**
- Yes/No fields must contain either 'Y' or 'N' (*Source: validation-rules+1.md, RULE-VAL-040*)
- Bill pay confirmation must be 'Y' or 'N' (*Source: validation-rules+1.md, RULE-VAL-041*)
- Card status and other boolean fields follow same validation pattern (*Source: validation-rules+1.md, RULE-VAL-018, RULE-VAL-019*)

**Source Traceability:** *validation-rules+1.md, RULE-VAL-018, RULE-VAL-019, RULE-VAL-040, RULE-VAL-041*

### US-024: Merchant Information Validation
**As a** system user  
**I want to** ensure merchant information is properly validated during transaction creation  
**So that** transaction records contain accurate merchant data

**Acceptance Criteria:**
- Merchant name, city, and zip code cannot be empty (*Source: validation-rules+1.md, RULE-VAL-028, RULE-VAL-029, RULE-VAL-030*)
- All merchant fields must contain valid data before transaction processing (*Source: screen-flow-final-new-corrected+1.md, SCREEN-011*)
- Transaction confirmation required after all validations pass (*Source: screen-flow-final-new-corrected+1.md, SCREEN-011*)

**Source Traceability:** *validation-rules+1.md, RULE-VAL-028, RULE-VAL-029, RULE-VAL-030; screen-flow-final-new-corrected+1.md, SCREEN-011*

### US-025: System Error Handling
**As a** system user  
**I want to** receive clear error messages when validation failures occur  
**So that** I can correct input errors and complete my tasks successfully

**Acceptance Criteria:**
- System displays specific error messages for each validation failure (*Source: screen-flow-final-new-corrected+1.md, multiple screens*)
- Error conditions include "not found" messages for invalid IDs (*Source: screen-flow-final-new-corrected+1.md, SCREEN-004, SCREEN-007, SCREEN-010*)
- Required field validations display appropriate messages when fields are empty (*Source: screen-flow-final-new-corrected+1.md, multiple screens*)

**Source Traceability:** *screen-flow-final-new-corrected+1.md, SCREEN-004, SCREEN-007, SCREEN-010, SCREEN-012*

---

## Summary

This user story catalog covers the complete CardDemo Credit Card Management System functionality, derived from:

- **17 screens** documented in screen-flow-final-new-corrected+1.md
- **41 validation rules** from validation-rules+1.md  
- **9 business rules** from business_rules_catalog+2+1.md
- **11 business entities** with foreign key relationships from enhanced-business-entities+1.md

The stories are organized into 9 functional areas with complete traceability to source documentation, ensuring comprehensive coverage of all user-facing features, business requirements, edge cases, and exception flows. Each user story includes 2-3 acceptance criteria derived directly from the validation rules and business logic documented in the source files.

**Total User Stories**: 25  
**Coverage**: Complete system functionality including authentication, account management, card management, transaction processing, bill payment, reporting, user administration, interest calculations, and data validation.
**As a** user, **I want to** enter valid yes/no responses, **so that** boolean fields are properly validated and processed.

**Acceptance Criteria:**
- Yes/No fields must contain either 'Y' or 'N' (RULE-VAL-040)
- Invalid values display validation errors
- Consistent validation across all yes/no fields in the system
- Proper handling of confirmation dialogs and status flags

*Source: validation-rules.md (RULE-VAL-040)*

### US-VAL-003: Card and Account Existence Validation
**As a** system, **I want to** validate that cards and accounts exist before processing transactions, **so that** only valid financial instruments are used for transactions.

**Acceptance Criteria:**
- Card number must exist in card cross-reference file (RULE-VAL-036)
- Account must exist in account master file (RULE-VAL-037)
- Invalid card or account references are rejected during transaction processing
- Proper error messages displayed for non-existent cards or accounts

*Source: validation-rules.md (RULE-VAL-036, RULE-VAL-037)*

---

## Summary

**Total User Stories**: 25
- **Authentication & Access Control**: 3 stories
- **Account Management**: 4 stories  
- **Card Management**: 4 stories
- **Transaction Processing**: 7 stories
- **Bill Payment**: 2 stories
- **Reporting**: 2 stories
- **User Administration**: 5 stories
- **Interest and Fee Calculations**: 1 story
- **Data Validation and Business Rules**: 3 stories

**Traceability Coverage**:
- **Validation Rules**: 41 rules referenced across user stories
- **Business Rules**: 9 rules integrated into functional requirements
- **Screen Flows**: 17 screens mapped to user interactions
- **Business Entities**: 11 entities supporting data relationships

All user stories include specific acceptance criteria derived from the source documentation, ensuring complete traceability from business requirements to functional specifications.
