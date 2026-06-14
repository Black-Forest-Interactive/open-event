const nx = require('@nx/eslint-plugin')
const baseConfig = require('../../eslint.config.cjs')

module.exports = [
  ...baseConfig,
  ...nx.configs['flat/angular'],
  ...nx.configs['flat/angular-template'],
  {
    files: ['**/*.ts'],
    rules: {
      '@angular-eslint/prefer-standalone': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'admin',
          style: 'kebab-case'
        }
      ]
    }
  },
  {
    files: ['**/*.html'],
    rules: {
      '@angular-eslint/template/interactive-supports-focus': 'off',
      '@angular-eslint/template/click-events-have-key-events': 'off'
    }
  }
]
