import request from "../util/request";

export default {
    namespace: 'userList',
    state:{
        list:[]
    },
    effects: {
        * initData(params, sagaEffects) {
            const {call,put} = sagaEffects;
            const url = "/ds/user/list";
            let data = yield call(request, url);
            yield put({
                type: "queryUserList",
                data: data
            });
        }
    },
    reducers:{
        queryUserList(state,result){
            let data = [...result.data];
            return {
                list: data
            }
        }
    }
}