import { Page, Locator } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly userIdInput: Locator;
  readonly passwordInput: Locator;
  readonly submitButton: Locator;
  readonly errorMessage: Locator;
  readonly title: Locator;

  constructor(page: Page) {
    this.page = page;
    this.userIdInput = page.locator('input[name="userId"]');
    this.passwordInput = page.locator('input[name="password"]');
    this.submitButton = page.locator('button[type="submit"]');
    this.errorMessage = page.locator('.text-red-600');
    this.title = page.locator('h2');
  }

  async goto() {
    await this.page.goto('/login');
  }

  async login(userId: string, password: string) {
    await this.userIdInput.fill(userId);
    await this.passwordInput.fill(password);
    await this.submitButton.click();
  }

  async getValidationError(field: 'userId' | 'password') {
    if (field === 'userId') {
      return this.page.locator('input[name="userId"] + p.text-red-600');
    }
    return this.page.locator('input[name="password"] + p.text-red-600');
  }
}
