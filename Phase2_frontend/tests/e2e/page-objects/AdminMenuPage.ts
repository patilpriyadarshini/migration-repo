import { Page, Locator, expect } from '@playwright/test';

export class AdminMenuPage {
  readonly page: Page;
  readonly menuTitle: Locator;
  readonly userManagementLink: Locator;
  readonly addUserLink: Locator;
  readonly updateUserLink: Locator;
  readonly deleteUserLink: Locator;
  readonly accountViewLink: Locator;
  readonly cardsLink: Locator;
  readonly transactionsLink: Locator;
  readonly reportsLink: Locator;
  readonly logoutButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.menuTitle = page.locator('h2');
    this.userManagementLink = page.locator('a[href="/admin/users"], button:has-text("User Management")');
    this.addUserLink = page.locator('a[href="/admin/users/add"], button:has-text("Add User")');
    this.updateUserLink = page.locator('a[href="/admin/users/update"], button:has-text("Update User")');
    this.deleteUserLink = page.locator('a[href="/admin/users/delete"], button:has-text("Delete User")');
    this.accountViewLink = page.locator('a[href="/accounts/view"], button:has-text("Account View")');
    this.cardsLink = page.locator('a[href="/cards"], button:has-text("Cards")');
    this.transactionsLink = page.locator('a[href="/transactions"], button:has-text("Transactions")');
    this.reportsLink = page.locator('a[href="/reports"], button:has-text("Reports")');
    this.logoutButton = page.locator('button:has-text("Logout"), a:has-text("Logout")');
  }

  async verifyAdminMenuVisible() {
    await expect(this.menuTitle).toContainText('Admin Menu');
    await expect(this.userManagementLink).toBeVisible();
    await expect(this.accountViewLink).toBeVisible();
  }

  async verifyUserManagementVisible() {
    await expect(this.userManagementLink).toBeVisible();
    await expect(this.addUserLink).toBeVisible();
    await expect(this.updateUserLink).toBeVisible();
  }

  async verifyAllAdminOptionsVisible() {
    await expect(this.userManagementLink).toBeVisible();
    await expect(this.addUserLink).toBeVisible();
    await expect(this.updateUserLink).toBeVisible();
    await expect(this.deleteUserLink).toBeVisible();
    await expect(this.accountViewLink).toBeVisible();
    await expect(this.cardsLink).toBeVisible();
    await expect(this.transactionsLink).toBeVisible();
    await expect(this.reportsLink).toBeVisible();
  }

  async navigateToUserManagement() {
    await this.userManagementLink.click();
  }

  async navigateToAddUser() {
    await this.addUserLink.click();
  }

  async navigateToUpdateUser() {
    await this.updateUserLink.click();
  }

  async navigateToDeleteUser() {
    await this.deleteUserLink.click();
  }

  async navigateToAccountView() {
    await this.accountViewLink.click();
  }

  async navigateToCards() {
    await this.cardsLink.click();
  }

  async navigateToTransactions() {
    await this.transactionsLink.click();
  }

  async navigateToReports() {
    await this.reportsLink.click();
  }

  async logout() {
    await this.logoutButton.click();
  }
}
