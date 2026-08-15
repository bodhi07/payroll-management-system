import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

/**
 * Vite Configuration File
 *
 * Configures the Vite development and build system for GlobalPay Enterprise HRMS.
 * Includes React 19 JSX transformations and path aliases `@/` pointing to `src/`.
 */
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3000,
    open: false,
    cors: true,
  },
});
