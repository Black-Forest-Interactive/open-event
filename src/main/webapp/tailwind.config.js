module.exports = {
  prefix: '',
  content: {
    enabled: true,
    content: [
      './src/**/*.{html,ts}',
    ]
  },
  darkMode: 'class', // or 'media' or 'class'
  theme: {
    extend: {},
  },
  variants: {
    extend: {},
  },
  // plugins: [require('@tailwindcss/forms'), require('@tailwindcss/typography')],
  plugins: [require('@tailwindcss/typography')],
};
