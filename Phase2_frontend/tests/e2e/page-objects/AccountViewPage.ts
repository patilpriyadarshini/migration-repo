import { Page, Locator, expect } from '@playwright/test';

export class AccountViewPage {
  readonly page: Page;
  readonly pageTitle: Locator;
  readonly accountNumberInput: Locator;
  readonly searchButton: Locator;
  readonly accountDetails: Locator;
  readonly accountBalance: Locator;
  readonly accountHolderName: Locator;
  readonly accountStatus: Locator;
  readonly updateAccountButton: Locator;
  readonly errorMessage: Locator;
  readonly validationError: Locator;

  constructor(page: Page) {
    this.page = page;
    this.pageTitle = page.locator('h2');
    this.accountNumberInput = page.locator('input[name="accountNumber"]');
    this.searchButton = page.locator('button:has-text("Search"), button[type="submit"]');
    this.accountDetails = page.locator('.account-details');
    this.accountBalance = page.locator('.account-balance');
    this.accountHolderName = page.locator('.account-holder-name');
    this.accountStatus = page.locator('.account-status');
    this.updateAccountButton = page.locator('button:has-text("Update Account")');
    this.errorMessage = page.locator('.error-message, .text-red-600');
    this.validationError = page.locator('.validation-error, .text-red-600');
  }

  async verifyAccountViewVisible() {
    await expect(this.pageTitle).toContainText('Account View');
    await expect(this.accountNumberInput).toBeVisible();
    await expect(this.searchButton).toBeVisible();
  }

  async searchAccount(accountNumber: string) {
    await this.accountNumberInput.fill(accountNumber);
    await this.searchButton.click();
  }

  async verifyAccountDetailsVisible() {
    await expect(this.accountDetails).toBeVisible();
    await expect(this.accountBalance).toBeVisible();
    await expect(this.accountHolderName).toBeVisible();
  }

  async verifyAccountDetailsNotVisible() {
    await expect(this.accountDetails).not.toBeVisible();
  }

  async verifyAccountBalance() {
    await expect(this.accountBalance).toBeVisible();
    await expect(this.accountBalance).toContainText('$');
  }

  async verifyAccountHolderName() {
    await expect(this.accountHolderName).toBeVisible();
  }

  async verifyAccountStatus() {
    await expect(this.accountStatus).toBeVisible();
  }

  async clickUpdateAccount() {
    await this.updateAccountButton.click();
  }

  async verifyErrorMessage(expectedMessage: string) {
    await expect(this.errorMessage.first()).toBeVisible();
    await expect(this.errorMessage.first()).toContainText(expectedMessage);
  }

  async verifyValidationError(expectedMessage: string) {
    await expect(this.validationError.first()).toBeVisible();
    await expect(this.validationError.first()).toContainText(expectedMessage);
  }
}
