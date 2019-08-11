import { queryResource } from '@/services/houseResource';

export default {
  namespace: 'houseResource',

  state: {
    data: {
      list: [],
      pagination: {},
    },
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryResource, payload);
      console.log("返回的 结果是");
      console.log(response);
      yield put({
        type: 'save',
        payload: response.data,
      });
    }
  },

  reducers: {
    save(state, action) {
      console.log(state);
      console.log("save之前的结果是 " + action.payload.data);
        return {
        ...state,
        data: action.payload,
      };
    },
  },
};
