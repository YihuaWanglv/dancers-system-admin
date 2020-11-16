import env from './env'

const DEV_URL = 'https://www.easy-mock.com/mock/5add9213ce4d0e69998a6f51/iview-admin/'
const PRO_URL = 'http://device-admin.iyihua.cn'

export default env === 'development' ? DEV_URL : PRO_URL
