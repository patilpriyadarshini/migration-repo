import { Page, Locator, expect } from '@playwright/test';

export class MainMenuPage {
  readonly page: Page;
  readonly menuTitle: Locator;
  readonly accountViewLink: Locator;
  readonly accountUpdateLink: Locator;
  readonly cardsLink: Locator;
  readonly transactionsLink: Locator;
  readonly billPaymentLink: Locator;
  readonly reportsLink: Locator;
  readonly logoutButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.menuTitle = page.locator('h2');
    this.accountViewLink = page.locator('a[href="/accounts/view"], button:has-text("Account View")');
    this.accountUpdateLink = page.locator('a[href="/accounts/update"], button:has-text("Account Update")');
    this.cardsLink = page.locator('a[href="/cards"], button:has-text("Cards")');
    this.transactionsLink = page.locator('a[href="/transactions"], button:has-text("Transactions")');
    this.billPaymentLink = page.locator('a[href="/bill-payment"], button:has-text("Bill Payment")');
    this.reportsLink = page.locator('a[href="/reports"], button:has-text("Reports")');
    this.logoutButton = page.locator('button:has-text("Logout"), a:has-text("Logout")');
  }

  async verifyMainMenuVisible() {
    await expect(this.menuTitle).toContainText('Main Menu');
    await expect(this.accountViewLink).toBeVisible();
    await expect(this.cardsLink).toBeVisible();
    await expect(this.transactionsLink).toBeVisible();
  }

  async navigateToAccountView() {
    await this.accountViewLink.click();
  }

  async navigateToAccountUpdate() {
    await this.accountUpdateLink.click();
  }

  async navigateToCards() {
    await this.cardsLink.click();
  }

  async navigateToTransactions() {
    await this.transactionsLink.click();
  }

  async navigateToBillPayment() {
    await this.billPaymentLink.click();
  }

  async navigateToReports() {
    await this.reportsLink.click();
  }

  async logout() {
    await this.logoutButton.click();
  }

  async verifyAllMenuOptionsVisible() {
    await expect(this.accountViewLink).toBeVisible();
    await expect(this.accountUpdateLink).toBeVisible();
    await expect(this.cardsLink).toBeVisible();
    await expect(this.transactionsLink).toBeVisible();
    await expect(this.billPaymentLink).toBeVisible();
    await expect(this.reportsLink).toBeVisible();
  }
}
