import request from '@/utils/request';
//添加房源的service
export async function addHouseResource(params) {
  console.log(params);
  return request(`/haoke/house/houseResources`,{
    method:'POST',
    body:params
  });
}
