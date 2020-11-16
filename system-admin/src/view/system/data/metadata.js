export const dynamicColumn = [
{
  key: 'areaId',
  fixed: 'left',
  width: 78,
  title: '序号'
},
{
  key: 'areaName',
  fixed: 'left',
  width: 140,
  title: '段名称'
},
{
  title: '科室',
  align: 'center',
  children: [
  {
    key: 'score',
    title: '扣分'
  },
  {
    key: 'rank',
    title: '名次'
  }]
}]