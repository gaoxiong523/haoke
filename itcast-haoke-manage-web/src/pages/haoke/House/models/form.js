import { routerRedux } from 'dva/router';
import { message } from 'antd';
import { addHouseResource } from '@/services/haoke';

export default {
  namespace: 'haoke',

  state: {

  },

  effects: {
    *submitRegularForm({ payload }, { call }) {
      console.log(payload);
      yield call(addHouseResource, payload);
      message.success('提交成功');
    }
  },

  reducers: {
    saveStepFormData(state, { payload }) {
      return {
        ...state,
      };
    },
  },
};
