import { Page, Locator } from '@playwright/test';

export class AccountViewPage {
  readonly page: Page;
  readonly accountIdInput: Locator;
  readonly searchButton: Locator;
  readonly backButton: Locator;
  readonly updateButton: Locator;
  readonly accountInfo: Locator;
  readonly errorMessage: Locator;

  constructor(page: Page) {
    this.page = page;
    this.accountIdInput = page.locator('input[name="accountId"]');
    this.searchButton = page.locator('button:has-text("Search")');
    this.backButton = page.locator('button:has-text("Back to Menu")');
    this.updateButton = page.locator('button:has-text("Update Account")');
    this.accountInfo = page.locator('text=Account Information');
    this.errorMessage = page.locator('.text-red-600');
  }

  async searchAccount(accountId: string) {
    await this.accountIdInput.fill(accountId);
    await this.searchButton.click();
  }

  async goBack() {
    await this.backButton.click();
  }

  async updateAccount() {
    await this.updateButton.click();
  }
}
