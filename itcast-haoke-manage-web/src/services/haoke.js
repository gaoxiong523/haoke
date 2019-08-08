import request from '@/utils/request';

export async function addHouseResource(params) {
  console.log(params);
  return request(`/haoke/house/houseResources`,{
    method:'POST',
    body:params
  });
}
