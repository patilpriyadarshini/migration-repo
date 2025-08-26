import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests/e2e',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:5174',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: {
    command: process.env.CI ? 'npm run dev -- --port 5174 --host 0.0.0.0' : 'npm run dev -- --port 5174 --host localhost',
    url: 'http://localhost:5174',
    reuseExistingServer: !process.env.CI,
    timeout: process.env.CI ? 180 * 1000 : 120 * 1000,
    stdout: 'pipe',
    stderr: 'pipe',
    ignoreHTTPSErrors: true,
  },
  testIgnore: ['**/src/**', '**/node_modules/**', '**/tests/example.spec.ts'],
});
