import { Page, Locator, expect } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly userIdInput: Locator;
  readonly passwordInput: Locator;
  readonly signInButton: Locator;
  readonly errorMessage: Locator;
  readonly validationErrors: Locator;

  constructor(page: Page) {
    this.page = page;
    this.userIdInput = page.locator('input[name="userId"]');
    this.passwordInput = page.locator('input[name="password"]');
    this.signInButton = page.locator('button[type="submit"]');
    this.errorMessage = page.locator('.text-red-600');
    this.validationErrors = page.locator('.text-red-600');
  }

  async goto() {
    await this.page.goto('/login');
  }

  async fillUserId(userId: string) {
    await this.userIdInput.fill(userId);
  }

  async fillPassword(password: string) {
    await this.passwordInput.fill(password);
  }

  async clickSignIn() {
    await this.signInButton.click();
  }

  async login(userId: string, password: string) {
    await this.fillUserId(userId);
    await this.fillPassword(password);
    await this.clickSignIn();
  }

  async verifyLoginFormVisible() {
    await expect(this.page.locator('h2')).toContainText('CardDemo System');
    await expect(this.userIdInput).toBeVisible();
    await expect(this.passwordInput).toBeVisible();
    await expect(this.signInButton).toBeVisible();
  }

  async verifyErrorMessage(expectedMessage: string) {
    await expect(this.errorMessage.first()).toBeVisible();
    await expect(this.errorMessage.first()).toContainText(expectedMessage);
  }

  async verifyValidationErrors() {
    await expect(this.validationErrors).toHaveCount(2);
  }

  async verifyPasswordValidationError() {
    await expect(this.passwordInput.locator('+ .text-red-600')).toBeVisible();
  }
}
