import request from '@/utils/request';
import { stringify } from 'qs';

export async function queryResource(params) {
  console.log("请求的参数是:"+params)
  return request(`/haoke/house/houseResources?${stringify(params)}`);
}
