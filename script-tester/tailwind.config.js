/** @type {import('tailwindcss/tailwind-config').TailwindConfig} */
module.exports = {
  purge: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      backgroundColor: {
        background: '#282c34',
      },
    },
  },
  variants: {
    extend: {
      borderColor: ['disabled'],
      textColor: ['disabled'],
      backgroundColor: ['disabled'],
    },
  },
  plugins: [],
};
