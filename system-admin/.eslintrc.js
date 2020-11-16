// module.exports = {
//   root: true,
//   'extends': [
//     'plugin:vue/essential',
//     '@vue/standard'
//   ],
//   rules: {
//     // allow async-await
//     'generator-star-spacing': 'off',
//     // allow debugger during development
//     'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
//     'vue/no-parsing-error': [2, { 'x-invalid-end-tag': false }],
//     'no-undef': 'off'
//   },
//   parserOptions: {
//     parser: 'babel-eslint'
//   }
// }

module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ['plugin:vue/essential', 'eslint:recommended'],
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/no-parsing-error': [2, { 'x-invalid-end-tag': false }],
    'no-undef': 'off'
    //强制使用单引号
    // quotes: ['error', 'single'],
    //强制不使用分号结尾
    // semi: ['error', 'never']
  },
  parserOptions: {
    parser: 'babel-eslint'
  }
}