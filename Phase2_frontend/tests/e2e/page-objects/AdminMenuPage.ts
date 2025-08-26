import { Page, Locator } from '@playwright/test';

export class AdminMenuPage {
  readonly page: Page;
  readonly title: Locator;
  readonly adminDisplay: Locator;
  readonly logoutButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.title = page.locator('h1:has-text("CardDemo")');
    this.adminDisplay = page.locator('text=Admin:');
    this.logoutButton = page.locator('text=Logout');
  }

  async clickMenuOption(optionText: string) {
    await this.page.click(`text=${optionText}`);
  }

  async logout() {
    await this.logoutButton.click();
  }
}
