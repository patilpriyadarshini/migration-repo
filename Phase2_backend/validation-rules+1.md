# CardDemo Business Validation Rules

## Overview
This document contains business-level validation rules extracted from the CardDemo COBOL codebase for migration to modern validation layers (frontend + backend APIs). These rules focus on data integrity and input validation, excluding calculation logic and technical validations.

## Validation Rule Categories
- **Field-Level**: Input validations (non-empty, length checks, format constraints)
- **Range**: Numeric range validations (age between 18-60, FICO 300-850)
- **Code/Value**: Valid code/value checks (status must be "A", "I", or "P")
- **Domain-Specific**: Business domain validation (policy must be active, date not in future)
- **Conditional**: Conditional validations (if plan type = X, then field Y required)

---

## USER MANAGEMENT VALIDATIONS

### RULE-VAL-001: First Name Required
**Rule Description**: First Name field cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COUSR01C.cbl, lines 118-123
**Field(s) Involved**: FNAMEI (First Name input field)
**Validation Condition**: Field must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing user addition form (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-002: Last Name Required
**Rule Description**: Last Name field cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COUSR01C.cbl, lines 124-129
**Field(s) Involved**: LNAMEI (Last Name input field)
**Validation Condition**: Field must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing user addition form (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-003: User ID Required
**Rule Description**: User ID field cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COUSR01C.cbl, lines 130-135
**Field(s) Involved**: USERIDI (User ID input field)
**Validation Condition**: Field must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing user addition form (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-004: Password Required
**Rule Description**: Password field cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COUSR01C.cbl, lines 136-141
**Field(s) Involved**: PASSWDI (Password input field)
**Validation Condition**: Field must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing user addition form (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-005: User Type Required
**Rule Description**: User Type field cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COUSR01C.cbl, lines 142-147
**Field(s) Involved**: USRTYPEI (User Type input field)
**Validation Condition**: Field must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing user addition form (PROCESS-ENTER-KEY paragraph)

---

## ACCOUNT MANAGEMENT VALIDATIONS

### RULE-VAL-006: Account ID Format Validation
**Rule Description**: Account ID must be an 11-digit numeric value and cannot be zero
**COBOL Source Location**: COACTVWC.cbl, lines 666-680
**Field(s) Involved**: CC-ACCT-ID (Account ID field)
**Validation Condition**: Must be numeric AND not equal to ZEROES AND not equal to SPACES/LOW-VALUES
**Trigger Conditions**: When editing account filter (2210-EDIT-ACCOUNT paragraph)

### RULE-VAL-007: Account ID Non-Empty Validation
**Rule Description**: Account ID cannot be empty, spaces, or low-values when supplied
**COBOL Source Location**: COACTVWC.cbl, lines 653-662
**Field(s) Involved**: CC-ACCT-ID (Account ID field)
**Validation Condition**: Must not equal LOW-VALUES or SPACES
**Trigger Conditions**: When editing account filter (2210-EDIT-ACCOUNT paragraph)

### RULE-VAL-008: FICO Score Range Validation
**Rule Description**: FICO credit score must be between 300 and 850
**COBOL Source Location**: COACTUPC.cbl, lines 2515-2528
**Field(s) Involved**: FICO score field
**Validation Condition**: Value must be >= 300 AND <= 850
**Trigger Conditions**: When editing FICO score (1275-EDIT-FICO-SCORE paragraph)

### RULE-VAL-009: SSN Part 1 Format Validation
**Rule Description**: First 3 digits of SSN cannot be 000, 666, or between 900-999
**COBOL Source Location**: COACTUPC.cbl, lines 2447-2464
**Field(s) Involved**: ACUP-NEW-CUST-SSN-1 (SSN first part)
**Validation Condition**: Must be 3 numeric digits AND not equal to 000, 666, or 900-999 range
**Trigger Conditions**: When editing SSN (1265-EDIT-US-SSN paragraph)

### RULE-VAL-010: SSN Part 2 Format Validation
**Rule Description**: Middle 2 digits of SSN must be numeric and between 01-99
**COBOL Source Location**: COACTUPC.cbl, lines 2469-2476
**Field(s) Involved**: ACUP-NEW-CUST-SSN-2 (SSN middle part)
**Validation Condition**: Must be 2 numeric digits
**Trigger Conditions**: When editing SSN (1265-EDIT-US-SSN paragraph)

### RULE-VAL-011: SSN Part 3 Format Validation
**Rule Description**: Last 4 digits of SSN must be numeric
**COBOL Source Location**: COACTUPC.cbl, lines 2481-2488
**Field(s) Involved**: ACUP-NEW-CUST-SSN-3 (SSN last part)
**Validation Condition**: Must be 4 numeric digits
**Trigger Conditions**: When editing SSN (1265-EDIT-US-SSN paragraph)

### RULE-VAL-012: US State Code Validation
**Rule Description**: State code must be a valid US state abbreviation
**COBOL Source Location**: COACTUPC.cbl, lines 2494-2509; CSLKPCDY.cpy, lines 1013-1069
**Field(s) Involved**: ACUP-NEW-CUST-ADDR-STATE-CD (State code field)
**Validation Condition**: Must match one of the valid US state codes (AL, AK, AZ, AR, CA, CO, etc.)
**Trigger Conditions**: When editing state code (1270-EDIT-US-STATE-CD paragraph)

### RULE-VAL-013: State-Zip Code Combination Validation
**Rule Description**: Zip code must be valid for the specified state
**COBOL Source Location**: COACTUPC.cbl, lines 2537-2556; CSLKPCDY.cpy, lines 1073-1313
**Field(s) Involved**: ACUP-NEW-CUST-ADDR-STATE-CD, ACUP-NEW-CUST-ADDR-ZIP
**Validation Condition**: State+first 2 digits of zip must match valid combinations (e.g., CA90, CA91, TX75, etc.)
**Trigger Conditions**: When editing zip code (1280-EDIT-US-STATE-ZIP-CD paragraph)

---

## CARD MANAGEMENT VALIDATIONS

### RULE-VAL-014: Card Number Format Validation
**Rule Description**: Card number must be a 16-digit numeric value when supplied
**COBOL Source Location**: COCRDLIC.cbl, lines 1017-1029
**Field(s) Involved**: CC-CARD-NUM (Card number field)
**Validation Condition**: Must be numeric AND 16 digits in length
**Trigger Conditions**: When editing card filter (2220-EDIT-CARD paragraph)

### RULE-VAL-015: Card Number Non-Empty Validation
**Rule Description**: Card number cannot be empty, spaces, low-values, or zeros when supplied
**COBOL Source Location**: COCRDLIC.cbl, lines 1042-1048
**Field(s) Involved**: CC-CARD-NUM (Card number field)
**Validation Condition**: Must not equal LOW-VALUES, SPACES, or ZEROS
**Trigger Conditions**: When editing card filter (2220-EDIT-CARD paragraph)

### RULE-VAL-016: Card Name Alphabetic Validation
**Rule Description**: Card name must contain only alphabetic characters and spaces
**COBOL Source Location**: COCRDUPC.cbl, lines 822-837
**Field(s) Involved**: CCUP-NEW-CRDNAME (Card name field)
**Validation Condition**: After converting alphabetic characters to spaces, trimmed length must be 0
**Trigger Conditions**: When editing card name (1230-EDIT-NAME paragraph)

### RULE-VAL-017: Card Name Required Validation
**Rule Description**: Card name cannot be empty, low-values, spaces, or zeros
**COBOL Source Location**: COCRDUPC.cbl, lines 811-820
**Field(s) Involved**: CCUP-NEW-CRDNAME (Card name field)
**Validation Condition**: Must not equal LOW-VALUES, SPACES, or ZEROS
**Trigger Conditions**: When editing card name (1230-EDIT-NAME paragraph)

### RULE-VAL-018: Card Status Y/N Validation
**Rule Description**: Card active status must be either 'Y' or 'N'
**COBOL Source Location**: COCRDUPC.cbl, lines 863-870
**Field(s) Involved**: CCUP-NEW-CRDSTCD (Card status code field)
**Validation Condition**: Must equal 'Y' or 'N'
**Trigger Conditions**: When editing card status (1240-EDIT-CARDSTATUS paragraph)

### RULE-VAL-019: Card Status Required Validation
**Rule Description**: Card status cannot be empty, low-values, spaces, or zeros
**COBOL Source Location**: COCRDUPC.cbl, lines 850-859
**Field(s) Involved**: CCUP-NEW-CRDSTCD (Card status code field)
**Validation Condition**: Must not equal LOW-VALUES, SPACES, or ZEROS
**Trigger Conditions**: When editing card status (1240-EDIT-CARDSTATUS paragraph)

### RULE-VAL-020: Card Expiry Month Range Validation
**Rule Description**: Card expiry month must be between 1 and 12
**COBOL Source Location**: COCRDUPC.cbl, lines 896-900
**Field(s) Involved**: CCUP-NEW-EXPMON (Card expiry month field)
**Validation Condition**: Must be numeric AND between 1 and 12 inclusive
**Trigger Conditions**: When editing expiry month (1250-EDIT-EXPIRY-MON paragraph)

### RULE-VAL-021: Card Expiry Month Required Validation
**Rule Description**: Card expiry month cannot be empty, low-values, spaces, or zeros
**COBOL Source Location**: COCRDUPC.cbl, lines 883-892
**Field(s) Involved**: CCUP-NEW-EXPMON (Card expiry month field)
**Validation Condition**: Must not equal LOW-VALUES, SPACES, or ZEROS
**Trigger Conditions**: When editing expiry month (1250-EDIT-EXPIRY-MON paragraph)

### RULE-VAL-022: Card Expiry Year Range Validation
**Rule Description**: Card expiry year must be between 1950 and 2099
**COBOL Source Location**: COCRDUPC.cbl, lines 128-129 (VALID-YEAR condition)
**Field(s) Involved**: Card expiry year field
**Validation Condition**: Must be numeric AND between 1950 and 2099 inclusive
**Trigger Conditions**: When editing expiry year

---

## TRANSACTION PROCESSING VALIDATIONS

### RULE-VAL-023: Transaction Type Code Numeric Validation
**Rule Description**: Transaction type code must be numeric
**COBOL Source Location**: COTRN02C.cbl, lines 323-328
**Field(s) Involved**: TTYPCDI (Transaction type code input field)
**Validation Condition**: Must be numeric
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-024: Transaction Category Code Numeric Validation
**Rule Description**: Transaction category code must be numeric
**COBOL Source Location**: COTRN02C.cbl, lines 329-334
**Field(s) Involved**: TCATCDI (Transaction category code input field)
**Validation Condition**: Must be numeric
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-025: Transaction Amount Format Validation
**Rule Description**: Transaction amount must be in format -99999999.99 (sign + 8 digits + decimal + 2 digits)
**COBOL Source Location**: COTRN02C.cbl, lines 340-351
**Field(s) Involved**: TRNAMTI (Transaction amount input field)
**Validation Condition**: Position 1 must be '-' or '+', positions 2-9 must be numeric, position 10 must be '.', positions 11-12 must be numeric
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-026: Transaction Origin Date Format Validation
**Rule Description**: Transaction origin date must be in YYYY-MM-DD format
**COBOL Source Location**: COTRN02C.cbl, lines 354-366
**Field(s) Involved**: TORIGDTI (Transaction origin date input field)
**Validation Condition**: Positions 1-4 must be numeric (year), position 5 must be '-', positions 6-7 must be numeric (month), position 8 must be '-', positions 9-10 must be numeric (day)
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-027: Transaction Process Date Format Validation
**Rule Description**: Transaction process date must be in YYYY-MM-DD format
**COBOL Source Location**: COTRN02C.cbl, lines 369-381
**Field(s) Involved**: TPROCDTI (Transaction process date input field)
**Validation Condition**: Positions 1-4 must be numeric (year), position 5 must be '-', positions 6-7 must be numeric (month), position 8 must be '-', positions 9-10 must be numeric (day)
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-028: Merchant Name Required Validation
**Rule Description**: Merchant name cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COTRN02C.cbl, lines 300-305
**Field(s) Involved**: MNAMEI (Merchant name input field)
**Validation Condition**: Must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-029: Merchant City Required Validation
**Rule Description**: Merchant city cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COTRN02C.cbl, lines 306-311
**Field(s) Involved**: MCITYI (Merchant city input field)
**Validation Condition**: Must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

### RULE-VAL-030: Merchant Zip Required Validation
**Rule Description**: Merchant zip code cannot be empty or contain only spaces/low-values
**COBOL Source Location**: COTRN02C.cbl, lines 312-317
**Field(s) Involved**: MZIPI (Merchant zip input field)
**Validation Condition**: Must not equal SPACES or LOW-VALUES
**Trigger Conditions**: When processing transaction entry (PROCESS-ENTER-KEY paragraph)

---

## DATE AND TIME VALIDATIONS

### RULE-VAL-031: Month Range Validation
**Rule Description**: Month value must be between 1 and 12
**COBOL Source Location**: CSUTLDWY.cpy, lines 19-20
**Field(s) Involved**: WS-EDIT-DATE-MM-N (Month field)
**Validation Condition**: Must be numeric AND between 1 and 12 inclusive
**Trigger Conditions**: When validating date components

### RULE-VAL-032: Day Range Validation
**Rule Description**: Day value must be between 1 and 31
**COBOL Source Location**: CSUTLDWY.cpy, lines 28-29
**Field(s) Involved**: WS-EDIT-DATE-DD-N (Day field)
**Validation Condition**: Must be numeric AND between 1 and 31 inclusive
**Trigger Conditions**: When validating date components

### RULE-VAL-033: February Day Range Validation
**Rule Description**: For February, day value must be between 1 and 28
**COBOL Source Location**: CSUTLDWY.cpy, lines 33-34
**Field(s) Involved**: WS-EDIT-DATE-DD-N (Day field)
**Validation Condition**: Must be numeric AND between 1 and 28 inclusive when month is February
**Trigger Conditions**: When validating February dates

---

## BUSINESS RULE VALIDATIONS

### RULE-VAL-034: Credit Limit Validation
**Rule Description**: Transaction amount cannot exceed available credit limit
**COBOL Source Location**: CBTRN02C.cbl, lines 407-413
**Field(s) Involved**: ACCT-CREDIT-LIMIT, WS-TEMP-BAL (calculated balance)
**Validation Condition**: Account credit limit must be >= calculated temporary balance
**Trigger Conditions**: During transaction validation (1500-B-LOOKUP-ACCT paragraph)

### RULE-VAL-035: Account Expiration Validation
**Rule Description**: Transaction cannot be processed after account expiration date
**COBOL Source Location**: CBTRN02C.cbl, lines 414-420
**Field(s) Involved**: ACCT-EXPIRATION-DATE, DALYTRAN-ORIG-TS
**Validation Condition**: Account expiration date must be >= transaction origin date
**Trigger Conditions**: During transaction validation (1500-B-LOOKUP-ACCT paragraph)

### RULE-VAL-036: Card Number Existence Validation
**Rule Description**: Card number must exist in the card cross-reference file
**COBOL Source Location**: CBTRN02C.cbl, lines 382-388
**Field(s) Involved**: DALYTRAN-CARD-NUM (Transaction card number)
**Validation Condition**: Card number must be found in XREF-FILE
**Trigger Conditions**: During transaction validation (1500-A-LOOKUP-XREF paragraph)

### RULE-VAL-037: Account Existence Validation
**Rule Description**: Account must exist in the account master file
**COBOL Source Location**: CBTRN02C.cbl, lines 394-400
**Field(s) Involved**: XREF-ACCT-ID (Account ID from cross-reference)
**Validation Condition**: Account ID must be found in ACCOUNT-FILE
**Trigger Conditions**: During transaction validation (1500-B-LOOKUP-ACCT paragraph)

---

## PHONE NUMBER VALIDATIONS

### RULE-VAL-038: Phone Area Code Validation
**Rule Description**: Phone area code must be a valid US area code
**COBOL Source Location**: CSLKPCDY.cpy, lines 149-520
**Field(s) Involved**: Phone area code field
**Validation Condition**: Must match one of the valid phone area codes (201, 202, 203, etc.)
**Trigger Conditions**: When validating phone number input

### RULE-VAL-039: Phone Number Format Validation
**Rule Description**: Phone number components must be numeric and within valid ranges
**COBOL Source Location**: COACTUPC.cbl, lines 2404-2418
**Field(s) Involved**: WS-EDIT-US-PHONE-NUMC-N (Phone number component)
**Validation Condition**: Phone line number code cannot be zero
**Trigger Conditions**: When editing US phone number (EDIT-US-PHONE paragraph)

---

## CONDITIONAL VALIDATIONS

### RULE-VAL-040: Yes/No Field Validation
**Rule Description**: Yes/No fields must contain either 'Y' or 'N'
**COBOL Source Location**: COACTUPC.cbl, lines 211-212; COCRDUPC.cbl, lines 120-121
**Field(s) Involved**: Various Y/N flag fields
**Validation Condition**: Must equal 'Y' or 'N'
**Trigger Conditions**: When validating yes/no input fields

### RULE-VAL-041: Bill Pay Confirmation Validation
**Rule Description**: Bill pay confirmation must be 'Y' or 'N'
**COBOL Source Location**: COBIL00C.cbl, lines 187-190
**Field(s) Involved**: CONFIRML (Confirmation field)
**Validation Condition**: Must equal 'Y' or 'N'
**Trigger Conditions**: When processing bill payment confirmation

---

## Summary
- **Total Validation Rules**: 41
- **Field-Level Validations**: 15 rules
- **Range Validations**: 8 rules  
- **Code/Value Validations**: 8 rules
- **Domain-Specific Validations**: 6 rules
- **Conditional Validations**: 4 rules

All validation rules have been extracted from concrete COBOL source code with exact program names and line number references. These rules are suitable for implementation in modern validation layers during system modernization efforts.
