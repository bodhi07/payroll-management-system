/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      spacing: {
        '70': '17.5rem', // 280px sidebar width
      },
      colors: {
        brand: {
          50: '#eff6ff',
          100: '#dbeaff',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb', // primary-container
          700: '#004ac6', // primary brand
          800: '#003ea8',
          900: '#00174b',
        },
        surface: {
          DEFAULT: '#faf8ff',
          dim: '#d9d9e5',
          bright: '#faf8ff',
          lowest: '#ffffff',
          low: '#f3f3fe',
          container: '#ededf9',
          high: '#e7e7f3',
          highest: '#e1e2ed',
          variant: '#e1e2ed',
        },
        'on-surface': {
          DEFAULT: '#191b23',
          variant: '#434655',
        },
        outline: {
          DEFAULT: '#737686',
          variant: '#c3c6d7',
        },
      },
      fontFamily: {
        heading: ['Manrope', 'sans-serif'],
        body: ['Inter', 'sans-serif'],
      },
      boxShadow: {
        'glass': '0 4px 16px 0 rgba(0, 0, 0, 0.04)',
        'glass-hover': '0 8px 24px 0 rgba(0, 0, 0, 0.08)',
        'card': '0 1px 3px 0 rgba(0, 0, 0, 0.05), 0 1px 2px -1px rgba(0, 0, 0, 0.05)',
        'dropdown': '0 10px 15px -3px rgba(0, 0, 0, 0.08), 0 4px 6px -4px rgba(0, 0, 0, 0.03)',
      },
      borderRadius: {
        'xl': '1rem',
        '2xl': '1.5rem',
      }
    },
  },
  plugins: [],
}
