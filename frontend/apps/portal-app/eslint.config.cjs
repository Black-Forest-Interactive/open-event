const nx = require('@nx/eslint-plugin')
const baseConfig = require('../../eslint.config.cjs')

module.exports = [
  ...baseConfig,
  ...nx.configs['flat/angular'],
  ...nx.configs['flat/angular-template'],
  {
    files: ['**/*.ts'],
    rules: {
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'portal',
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
  },
  {
    files: ['**/*.ts'],
    rules: {
      '@angular-eslint/prefer-standalone': 'off'
    }
  }
]
