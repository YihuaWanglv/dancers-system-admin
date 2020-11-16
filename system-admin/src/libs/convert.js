const convert = {
  valueOfIsYesOrNot(value) {
    return (value && value == 1) ? '是' : '否'
  },
  valueOfStatus(value) {
    return (value && value == 1) ? '启用' : '禁用'
  },
  valueOfSex(value) {
    if (value == '1') return '男'
    if (value == '2') return '女'
    return '未知'
  }
}

export { convert }